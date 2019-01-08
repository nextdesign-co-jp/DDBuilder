/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.parser;

import jp.co.nextdesign.jcr.NdBaseException;

/**
 * パーサー例外
 * @author murayama
 *
 */
public class NdParserException extends NdBaseException {
	
	private static final long serialVersionUID = 1L;

	/** コンストラクタ */
	public NdParserException(String applMessage, Exception origin){
		super(applMessage, origin);
	}
	
	/** コンストラクタ */
	public NdParserException(String applMessage){
		super(applMessage);
	}
}
