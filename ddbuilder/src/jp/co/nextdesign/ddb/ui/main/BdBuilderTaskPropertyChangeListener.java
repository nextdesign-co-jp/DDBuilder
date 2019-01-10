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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ProgressMonitor;


/**
 * ビルドタスク用 リスナー
 * @author murayama
 */
class BdBuilderTaskPropertyChangeListener implements PropertyChangeListener {

	private BdMainWindow mainWindow; //メイン画面

	/**
	 * PropertyChangeListenerを実装する
	 * このメソッドでは次の２つを行う。
	 * 1.進捗率（文字列部○○％完了など）を更新する
	 * 2.プログレス画面の取消しボタンが押下されたらバックグランド処理を中断する
	 * [1.の実現方法]
	 * このメソッドは、NdReportTask#doInBackground()でSwingWoker#setProgress()を実行したときにコールバックされる。
	 * その時に新しい値（進捗率）をメッセージ加工してプログレス画面の注記域にセットする。
	 * SwingWoker#setProgress()はNdReportActionListner#actionPerformedからも使用される。
	 * ただし、そこでの目的は、プログレス画面の表示を開始である。
	 * 従って、重要なシーケンスはNdReportTask#doInBackground()である。
	 * [2.の実現方法]
	 * ProgressMonitor#isCalcelled()は、ProgressMonitorの「取消し」ボタン押下でtrueになる。
	 * ここでは進捗率が変わったタイミング（このメソッドがコールバックされるタイミング）で
	 * ProgressMonitor#isCalcelled()をチェックし、必要な場合にはSwingWoker#cancel()を実行する。
	 * 取消しボタンの状態をチェックするのは、このメソッドである。
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		ProgressMonitor monitor = mainWindow.getProgressMonitor();
		if (monitor.isCanceled()){
			mainWindow.getCurrentTask().cancel(true);
		} else {
			if ("progress" == evt.getPropertyName()){
				int progress = (Integer)evt.getNewValue();
				monitor.setProgress(progress);
				String message = String.format(mainWindow.getCurrentTask().getCurrentTaskStateName() + "%d%%", progress);
				monitor.setNote(message);
			}
		}
	}

	/** コンストラクタ */
	public BdBuilderTaskPropertyChangeListener(BdMainWindow mainWindow){
		super();
		this.mainWindow = mainWindow;
	}
}
