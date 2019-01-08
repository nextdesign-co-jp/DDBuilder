/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.typelink;

import jp.co.nextdesign.util.logging.NdLogger;

import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.WildcardType;

/**
 * NdTypeLinkファクトリ
 * @author murayama
 *
 */
public class NdTypeLinkFactory {

	/**
	 * TypeLinkサブクラスのインスタンスを応答する
	 * @param type
	 * @return
	 */
	public static NdAbstractTypeLink createTypeLink(Type type){
		NdAbstractTypeLink result = NdAbstractTypeLink.UNDEFINED;
		if (type != null){
			if (type.isArrayType()){
				result = new NdArrayTypeLink((ArrayType)type);
			} else if (type.isParameterizedType()){
				result = new NdParameterizedTypeLink((ParameterizedType)type);				
			} else if (type.isPrimitiveType()){
				result = new NdPrimitiveTypeLink((PrimitiveType)type);				
			} else if (type.isQualifiedType()){
				//dom.QuakifiedTypeのAPIドキュメントを参照。QualifiedTypeではなくSimpleTypeまたはParameterizedTypeになるようだ。2011.9.4
				NdLogger.getInstance().error("NdTypeLinkFactory#createTypeLink:isQualifiedType()を検出。未実装例外。 ", null);
			} else if (type.isSimpleType()){
				result = new NdSimpleTypeLink((SimpleType)type);
			} else if (type.isWildcardType()){
				result = new NdWildcardTypeLink((WildcardType)type);				
			}
		}
		return result;
	}
}
