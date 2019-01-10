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
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodParameter;

public class BdCodeCaseJavaInvokeParametersAll extends BdBaseCodeCase {

	/**
	 * コンストラクタ
	 * @param lineListOfCase
	 */
	public BdCodeCaseJavaInvokeParametersAll(List<BdCodeLine> lineListOfCase){
		super(lineListOfCase);
	}
	
	private static final String TAB = "				";
	private static final String COMMA = ",";
	
	/** 引数に対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserServiceMethod m){
		List<String> resultList = new ArrayList<String>();
		List<BdUserServiceMethodParameter> pList = m.getServiceMethodParameterList();
		if (pList.size() > 0){
			resultList.add(TAB + pList.get(0).getName());
		}
		for(int i=1; i<pList.size(); i++){
			resultList.add(TAB + COMMA + pList.get(i).getName());
		}
		return resultList;
	}
}

