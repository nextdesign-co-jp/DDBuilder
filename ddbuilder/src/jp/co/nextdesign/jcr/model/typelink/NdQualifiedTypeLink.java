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

import org.eclipse.jdt.core.dom.QualifiedType;

/**
 * Qualified型
 * Type . SimpleName
 * 実際にはSimpleTypeまたはParameterizedTypeの何れかで表現されるので、Qualifiedはありえない。
 * 例： A.B や A.B<T> は各々SimpleType、ParameterizedTypeで表現される。
 * @author murayama
 */
public class NdQualifiedTypeLink extends NdAbstractTypeLink {

	/**
	 * 参照しているタイプ名リストを応答する
	 * @return
	 */
	@Override public List<NdName> getReferenceList(){
		List<NdName> resultList = new ArrayList<NdName>();
		resultList.add(this.getTypeName());
		return resultList;
	}

	/**
	 * QualifiedTypeか否かを応答する
	 */
	@Override
	public boolean isQualifiedType(){
		return true;
	}
	
	/**
	 * コンストラクタ
	 * @param jdtType
	 */
	public NdQualifiedTypeLink(QualifiedType jdtType){
		super(jdtType);
	}

	/** デバッグ */
	public void debugPrint(String tab){
		if (!NdLogger.getInstance().getDebugLogging()) return;
		
	}
}
