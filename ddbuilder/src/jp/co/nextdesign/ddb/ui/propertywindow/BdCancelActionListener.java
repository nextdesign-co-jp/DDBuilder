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

import java.awt.event.ActionEvent;

import jp.co.nextdesign.ddb.BdMessageResource;

/**
 * プロパティ設定画面のキャンセルアクション
 * @author murayama
 */
public class BdCancelActionListener extends BdBaseActionListener {

	/**
	 * コンストラクタ
	 * @param propertyWindow
	 */
	public BdCancelActionListener(BdPropertyWindow propertyWindow){
		super(propertyWindow);
	}
	
	/**
	 * プロパティ設定画面を終了する
	 */
	@Override public void actionPerformed(ActionEvent e){
		this.propertyWindow.setMainWindowMessage(BdMessageResource.get("propertywindow.message.canceled")); //"設定は更新されていません。キャンセルされました。"
		this.propertyWindow.dispose();
	}
}
