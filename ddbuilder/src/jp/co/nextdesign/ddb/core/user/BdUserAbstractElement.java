/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.user;

import jp.co.nextdesign.jcr.model.core.NdJavadoc;

public abstract class BdUserAbstractElement {

	protected BdUserAbstractElement(){
		super();
	}

	protected String formatJavadoc(NdJavadoc javadoc){
		String result = "---";
		if (javadoc != null){
			result = javadoc.getFirstLine().replace("*", "").trim();
			String[] splitted = result.split(" ");
			if (splitted != null && splitted.length > 0){
				result = splitted[0];
			}
		}
		return result;
	}
}
