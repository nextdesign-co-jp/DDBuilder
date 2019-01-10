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
import java.util.List;

import jp.co.nextdesign.ddb.BdApplicationPropertyException;
import jp.co.nextdesign.ddb.core.BdBuilderException;
import jp.co.nextdesign.ddb.core.template.BdBaseCodeFile;
import jp.co.nextdesign.ddb.core.template.BdCodeLine;
import jp.co.nextdesign.ddb.core.template.BdGeneratedFile;
import jp.co.nextdesign.ddb.core.template.BdTemplateProject;
import jp.co.nextdesign.ddb.core.template.block.BdBaseCodeBlock;
import jp.co.nextdesign.ddb.core.user.BdUserAttribute;
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethod;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodParameter;
import jp.co.nextdesign.util.BdStringHelper;
import jp.co.nextdesign.util.BdStringHelperReplacementPattern;

public class BdCodeFileMultipleSelectPanelHtml extends BdBaseCodeFile {

	/**
	 * コンストラクタ
	 * @param file
	 * @param project
	 * @throws BdBuilderException
	 * @throws BdApplicationPropertyException
	 */
	public BdCodeFileMultipleSelectPanelHtml(File file, BdTemplateProject project) throws BdBuilderException, BdApplicationPropertyException{
		super(file, project);
	}
	
	/**
	 * 出力ファイルを生成する
	 * 各サブクラスでOverrideする
	 */
	@Override
	public List<BdGeneratedFile> generate(BdUserServiceMethod m, String groupId){ //HTMLではgroupIdでの置換は必要ないので実際には参照はしない
		List<BdGeneratedFile> resultList = new ArrayList<BdGeneratedFile>();
		List<BdUserServiceMethodParameter> targetParameterList = m.getParameterListByDdbTypeKey(BdUserAttribute.KEY_OneToMany);
		for(BdUserServiceMethodParameter p : targetParameterList){
			BdGeneratedFile generatedFile = new BdGeneratedFile();
			List<BdStringHelperReplacementPattern> replacementPatternList = this.createReplacementPatternList(m, p);
			generatedFile.setFullPathName(BdStringHelper.replace(this.getPlainFile().getAbsolutePath(), replacementPatternList)); //パス名調整
			generatedFile.addLines(this.generateLines(m, p, replacementPatternList));
			resultList.add(generatedFile);
		}
		return resultList;
	}
	
	/**
	 * 出力ファイルのコード部分を作成する
	 * BLOCKなし, CASEなし, 文字列置換なし(customizeメソッド)
	 * @param c
	 * @return
	 */
	private List<String> generateLines(BdUserServiceMethod m, BdUserServiceMethodParameter p, List<BdStringHelperReplacementPattern> replacementPatternList) {
		List<String> resultList = new ArrayList<String>();
		int lineIndex = 0;
		while (lineIndex < this.getCodeLineList().size()){
			BdCodeLine bdCodeLine = this.getCodeLineList().get(lineIndex);
			if (bdCodeLine.hasCodeBlock()){ //2015.7.3時点ではBLOCKなし
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
		return line;
	}

	/** 文字置換パターンを作成する */
	private List<BdStringHelperReplacementPattern> createReplacementPatternList(BdUserServiceMethod m, BdUserServiceMethodParameter p){
		List<BdStringHelperReplacementPattern> resultList = new ArrayList<BdStringHelperReplacementPattern>();
		resultList.addAll(NOT_REPLACE_PATTERNS);
		BdUserClass bdUserClass = p.getCollectionElementBdUserClass();
		if (bdUserClass != null){
			resultList.add(new BdStringHelperReplacementPattern("Edition", bdUserClass.getName()));
		}
		return resultList;
	}

	/**
	 * コードブロックを生成する
	 */
	@Override
	protected BdBaseCodeBlock createCodeBlock(String key, List<BdCodeLine> lineListOfBlock){
		return null;
	}
}
