/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.documentation;

import jp.co.nextdesign.jcr.NdBaseException;

public class NdDocumentationException extends NdBaseException {

	private static final long serialVersionUID = 1L;
	
	/** コンストラクタ */
	public NdDocumentationException(String applMessage, Exception origin){
		super(applMessage, origin);
	}
	
	/** コンストラクタ */
	public NdDocumentationException(String applMessage){
		super(applMessage);
	}	

}
