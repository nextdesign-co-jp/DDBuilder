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
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethod;
import jp.co.nextdesign.util.BdStringHelper;
import jp.co.nextdesign.util.BdStringHelperReplacementPattern;

public class BdCodeCaseJavaInvokeToResultPageVoid extends BdBaseCodeCase {

	/**
	 * コンストラクタ
	 * @param lineListOfCase
	 */
	public BdCodeCaseJavaInvokeToResultPageVoid(List<BdCodeLine> lineListOfCase){
		super(lineListOfCase);
	}
	
	/** 引数に対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserServiceMethod m){
		List<String> resultList = new ArrayList<String>();
		List<BdStringHelperReplacementPattern> replacementPatternList = this.createReplacementPatternList(m);
		for(BdCodeLine codeLine : this.getCodeLineList()){
			resultList.add(BdStringHelper.replace(codeLine.getLine(), replacementPatternList));
		}
		return resultList;
	}
	
	/** 文字置換パターンを作成する */
	private List<BdStringHelperReplacementPattern> createReplacementPatternList(BdUserServiceMethod m){
		List<BdStringHelperReplacementPattern> resultList = new ArrayList<BdStringHelperReplacementPattern>();
		resultList.add(new BdStringHelperReplacementPattern("BookServiceResultPageMethod1", m.getBdServiceClass().getName() + "ResultPage" + m.getUpperStartedName()));
		resultList.add(new BdStringHelperReplacementPattern("BookServiceInvokePageMethod1", m.getBdServiceClass().getName() + "InvokePage" + m.getUpperStartedName()));
		resultList.add(new BdStringHelperReplacementPattern("//this", "this"));
		resultList.add(new BdStringHelperReplacementPattern("メソッド名", m.getName()));
		return resultList;
	}

}

