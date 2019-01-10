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

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.jcr.model.core.NdClass;

/**
 * ユーザ定義のサービスクラス
 * @author murayama
 *
 */
public class BdUserServiceClass  extends BdUserAbstractElement {

	private BdUserClass bdUserClass;
	private List<BdUserServiceMethod> serviceMethodList;

	/**
	 * コンストラクタ
	 * @param ndClass
	 */
	public BdUserServiceClass(BdUserClass bdUserClass){
		super();
		this.bdUserClass = bdUserClass;
		this.buildMethodList();
	}
	
	/** メソッドリストを構築する */
	private void buildMethodList(){
		this.serviceMethodList = new ArrayList<BdUserServiceMethod>();
		for(BdUserMethod bdUserMethod : this.bdUserClass.getPublicMethodList()){
			BdUserServiceMethod bdUserServiceMethod = new BdUserServiceMethod(bdUserMethod, this);
			this.serviceMethodList.add(bdUserServiceMethod);
			bdUserServiceMethod.checkDdbRule();
		}
	}

	/**
	 * クラス名を応答する
	 * @return
	 */
	public String getName(){
		return this.bdUserClass.getName();
	}

	/**
	 * 属性リストを応答する
	 * @return
	 */
	public List<BdUserAttribute> getAttributeList(){
		return null;
	}

	/**
	 * DDB分類Keyに該当する属性のリストを応答する
	 * @param ddbTypeKey
	 * @return
	 */
	public List<BdUserAttribute> getAttributeListByDdbTypeKey(String ddbTypeKey){
		return null;
	}

	/** 先頭小文字のクラス名を応答する */
	public String getLowerStartedName(){
		return null;
	}

	/**
	 * Javadocからタイトルを応答する
	 * @return
	 */
	public String getJavadocTitle(){
		return null;
	}

	/** groupId */
	public String getGroupId(){
		return null;
	}

	/** すべてのimport文を応答する(importキーワードを除いたもの) */
	public List<String> getImportList(){
		return this.bdUserClass.getImportList();
	}

	/**
	 * パッケージ名付きクラス名を応答する
	 * @return
	 */
	public String getFullName(){
		return null;
	}

	/** NdClassを応答する */
	public NdClass getNdClass(){
		return this.bdUserClass.getNdClass();
	}

	/** サービスメソッドリストを応答する */
	public List<BdUserServiceMethod> getServiceMethodList() {
		return serviceMethodList;
	}

	/** BdUserClassを応答する */
	public BdUserClass getBdUserClass() {
		return bdUserClass;
	}
}
