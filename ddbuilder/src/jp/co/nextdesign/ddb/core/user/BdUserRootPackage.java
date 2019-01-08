/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.user;

import jp.co.nextdesign.jcr.model.core.NdPackage;

/**
 * ルートパッケージ
 * ルートパッケージはパッケージを継承し、固有の振る舞いをもつ。
 * @author murayama
 *
 */
public class BdUserRootPackage extends BdUserPackage {

	//ルートパッケージか否か
	@Override
	public boolean isRootPackage(){
		return true;
	}

	/**
	 * コンストラクタ
	 * @param ndPackage
	 * @param userProject
	 */
	public BdUserRootPackage(NdPackage ndPackage, BdUserProject userProject){
		super(ndPackage, null, userProject);
	}
	
	/**
	 * ユーザプロジェクトを応答する
	 */
	public BdUserProject getUserProject(){
		return this.userProject;
	}
}
