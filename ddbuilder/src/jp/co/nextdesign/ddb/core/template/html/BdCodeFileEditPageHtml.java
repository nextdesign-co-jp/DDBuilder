/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.template.html;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.nextdesign.ddb.BdApplicationPropertyException;
import jp.co.nextdesign.ddb.core.BdBuilderException;
import jp.co.nextdesign.ddb.core.template.BdBaseCodeFile;
import jp.co.nextdesign.ddb.core.template.BdCodeLine;
import jp.co.nextdesign.ddb.core.template.BdGeneratedFile;
import jp.co.nextdesign.ddb.core.template.BdTemplateProject;
import jp.co.nextdesign.ddb.core.template.block.BdBaseCodeBlock;
import jp.co.nextdesign.ddb.core.template.block.BdCodeBlockHtmlField;
import jp.co.nextdesign.ddb.core.template.block.BdCodeBlockHtmlModal;
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.util.BdStringHelper;
import jp.co.nextdesign.util.BdStringHelperReplacementPattern;
import jp.co.nextdesign.util.logging.NdLogger;

public class BdCodeFileEditPageHtml extends BdBaseCodeFile {

	/**
	 * コンストラクタ
	 * @param file
	 * @param project
	 * @throws BdBuilderException
	 * @throws BdApplicationPropertyException
	 */
	public BdCodeFileEditPageHtml(File file, BdTemplateProject project) throws BdBuilderException, BdApplicationPropertyException{
		super(file, project);
	}
	
	/**
	 * 出力ファイルを生成する
	 * 各サブクラスでOverrideする
	 */
	@Override
	public List<BdGeneratedFile> generate(BdUserClass c, String groupId){ //HTMLではgroupIdでの置換は必要ないので実際には参照はしない
		BdGeneratedFile result = new BdGeneratedFile();
		List<BdStringHelperReplacementPattern> replacementPatternList = this.createReplacementPatternList(c);
		result.setFullPathName(BdStringHelper.replace(this.getPlainFile().getAbsolutePath(), replacementPatternList)); //パス名調整
		result.addLines(this.generateLines(c, replacementPatternList));
		return new ArrayList<BdGeneratedFile>(Arrays.asList(result));
	}
	
	/**
	 * 出力ファイルのコード部分を作成する
	 * 各行を順に読みBdCodeBlockに関連付いた行があれば、BdCodeBlockにコード生成を依頼する
	 * @param c
	 * @return
	 */
	private List<String> generateLines(BdUserClass c, List<BdStringHelperReplacementPattern> replacementPatternList) {
		List<String> resultList = new ArrayList<String>();
		int lineIndex = 0;
		while (lineIndex < this.getCodeLineList().size()){
			BdCodeLine bdCodeLine = this.getCodeLineList().get(lineIndex);
			if (bdCodeLine.hasCodeBlock()){
				resultList.addAll(bdCodeLine.getCodeBlock().generate(c));
				lineIndex = bdCodeLine.getCodeBlock().getEndIndex() + 1;
			}else{
				resultList.add(this.customize(bdCodeLine.getLine(), c, replacementPatternList));
				lineIndex++;
			}
		}
		return resultList;
	}
	
	/** 
	 * 文字列を置き換える
	 */
	private String customize(String line, BdUserClass c, List<BdStringHelperReplacementPattern> replacementPatternList){
		String result = BdStringHelper.replace(line, replacementPatternList);
		return result;
	}

	/** 文字置換パターンを作成する */
	private List<BdStringHelperReplacementPattern> createReplacementPatternList(BdUserClass c){
		List<BdStringHelperReplacementPattern> resultList = new ArrayList<BdStringHelperReplacementPattern>();
		resultList.addAll(NOT_REPLACE_PATTERNS);
		resultList.add(new BdStringHelperReplacementPattern("Book 編集画面", c.getJavadocTitle() + " " + c.getName() + " 編集画面"));
		resultList.add(new BdStringHelperReplacementPattern("book", c.getLowerStartedName()));
		resultList.add(new BdStringHelperReplacementPattern("Book", c.getName()));
		return resultList;
	}

	//このテンプレートファイルに含まれるブロックキー
	private static final String BLOCK_KEY_Field = "Field";
	private static final String BLOCK_KEY_Modal = "Modal";

	/** 
	 * 各サブクラスでOverrideする
	 */
	@Override
	protected BdBaseCodeBlock createCodeBlock(String key, List<BdCodeLine> lineListOfBlock){
		BdBaseCodeBlock result = null;
		if (BLOCK_KEY_Field.equals(key)){
			result = new BdCodeBlockHtmlField(lineListOfBlock);
		} else if (BLOCK_KEY_Modal.equals(key)){
			result = new BdCodeBlockHtmlModal(lineListOfBlock);
		} else {
			key = (key == null || "".equals(key)) ? "BLOCKキーなし" : key;
			NdLogger.getInstance().error(this.getClass().getName() + " : [WARNING] Unknown Key = " + key, null);
		}
		return result;
	}
}
