/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
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
