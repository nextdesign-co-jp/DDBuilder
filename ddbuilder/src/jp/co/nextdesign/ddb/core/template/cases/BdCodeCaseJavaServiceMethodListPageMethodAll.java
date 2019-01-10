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

public class BdCodeCaseJavaServiceMethodListPageMethodAll extends BdBaseCodeCase {
	/**
	 * コンストラクタ
	 * @param lineListOfCase
	 */
	public BdCodeCaseJavaServiceMethodListPageMethodAll(List<BdCodeLine> lineListOfCase){
		super(lineListOfCase);
	}

	/** クラスに対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserServiceMethod m){
		List<String> resultList = new ArrayList<String>();
		for(BdCodeLine line : this.getCodeLineList()){
			String customized = line.getLine().replaceAll("_info", m.getBdServiceClass().getBdUserClass().getLowerStartedName() + "_" + m.getName() + "_info");
			customized = customized.replaceAll("serviceClassName", m.getBdServiceClass().getName());
			customized = customized.replaceAll("serviceMethodTitle", m.getJavadocTitle());
			customized = customized.replaceAll("serviceMethodName", m.getName());
			customized = customized.replaceAll("returnValueTypeName", m.getReturnValueDisplayName());
			customized = customized.replaceAll("parameterNames", m.getMethodParametersDisplayName());
			customized = customized.replaceAll("BookServiceInvokePageMethod1", m.getBdServiceClass().getName() + "InvokePage" + m.getUpperStartedName());
			customized = customized.replaceAll("_nextPage", m.getBdServiceClass().getBdUserClass().getLowerStartedName() + "_" + m.getName() + "_nextPage");
			resultList.add(customized);
		}
		return resultList;
	}
}
