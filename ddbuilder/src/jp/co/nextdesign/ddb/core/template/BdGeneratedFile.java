/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.template;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.ddb.BdConstants;
import jp.co.nextdesign.ddb.ui.main.BdMainWindow;

/**
 * 生成ファイル
 * @author murayama
 *
 */
public class BdGeneratedFile {

	private String fullPathName;
	private List<String> lineList;
	private boolean overwriteIfExists;
	
	/**
	 * コンストラクタ
	 */
	public BdGeneratedFile(){
		super();
		this.lineList = new ArrayList<String>();
		this.overwriteIfExists = true;
	}
	
	/** 生成するファイルがすでに存在する場合に上書きするか否か */
	public boolean isOverwriteIfExists() {
		return overwriteIfExists;
	}

	public void setOverwriteIfExists(boolean overwriteIfExists) {
		this.overwriteIfExists = overwriteIfExists;
	}

	public String getFullPathName() {
		return fullPathName;
	}

	public void setFullPathName(String fullPathName) {
		this.fullPathName = fullPathName;
	}

	//追加時に全ファイルに共通する文字列置換を行う
	public void addLines(List<String> lines){
		for(String line : lines){
			line = line.replaceAll("%DDB_VERSION%", BdConstants.VERSION);
			line = line.replaceAll("%FILE_GENERATED_AT%", BdMainWindow.FILE_GENERATED_AT);
			this.lineList.add(line);
		}
	}
	
	public List<String> getLineList(){
		return this.lineList;
	}
}
