/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.parser.reflector;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.jcr.model.core.NdAttribute;
import jp.co.nextdesign.jcr.model.core.NdAbstractClassifier;
import jp.co.nextdesign.jcr.model.core.NdExtendedModifier;
import jp.co.nextdesign.jcr.model.core.NdJavaCode;
import jp.co.nextdesign.jcr.model.core.NdJavadoc;
import jp.co.nextdesign.jcr.model.typelink.NdAbstractTypeLink;
import jp.co.nextdesign.jcr.model.typelink.NdTypeLinkFactory;
import jp.co.nextdesign.jcr.parser.NdASTVisitor;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 * フィールド要素（属性要素）リフレクタ
 * @author murayama
 */
public class NdFieldDeclarationReflector extends NdAbstractReflector {

	/**
	 * 反映する
	 * @param node
	 * @param target
	 * @param astVisitor
	 */
	public void reflect(FieldDeclaration node, NdAbstractClassifier target, NdASTVisitor astVisitor){
		String allString = node.toString();
		if (allString != null){
			allString = NdJavaCode.removeTailFrom(allString);
			NdJavadoc javadoc = this.getJavadoc(node);
			String header = this.getHeaderLine(node, javadoc);
			ArrayList<NdExtendedModifier> modifierList = this.getModifierList(node);

			//例：privte int a, b, c など複数ある
			Type baseType = node.getType(); //APIドキュメントにbaseTypeの注記がある
			NdAbstractTypeLink typeLink = NdTypeLinkFactory.createTypeLink(baseType);
			for(Object fragment : node.fragments()){
				if (fragment instanceof VariableDeclaration){
					VariableDeclarationFragment variable = (VariableDeclarationFragment)fragment;
					NdAttribute newAttribute = new NdAttribute();
					astVisitor.putAttributeCache(node, newAttribute);
					target.addAttribute(newAttribute);
					newAttribute.setJavadoc(javadoc);
					newAttribute.setModifierList(modifierList);
					newAttribute.setAttributeType(typeLink);
					newAttribute.setName(this.getName(variable));
					newAttribute.setHeaderLine(header);
					newAttribute.setLineCountOfStatement(this.getLineCountOfNode(node, javadoc));
				}
			}
		}
	}
	
	/**
	 * 変数名を応答する
	 * @param variable
	 * @return
	 */
	private String getName(VariableDeclarationFragment variable){
		String result = "";
		if ((variable != null) && (variable.getName() != null)) {
			result = variable.getName().getIdentifier();
		}
		return result;
	}

	/**
	 * node部の行数を応答する
	 * @param node
	 * @param javadoc
	 * @return
	 */
	private int getLineCountOfNode(FieldDeclaration node, NdJavadoc javadoc){
		int result = 0;
		if (node != null){
			String nodeString = node.toString();
			List<String> lines = NdJavaCode.getLineListOf(nodeString);
			result = lines.size();
			if (javadoc != null){
				result -= javadoc.getLineCount();
			}
		}
		return result;
	}
}
