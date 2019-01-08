/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.ui.propertywindow;

import java.awt.event.ActionListener;

public abstract class BdBaseActionListener implements ActionListener {

	protected BdPropertyWindow propertyWindow;
	
	/**
	 * コンストラクタ
	 * @param propertyWindow
	 */
	public BdBaseActionListener(BdPropertyWindow propertyWindow){
		super();
		this.propertyWindow = propertyWindow;
	}
}
