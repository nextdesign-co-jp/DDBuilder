/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.parser.reflector;

import jp.co.nextdesign.jcr.model.core.NdAbstractClassifier;
import jp.co.nextdesign.jcr.model.core.NdJavadoc;
import jp.co.nextdesign.jcr.model.core.NdMethod;
import jp.co.nextdesign.jcr.parser.NdASTVisitor;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * メソッド要素リフレクタ
 * @author murayama
 */
public class NdMethodDeclarationReflector extends NdAbstractMethodReflector {

	/**
	 * 反映する
	 * @param node
	 * @param target
	 * @param astVisitor
	 */
	public void reflect(MethodDeclaration node, NdAbstractClassifier target, NdASTVisitor astVisitor){
		NdMethod newMethod = new NdMethod();
		astVisitor.putMethodCache(node, newMethod);
		target.addMethod(newMethod);
		NdJavadoc javadoc = this.getJavadoc(node);
		newMethod.setJavadoc(javadoc);
		newMethod.setHeaderLine(this.getHeaderLine(node, javadoc));
		newMethod.setModifierList(this.getModifierList(node));
		newMethod.setName(this.getMethodName(node));
		newMethod.setParameterList(getParameterList(node));
		newMethod.setIsConstructor(node.isConstructor());
		if (!node.isConstructor()){
			newMethod.setReturnValue(this.getReturnValue(node));
		}
		Block jdtBlock = node.getBody();
		newMethod.setLineCountOfStatement(this.getLineCountOfBody(jdtBlock));
		newMethod.setBodyStatement(this.buildStatementTree(jdtBlock, astVisitor));
		newMethod.calculateMetrics();
	}
}
