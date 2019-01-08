/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr;

/**
 * Jクラスレポート　すべての例外の基底クラス
 * @author murayama
 *
 */
public abstract class NdBaseException extends Exception {

	private static final long serialVersionUID = 1L;
	private Exception origin;
	private String applMessage;
	
	/** コンストラクタ */
	public NdBaseException(String applMessage, Exception origin){
		super(origin);
		this.applMessage = applMessage;
		this.origin = origin;
	}
	
	/** コンストラクタ */
	public NdBaseException(String applMessage){
		super();
		this.applMessage = applMessage;
	}

	@Override
	public String toString(){
		String result = "メッセージ：" + applMessage;
		if (origin != null){
			result += "\n" + origin.toString();
		}
		result += "\n" + super.toString();
		return result;
	}
}
