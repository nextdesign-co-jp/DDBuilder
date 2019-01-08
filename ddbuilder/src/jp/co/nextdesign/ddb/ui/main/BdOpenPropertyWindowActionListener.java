/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.ui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jp.co.nextdesign.ddb.BdApplicationPropertyException;
import jp.co.nextdesign.ddb.ui.propertywindow.BdPropertyWindow;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * プロパティ設定画面の開始アクション
 * @author murayama
 */
class BdOpenPropertyWindowActionListener implements ActionListener {
	
	BdMainWindow mainWindow;
	
	/**
	 * コンストラクタ
	 * @param mainWindow
	 */
	BdOpenPropertyWindowActionListener(BdMainWindow mainWindow){
		this.mainWindow = mainWindow;
	}
	
	/**
	 * 開始する
	 */
	@Override public void actionPerformed(ActionEvent e) {
		try{
			BdPropertyWindow subWindow = new BdPropertyWindow(mainWindow);
			subWindow.setModal(true);
			subWindow.setVisible(true);
		} catch (BdApplicationPropertyException ex){
			NdLogger.getInstance().error(this.getClass().getSimpleName() + " プロパティ読み取り例外 ", ex);
		}
	}
}
