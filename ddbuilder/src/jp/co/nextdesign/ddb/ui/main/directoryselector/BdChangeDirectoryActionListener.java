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
