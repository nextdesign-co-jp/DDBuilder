/*
 * DDBuilder
 * http://www.nextdesign.co.jp/ddd/index.html
 * Copyright 2015 NEXT DESIGN Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
