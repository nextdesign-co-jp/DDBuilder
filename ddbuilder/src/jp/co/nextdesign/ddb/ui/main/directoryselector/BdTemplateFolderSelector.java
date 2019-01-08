/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.ui.main.directoryselector;

import java.io.File;

import jp.co.nextdesign.ddb.BdConstants;
import jp.co.nextdesign.ddb.ui.main.BdMainWindow;

/**
 * メイン画面　テンプレートフォルダ選択部パネル
 * @author murayama
 */
public class BdTemplateFolderSelector extends BdBaseDirectorySelector {

	private static final long serialVersionUID = 1L;
	
	/** 前回の選択ディレクトリを応答する */
	public String getOldSelectedDirectoryPath(){
		String result = BdConstants.UNDEFINED_PATH;
		if ((mainWindow != null) && (mainWindow.getBuilderSetting() != null)){
			File directory = mainWindow.getBuilderSetting().getTemplateProjectFolder();
			if (directory != null){
				result = directory.getAbsolutePath();
			}
		}
		return result;
	}
	
	/** 選択されたディレクトリを設定する */
	public void setSelectedDirectory(File directory){
		if ((mainWindow != null) && (mainWindow.getBuilderSetting() != null)){
			mainWindow.getBuilderSetting().setTemplateProjectFolder(directory);
			this.directoryLabel.setText(directory.getAbsolutePath());
		}
	}

	/** 見出しレベル文字列を応答する */
	@Override
	protected String getLabelText(){
		return "(2) テンプレート : ";
	}

	/** コンストラクタ */
	public BdTemplateFolderSelector(BdMainWindow parent){
		super(parent);
	}
}
