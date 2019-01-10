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
package jp.co.nextdesign.ddb.ui.main;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;

import jp.co.nextdesign.ddb.BdMessageResource;
import jp.co.nextdesign.util.logging.NdLogger;


/**
 * ビルドボタンのアクションリスナー
 * @author murayama
 */
class BdBuildActionListener extends BdBaseActionListener {

	/** ファイル生成・更新日時フォーマット */
	private static final SimpleDateFormat FILE_GENERATED_AT_FORMAT = new SimpleDateFormat("  [生成日時 yyyy/MM/dd-HH:mm:ss]");

	/**
	 * ActionListenerを実装する
	 * 1.ProgressMonitorを生成しMainWindowに参照をセットする
	 * 2.NdReportTaskを生成しMainWindowに参照をセットする
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		BdMainWindow.FILE_GENERATED_AT = FILE_GENERATED_AT_FORMAT.format(Calendar.getInstance().getTime());
		BdBuilderSetting setting = mainWindow.getBuilderSetting();
		if (setting != null){
			setting.setGroupId(mainWindow.getGroupId());
			setting.setArtifactId(mainWindow.getArtifactId());
			String errorMessage = setting.checkSetting();
			if (errorMessage.isEmpty()){
				saveSetting(setting);
				ProgressMonitor monitor = this.createProgressMonitor();
				mainWindow.setProgressMonitor(monitor);
				BdBuilderTask task = this.createNdReportTask();
				mainWindow.setCurrentTask(task);
				mainWindow.setEnabled(false);
				task.execute();
				monitor.setProgress(0); //最初にプログレスダイアログを表示するためにイベントを発生させる
			} else {
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null, errorMessage);
			}
		}
	}
	
	/** 設定情報を保存する */
	private void saveSetting(BdBuilderSetting setting){
		try{
			setting.save();
		} catch(Exception ex){
			NdLogger.getInstance().error(this.getClass().getSimpleName() + "#actionPerformed", ex);
		}
	}

	/**
	 * NdReportTaskを生成し応答する
	 * PropertyChangeListenerとしてNdPropertyChangeListenerを設定する
	 * @return
	 */
	private BdBuilderTask createNdReportTask(){
		BdBuilderTask task = new BdBuilderTask(mainWindow);
		task.addPropertyChangeListener(new BdBuilderTaskPropertyChangeListener(mainWindow));
		return task;
	}
	
	/** ProgressMonitorを生成し応答する */
	private ProgressMonitor createProgressMonitor(){
		String initialMessage = BdMessageResource.get("builder.preparing"); //"準備中　　　　　　　　　　　　　　　　　　　　　　　　";
		ProgressMonitor monitor = new ProgressMonitor(mainWindow, BdMessageResource.get("builder.updating"), initialMessage, 0, 100); //"更新中"
		monitor.setMillisToDecideToPopup(0);
		monitor.setMillisToPopup(0);
		return monitor;
	}
	
	/** コンストラクタ */
	public BdBuildActionListener(BdMainWindow mainWindow){
		super(mainWindow);
	}
}
