/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.util;

import java.util.List;

/**
 * 文字列ヘルパー
 */
public class BdStringHelper {

	/**
	 * 文字列を指定された置換パターンで置き換えた結果を応答する。
	 * 置換パターンの置換前文字列と置換後文字列に同じ文字列があっても期待通りに置換できる。
	 * 文字列を複数回のString#replaceAllで置き換えていくと、例えば時間前文字列にBook、置換後文字列にもBookが含まれている場合、
	 * 期待通りの結果を得られない。その点を考慮した文字列置換を行う。
	 * @param inputLine
	 * @param replacementPatternList
	 * @return
	 */
	public static String replace(final String inputLine, final List<BdStringHelperReplacementPattern> replacementPatternList){
		String result = inputLine;
		for(int i=0; i<replacementPatternList.size(); i++){
			result = result.replaceAll(replacementPatternList.get(i).getOldString(), "%" + i + "%");
		}
		for(int i=0; i<replacementPatternList.size(); i++){
			result = result.replaceAll("%" + i + "%", replacementPatternList.get(i).getNewString());
		}
		return result;
	}
	
	/** HTMLエスケープされた文字列を応答する */
	public static String toHtmlEscaped(String in){
		String result = in;
		if (result != null){
			result = result.replaceAll("&" , "&amp;" );
			result = result.replaceAll("<" , "&lt;"  );
			result = result.replaceAll(">" , "&gt;"  );
			result = result.replaceAll("\"", "&quot;");
			result = result.replaceAll("'" , "&#39;" );
		}
		return result;
	}
	
	/** 先頭小文字の文字列にして応答する */
	public static  String toLowerStarted(String text){
		String result = text;
		if (result != null && result.length() > 0){
			if (result.length() > 1){
				result = result.substring(0, 1).toLowerCase() + result.substring(1);
			} else {
				result = result.toLowerCase();
			}
		}
		return result;
	}

}
