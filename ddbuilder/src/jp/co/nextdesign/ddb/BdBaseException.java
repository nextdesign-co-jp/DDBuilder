/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb;

/**
 * DDBuilder　すべての例外の基底クラス
 * @author murayama
 *
 */
public abstract class BdBaseException extends Exception {
	private static final long serialVersionUID = 1L;
	private Exception origin;
	private String applMessage;
	
	/** コンストラクタ */
	public BdBaseException(String applMessage, Exception origin){
		super(origin);
		this.applMessage = applMessage;
		this.origin = origin;
	}
	
	/** コンストラクタ */
	public BdBaseException(String applMessage){
		super();
		this.applMessage = applMessage;
	}

	@Override
	public String toString(){
		String result = BdMessageResource.get("dialog.title.message") + applMessage; //"メッセージ：" + applMessage;
		if (origin != null){
			result += BdConstants.CR + origin.toString();
		}
		result += BdConstants.CR + super.toString();
		return result;
	}
	
	/** アプリケーションメッセージを応答する */
	public String getApplMessage(){
		return this.applMessage;
	}
}
