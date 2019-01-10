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
import java.util.Collections;
import java.util.List;

import jp.co.nextdesign.ddb.core.template.BdCodeLine;
import jp.co.nextdesign.ddb.core.template.BdTemplateProject;
import jp.co.nextdesign.ddb.core.user.BdUserAttribute;
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodParameter;

public class BdCodeCaseJavaImportMultipleSelectPanel extends BdBaseCodeCase {

	/**
	 * コンストラクタ
	 * @param lineListOfCase
	 */
	public BdCodeCaseJavaImportMultipleSelectPanel(List<BdCodeLine> lineListOfCase){
		super(lineListOfCase);
	}

	/** 属性に対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserAttribute a){
		List<String> resultList = new ArrayList<String>();
		resultList.add("import " + a.getBdUserClass().getFullName() + ";");		
		BdUserClass collectionElementClass = a.getCollectionElementBdUserClass();
		if (collectionElementClass != null){
			resultList.add("import " + collectionElementClass.getFullName() + ";");
			resultList.add("import " + a.getBdUserClass().getGroupId() + BdTemplateProject.DDB_SERVICE_PACKAGE_AND_HEADER + collectionElementClass.getName() + "Service;");
		}
		Collections.sort(resultList);
		return resultList;
	}

	/** 引数に対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserServiceMethodParameter p){
		List<String> resultList = new ArrayList<String>();
		resultList.add("import " + p.getBdUserServiceMethod().getBdServiceClass().getBdUserClass().getFullName() + ";");		
		BdUserClass collectionElementClass = p.getCollectionElementBdUserClass();
		if (collectionElementClass != null){
			resultList.add("import " + collectionElementClass.getFullName() + ";");
			resultList.add("import " + p.getBdUserServiceMethod().getBdServiceClass().getBdUserClass().getGroupId() + BdTemplateProject.DDB_SERVICE_PACKAGE_AND_HEADER + collectionElementClass.getName() + "Service;");
		}
		Collections.sort(resultList);
		return resultList;
	}
}
