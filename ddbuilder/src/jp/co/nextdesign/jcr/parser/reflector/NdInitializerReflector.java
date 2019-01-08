/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.parser.reflector;

import jp.co.nextdesign.jcr.model.core.NdInitializer;
import jp.co.nextdesign.jcr.model.core.NdJavadoc;
import jp.co.nextdesign.jcr.parser.NdASTVisitor;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Initializer;

/**
 * 初期化子定義の反映
 * @author murayama
 */
public class NdInitializerReflector extends NdAbstractMethodReflector {

	/**
	 * 反映する
	 * @param node
	 * @param target
	 * @param astVisitor
	 */
	public void reflect(Initializer node, NdInitializer target, NdASTVisitor astVisitor){
		if ((node != null) && (target != null)){
			astVisitor.putMethodCache(node, target);
			NdJavadoc javadoc = this.getJavadoc(node);
			target.setJavadoc(javadoc);
			target.setHeaderLine(this.getHeaderLine(node, javadoc));
			target.setModifierList(this.getModifierList(node));
			Block jdtBlock = node.getBody();
			target.setLineCountOfStatement(this.getLineCountOfBody(jdtBlock));			
			target.setBodyStatement(this.buildStatementTree(jdtBlock, astVisitor));
			target.calculateMetrics();
		}
	}
}
