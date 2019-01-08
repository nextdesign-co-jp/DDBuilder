/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.template.java;

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
import jp.co.nextdesign.ddb.core.template.block.BdCodeBlockJavaDomainClassListPageEntity;
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.util.logging.NdLogger;

public class BdCodeFileDomainClassListPageJava extends BdBaseCodeFile {

	/**
	 * コンストラクタ
	 * @param file
	 * @param project
	 * @throws BdBuilderException
	 * @throws BdApplicationPropertyException
	 */
	public BdCodeFileDomainClassListPageJava(File file, BdTemplateProject project) throws BdBuilderException, BdApplicationPropertyException{
		super(file, project);
	}
	
	/**
	 * 出力ファイルを生成する
	 * 各サブクラスでOverrideする
	 */
	@Override
	public List<BdGeneratedFile> generate(List<BdUserClass> cList, String groupId){
		BdGeneratedFile result = new BdGeneratedFile();
		result.setFullPathName(this.getPlainFile().getAbsolutePath()); //パス名に置換文字なし
		result.addLines(this.generateLines(cList, groupId));
		return new ArrayList<BdGeneratedFile>(Arrays.asList(result));
	}	
	
	/**
	 * 出力ファイルのコード部分を作成する
	 * 各行を順に読みBdCodeBlockに関連付いた行があれば、BdCodeBlockにコード生成を依頼する
	 * @param c
	 * @return
	 */
	private List<String> generateLines(List<BdUserClass> cList, String groupId) {
		List<String> resultList = new ArrayList<String>();
		int lineIndex = 0;
		while (lineIndex < this.getCodeLineList().size()){
			BdCodeLine bdCodeLine = this.getCodeLineList().get(lineIndex);
			if (bdCodeLine.hasCodeBlock()){
				resultList.addAll(bdCodeLine.getCodeBlock().generate(cList));
				lineIndex = bdCodeLine.getCodeBlock().getEndIndex() + 1;
			}else{
				resultList.add(this.customize(bdCodeLine.getLine(), groupId));
				lineIndex++;
			}
		}
		return resultList;
	}
	
	/**
	 * 出力ファイルを生成する
	 * 各サブクラスでOverrideする
	 * ドメインクラスが1つも定義されていない場合もDomainClassListPageは生成するため
	 */
	@Override
	public List<BdGeneratedFile> generate(String groupId){
		BdGeneratedFile result = new BdGeneratedFile();
		result.setFullPathName(this.getPlainFile().getAbsolutePath()); //パス名に置換文字なし
		result.addLines(this.generateLines(groupId));
		return new ArrayList<BdGeneratedFile>(Arrays.asList(result));
	}

	/**
	 * 出力ファイルのコード部分を作成する
	 * ドメインクラスが1つも定義されていない場合もDomainClassListPageは生成するため
	 */
	private List<String> generateLines(String groupId) {
		List<String> resultList = new ArrayList<String>();
		int lineIndex = 0;
		while (lineIndex < this.getCodeLineList().size()){
			BdCodeLine bdCodeLine = this.getCodeLineList().get(lineIndex);
			if (bdCodeLine.hasCodeBlock()){
				//CodeBlock内は空
				lineIndex = bdCodeLine.getCodeBlock().getEndIndex() + 1;
			}else{
				resultList.add(this.customize(bdCodeLine.getLine(), groupId));
				lineIndex++;
			}
		}
		return resultList;
	}

	/** 
	 * 文字列を置き換える
	 */
	private String customize(String line, String groupId){
		if (line != null && line.contains(BdTemplateProject.GROUP_ID)){
			line = line.replaceAll(BdTemplateProject.GROUP_ID, groupId);
		}
		return line;
	}

	//このテンプレートファイルに含まれるブロックキー
	private static final String BLOCK_KEY_Entity = "Entity";

	/** 
	 * 各サブクラスでOverrideする
	 */
	@Override
	protected BdBaseCodeBlock createCodeBlock(String key, List<BdCodeLine> lineListOfBlock){
		BdBaseCodeBlock result = null;
		if (BLOCK_KEY_Entity.equals(key)){
			result = new BdCodeBlockJavaDomainClassListPageEntity(lineListOfBlock);
		} else {
			key = (key == null || "".equals(key)) ? "BLOCKキーなし" : key;
			NdLogger.getInstance().error(this.getClass().getName() + " : [WARNING] Unknown Key = " + key, null);
		}
		return result;
	}
}
