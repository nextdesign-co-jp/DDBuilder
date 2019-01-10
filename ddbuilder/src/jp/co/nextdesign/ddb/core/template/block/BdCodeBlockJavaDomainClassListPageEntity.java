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

import java.util.List;

import jp.co.nextdesign.ddb.core.template.BdCodeLine;
import jp.co.nextdesign.ddb.core.template.cases.BdBaseCodeCase;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaDomainClassListPageEntityAll;
import jp.co.nextdesign.util.logging.NdLogger;

public class BdCodeBlockJavaDomainClassListPageEntity extends BdCodeBlockBaseDomainClassListPageEntity {

	/** コンストラクタ */
	public BdCodeBlockJavaDomainClassListPageEntity(List<BdCodeLine> lineList){
		super(lineList);
	}
	
	/** 
	 * 各サブクラスでOverrideする
	 * このBLOCKは、key=Entity, CASE key=Allのみを含む
	 */
	@Override
	protected BdBaseCodeCase createCodeCase(String key, List<BdCodeLine> lineListOfCase){
		BdBaseCodeCase result = null;
		if (BdBaseCodeCase.KEY_All.equals(key)){
			result = new BdCodeCaseJavaDomainClassListPageEntityAll(lineListOfCase);
		} else {
			key = key == null ? "null" : key;
			NdLogger.getInstance().error(this.getClass().getName() + " : [WARNING] Unknown Key = " + key, null);
		}
		return result;
	}
}
