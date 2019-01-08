/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;

/**
 * アノテーション型
 * @author murayama
 */
public class NdAnnotationType extends NdAbstractClassifier {
	
	/**
	 * 要素型の表示名を応答する
	 * @return
	 */
	public String getDisplayTypeName(){
		return "アノテーション";
	}
	
	/**
	 * 親要素を持つか否かを応答する
	 * @return
	 */
	@Override public boolean hasParent(){
		return false;
	}

	/**
	 * コンストラクタ
	 * @param node
	 */
	public NdAnnotationType(AnnotationTypeDeclaration node){
		super(node);
	}
}
