/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.typelink;

import java.util.List;

import jp.co.nextdesign.jcr.model.NdName;

import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.WildcardType;

/**
 * 型リンク情報の基底クラス
 * @author murayama
 *
 */
public abstract class NdAbstractTypeLink {
	
	public static final NdAbstractTypeLink UNDEFINED = new NdSimpleTypeLink(null);
	private NdName typeName;
	
	/**
	 * 参照しているタイプ名リストを応答する
	 * @return
	 */
	public abstract List<NdName> getReferenceList();
	
	/**
	 * 型名（NdName）を応答する
	 * @return
	 */
	public NdName getTypeName(){
		return this.typeName;
	}
	
	/**
	 * 型名（文字列）を応答する
	 * SimpleまたはQualified名を応答する
	 */
	public String getSimpleOrQualifiedName(){
		String result = "";
		if (this.typeName.isQualifiedName()){
			result = this.typeName.getQualifiedName();
		} else {
			result = this.typeName.getSimpleName();
		}
		return result;
	}
	
	/**
	 * 表示用の型名を応答する
	 */
	public String getDisplayName(){
		return this.getSimpleOrQualifiedName();
	}
	
	/**
	 * コンストラクタ
	 * @param jdtType
	 */
	public NdAbstractTypeLink(Type jdtType){
		super();
		if (jdtType != null){
			this.typeName = this.retrieveName(jdtType, 0);
		} else {
			this.typeName = new NdName();
		}
	}
	
	/**
	 * JDTタイプ別に名前を取り出す
	 * @param jdtType
	 * @return
	 */
	private NdName retrieveName(Type jdtType, int foreverLoopGuard){
		NdName result = new NdName();
		if (foreverLoopGuard > 10){
			result.setSimpleName("CHECK_foreverLoopGuard");
			return result;
		}
		foreverLoopGuard++;
		if (jdtType.isArrayType()){
			Type more = ((ArrayType)jdtType).getElementType();
			result = this.retrieveName(more, foreverLoopGuard);
		} else if (jdtType.isParameterizedType()){
			Type more = ((ParameterizedType)jdtType).getType();
			result = this.retrieveName(more, foreverLoopGuard);
		} else if (jdtType.isPrimitiveType()){
			PrimitiveType primitiveType = (PrimitiveType)jdtType;
			result.setSimpleName(primitiveType.getPrimitiveTypeCode().toString());
		} else if (jdtType.isQualifiedType()){
			Name name = ((QualifiedType)jdtType).getName();
			result = new NdName(name);
		} else if (jdtType.isSimpleType()){
			Name name = ((SimpleType)jdtType).getName();
			result = new NdName(name);
		} else if (jdtType.isWildcardType()){
			WildcardType wildcardType = (WildcardType)jdtType;
			Type bound = wildcardType.getBound();
			if (bound != null){
				result = this.retrieveName(bound, foreverLoopGuard);
			}
		}
		return result;
	}
	
	/**
	 * ArrayTypeか否かを応答する（各サブクラスでOverrideする）
	 * @return
	 */
	public boolean isArrayType(){
		return false;
	}
	
	/**
	 * ParameterizedTypeか否かを応答する（各サブクラスでOverrideする）
	 * @return
	 */
	public boolean isParameterizedType(){
		return false;
	}
	
	/**
	 * PrimitiveTypeか否かを応答する（各サブクラスでOverrideする）
	 * @return
	 */
	public boolean isPrimitiveType(){
		return false;
	}
	
	/**
	 * QualifiedTypeか否かを応答する（各サブクラスでOverrideする）
	 * @return
	 */
	public boolean isQualifiedType(){
		return false;
	}
	
	/**
	 * SimpleTypeか否かを応答する（各サブクラスでOverrideする）
	 * @return
	 */
	public boolean isSimpleType(){
		return false;
	}
	
	/**
	 * WildcardTypeか否かを応答する（各サブクラスでOverrideする）
	 * @return
	 */
	public boolean isWildcardType(){
		return false;
	}
	
	/** デバッグ */
	public abstract void debugPrint(String tab);
	
	/** デバッグ */
	protected String getDebugTitle(){
		String msg = "リンク:" + this.getClass().getSimpleName() + "->";
		msg += "getSimpleName=" + this.typeName.getSimpleName();
		msg += " getDisplayName()=" + this.getDisplayName();
		if (this.typeName.isQualifiedName()){
			msg += " JDTから取得した限定名=" + typeName.getQualifiedName();
		}
		return msg;
	}
}
