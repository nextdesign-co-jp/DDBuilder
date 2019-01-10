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
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaInvokeToResultPageNotVoid;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaInvokeToResultPageVoid;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethod;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * BdBaseCodeBlockを継承していることに注意
 * @author murayama
 *
 */
public class BdCodeBlockJavaInvokeToResultPage extends BdBaseCodeBlock {

	/** コンストラクタ */
	public BdCodeBlockJavaInvokeToResultPage(List<BdCodeLine> lineList){
		super(lineList);
	}

	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(BdUserServiceMethod m) {
		List<String> resultList = new ArrayList<String>();
		BdBaseCodeCase codeCase = null;
		if (m.isReturnVoid()){
			codeCase = this.getCodeCaseByKey(KEY_Void);
		} else {
			codeCase = this.getCodeCaseByKey(KEY_NotVoid);
		}
		if (codeCase != null){
			resultList.addAll(codeCase.generate(m));
		} else {
			NdLogger.getInstance().error(KEY_Void + " AND " + KEY_NotVoid + " **IS UNKNOWN CodeCase**.", null);
		}
		return resultList;
	}

	private static final String KEY_Void = "Void";
	private static final String KEY_NotVoid = "NotVoid";

	/** 
	 * 各サブクラスでOverrideする
	 */
	@Override
	protected BdBaseCodeCase createCodeCase(String key, List<BdCodeLine> lineListOfCase){
		BdBaseCodeCase result = null;
		if (KEY_Void.equals(key)){
			result = new BdCodeCaseJavaInvokeToResultPageVoid(lineListOfCase);
		} else if (KEY_NotVoid.equals(key)){
			result = new BdCodeCaseJavaInvokeToResultPageNotVoid(lineListOfCase);
		} else if (KEY_Nop.equals(key)){
		} else {
			key = (key == null || "".equals(key)) ? "CASEキーなし" : key;
			NdLogger.getInstance().error(this.getClass().getName() + " : [WARNING] Unknown Key = " + key, null);
		}
		return result;
	}
}
