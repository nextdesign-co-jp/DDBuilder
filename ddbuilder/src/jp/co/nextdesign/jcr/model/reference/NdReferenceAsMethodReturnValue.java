/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.reference;

import jp.co.nextdesign.jcr.model.NdName;


/**
 * 参照　メソッド戻り値としての参照
 * @author murayama
 */
public class NdReferenceAsMethodReturnValue extends NdAbstractReference {

	/**
	 * 参照の種類を応答する
	 * @return
	 */
	@Override public boolean isAsMethodReturnValue() {
		return true;
	}

	/**
	 * コンストラクタ
	 * @param referenceName
	 */
	public NdReferenceAsMethodReturnValue(NdName referenceName){
		super(referenceName);
	}
	/**
	 * 参照箇所を応答する
	 * @return
	 */
	public String getReferencePointName(){
		return "メソッド戻り値";
	}
}
