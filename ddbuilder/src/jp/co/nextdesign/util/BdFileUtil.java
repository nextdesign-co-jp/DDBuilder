/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.util;

import java.io.File;

public class BdFileUtil {

	/** HTMLファイルか否か */
	public static  boolean isHtmlFile(File f){
		return f != null && f.getName().toLowerCase().endsWith(".html");
	}
	
	/** 画像ファイルか否か */
	public static boolean isImageFile(File f){
		boolean result = false;
		if (f != null){
			String lowerName = f.getName().toLowerCase();
			result = lowerName.endsWith(".png") || lowerName.endsWith(".gif");
		}
		return result;
	}
}
