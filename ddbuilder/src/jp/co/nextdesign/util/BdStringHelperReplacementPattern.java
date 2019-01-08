/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.util;

/**
 * BdStringHelper#replaceメソッド用文字置換パターン
 * @author murayama
 *
 */
public class BdStringHelperReplacementPattern {
	private String oldString;
	private String newString;
	
	public BdStringHelperReplacementPattern(String oldString, String newString){
		super();
		this.oldString = oldString;
		this.newString = newString;
	}
	public String getOldString() {
		return oldString;
	}
	public String getNewString() {
		return newString;
	}
}
