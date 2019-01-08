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

import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Type;

/**
 * Array型
 * Type []
 * （例） A[][][]の時
 * ArrayType#getElement()=A
 * ArrayType#getComponent()=A[][]
 * ArrayType#getDimension()=3
 * （例）倉庫<T,K,V>[]
 * ArrayType#getElement()=倉庫<T,K,V> : ParameterizedType
 * ArrayType#getComponent()=倉庫<T,K,V> : ParameterizedType
 * ArrayType#getDimension()=1
 * @author murayama
 */
public class NdArrayTypeLink extends NdAbstractTypeLink {
	
	private int dimension;
	//(例)A<T>[][][]の時、rootComponentTypeLinkはA<T>になる
	private NdAbstractTypeLink rootComponentTypeLink;
	
	/**
	 * 配列の次数を応答する DDB add 2014.4.19
	 * @return
	 */
	public int getDimension(){
		return this.dimension;
	}
	
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
	 * 表示用の型名を応答する
	 */
	@Override public String getDisplayName(){
		String result = "";
		if (this.rootComponentTypeLink != null){
			result = this.rootComponentTypeLink.getDisplayName();
		} else {
			result = super.getDisplayName();
		}
		for(int i=0; i<this.dimension; i++){
			result += "[]";
		}
		return result;
	}

	/**
	 * ArrayTypeか否かを応答する
	 */
	@Override
	public boolean isArrayType(){
		return true;
	}

	/**
	 * コンストラクタ
	 * @param jdtType
	 */
	public NdArrayTypeLink(ArrayType jdtType){
		super(jdtType);
		if ((jdtType != null) && (jdtType.getElementType() != null)){
			this.dimension = jdtType.getDimensions();			
			Type jdtComponentType = jdtType.getComponentType();
			if (jdtComponentType != null){
				Type jdtRootComponet = this.findRootComponent(jdtComponentType);
				if (jdtRootComponet != null){
					this.rootComponentTypeLink = NdTypeLinkFactory.createTypeLink(jdtRootComponet);
				}
			}
		}
		return;
	}
	
	/**
	 * Arrayではない型を探す
	 * A<Integer>[][][]の時、A<Integer>を見つける
	 * @param component
	 * @return
	 */
	private Type findRootComponent(Type component){
		Type result = component;
		if ((component != null) && (component.isArrayType())){
			Type next = ((ArrayType)component).getComponentType();
			if (next != null){
				result = findRootComponent(next);
			}
		}
		return result;
	}

	/** デバッグ */
	public void debugPrint(String tab){
		if (!NdLogger.getInstance().getDebugLogging()) return;
		NdLogger.getInstance().debug(tab + this.getDebugTitle());
	}
}
