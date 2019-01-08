/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

public abstract class NdExtendedModifier {

	protected String keyword;
	
	/**
	 * アノテーションか否かを応答する
	 * @return
	 */
	public boolean isAnnoteation(){
		return false;
	}
	
	/**
	 * モディファイアか否かを応答する
	 * @return
	 */
	public boolean isModifier(){
		return false;
	}
	
	/**
	 * キーワードを応答する
	 */
	public String getKeyword(){
		return this.keyword;
	}
	
	/**
	 * コンストラクタ
	 * @param keyword
	 */
	public NdExtendedModifier(String keyword){
		this.keyword = keyword;
	}
}
