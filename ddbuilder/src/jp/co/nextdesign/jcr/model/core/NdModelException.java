/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

import jp.co.nextdesign.jcr.NdBaseException;

/**
 * modelパッケージ内の例外
 * @author murayama
 *
 */
public class NdModelException extends NdBaseException {
	
	private static final long serialVersionUID = 1L;

	/** コンストラクタ */
	public NdModelException(String applMessage, Exception origin){
		super(applMessage, origin);
	}
	
	/** コンストラクタ */
	public NdModelException(String applMessage){
		super(applMessage);
	}
}
