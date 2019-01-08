/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.key;

import jp.co.nextdesign.util.NdUtilException;

/**
 * Nextdesign キーに関する例外
 * @author murayama
 */
public class NdKeyException extends NdUtilException {

	private static final long serialVersionUID = 1L;
	
	/** コンストラクタ */
	public NdKeyException(String applMessage, Exception origin){
		super(applMessage, origin);
	}
	
	/** コンストラクタ */
	public NdKeyException(String applMessage){
		super(applMessage);
	}
}
