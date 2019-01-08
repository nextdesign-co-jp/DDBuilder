/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.util;

import jp.co.nextdesign.ddb.BdConstants;

/**
 * Nextdesign util パッケージ内の例外
 * @author murayama
 */
public class NdUtilException extends Exception {

	private static final long serialVersionUID = 1L;
	private Exception origin;
	private String applMessage;
	
	/** コンストラクタ */
	public NdUtilException(String applMessage, Exception origin){
		super(origin);
		this.applMessage = applMessage;
		this.origin = origin;
	}
	
	/** コンストラクタ */
	public NdUtilException(String applMessage){
		super();
		this.applMessage = applMessage;
	}

	@Override
	public String toString(){
		String result = applMessage;
		if (origin != null){
			result += BdConstants.CR + origin.toString();
		}
		result += BdConstants.CR + super.toString();
		return result;
	}
}
