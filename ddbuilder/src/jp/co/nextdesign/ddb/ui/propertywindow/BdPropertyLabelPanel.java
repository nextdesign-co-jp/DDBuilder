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
