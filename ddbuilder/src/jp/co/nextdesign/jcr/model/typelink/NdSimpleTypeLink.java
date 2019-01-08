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

import org.eclipse.jdt.core.dom.SimpleType;

/**
 * Simple型
 * TypeName
 * （例）A.B.C
 * SimpleName#getName()=A.B.C : QualifiedName
 * @author murayama
 */
public class NdSimpleTypeLink extends NdAbstractTypeLink {

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
	 * SimpleTypeか否かを応答する
	 */
	@Override
	public boolean isSimpleType(){
		return true;
	}

	/**
	 * コンストラクタ
	 * @param jdtType
	 */
	public NdSimpleTypeLink(SimpleType jdtType){
		super(jdtType);
	}
	
	/** デバッグ */
	public void debugPrint(String tab){
		if (!NdLogger.getInstance().getDebugLogging()) return;
		NdLogger.getInstance().debug(tab + this.getDebugTitle());
	}
}
