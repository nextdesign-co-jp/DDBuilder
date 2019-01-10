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
import jp.co.nextdesign.ddb.core.user.BdUserClass;

/**
 * BdCodeBlockDomainClassListPageHtmlEntityとBdCodeBlockDomainClassListPageJavaEntityの基底クラス
 * クラスを基にソースを生成する動きはHtmlとJava側で共通である
 * @author murayama
 *
 */
public abstract class BdCodeBlockBaseDomainClassListPageEntity extends BdBaseCodeBlock {

	/** コンストラクタ */
	public BdCodeBlockBaseDomainClassListPageEntity(List<BdCodeLine> lineList){
		super(lineList);
	}
	
	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(List<BdUserClass> cList) {
		List<String> resultList = new ArrayList<String>();
		BdBaseCodeCase codeCase = this.getCodeCase(BdBaseCodeCase.KEY_All);
		if (codeCase != null){
			for(BdUserClass c : cList){
				resultList.addAll(codeCase.generate(c));
			}
		}
		return resultList;
	}

	/**
	 * 属性からCodeCaseを応答する
	 * @return
	 */
	private BdBaseCodeCase getCodeCase(String caseKey){
		BdBaseCodeCase result = null;
		if (codeCaseMap.containsKey(caseKey)){
			result = codeCaseMap.get(caseKey);
		}
		return result;
	}
}
