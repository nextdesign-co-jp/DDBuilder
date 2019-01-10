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
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethod;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodResult;

public class BdCodeCaseJavaImportBigDecimal extends BdBaseCodeCase {

	/**
	 * コンストラクタ
	 * @param lineListOfCase
	 */
	public BdCodeCaseJavaImportBigDecimal(List<BdCodeLine> lineListOfCase){
		super(lineListOfCase);
	}

	/** 属性に対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserClass c){
		List<String> resultList = new ArrayList<String>();
		for(String line : c.getImportList()){
			if (line != null && line.endsWith(".BigDecimal")){
				resultList.add("import " + line + ";");
			}
		}
		return resultList;
	}

	/** サービスメソッドに対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserServiceMethod m){
		List<String> resultList = new ArrayList<String>();
		for(String line : m.getBdServiceClass().getImportList()){
			if (line != null && line.endsWith(".BigDecimal")){
				resultList.add("import " + line + ";");
			}
		}
		return resultList;
	}

	/** サービスメソッド戻り型に対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserServiceMethodResult r){
		List<String> resultList = new ArrayList<String>();
		if (r.isDdbBigDecimal()){
			for(BdCodeLine line : this.getCodeLineList()){
				resultList.add(line.getLine());
			}
		}
		return resultList;
	}
}
