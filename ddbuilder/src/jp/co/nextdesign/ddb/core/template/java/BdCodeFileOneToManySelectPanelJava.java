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
package jp.co.nextdesign.ddb.core.template.java;

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
import jp.co.nextdesign.ddb.core.template.block.BdCodeBlockJavaImport;
import jp.co.nextdesign.ddb.core.user.BdUserAttribute;
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.util.BdStringHelper;
import jp.co.nextdesign.util.BdStringHelperReplacementPattern;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * OneToMany 複数選択パネル Book-----Edition
 * @author murayama
 *
 */
public class BdCodeFileOneToManySelectPanelJava extends BdBaseCodeFile {

	/**
	 * コンストラクタ
	 * @param file
	 * @param project
	 * @throws BdBuilderException
	 * @throws BdApplicationPropertyException
	 */
	public BdCodeFileOneToManySelectPanelJava(File file, BdTemplateProject project) throws BdBuilderException, BdApplicationPropertyException{
		super(file, project);
	}

	/**
	 * 出力ファイルを生成する
	 * 各サブクラスでOverrideする
	 */
	@Override
	public List<BdGeneratedFile> generate(BdUserClass c, String groupId){
		List<BdGeneratedFile> resultList = new ArrayList<BdGeneratedFile>();
		List<BdUserAttribute> targetAttributeList = c.getAttributeListByDdbTypeKey(BdUserAttribute.KEY_OneToMany);
		for(BdUserAttribute a : targetAttributeList){
			List<BdStringHelperReplacementPattern> replacementPatternList = this.createReplacementPatternList(c, a);
			BdGeneratedFile generatedFile = new BdGeneratedFile();
			generatedFile.setFullPathName(BdStringHelper.replace(this.getPlainFile().getAbsolutePath(), replacementPatternList)); //パス名調整
			generatedFile.addLines(this.generateLines(c, a, replacementPatternList, groupId));
			resultList.add(generatedFile);
		}
		return resultList;
	}

	/**
	 * 出力ファイルのコード部分を作成する
	 * BLOCKなし, CASEなし, 文字列置換のみ(customizeメソッド)
	 * @param c
	 * @return
	 */
	private List<String> generateLines(BdUserClass c, BdUserAttribute a, List<BdStringHelperReplacementPattern> replacementPatternList, String groupId) {
		List<String> resultList = new ArrayList<String>();
		int lineIndex = 0;
		while (lineIndex < this.getCodeLineList().size()){
			BdCodeLine bdCodeLine = this.getCodeLineList().get(lineIndex);
			if (bdCodeLine.hasCodeBlock()){
				resultList.addAll(bdCodeLine.getCodeBlock().generate(a));
				lineIndex = bdCodeLine.getCodeBlock().getEndIndex() + 1;
			}else{
				resultList.add(this.customize(bdCodeLine.getLine(), c, a, replacementPatternList, groupId));
				lineIndex++;
			}
		}
		return resultList;
	}

	/** 
	 * 行をカスタマイズする
	 */
	private String customize(String line, BdUserClass c, BdUserAttribute a, List<BdStringHelperReplacementPattern> replacementPatternList, String groupId){
		String result = line.replaceAll(BdTemplateProject.GROUP_ID, groupId);
		result = BdStringHelper.replace(result, replacementPatternList);
		return result;
	}

	/** 文字置換パターンを作成する */
	private List<BdStringHelperReplacementPattern> createReplacementPatternList(BdUserClass c, BdUserAttribute a){
		List<BdStringHelperReplacementPattern> resultList = new ArrayList<BdStringHelperReplacementPattern>();
		resultList.addAll(NOT_REPLACE_PATTERNS);
		resultList.add(new BdStringHelperReplacementPattern("book", c.getLowerStartedName()));
		resultList.add(new BdStringHelperReplacementPattern("Book", c.getName()));
		resultList.add(new BdStringHelperReplacementPattern("editionList", a.getName()));
		resultList.add(new BdStringHelperReplacementPattern("EditionList", a.getUpperStartedName()));
		BdUserClass collectionElementClass = a.getCollectionElementBdUserClass();
		if (collectionElementClass != null){
			resultList.add(new BdStringHelperReplacementPattern("edition", collectionElementClass.getLowerStartedName()));
			resultList.add(new BdStringHelperReplacementPattern("Edition", collectionElementClass.getName()));
		}
		return resultList;
	}

	//このテンプレートファイルに含まれるブロックキー
	private static final String BLOCK_KEY_Import = "Import";

	/**
	 * コードブロックを生成する
	 */
	@Override
	protected BdBaseCodeBlock createCodeBlock(String key, List<BdCodeLine> lineListOfBlock){
		BdBaseCodeBlock result = null;
		if (BLOCK_KEY_Import.equals(key)) {
			result = new BdCodeBlockJavaImport(lineListOfBlock);
		} else {
			key = (key == null || "".equals(key)) ? "BLOCKキーなし" : key;
			NdLogger.getInstance().error(this.getClass().getName() + " : [WARNING] Unknown Key = " + key, null);
		}
		return result;
	}
}
