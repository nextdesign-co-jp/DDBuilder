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
package jp.co.nextdesign.ddb.core.template.cases;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.ddb.core.template.BdCodeLine;
import jp.co.nextdesign.ddb.core.user.BdUserAttribute;
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.util.BdStringHelper;
import jp.co.nextdesign.util.BdStringHelperReplacementPattern;

public class BdCodeCaseJavaFieldManyToMany extends BdBaseCodeCase {
	/**
	 * コンストラクタ
	 * @param lineListOfCase
	 */
	public BdCodeCaseJavaFieldManyToMany(List<BdCodeLine> lineListOfCase){
		super(lineListOfCase);
	}

	/** 属性に対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserAttribute a){
		List<String> resultList = new ArrayList<String>();
		List<BdStringHelperReplacementPattern> replacementPatternList = this.createReplacementPatternList(a);
		for(BdCodeLine codeLine : this.getCodeLineList()){
			resultList.add(BdStringHelper.replace(codeLine.getLine(), replacementPatternList));
		}
		return resultList;
	}

	/** 文字置換パターンを作成する */
	private List<BdStringHelperReplacementPattern> createReplacementPatternList(BdUserAttribute a){
		List<BdStringHelperReplacementPattern> resultList = new ArrayList<BdStringHelperReplacementPattern>();
		//BdCodeCaseJavaBeforeAfterListManyToManyの置き換えパターンと一致すること　ここから
		resultList.add(new BdStringHelperReplacementPattern("OtherSideBookList", "OtherSideOfThis" + a.getUpperStartedName())); //自分の属性名でユニックになる
		resultList.add(new BdStringHelperReplacementPattern("ddbBeforeStoreList", "ddbBefore" + a.getUpperStartedName()));
		resultList.add(new BdStringHelperReplacementPattern("ddbAddStoreList", "ddbAdd" + a.getUpperStartedName()));
		resultList.add(new BdStringHelperReplacementPattern("ddbRemoveStoreList", "ddbRemove" + a.getUpperStartedName()));
		//BdCodeCaseJavaBeforeAfterListManyToManyの置き換えパターンと一致すること　ここまで
		resultList.add(new BdStringHelperReplacementPattern("book", a.getBdUserClass().getLowerStartedName()));
		resultList.add(new BdStringHelperReplacementPattern("Book", a.getBdUserClass().getName()));
		resultList.add(new BdStringHelperReplacementPattern("storeList", a.getName()));
		resultList.add(new BdStringHelperReplacementPattern("StoreList", a.getUpperStartedName()));
		BdUserClass collectionElementClass = a.getCollectionElementBdUserClass();
		if (collectionElementClass != null){
			resultList.add(new BdStringHelperReplacementPattern("store", collectionElementClass.getLowerStartedName()));
			resultList.add(new BdStringHelperReplacementPattern("Store", collectionElementClass.getName()));
		}
		return resultList;
	}
}
