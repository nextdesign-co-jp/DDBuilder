/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.parser.reflector;

import jp.co.nextdesign.jcr.model.core.NdEnum;
import jp.co.nextdesign.jcr.model.core.NdJavadoc;
import jp.co.nextdesign.jcr.parser.NdASTVisitor;

import org.eclipse.jdt.core.dom.EnumDeclaration;

/**
 * 列挙型宣言の反映
 * @author murayama
 */
public class NdEnumDeclarationReflector extends NdAbstractReflector {

	/**
	 * 反映する
	 * @param node
	 * @param target
	 * @param astVisitor
	 */
	public void reflect(EnumDeclaration node, NdEnum target, NdASTVisitor astVisitor){
		if ((node != null) && (target != null)){
			astVisitor.putEnumCache(node, target);
			NdJavadoc javadoc = this.getJavadoc(node);
			target.setJavadoc(javadoc); //Javadocを設定する
			target.setHeaderLine(this.getHeaderLine(node, javadoc)); //先頭行を設定する
			target.setModifierList(this.getModifierList(node)); //Modifierを設定する
		}
	}
}
