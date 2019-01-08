/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.ui.main;

import java.awt.event.ActionEvent;


/**
 * 終了ボタンのアクションリスナー
 * @author murayama
 */
class BdExitActionListener extends BdBaseActionListener {

	@Override
	public void actionPerformed(ActionEvent e){
		mainWindow.dispose();
		System.exit(0);
	}
	
	/** コンストラクタ */
	public BdExitActionListener(BdMainWindow mainWindow){
		super(mainWindow);
	}
}
