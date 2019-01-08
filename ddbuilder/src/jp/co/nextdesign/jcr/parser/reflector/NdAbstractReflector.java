/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.parser.reflector;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.jcr.model.core.NdAnnotation;
import jp.co.nextdesign.jcr.model.core.NdAnnotationAttribute;
import jp.co.nextdesign.jcr.model.core.NdExtendedModifier;
import jp.co.nextdesign.jcr.model.core.NdJavaCode;
import jp.co.nextdesign.jcr.model.core.NdJavadoc;
import jp.co.nextdesign.jcr.model.core.NdModifier;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;

/**
 * AST要素リフレクタの基底クラス
 * @author murayama
 */
public abstract class NdAbstractReflector {

	/**
	 * コンストラクタ
	 */
	public NdAbstractReflector(){
		super();
	}
	
	/**
	 * NodeからNdJavadocを作成し応答する(BodyDeclarationのサブクラスで共通)
	 * @param node
	 * @return
	 */
	protected NdJavadoc getJavadoc(BodyDeclaration node){
		NdJavadoc result = null;
		if (node != null) {
			Javadoc jdtJavadoc = node.getJavadoc();
			if (jdtJavadoc != null){
				result = new NdJavadoc(jdtJavadoc);
			}
		}
		return result;
	}
	
	/**
	 * Modifierリストを応答する
	 * @param node
	 * @return
	 */
	protected ArrayList<NdExtendedModifier> getModifierList(BodyDeclaration node){
		ArrayList<NdExtendedModifier> resultList = new ArrayList<NdExtendedModifier>();
		for(Object o : node.modifiers()){
			if (o instanceof IExtendedModifier){
				IExtendedModifier extendedModifier = (IExtendedModifier)o;
				if (extendedModifier.isModifier()){
					Modifier jdtModifier = (Modifier)extendedModifier;
					NdModifier newModifier = NdModifier.getModifierByKeyword(jdtModifier.getKeyword().toString());
					resultList.add(newModifier);
				} else {
					Annotation jdtAnnotation = (Annotation)extendedModifier;
					
					//DDBuilder対応で追加した(@OneToMany(cascade=CascadeType.ALL)を取得できるが、汎用的ではないかもしれない) 2015.7.20 kokokara
					List<NdAnnotationAttribute> annotationAttributeList = new ArrayList<NdAnnotationAttribute>();
					Name jdtName = jdtAnnotation.getTypeName();
					String keyword = "";
					if (jdtName != null){
						keyword = jdtName.getFullyQualifiedName();
						if (jdtAnnotation.isNormalAnnotation()){
							NormalAnnotation jdtNormalAnnotation = (NormalAnnotation)jdtAnnotation;
							for(Object obj : jdtNormalAnnotation.values()){
								if (obj instanceof MemberValuePair){
									NdAnnotationAttribute newAnnotationAttribute = new NdAnnotationAttribute();
									MemberValuePair pair = (MemberValuePair)obj;
									newAnnotationAttribute.setName(pair.getName().getFullyQualifiedName());
									Expression expression = pair.getValue();
									if (expression instanceof Name){
										Name expressionInstance = (Name)expression;
										newAnnotationAttribute.setValue(expressionInstance.getFullyQualifiedName());
									} else if (expression instanceof StringLiteral){
										StringLiteral expressionInstance = (StringLiteral)expression;
										newAnnotationAttribute.setValue(expressionInstance.getEscapedValue());
									}
									annotationAttributeList.add(newAnnotationAttribute);
								}
							}
						}
						NdAnnotation newAnnotation = new NdAnnotation(keyword, annotationAttributeList);
						resultList.add(newAnnotation);
					}
					//DDBuilder対応で追加した(@OneToMany(cascade=CascadeType.ALL)を取得できるが、汎用的ではないかもしれない) 2015.7.20 kokomade
					
//2015.7.19までのコード kokokara					
//					Name jdtName = jdtAnnotation.getTypeName();
//					String keyword = "";
//					if (jdtName.isQualifiedName()){
//						keyword = jdtName.getFullyQualifiedName();
//					} else {
//						keyword = jdtName.toString();
//					}
//					NdAnnotation newAnnotation = new NdAnnotation(keyword);
//					resultList.add(newAnnotation);
//2015.7.19までのコード kokomade					
				}
			}
		}
		return resultList;
	}
	
	/**
	 * 先頭行を応答する
	 * @param node
	 * @param javadoc
	 * @return
	 */
	protected String getHeaderLine(BodyDeclaration node, NdJavadoc javadoc){
		int headerLineNo = 0;
		if (javadoc != null){
			headerLineNo = javadoc.getLineCount();
		}
		String result = "";		
		List<String> list = NdJavaCode.getLineListOf(node.toString());
		if ((list != null) && (list.size() > headerLineNo)){
			result = list.get(headerLineNo);
		}
		return result.trim();
	}
}
