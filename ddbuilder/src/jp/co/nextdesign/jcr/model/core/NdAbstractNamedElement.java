/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

/**
 * 名前付き要素の基底クラス
 * (1)
 * jdt.AbstractTypeDeclaration#getName() : SimpleNaem
 * なので、このクラスのname属性はString型とする。（isQualifiedNameを持つNameクラスではない）
 * jdt.AbstractTypeDeclarationのサブクラスは
 * TypeDeclartion, AnnotationTypeDeclaration, EnumTypeDeclarationがある。
 * (2)
 * Declarationの名前はSinpleNameで,Typeの名前はNameオブジェクトである。
 * @author murayama
 */
public abstract class NdAbstractNamedElement {
	
	protected String name;
	
	/**
	 * 名前を応答する
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 名前を設定する
	 * @param name
	 */
	public void setName(String name) {
		if (name != null){
			this.name = name;
		}
	}
	
	/**
	 * コンストラクタ
	 */
	public NdAbstractNamedElement(){
		this.name = "";
	}
}
