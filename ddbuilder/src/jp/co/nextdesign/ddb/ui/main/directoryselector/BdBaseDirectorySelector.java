/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
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
