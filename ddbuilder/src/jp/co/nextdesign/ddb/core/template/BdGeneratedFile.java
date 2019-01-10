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
