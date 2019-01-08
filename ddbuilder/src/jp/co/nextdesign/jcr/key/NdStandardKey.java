/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.key;



public class NdStandardKey extends NdAbstractKey {

	private static final long serialVersionUID = 6L;
	
	/**
	 * コンストラクタ
	 */
	public NdStandardKey(int version, String userName, String userName4Name){
		super();
		this.version = version;
		this.userName = userName;
		this.userName4Footer = userName4Name;
	}
}
