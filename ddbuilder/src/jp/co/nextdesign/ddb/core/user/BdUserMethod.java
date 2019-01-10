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
package jp.co.nextdesign.ddb.core.user;

import jp.co.nextdesign.jcr.model.core.NdMethod;

public class BdUserMethod extends BdUserAbstractElement {

	private NdMethod ndMethod;
	private BdUserClass bdClass;
	private BdUserMethodJavadoc bdUserMethodJavadoc;
	
	/**
	 * コンストラクタ
	 * @param ndClass
	 */
	public BdUserMethod(NdMethod ndMethod, BdUserClass bdClass){
		super();
		this.ndMethod = ndMethod;
		this.bdClass = bdClass;
		this.bdUserMethodJavadoc = new BdUserMethodJavadoc(this.getNdMethod().getJavadoc());
	}
	
	/** 引数のタイトルを応答する */
	public String getParameterTitle(String paramName){
		String result = "";
		if (this.bdUserMethodJavadoc != null){
			BdUserMethodJavadocInfo info = this.bdUserMethodJavadoc.getParameterInfoByName(paramName);
			if (info != null){
				result = info.getParamName() + info.getDescription();
			}
		}
		return result;
	}

	/** 戻り値のタイトルを応答する */
	public String getReturnTitle(){
		String result = "";
		if (this.bdUserMethodJavadoc != null){
			BdUserMethodJavadocInfo info = this.bdUserMethodJavadoc.getReturnInfo();
			if (info != null){
				result = info.getParamName() + info.getDescription();
			}
		}
		return result;
	}

	public BdUserMethodJavadoc getBdUserMethodJavadoc() {
		return bdUserMethodJavadoc;
	}

	public NdMethod getNdMethod() {
		return ndMethod;
	}

	public BdUserClass getBdClass() {
		return bdClass;
	}
}
