/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.ui.propertywindow;

import java.awt.Toolkit;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import jp.co.nextdesign.ddb.BdConstants;
import jp.co.nextdesign.ddb.BdMessageResource;

/**
 * 数値専用プロパティの入力部品
 * @author murayama
 */
public class BdIntegerPropertyFieldPanel extends BdPropertyFieldPanel {

	private static final long serialVersionUID = 1L;

	//パネル配置の上部インセット
	private static final int topInset = 20;
	/**
	 * コンストラクタ
	 * @param title
	 */
	public BdIntegerPropertyFieldPanel(String title){
		super(title, topInset);
		this.field.setInputVerifier(new IntegerInputVerifier());
	}

	/**
	 * 入力文字列チェック用オブジェクト
	 * @author murayama
	 */
	class IntegerInputVerifier extends InputVerifier{
		@Override public boolean verify(JComponent input) {
			boolean result = false;
			if (input instanceof JTextField){
				JTextField textField = (JTextField)input;
				try{
					Integer.parseInt(textField.getText());
					result = true;
				} catch(NumberFormatException ex) {
					Toolkit.getDefaultToolkit().beep();
					//"数字以外は入力できません。" "数字を入力してください。"
					JOptionPane.showMessageDialog(
							null, 
							BdMessageResource.get("propertywindow.error.numeric1") + BdConstants.CR + 
							BdMessageResource.get("propertywindow.error.numeric2") + BdConstants.CR  + textField.getText());
				}
			}
			return result;
		}
	}
}
