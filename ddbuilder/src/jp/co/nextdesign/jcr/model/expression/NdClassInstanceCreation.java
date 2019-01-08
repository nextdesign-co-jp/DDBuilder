/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.expression;

import jp.co.nextdesign.jcr.model.typelink.NdAbstractTypeLink;
import jp.co.nextdesign.jcr.model.typelink.NdTypeLinkFactory;

import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Type;

/**
 * JDT ClassInstanceCreation相当
 * @author murayama
 */
public class NdClassInstanceCreation extends NdAbstractExpression {
	
	/**
	 * new式でインスタンス化する型
	 */
	private NdAbstractTypeLink creationTypeLink;

	/**
	 * コンストラクタ
	 * @param jdtExpression
	 */
	public NdClassInstanceCreation(ClassInstanceCreation jdtExpression){
		if (jdtExpression != null){
			Type jdtType = jdtExpression.getType();
			this.creationTypeLink = NdTypeLinkFactory.createTypeLink(jdtType);
		}
	}

	/**
	 * この式（new式）でインスタンス化する型を応答する
	 * @return
	 */
	public NdAbstractTypeLink getCreationTypeLink() {
		return creationTypeLink;
	}
}
