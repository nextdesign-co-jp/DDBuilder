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
import java.util.Iterator;
import java.util.List;

import jp.co.nextdesign.ddb.core.template.BdCodeLine;
import jp.co.nextdesign.ddb.core.template.cases.BdBaseCodeCase;
import jp.co.nextdesign.ddb.core.user.BdUserAttribute;
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethod;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodParameter;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodResult;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * BdCodeBlockEditPageHtmlFieldとBdCodeBlockEditPageJavaFieldの基底クラス
 *  属性のDDB分類を基にソースを生成する動きはHtmlとJava側で共通である
 * @author murayama
 *
 */
public abstract class BdCodeBlockBaseField extends BdBaseCodeBlock {

	/** コンストラクタ */
	public BdCodeBlockBaseField(List<BdCodeLine> lineList){
		super(lineList);
	}
	
	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(BdUserClass c) {
		List<String> resultList = new ArrayList<String>();
		for(BdUserAttribute a : c.getAttributeList()){			
			BdBaseCodeCase codeCase = this.getCodeCaseByAttribute(a);
			if (codeCase != null){
				resultList.addAll(codeCase.generate(a));
			} else {
				if ("".equals(a.getDdbTypeKey())){
					String log = "[**NOT SUPPORTED**. ATTRIBUTE TYPE]：" + c.getName() + " AT " + a.getName();
					NdLogger.getInstance().error(log, null);
				}
			}
		}
		return resultList;
	}

	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(BdUserServiceMethod m) {
		List<String> resultList = new ArrayList<String>();
		for(BdUserServiceMethodParameter p : m.getServiceMethodParameterList()){			
			BdBaseCodeCase codeCase = this.getCodeCaseByMethodParameter(p);
			if (codeCase != null){
				resultList.addAll(codeCase.generate(p));
			} else {
				String log = "[**NOT SUPPORTED**. PARAMETER TYPE]：" + p.getBdUserServiceMethod().getBdServiceClass().getName() + " AT " + p.getBdUserServiceMethod().getName() + " AT " + p.getName();
				NdLogger.getInstance().error(log, null);
			}
		}
		return resultList;
	}

	/**
	 * 属性からCodeCaseを応答する
	 * @param a
	 * @return
	 */
	private BdBaseCodeCase getCodeCaseByAttribute(BdUserAttribute a){
		return this.getCodeCaseByKey(a.getDdbTypeKey());
	}

	/**
	 * 引数からCodeCaseを応答する
	 * @param a
	 * @return
	 */
	private BdBaseCodeCase getCodeCaseByMethodParameter(BdUserServiceMethodParameter p){
		return this.getCodeCaseByKey(p.getDdbTypeKey());
	}
	
	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(BdUserServiceMethodResult  r) {
		List<String> resultList = new ArrayList<String>();
		BdBaseCodeCase codeCase = this.getCodeCaseByKey(r.getDdbTypeKey());
		if (codeCase != null){
			resultList.addAll(codeCase.generate(r));
		} else {
			String log = "[**NOT SUPPORTED**. RETURN VALUE TYPE]：" + r.getBdUserServiceMethod().getBdServiceClass().getName() + " AT " +  r.getBdUserServiceMethod().getName();
			NdLogger.getInstance().error(log, null);
		}
		return resultList;
	}
}
