/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.key;

import java.io.Serializable;
import java.util.Calendar;

import jp.co.nextdesign.jcr.NdConstants;

public abstract class NdAbstractKey implements Serializable {
	
	private static final long serialVersionUID = 6L;
	protected int version;
	protected String userName4Footer;
	protected String userName;
	protected String creation;
	
	/**
	 * コンストラクタ
	 */
	public NdAbstractKey(){
		super();
		this.version = -1;
		this.userName4Footer = "";
		this.userName = "評価用";
		this.creation = Calendar.getInstance().getTime().toString();
	}
	
	/**
	 * 有効か否かを応答する
	 * メジャー番号だけで判定する。
	 * マイナー番号はリリース管理のために使用する。
	 * @return
	 */
	public boolean isAvailable(){
		if (this.version < NdConstants.VERSION_MAJOR){
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 生成日情報を応答する
	 * @return
	 */
	public String getCreation(){
		return this.creation;
	}
	
	/**
	 * ユーザ名を応答する
	 * @return
	 */
	public String getUserName(){
		return this.userName;
	}
	
	/**
	 * フッター用ユーザ名を応答する
	 * @return
	 */
	public String getUserName4Footer(){
		return this.userName4Footer;
	}
	
	/**
	 * バージョンを応答する
	 * @return
	 */
	public int getVersion(){
		return this.version;
	}
}
