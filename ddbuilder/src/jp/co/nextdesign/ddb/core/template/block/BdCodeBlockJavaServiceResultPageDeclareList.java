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
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaServiceResultPageDeclareListAll;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaServiceResultPageDeclareListDate;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodResult;
import jp.co.nextdesign.util.logging.NdLogger;

public class BdCodeBlockJavaServiceResultPageDeclareList extends BdCodeBlockBaseField {

	/** コンストラクタ */
	public BdCodeBlockJavaServiceResultPageDeclareList(List<BdCodeLine> lineList){
		super(lineList);
	}

	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(BdUserServiceMethodResult r) {
		List<String> resultList = new ArrayList<String>();
		if (r.isDdbDate()){
			resultList.addAll(this.generateForKey(KEY_Date, r));
		}
		if (r.isDdbBigDecimal()){
			resultList.addAll(this.generateForKey(KEY_BigDecimal, r));
		}
		resultList.addAll(this.generateForKey(KEY_All, r));
		return resultList;
	}

	private List<String> generateForKey(String key, BdUserServiceMethodResult r){
		List<String> resultList = new ArrayList<String>();
		BdBaseCodeCase codeCase = this.getCodeCaseByKey(key);
		if (codeCase != null){
			resultList.addAll(codeCase.generate(r));
		} else {
			NdLogger.getInstance().error(key + " **IS UNKNOWN CodeCase**.", null);
		}
		return resultList;
	}

	private static final String KEY_All = "All";
	private static final String KEY_Date = "Date";
	private static final String KEY_BigDecimal = "BigDecimal";

	/** 
	 * 各サブクラスでOverrideする
	 */
	@Override
	protected BdBaseCodeCase createCodeCase(String key, List<BdCodeLine> lineListOfCase){
		BdBaseCodeCase result = null;
		if (KEY_All.equals(key)){
			result = new BdCodeCaseJavaServiceResultPageDeclareListAll(lineListOfCase);
		} else if (KEY_Date.equals(key)){
			result = new BdCodeCaseJavaServiceResultPageDeclareListDate(lineListOfCase);
		} else if (KEY_Nop.equals(key)){
		} else {
			key = (key == null || "".equals(key)) ? "CASEキーなし" : key;
			NdLogger.getInstance().error(this.getClass().getName() + " : [WARNING] Unknown Key = " + key, null);
		}
		return result;
	}
}
