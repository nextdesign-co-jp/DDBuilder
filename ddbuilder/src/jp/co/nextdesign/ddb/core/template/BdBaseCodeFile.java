/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.template;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.nextdesign.ddb.BdApplicationProperty;
import jp.co.nextdesign.ddb.BdApplicationPropertyException;
import jp.co.nextdesign.ddb.BdMessageResource;
import jp.co.nextdesign.ddb.core.BdBuilderException;
import jp.co.nextdesign.ddb.core.template.block.BdBaseCodeBlock;
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethod;
import jp.co.nextdesign.jcr.NdConstants;
import jp.co.nextdesign.util.BdFileUtil;
import jp.co.nextdesign.util.BdStringHelperReplacementPattern;
import jp.co.nextdesign.util.NdFileUtilStringReplacer;

/**
 * テンプレートコードを持つクラスの基底クラス
 * @author murayama
 *
 */
public abstract class BdBaseCodeFile extends BdTemplateFile {

	private List<BdCodeLine> codeLineList;
	private List<BdBaseCodeBlock> codeBlockList;
	/** 置き換えなしのパターン */
	protected static List<BdStringHelperReplacementPattern> NOT_REPLACE_PATTERNS = Arrays.asList(
			new BdStringHelperReplacementPattern("@author", "@author"),
			new BdStringHelperReplacementPattern("@param", "@param")
			);

	/**
	 * コンストラクタ
	 * @param file
	 * @param project
	 * @throws BdApplicationPropertyException 
	 * @throws BdBuilderException 
	 */
	public BdBaseCodeFile(File file, BdTemplateProject project) throws BdBuilderException, BdApplicationPropertyException{
		super(file, project);
		this.codeLineList = new ArrayList<BdCodeLine>();
		this.codeBlockList = new ArrayList<BdBaseCodeBlock>();
		this.readFile();
	}

	/**
	 * コードファイルを生成する
	 * 各サブクラスでOverrideする（ドメインクラス毎の情報をもとにコードファイルを生成するテンプレートクラス）
	 */
	public List<BdGeneratedFile> generate(BdUserClass c, String groupId){
		return null;
	}

	/**
	 * コードファイルを生成する
	 * 各サブクラスでOverrideする（全ドメインクラスの情報をもとにコードファイルを生成するテンプレートクラス）
	 */
	public List<BdGeneratedFile> generate(List<BdUserClass> cList, String groupId){
		return null;
	}
	
	/**
	 * コードファイルを生成する
	 * 各サブクラスでOverrideする（ドメインクラスが定義されていない場合でもコードファイルを生成するテンプレートクラス）
	 */
	public List<BdGeneratedFile> generate(String groupId){
		return null;
	}

	/**
	 * コードファイルを生成する
	 * 各サブクラスでOverrideする（ドメインクラス毎の情報をもとにコードファイルを生成するテンプレートクラス）
	 */
	public List<BdGeneratedFile> generate(BdUserServiceMethod m, String groupId){
		return null;
	}

	/**
	 * コードファイルを生成する
	 * 各サブクラスでOverrideする（サービスメソッドの情報をもとにコードファイルを生成するテンプレートクラス）
	 * Javaの型消去のために「generate(List<BdUserClass> cList, String groupId)」のオーバーロードにできないのでメソッド名に4Serviceとした
	 */
	public List<BdGeneratedFile> generate4Service(List<BdUserServiceMethod> mList, String groupId){
		return null;
	}
	
	/**
	 * 全行を応答する
	 * @return
	 */
	public List<BdCodeLine> getCodeLineList(){
		return this.codeLineList;
	}

	/**
	 * コードブロック情報を作成する
	 * @throws BdBuilderException
	 */
	public void buildCodeBlockList() throws BdBuilderException{
		boolean inBlock = false;
		List<BdCodeLine> lineListOfBlock = new ArrayList<BdCodeLine>();
		for(BdCodeLine line : this.codeLineList){
			if (line.isBlockStartLine()){
				if (inBlock){
					throw new BdBuilderException("BLOCKSTART ORDER EXCEPTION:" + this.getPlainFile().getName() + ":LINE#:" + (line.getLineIndex() + 1) + ":" + line.getLine());
				}
				lineListOfBlock.clear();
				inBlock = true;
			}
			if(inBlock){
				lineListOfBlock.add(line);
			}
			if(line.isBlockEndLine()){
				if (!inBlock){
					throw new BdBuilderException("BLOCKEND ORDER EXCEPTION:" + this.getPlainFile().getName() + ":LINE#:" + (line.getLineIndex() + 1) + ":" + line.getLine());
				}
				BdBaseCodeBlock block = this.createCodeBlock(line.getKey(), lineListOfBlock);
				if (block != null){
					block.buildCodeCaseList(lineListOfBlock);
					this.codeBlockList.add(block);
				}
				inBlock = false;
			}
		}
	}

