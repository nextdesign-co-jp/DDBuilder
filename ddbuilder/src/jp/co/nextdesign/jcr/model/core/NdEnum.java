/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

import org.eclipse.jdt.core.dom.EnumDeclaration;

/**
 * 列挙型
 * @author murayama
 */
public class NdEnum extends NdAbstractClassifier {
	
	private NdAbstractClassifier parentClass;
	
	/**
	 * 要素型の表示名を応答する
	 * @return
	 */
	public String getDisplayTypeName(){
		return "列挙型";
	}
	
	/**
	 * 親要素を持つか否かを応答する
	 * @return
	 */
	@Override public boolean hasParent(){
		return this.parentClass != null;
	}

	/**
	 * パッケージ名は含まない限定名を応答する
	 * @return
	 */
	@Override public String getQualifiedNameWithoutPackage(){
		String result = this.name;
		if (this.parentClass != null){
			result = this.parentClass.getQualifiedNameWithoutPackage() + "." + result;
		}
		return result;
	}
	
	/**
	 * 親クラス・クラス内定義の列挙型の双方向関連（親クラスを応答する）
	 */
	public NdAbstractClassifier getParentClass() {
		return parentClass;
	}

	/**
	 * 親クラス・クラス内定義の列挙型スの双方向関連（親クラスを設定する）
	 * @param parentClass
	 */
	public void setParentClass(NdAbstractClassifier parentClass) {
		if ((parentClass != null) && (this.parentClass != parentClass)){
			this.parentClass = parentClass;
			parentClass.addInnerEnum(this);
		}
	}

	/**
	 * コンストラクタ
	 * @param node
	 */
	public NdEnum(EnumDeclaration node){
		super(node);
	}
}
