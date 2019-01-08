/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.ui.propertywindow;

import java.awt.event.ActionEvent;

import jp.co.nextdesign.ddb.BdApplicationPropertyException;
import jp.co.nextdesign.ddb.BdMessageResource;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * プロパティ設定の反映アクション
 * @author murayama
 */
public class BdCompleteActionListener extends BdBaseActionListener {
	/**
	 * コンストラクタ
	 * @param propertyWindow
	 */
	public BdCompleteActionListener(BdPropertyWindow propertyWindow){
		super(propertyWindow);
	}
	
	/**
	 * プロパティ設定を反映する
	 */
	@Override public void actionPerformed(ActionEvent e){
		try{
		this.propertyWindow.updateApplicationProperties();
		this.propertyWindow.setMainWindowMessage(BdMessageResource.get("propertywindow.message.completed")); //"設定を更新しました。"
		} catch(BdApplicationPropertyException ex) {
			NdLogger.getInstance().error(this.getClass().getSimpleName() + " PROPERTIES ACCESS EXCEPTION ", ex);
		}
		this.propertyWindow.dispose();
	}
}
