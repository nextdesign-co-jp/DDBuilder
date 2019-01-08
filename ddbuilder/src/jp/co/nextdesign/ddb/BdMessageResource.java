/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb;

import java.util.ResourceBundle;

/**
 * メッセージリソース
 * @author murayama
 *
 */
public class BdMessageResource {

	private static ResourceBundle resouceBundle;
	static {
		resouceBundle = ResourceBundle.getBundle("resources.messages");
	}
		
	public static String get(String key){
		String result = "";
		if (resouceBundle != null){
			try{
				result =  resouceBundle.getString(key);
			} catch(Exception ex) {
				result = "";
			}
		}
		return result;
	}
	
}
