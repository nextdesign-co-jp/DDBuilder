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
package jp.co.nextdesign.ddb.core.template.block;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.ddb.core.template.BdCodeLine;
import jp.co.nextdesign.ddb.core.template.cases.BdBaseCodeCase;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaServiceFetchCollection;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaServiceFetchSimple;
import jp.co.nextdesign.ddb.core.user.BdUserAttribute;
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.util.logging.NdLogger;

public class BdCodeBlockJavaServiceFetch extends BdBaseCodeBlock {

	/** コンストラクタ */
	public BdCodeBlockJavaServiceFetch(List<BdCodeLine> lineList){
		super(lineList);
	}
	
	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(BdUserClass c) {
		List<String> resultList = new ArrayList<String>();
		for(BdUserAttribute a : c.getAttributeList()){
			if (a.isDdbEmbedded() || a.isDdbManyToOne() || a.isDdbOneToMany() || a.isDdbManyToMany()){
				BdBaseCodeCase codeCase = null;
				if (a.isDdbEmbedded() || a.isDdbManyToOne()){
					codeCase = this.getCodeCaseByKey(CASE_KEY_Simple);
				} else if (a.isDdbOneToMany() || a.isDdbManyToMany()){
					codeCase = this.getCodeCaseByKey(CASE_KEY_Collection);
				}
				if (codeCase != null){
					resultList.addAll(codeCase.generate(a));
				} else{
					NdLogger.getInstance().error(c.getName() + "." + a.getName() + "CodeCaseが見つからない", null);
				}
			}
		}
		return resultList;
	}
	
	//このBLOCKに含まれるCASEキー
	private static final String CASE_KEY_Collection = "Collection";
	private static final String CASE_KEY_Simple = "Simple";
	
	/** 
	 * 各サブクラスでOverrideする
	 */
	@Override
	protected BdBaseCodeCase createCodeCase(String key, List<BdCodeLine> lineListOfCase){
		BdBaseCodeCase result = null;
		if (CASE_KEY_Simple.equals(key)){
			result = new BdCodeCaseJavaServiceFetchSimple(lineListOfCase);
		} else if (CASE_KEY_Collection.equals(key)){
			result = new BdCodeCaseJavaServiceFetchCollection(lineListOfCase);
		} else if (KEY_Nop.equals(key)){
		} else {
			key = (key == null || "".equals(key)) ? "CASEキーなし" : key;
			NdLogger.getInstance().error(this.getClass().getName() + " : [WARNING] Unknown Key = " + key, null);
		}
		return result;
	}
}
