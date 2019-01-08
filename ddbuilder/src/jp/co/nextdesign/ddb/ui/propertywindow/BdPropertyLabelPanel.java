/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.ui.propertywindow;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * プロパティの入力部品
 * @author murayama
 */
public class BdPropertyLabelPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * @param text
	 */
	public BdPropertyLabelPanel(String text){
		super();
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(20,10,0,10));
		JLabel label = new JLabel(text);
		label.setBorder(new EmptyBorder(0,0,0,10));
		this.add(label, BorderLayout.WEST);
	}
}
