/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.ui.propertywindow;

import java.awt.BorderLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * プロパティの入力部品
 * @author murayama
 */
public class BdPropertyFieldPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	protected JTextField field;

	/**
	 * コンストラクタ
	 * @param title
	 */
	public BdPropertyFieldPanel(String title, int topInset){
		super();
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(topInset,5,6,5));
		JLabel label = new JLabel(title);
		label.setBorder(new EmptyBorder(0,0,0,0));
		this.add(label, BorderLayout.WEST);
		field = new JTextField();
		//field.setBorder(new EmptyBorder(0,0,0,0));
		field.setMargin(new Insets(5,5,5,5));
		this.add(field, BorderLayout.CENTER);
	}

	/**
	 * フィールド値を応答する
	 * @return
	 */
	public String getStringValue(){
		String result = "";
		if (field != null){
			result = this.field.getText();
		}
		return result;
	}

	/**
	 * フィールド値を応答する
	 * @return
	 */
	public int getIntValue(){
		int result = 0;
		if (field != null){
			try{
				result = Integer.parseInt(this.field.getText());
			} catch (NumberFormatException ex){
				result = 0;
			}
		}
		return result;
	}

	/**
	 * フィールド値を設定する
	 * @param value
	 */
	public void setValue(String value){
		if ((field != null) && (value != null)){
			this.field.setText(value);
		}
	}

	/**
	 * フィールド値を設定する
	 * @param value
	 */
	public void setValue(int value){
		if (field != null){
			this.field.setText(Integer.toString(value));
		}
	}
}
