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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jp.co.nextdesign.ddb.BdApplicationPropertyException;
import jp.co.nextdesign.ddb.ui.propertywindow.BdPropertyWindow;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * プロパティ設定画面の開始アクション
 * @author murayama
 */
class BdOpenPropertyWindowActionListener implements ActionListener {
	
	BdMainWindow mainWindow;
	
	/**
	 * コンストラクタ
	 * @param mainWindow
	 */
	BdOpenPropertyWindowActionListener(BdMainWindow mainWindow){
		this.mainWindow = mainWindow;
	}
	
	/**
	 * 開始する
	 */
	@Override public void actionPerformed(ActionEvent e) {
		try{
			BdPropertyWindow subWindow = new BdPropertyWindow(mainWindow);
			subWindow.setModal(true);
			subWindow.setVisible(true);
		} catch (BdApplicationPropertyException ex){
			NdLogger.getInstance().error(this.getClass().getSimpleName() + " プロパティ読み取り例外 ", ex);
		}
	}
}
