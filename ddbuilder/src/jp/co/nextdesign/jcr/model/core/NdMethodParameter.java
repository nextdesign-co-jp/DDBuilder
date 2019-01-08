/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

import jp.co.nextdesign.jcr.model.typelink.NdAbstractTypeLink;
import jp.co.nextdesign.util.logging.NdLogger;


/**
 * メソッドパラメタ
 * @author murayama
 */
public class NdMethodParameter {

	private String name;
	private NdAbstractTypeLink parameterType;
	private boolean isVarargs;

	/**
	 * 名前を応答する
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 名前を設定する
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * パラメータの型を応答する
	 * @return
	 */
	public NdAbstractTypeLink getParameterType() {
		return parameterType;
	}
	
	/**
	 * パラメータの型を設定する
	 * @param parameterType
	 */
	public void setParameterType(NdAbstractTypeLink parameterType) {
		this.parameterType = parameterType;
	}

	/**
	 * 可変引数か否かを応答する
	 * @return
	 */
	public boolean isVarargs() {
		return isVarargs;
	}
	
	/**
	 * 可変引数か否かを設定する
	 * @param isVarargs
	 */
	public void setVarargs(boolean isVarargs) {
		this.isVarargs = isVarargs;
	}

	/**
	 * コンストラクタ
	 */
	public NdMethodParameter(){
		super();
		name = "";
		this.parameterType = NdAbstractTypeLink.UNDEFINED;
		this.isVarargs = false;
	}
	
	/** デバッグ */
	public void debugPrint(String tab){
		if (!NdLogger.getInstance().getDebugLogging()) return;
		String tab2 = tab + this.getName() + " ";
		this.getParameterType().debugPrint(tab2);
	}
}
