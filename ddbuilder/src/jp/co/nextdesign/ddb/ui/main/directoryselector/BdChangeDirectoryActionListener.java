/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.ui.main.directoryselector;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import jp.co.nextdesign.ddb.BdConstants;
import jp.co.nextdesign.ddb.BdMessageResource;
import jp.co.nextdesign.ddb.ui.main.BdBaseActionListener;
import jp.co.nextdesign.ddb.ui.main.BdMainWindow;

/**
 * ディレクトリ変更ボタンのアクションリスナー
 * @author murayama
 */
public class BdChangeDirectoryActionListener extends BdBaseActionListener {

	private BdBaseDirectorySelector directorySelector;
	
	/** アクション実行 */
	@Override
	public void actionPerformed(ActionEvent e){
		JFileChooser chooser = createFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(mainWindow) == JFileChooser.APPROVE_OPTION){
			if (directorySelector != null){
			File selected = chooser.getSelectedFile();
			directorySelector.setSelectedDirectory(selected);
			saveApplHistory();
			}
		}
	}
	
	/** FileChooser（初期表示ディレクトリ設定済み）を生成し応答する */
	private JFileChooser createFileChooser(){
		JFileChooser result = new JFileChooser();
		
		if (directorySelector != null){
			String oldPath = directorySelector.getOldSelectedDirectoryPath();
			File oldDirectory = new File(oldPath);
			if ((oldDirectory.exists()) && (oldDirectory.isDirectory())){
				result.setCurrentDirectory(oldDirectory);
			}
		}
		return result;
	}
	
	/** ディレクトリ設定情報を保存する */
	private void saveApplHistory(){
		if ((mainWindow != null) && (mainWindow.getBuilderSetting() != null)){
			try{
				mainWindow.getBuilderSetting().save();
			} catch(Exception ex){
				JOptionPane.showMessageDialog(null, BdMessageResource.get("error.saveDirInfo") + BdConstants.CR + ex.toString()); //"ディレクトリ設定情報の保存に失敗しました。"
			}
		}
	}
	
	/** コンストラクタ */
	public BdChangeDirectoryActionListener(BdMainWindow mainWindow, BdBaseDirectorySelector directorySelector){
		super(mainWindow);
		this.directorySelector = directorySelector;
	}
}
