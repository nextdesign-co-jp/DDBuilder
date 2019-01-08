/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

import jp.co.nextdesign.jcr.model.NdName;

import org.eclipse.jdt.core.dom.ImportDeclaration;

/**
 * インポート宣言
 * @author murayama
 */
public class NdImportDeclaration {
	
	private boolean isStatic;
	private NdName name;
	private boolean isOnDemand;

	/**
	 * static importか否かを応答する
	 * @return
	 */
	public boolean isStatic() {
		return this.isStatic;
	}
	
	/**
	 * import文が個別クラス記述式か*記述式かを応答する
	 * true *記述
	 * @return
	 */
	public boolean isOnDemand(){
		return this.isOnDemand;
	}

	/**
	 * import名
	 */
	public NdName getName() {
		return this.name;
	}
	
	/**
	 * コンストラクタ
	 * @param node
	 */
	public NdImportDeclaration(ImportDeclaration node){
		super();
		this.name = new NdName();
		isStatic = false;
		isOnDemand = false;
		if (node != null){
			this.isStatic = node.isStatic();
			this.isOnDemand = node.isOnDemand();
			this.name = new NdName(node.getName());
		}
	}
}
