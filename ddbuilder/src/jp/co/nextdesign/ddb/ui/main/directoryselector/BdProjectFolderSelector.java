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

import java.io.File;

import jp.co.nextdesign.ddb.BdConstants;
import jp.co.nextdesign.ddb.BdMessageResource;
import jp.co.nextdesign.ddb.ui.main.BdMainWindow;

/**
 * メイン画面　プロジェクトフォルダ選択部パネル
 * @author murayama
 */
public class BdProjectFolderSelector extends BdBaseDirectorySelector {

	private static final long serialVersionUID = 1L;
	
	/** 前回の選択ディレクトリを応答する */
	public String getOldSelectedDirectoryPath(){
		String result = BdConstants.UNDEFINED_PATH;
		if ((mainWindow != null) && (mainWindow.getBuilderSetting() != null)){
			File directory = mainWindow.getBuilderSetting().getUserProjectFolder();
			if (directory != null){
				result = directory.getAbsolutePath();
			}
		}
		return result;
	}
	
	/** 選択されたディレクトリを設定する */
	public void setSelectedDirectory(File directory){
		if ((mainWindow != null) && (mainWindow.getBuilderSetting() != null)){
			mainWindow.getBuilderSetting().setUserProjectFolder(directory);
			this.directoryLabel.setText(directory.getAbsolutePath());
		}
	}
	
	/** 見出しレベル文字列を応答する */
	@Override
	protected String getLabelText(){
		return BdMessageResource.get("title.output"); //"(1) 出力先 : ";
	}

	/** コンストラクタ */
	public BdProjectFolderSelector(BdMainWindow parent){
		super(parent);
	}
}
