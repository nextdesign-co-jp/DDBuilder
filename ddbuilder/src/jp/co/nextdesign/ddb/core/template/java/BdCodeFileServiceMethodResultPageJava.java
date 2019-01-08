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
import jp.co.nextdesign.ddb.core.template.block.BdCodeBlockJavaImport;
import jp.co.nextdesign.ddb.core.template.block.BdCodeBlockJavaField;
import jp.co.nextdesign.ddb.core.template.block.BdCodeBlockJavaServiceResultPageNavi;
import jp.co.nextdesign.ddb.core.template.block.BdCodeBlockJavaServiceResultPageDeclareList;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethod;
import jp.co.nextdesign.util.BdStringHelper;
import jp.co.nextdesign.util.BdStringHelperReplacementPattern;
import jp.co.nextdesign.util.logging.NdLogger;

public class BdCodeFileServiceMethodResultPageJava extends BdBaseCodeFile {

	/**
	 * コンストラクタ
	 * @param file
	 * @param project
	 * @throws BdBuilderException
	 * @throws BdApplicationPropertyException
	 */
	public BdCodeFileServiceMethodResultPageJava(File file, BdTemplateProject project) throws BdBuilderException, BdApplicationPropertyException{
		super(file, project);
	}
	
	/**
	 * 出力ファイルを生成する
	 * 各サブクラスでOverrideする
	 */
	@Override
	public List<BdGeneratedFile> generate(BdUserServiceMethod m, String groupId){
		if (!m.isReturnVoid()){
			BdGeneratedFile result = new BdGeneratedFile();
			List<BdStringHelperReplacementPattern> replacementPatternList = this.createReplacementPatternList(m);
			result.setFullPathName(BdStringHelper.replace(this.getPlainFile().getAbsolutePath(), replacementPatternList)); //パス名調整
			result.addLines(this.generateLines(m, replacementPatternList, groupId));
			return new ArrayList<BdGeneratedFile>(Arrays.asList(result));
		} else {
			//戻り型がvoidの場合は結果ファイルは作成しない。DdVoidResultPageを使用する
			return new ArrayList<BdGeneratedFile>();
		}
	}
	
	/**
	 * 出力ファイルのコード部分を作成する
	 * 各行を順に読みBdCodeBlockに関連付いた行があれば、BdCodeBlockにコード生成を依頼する
	 * @param c
	 * @return
	 */
	private List<String> generateLines(BdUserServiceMethod m, List<BdStringHelperReplacementPattern> replacementPatternList, String groupId) {
		List<String> resultList = new ArrayList<String>();
		int lineIndex = 0;
		while (lineIndex < this.getCodeLineList().size()){
			BdCodeLine bdCodeLine = this.getCodeLineList().get(lineIndex);
			if (bdCodeLine.hasCodeBlock()){
				resultList.addAll(bdCodeLine.getCodeBlock().generate(m.getBdServiceMethodResult())); //引数=BdServiceMethodResult
				lineIndex = bdCodeLine.getCodeBlock().getEndIndex() + 1;
			}else{
				resultList.add(this.customize(bdCodeLine.getLine(), m, replacementPatternList, groupId));
				lineIndex++;
			}
		}
		return resultList;
	}
	
	/** 
	 * 文字列を置き換える
	 */
	private String customize(String line, BdUserServiceMethod m, List<BdStringHelperReplacementPattern> replacementPatternList, String groupId){
		String result = line.replaceAll(BdTemplateProject.GROUP_ID, groupId);
		result = BdStringHelper.replace(result, replacementPatternList);
		return result;
	}
	
	/** 文字置換パターンを作成する */
	private List<BdStringHelperReplacementPattern> createReplacementPatternList(BdUserServiceMethod m){
		List<BdStringHelperReplacementPattern> resultList = new ArrayList<BdStringHelperReplacementPattern>();
		resultList.addAll(NOT_REPLACE_PATTERNS);
		resultList.add(new BdStringHelperReplacementPattern("BookServiceResultPageMethod1", m.getBdServiceClass().getName() + "ResultPage" + m.getUpperStartedName()));
		resultList.add(new BdStringHelperReplacementPattern("Book", m.getBdServiceMethodResult().getSimpleOrCollectionElementTypeName()));
		resultList.add(new BdStringHelperReplacementPattern("book", m.getBdServiceMethodResult().getLowerStartedSimpleOrCollectionElementTypeName()));
		return resultList;
	}

	//このテンプレートファイルに含まれるブロックキー
	private static final String BLOCK_KEY_DeclareList = "DeclareList";
	private static final String BLOCK_KEY_Field = "Field";
	private static final String BLOCK_KEY_Import = "Import";
	private static final String BLOCK_KEY_Navi = "Navi";
	private static final String BLOCK_KEY_Nop = "nop";

	/** 
	 * 各サブクラスでOverrideする
	 */
	@Override
	protected BdBaseCodeBlock createCodeBlock(String key, List<BdCodeLine> lineListOfBlock){
		BdBaseCodeBlock result = null;
		if (BLOCK_KEY_Import.equals(key)){
			result = new BdCodeBlockJavaImport(lineListOfBlock);
		} else if (BLOCK_KEY_DeclareList.equals(key)){
			result = new BdCodeBlockJavaServiceResultPageDeclareList(lineListOfBlock);
		} else if (BLOCK_KEY_Field.equals(key)){
			result = new BdCodeBlockJavaField(lineListOfBlock);
		} else if (BLOCK_KEY_Navi.equals(key)){
			result = new BdCodeBlockJavaServiceResultPageNavi(lineListOfBlock);
		} else if (BLOCK_KEY_Nop.equals(key)) {
		} else {
			key = (key == null || "".equals(key)) ? "BLOCKキーなし" : key;
			NdLogger.getInstance().error(this.getClass().getName() + " : [WARNING] Unknown Key = " + key, null);
		}
		return result;
	}
}
