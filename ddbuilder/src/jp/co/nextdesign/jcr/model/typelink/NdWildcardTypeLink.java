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

import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.WildcardType;

/**
 * Wildcard型
 * ? [ ( extends | super ) Type ]
 * 型引数が記述できる箇所にのみ宣言できる。
 * （例）public void method(Foo<? extends Date> param)
 * （例）ParameterizedTypeのargument
 * 倉庫<?, ? extends A, ? super A> wild1
 * ParameterizedType#typeArguments()は3つのWildcardTypeのリストをもつ。
 * @author murayama
 */
public class NdWildcardTypeLink extends NdAbstractTypeLink {

	private boolean isUpperBounds;
	private NdAbstractTypeLink bound;

	/**
	 * 参照しているタイプ名リストを応答する
	 * @return
	 */
	@Override public List<NdName> getReferenceList(){
		List<NdName> resultList = new ArrayList<NdName>();
		if (bound != null){
			resultList.addAll(bound.getReferenceList());
		}
		return resultList;
	}

	/**
	 * 境界からextendsかsuperかを設定する
	 * @param b
	 */
	public void setIsUpperBounds(boolean b){
		this.isUpperBounds = b;
	}
	
	/**
	 * 境界からextendsかsuperかを応答する
	 * @return
	 */
	public boolean isUpperBounds(){
		return this.isUpperBounds;
	}
	
	/**
	 * 境界クラスを応答する
	 * @return
	 */
	public NdAbstractTypeLink getBound() {
		return bound;
	}
	
	/**
	 * 境界クラスを設定する
	 * @param bound
	 */
	public void setBound(NdAbstractTypeLink bound) {
		this.bound = bound;
	}
	
	/**
	 * 境界クラスの指定があるか否かを応答する
	 */
	public boolean hasBound(){
		return this.bound != null;
	}
	
	/**
	 * WildcardTypeか否かを応答する
	 */
	@Override
	public boolean isWildcardType(){
		return true;
	}

	/**
	 * コンストラクタ
	 * @param jdtType
	 */
	public NdWildcardTypeLink(WildcardType jdtType){
		super(jdtType);
		this.isUpperBounds = jdtType.isUpperBound();
		if (jdtType.getBound() != null) {
			Type typeBound = jdtType.getBound();
			NdAbstractTypeLink typeLinkBound = NdTypeLinkFactory.createTypeLink(typeBound);
			this.bound = typeLinkBound;
		} else{
			this.bound = null;
		}
	}

	/** デバッグ */
	public void debugPrint(String tab){
		if (!NdLogger.getInstance().getDebugLogging()) return;
		String message = tab + "リンク:" + this.getClass().getSimpleName() + "->" + this.getDisplayName();
		if (this.hasBound()){
			message += " 境界指定:" + this.bound.getDebugTitle();
			message += " isUpperBounds=" + isUpperBounds;
		} else {
			message += " 境界指定なし";
		}
		NdLogger.getInstance().debug(message);
	}
}
