/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
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
