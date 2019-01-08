/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.ui.propertywindow;

import java.awt.event.ActionEvent;

import jp.co.nextdesign.ddb.BdMessageResource;

/**
 * プロパティ設定画面のキャンセルアクション
 * @author murayama
 */
public class BdCancelActionListener extends BdBaseActionListener {

	/**
	 * コンストラクタ
	 * @param propertyWindow
	 */
	public BdCancelActionListener(BdPropertyWindow propertyWindow){
		super(propertyWindow);
	}
	
	/**
	 * プロパティ設定画面を終了する
	 */
	@Override public void actionPerformed(ActionEvent e){
		this.propertyWindow.setMainWindowMessage(BdMessageResource.get("propertywindow.message.canceled")); //"設定は更新されていません。キャンセルされました。"
		this.propertyWindow.dispose();
	}
}
