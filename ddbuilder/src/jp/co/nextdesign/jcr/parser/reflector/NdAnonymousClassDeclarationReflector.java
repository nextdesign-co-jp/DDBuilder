/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.parser.reflector;

import java.util.List;

import jp.co.nextdesign.jcr.model.core.NdAbstractClassifier;
import jp.co.nextdesign.jcr.parser.NdASTVisitor;

import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * 無名クラス定義の反映
 * @author murayama
 */
public class NdAnonymousClassDeclarationReflector extends NdAbstractReflector {

	/**
	 * 反映する
	 * @param node
	 * @param target
	 * @param astVisitor
	 */
	public void reflect(AnonymousClassDeclaration node, NdAbstractClassifier target, NdASTVisitor astVisitor){
		if ((node != null) && (target != null)){
			List<?> declaratoinList = node.bodyDeclarations();
			for (Object element : declaratoinList){
				if ((element != null) && (element instanceof BodyDeclaration)){
					BodyDeclaration bodyDeclaration = (BodyDeclaration)element;
					if (bodyDeclaration instanceof FieldDeclaration){
						this.reflectAttribute((FieldDeclaration)bodyDeclaration, target, astVisitor);
					} else if (bodyDeclaration instanceof MethodDeclaration){
						this.reflectMethod((MethodDeclaration)bodyDeclaration, target, astVisitor);
					}
				}
			}
		}
	}
	
	/**
	 * 属性を反映する
	 * @param fieldDeclaration
	 * @param target
	 * @param astVisitor
	 */
	private void reflectAttribute(FieldDeclaration fieldDeclaration, NdAbstractClassifier target, NdASTVisitor astVisitor){
		NdFieldDeclarationReflector reflector = new NdFieldDeclarationReflector();
		reflector.reflect(fieldDeclaration, target, astVisitor);
	}

	/**
	 * メソッドを反映する
	 * @param methodDeclaration
	 * @param target
	 * @param astVisitor
	 */
	private void reflectMethod(MethodDeclaration methodDeclaration, NdAbstractClassifier target, NdASTVisitor astVisitor){
		NdMethodDeclarationReflector reflector = new NdMethodDeclarationReflector();
		reflector.reflect(methodDeclaration, target, astVisitor);
	}
}
