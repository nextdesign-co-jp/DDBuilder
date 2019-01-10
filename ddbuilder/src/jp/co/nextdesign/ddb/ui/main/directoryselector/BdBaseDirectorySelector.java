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
package jp.co.nextdesign.ddb.ui.main.directoryselector;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import jp.co.nextdesign.ddb.BdMessageResource;
import jp.co.nextdesign.ddb.ui.main.BdMainWindow;

/**
 * ディレクトリ選択パネルの基底クラス
 * @author murayama
 */
public abstract class BdBaseDirectorySelector extends JPanel {
	
	private static final long serialVersionUID = 1L;
	protected JTextField directoryLabel;
	protected JButton changeDirectoryButton;
	protected BdMainWindow mainWindow;
	
	//このコンポーネントを活性／非活性する
	public void setEnabled(boolean param){
		if (changeDirectoryButton != null) changeDirectoryButton.setEnabled(param);
	}
	
	/** 前回の選択ディレクトリを応答する */
	public abstract String getOldSelectedDirectoryPath();
	
	/** 選択されたディレクトリを設定する */
	public abstract void setSelectedDirectory(File directory);

	/** コンストラクタ */
	public BdBaseDirectorySelector(BdMainWindow parent){
		super(new BorderLayout());
		this.mainWindow = parent;
		this.add(new JLabel(getLabelText()), BorderLayout.WEST);
		directoryLabel = new JTextField();
		directoryLabel.setBorder(new EmptyBorder(0, 10, 0, 10));
		directoryLabel.setText(getOldSelectedDirectoryPath());
		directoryLabel.setEditable(false);
		directoryLabel.setBackground(Color.white);
		this.add(directoryLabel, BorderLayout.CENTER);
		changeDirectoryButton = new JButton(BdMessageResource.get("button.browse")); //" 参照 "
		changeDirectoryButton.addActionListener(new BdChangeDirectoryActionListener(mainWindow, this));
		this.add(changeDirectoryButton, BorderLayout.EAST);
	}
	
	/** 見出しレベル文字列を応答する（サブクラス毎に異なる） */
	protected String getLabelText(){
		return "";
	}
}
