/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.parser.reflector;

import jp.co.nextdesign.jcr.model.core.NdClassTypeParameter;
import jp.co.nextdesign.jcr.model.core.NdAbstractClassifier;
import jp.co.nextdesign.jcr.model.core.NdJavadoc;
import jp.co.nextdesign.jcr.model.typelink.NdAbstractTypeLink;
import jp.co.nextdesign.jcr.model.typelink.NdTypeLinkFactory;
import jp.co.nextdesign.jcr.parser.NdASTVisitor;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;

/**
 * 型（クラス、インタフェース）宣言の反映
 * @author murayama
 */
public class NdTypeDeclarationReflector extends NdAbstractReflector {

	/**
	 * 反映する
	 * @param node
	 * @param target
	 * @param astVisitor
	 */
	public void reflect(TypeDeclaration node, NdAbstractClassifier target, NdASTVisitor astVisitor){
		if ((node != null) && (target != null)){
			NdJavadoc javadoc = this.getJavadoc(node);
			target.setJavadoc(javadoc); //Javadocを設定する
			target.setHeaderLine(this.getHeaderLine(node, javadoc)); //先頭行を設定する
			target.setIsInterface(node.isInterface()); //インタフェースか否かを設定する
			target.setModifierList(this.getModifierList(node)); //Modifierを設定する
			this.reflectAttributes(node, target, astVisitor); //属性を反映する
			this.reflectMethos(node, target, astVisitor); //メソッドを反映する
			this.reflectSuperClass(node, target); //スーパークラスを反映する
			this.reflectSuperInterfaces(node, target); //実装インタフェースを反映する
			this.reflectTypeParameters(node, target); //型パラメータを反映する
		}
	}
	
	/**
	 * 型パラメータを反映する
	 */
	private void reflectTypeParameters(TypeDeclaration node, NdAbstractClassifier target){
		for(Object o : node.typeParameters()){
			if (o instanceof TypeParameter){
				TypeParameter parameter = (TypeParameter)o;
				if ((parameter != null) && (parameter.getName()!= null)){
					NdClassTypeParameter newClassTypeParameter = new NdClassTypeParameter();
					target.addTypeParameter(newClassTypeParameter);
					newClassTypeParameter.setTypeName(parameter.getName().getIdentifier());
					for(Object o2 : parameter.typeBounds()){
						if (o2 instanceof Type){
							Type type = (Type)o2;
							NdAbstractTypeLink newTypeLink = NdTypeLinkFactory.createTypeLink(type);
							newClassTypeParameter.addTypeBound(newTypeLink);
						}
					}
				}
			}
		}
	}
	
	/**
	 * インタフェースを反映する
	 */
	private void reflectSuperInterfaces(TypeDeclaration node, NdAbstractClassifier target){
		for(Object o : node.superInterfaceTypes()){
			if (o instanceof Type){
				NdAbstractTypeLink interfaceTypeLink = NdTypeLinkFactory.createTypeLink((Type)o);
				if (node.isInterface()){
					target.addSuperInterfaceTypeLink(interfaceTypeLink);
				} else {
					target.addImplementInterfaceTypeLink(interfaceTypeLink);
				}
			}
		}
	}
	
	/**
	 * スーパークラスを反映する
	 */
	private void reflectSuperClass(TypeDeclaration node, NdAbstractClassifier target){
		Type superType = node.getSuperclassType();
		if (superType != null){
			NdAbstractTypeLink link = NdTypeLinkFactory.createTypeLink(superType);
			target.setSuperClassTypeLink(link);
		}
	}
	
	/**
	 * 属性を反映する
	 * @param node
	 * @param target
	 * @param astVisitor
	 */
	private void reflectAttributes(TypeDeclaration node, NdAbstractClassifier target, NdASTVisitor astVisitor){
		FieldDeclaration[] fields = node.getFields();
		if (fields != null){
			for (FieldDeclaration fd : fields){
				NdFieldDeclarationReflector reflector = new NdFieldDeclarationReflector();
				reflector.reflect(fd, target, astVisitor);
			}
		}
	}
	
	/**
	 * メソッドを反映する
	 * @param node
	 * @param target
	 * @param astVisitor
	 */
	private void reflectMethos(TypeDeclaration node, NdAbstractClassifier target, NdASTVisitor astVisitor){
		MethodDeclaration[] methods = node.getMethods();
		if (methods != null){
			for (MethodDeclaration md : methods){
				NdMethodDeclarationReflector reflector = new NdMethodDeclarationReflector();
				reflector.reflect(md, target, astVisitor);
			}
		}
	}
}
