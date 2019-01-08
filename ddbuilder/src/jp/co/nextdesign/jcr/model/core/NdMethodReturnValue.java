/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

import jp.co.nextdesign.jcr.model.typelink.NdAbstractTypeLink;
import jp.co.nextdesign.util.logging.NdLogger;


/**
 * メソッド戻り値
 * @author murayama
 *
 */
public class NdMethodReturnValue {
	
	public static final NdMethodReturnValue UNDEFINED = new NdMethodReturnValue();
	private NdAbstractTypeLink returnValueType;

	/**
	 * 戻り値の型を応答する
	 * @return
	 */
	public NdAbstractTypeLink getReturnValueType() {
		return returnValueType;
	}
	
	/**
	 * 戻り値の型を設定する
	 * @param returnValueType
	 */
	public void setReturnValueType(NdAbstractTypeLink returnValueType) {
		this.returnValueType = returnValueType;
	}

	/**
	 * コンストラクタ
	 */
	public NdMethodReturnValue(){
		super();
		this.returnValueType = NdAbstractTypeLink.UNDEFINED;
	}
	
	/** デバッグ */
	public void debugPrint(String tab){
		if (!NdLogger.getInstance().getDebugLogging()) return;
		//NdLogger.getInstance().debug(tab + "戻り値" + this.getReturnValueType().getTypeName());
		this.getReturnValueType().debugPrint(tab + "戻り値:");
	}
}
