/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core;

import java.io.File;

public class BdBuilderUtil {

	/**
	 * GroupIdに相当するディレクトリ名列を応答する
	 * @return
	 */
	public static String packageString2PathString(String packageString){
		String result = "";
		String[] splitted = packageString.split("\\.");
		if (splitted != null && splitted.length > 0){
			result = splitted[0];
			for(int i=1; i<splitted.length; i++){
				result += File.separator + splitted[i]; 
			}
		}
		return result;
	}
}
