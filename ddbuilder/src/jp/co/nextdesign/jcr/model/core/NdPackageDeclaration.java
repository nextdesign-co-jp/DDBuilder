/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

import org.eclipse.jdt.core.dom.PackageDeclaration;

/**
 * パッケージ宣言
 * @author murayama
 */
public class NdPackageDeclaration {

	private String fullName;

	/**
	 * package名を応答する
	 * @return
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * コンストラクタ
	 * @param node
	 */
	public NdPackageDeclaration(PackageDeclaration node){
		this.fullName = "";
		if (node.getName() != null){
			this.fullName = node.getName().toString();
		}
	}
}
