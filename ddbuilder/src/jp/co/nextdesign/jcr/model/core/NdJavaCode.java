/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Javaコード
 * @author murayama
 */
public class NdJavaCode {

	public static final String CR = "\n";
	public static final String TAIL = ";\n";


	/**
	 * 改行コードで分離した行リストを応答する
	 * @param blockString
	 * @return
	 */
	public static List<String> getLineListOf(String blockString){
		List<String> resultList = new ArrayList<String>();
		if (blockString != null){
			String[] splitted = blockString.split(CR);
			for (String line : splitted){
				if ((line != null) && (line.length() > 0)){
					resultList.add(line);
				}
			}
		}
		return resultList;
	}
	
	/**
	 * 末尾文字列を削除する
	 * @param codeString
	 * @return
	 */
	public static String removeTailFrom(String codeString){
		String result = codeString;
		if ((codeString != null) && (codeString.endsWith(TAIL))){
			result = codeString.substring(0, codeString.length() - TAIL.length());
		}
		return result;
	}
}
