/*
 * DDBuilder
 * http://www.nextdesign.co.jp/ddd/index.html
 * Copyright 2015 NEXT DESIGN Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethod;
import jp.co.nextdesign.util.BdStringHelper;
import jp.co.nextdesign.util.BdStringHelperReplacementPattern;
import jp.co.nextdesign.util.logging.NdLogger;

public class BdCodeFileServiceMethodInvokePageHtml extends BdBaseCodeFile {

	/**
	 * コンストラクタ
	 * @param file
	 * @param project
	 * @throws BdBuilderException
	 * @throws BdApplicationPropertyException
	 */
	public BdCodeFileServiceMethodInvokePageHtml(File file, BdTemplateProject project) throws BdBuilderException, BdApplicationPropertyException{
		super(file, project);
	}
	
	/**
	 * 出力ファイルを生成する
	 * 各サブクラスでOverrideする
	 */
	@Override
	public List<BdGeneratedFile> generate(BdUserServiceMethod m, String groupId){ //HTMLではgroupIdでの置換は必要ないので実際には参照はしない
		BdGeneratedFile result = new BdGeneratedFile();
		List<BdStringHelperReplacementPattern> replacementPatternList = this.createReplacementPatternList(m);
		result.setFullPathName(BdStringHelper.replace(this.getPlainFile().getAbsolutePath(), replacementPatternList)); //パス名調整
		result.addLines(this.generateLines(m, replacementPatternList));
		return new ArrayList<BdGeneratedFile>(Arrays.asList(result));
	}
	
	/**
	 * 出力ファイルのコード部分を作成する
	 * 各行を順に読みBdCodeBlockに関連付いた行があれば、BdCodeBlockにコード生成を依頼する
	 * @param c
	 * @return
	 */
	private List<String> generateLines(BdUserServiceMethod m, List<BdStringHelperReplacementPattern> replacementPatternList) {
		List<String> resultList = new ArrayList<String>();
		int lineIndex = 0;
		while (lineIndex < this.getCodeLineList().size()){
			BdCodeLine bdCodeLine = this.getCodeLineList().get(lineIndex);
			if (bdCodeLine.hasCodeBlock()){
				resultList.addAll(bdCodeLine.getCodeBlock().generate(m));
				lineIndex = bdCodeLine.getCodeBlock().getEndIndex() + 1;
			}else{
				resultList.add(this.customize(bdCodeLine.getLine(), m, replacementPatternList));
				lineIndex++;
			}
		}
		return resultList;
	}
	
	/** 
	 * 文字列を置き換える
	 */
	private String customize(String line, BdUserServiceMethod m, List<BdStringHelperReplacementPattern> replacementPatternList){
		String result = BdStringHelper.replace(line, replacementPatternList);
		return result;
	}

	/** 文字置換パターンを作成する */
	private List<BdStringHelperReplacementPattern> createReplacementPatternList(BdUserServiceMethod m){
		List<BdStringHelperReplacementPattern> resultList = new ArrayList<BdStringHelperReplacementPattern>();
		resultList.addAll(NOT_REPLACE_PATTERNS);
		resultList.add(new BdStringHelperReplacementPattern("BookServiceInvokePageMethod1", m.getBdServiceClass().getName() + "InvokePage" + m.getUpperStartedName()));
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