	/**
	 * コードブロックを生成する
	 * 各サブクラスでOverrideする
	 */
	protected abstract BdBaseCodeBlock createCodeBlock(String key, List<BdCodeLine> lineListOfBlock);

	/**
	 * ファイルを読み込む
	 * @throws BdBuilderException
	 * @throws BdApplicationPropertyException 
	 */
	private void readFile() throws BdBuilderException, BdApplicationPropertyException{
		String characterCode = BdApplicationProperty.getInstance().getCharacterCode();
		int lineIndex = 0;
		if (this.plainFile != null && this.plainFile.exists()){
			BufferedReader buffereReaderr = null;
			try{
				this.codeLineList.clear();
				FileInputStream fis = new FileInputStream(this.plainFile);
				InputStreamReader isr = new InputStreamReader(fis, characterCode);
				buffereReaderr = new BufferedReader(isr);
				String line;
				while ((line = buffereReaderr.readLine()) != null){
					if (BdFileUtil.isHtmlFile(this.plainFile)){
						line = this.convertLanguageInHtml(line);
					} else {
						line = this.convertLanguageInJava(line);
					}
					this.codeLineList.add(new BdCodeLine(line, lineIndex++, this.plainFile));
				}
			} catch(FileNotFoundException ex) {
				throw new BdBuilderException(this.getClass().getSimpleName() + "CHECK! -> FILE NAME=" + this.plainFile.getAbsolutePath(), ex);
			} catch (UnsupportedEncodingException ex) {
				throw new BdBuilderException(this.getClass().getSimpleName() + "CHECK! -> PROPERTY（Application.properties）CHARACTER SET CODE=" + characterCode, ex);
			} catch(IOException ex) {
				throw new BdBuilderException(this.getClass().getSimpleName() + "EXCEPTION! READ A FILE=" + this.plainFile.getAbsolutePath(), ex);
			} finally {
				if (buffereReaderr != null){
					try{
						buffereReaderr.close();
					} catch(Exception ex){
						throw new BdBuilderException(this.getClass().getSimpleName() + ":finnaly:close失敗");
					}
				}
			}
		} else {
			String msg = this.getClass().getSimpleName();
			msg += this.plainFile == null ? "sampleFile is NULL" : this.plainFile.getAbsolutePath();
			throw new BdBuilderException(this.getClass().getSimpleName() + "FILE NOT FOUND=" + msg);
		}
	}
	
	/** 多言語対応のためキーワードを置換える */
	private static NdFileUtilStringReplacer htmlLanguageReplacer;
	
	/** html 多言語対応のためキーワードを置換える */
	protected String convertLanguageInHtml(String line){
		if (htmlLanguageReplacer == null){
			htmlLanguageReplacer = new NdFileUtilStringReplacer();
			int key=1;
			while(true){
				String val = BdMessageResource.get("html.g." + key++);
				if (val == null || val.length() < 1){
					break;
				}
				String[] splitted = val.split(",");
				htmlLanguageReplacer.addReplacementPattern(splitted[0], splitted[1]);
			}
		}
		return htmlLanguageReplacer.replace(line);
	}

	/** 多言語対応のためキーワードを置換える */
	private static NdFileUtilStringReplacer javaLanguageReplacer;
	
	/** Java 多言語対応のためキーワードを置換える */
	protected String convertLanguageInJava(String line){
		if (javaLanguageReplacer == null){
			javaLanguageReplacer = new NdFileUtilStringReplacer();
			int key=1;
			while(true){
				String val = BdMessageResource.get("html.java." + key++);
				if (val == null || val.length() < 1){
					break;
				}
				String[] splitted = val.split(",");
				javaLanguageReplacer.addReplacementPattern(splitted[0], splitted[1]);
			}
		}
		return javaLanguageReplacer.replace(line);
	}

	public List<BdBaseCodeBlock> getCodeBlockList() {
		return codeBlockList;
	}

	public void setCodeBlockList(List<BdBaseCodeBlock> codeBlockList) {
		this.codeBlockList = codeBlockList;
	}

	public File getPlainFile() {
		return plainFile;
	}

	public void setPlainFile(File plainFile) {
		this.plainFile = plainFile;
	}

	public void setCodeLineList(List<BdCodeLine> codeLineList) {
		this.codeLineList = codeLineList;
	}

	/**
	 * デバッグ
	 * @param tab
	 */
	@Override
	public void debugPrint(String tab) {
		super.debugPrint(tab);
		for(BdBaseCodeBlock codeBlock : this.codeBlockList){
			codeBlock.debugPrint(tab + NdConstants.DEBUG_TAB);
		}
	}
}
