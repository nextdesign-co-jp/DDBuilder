/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.typelink;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.jcr.model.NdName;
import jp.co.nextdesign.util.logging.NdLogger;

import org.eclipse.jdt.core.dom.PrimitiveType;

/**
 * Primitive型
 * @author murayama
 */
public class NdPrimitiveTypeLink extends NdAbstractTypeLink {
	
	private static final List<NdName> empyuReferenceList = new ArrayList<NdName>();

	/**
	 * 参照しているタイプ名リストを応答する（常に空リストを応答する）
	 * Primitive型の名前は対象外なので空リストを応答する
	 * @return
	 */
	@Override public List<NdName> getReferenceList(){
		return empyuReferenceList;
	}

	/**
	 * PrimitiveTypeか否かを応答する
	 */
	@Override
	public boolean isPrimitiveType(){
		return true;
	}

	/**
	 * コンストラクタ
	 * @param jdtType
	 */
	public NdPrimitiveTypeLink(PrimitiveType jdtType){
		super(jdtType);
	}

	/** デバッグ */
	public void debugPrint(String tab){
		if (!NdLogger.getInstance().getDebugLogging()) return;
		NdLogger.getInstance().debug(tab + this.getDebugTitle());
	}
}
