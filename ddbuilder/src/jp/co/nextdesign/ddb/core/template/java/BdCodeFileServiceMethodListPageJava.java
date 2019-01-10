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
import java.util.Arrays;
import java.util.List;

import jp.co.nextdesign.ddb.BdApplicationPropertyException;
import jp.co.nextdesign.ddb.core.BdBuilderException;
import jp.co.nextdesign.ddb.core.template.BdBaseCodeFile;
import jp.co.nextdesign.ddb.core.template.BdCodeLine;
import jp.co.nextdesign.ddb.core.template.BdGeneratedFile;
import jp.co.nextdesign.ddb.core.template.BdTemplateProject;
import jp.co.nextdesign.ddb.core.template.block.BdBaseCodeBlock;
import jp.co.nextdesign.ddb.core.template.block.BdCodeBlockJavaServiceMethodListPageMethod;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethod;
import jp.co.nextdesign.util.logging.NdLogger;

public class BdCodeFileServiceMethodListPageJava extends BdBaseCodeFile {

	/**
	 * コンストラクタ
	 * @param file
	 * @param project
	 * @throws BdBuilderException
	 * @throws BdApplicationPropertyException
	 */
	public BdCodeFileServiceMethodListPageJava(File file, BdTemplateProject project) throws BdBuilderException, BdApplicationPropertyException{
		super(file, project);
	}
	
	/**
	 * 出力ファイルを生成する
	 * 各サブクラスでOverrideする
	 */
	@Override
	public List<BdGeneratedFile> generate4Service(List<BdUserServiceMethod> mList, String groupId){
		BdGeneratedFile result = new BdGeneratedFile();
		result.setFullPathName(this.getPlainFile().getAbsolutePath()); //パス名に置換文字なし
		result.addLines(this.generateLines(mList, groupId));
		return new ArrayList<BdGeneratedFile>(Arrays.asList(result));
	}
	
	/**
	 * 出力ファイルのコード部分を作成する
	 * 各行を順に読みBdCodeBlockに関連付いた行があれば、BdCodeBlockにコード生成を依頼する
	 * @param c
	 * @return
	 */
	private List<String> generateLines(List<BdUserServiceMethod> mList, String groupId) {
		List<String> resultList = new ArrayList<String>();
		int lineIndex = 0;
		while (lineIndex < this.getCodeLineList().size()){
			BdCodeLine bdCodeLine = this.getCodeLineList().get(lineIndex);
			if (bdCodeLine.hasCodeBlock()){
				resultList.addAll(bdCodeLine.getCodeBlock().generate4Service(mList));
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
	 * ドメインクラスが1つも定義されていない場合もServiceMethodListPageは生成するため
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
	 * ドメインクラスが1つも定義されていない場合もServiceMethodListPageは生成するため
	 * @param c
	 * @return
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
	private static final String BLOCK_KEY_Method = "Method";

	/** 
	 * 各サブクラスでOverrideする
	 */
	@Override
	protected BdBaseCodeBlock createCodeBlock(String key, List<BdCodeLine> lineListOfBlock){
		BdBaseCodeBlock result = null;
		if (BLOCK_KEY_Method.equals(key)){
			result = new BdCodeBlockJavaServiceMethodListPageMethod(lineListOfBlock);
		} else {
			key = (key == null || "".equals(key)) ? "BLOCKキーなし" : key;
			NdLogger.getInstance().error(this.getClass().getName() + " : [WARNING] Unknown Key = " + key, null);
		}
		return result;
	}
}
