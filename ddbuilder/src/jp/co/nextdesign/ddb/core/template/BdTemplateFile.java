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

import java.io.File;

import jp.co.nextdesign.util.NdFileUtil;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * テンプレートプロジェクトの内のファイル
 * BdBaseCodetemplateFileサブクラスのファクトリ
 * @author murayama
 *
 */
public class BdTemplateFile {
	private BdTemplateProject project;
	protected File plainFile;
	private BdTemplateDirectory directory;
	
	/**
	 * コンストラクタ
	 * @param file
	 */
	public BdTemplateFile(File file, BdTemplateProject project){
		super();
		this.plainFile = file;
		this.project = project;
	}

	/**
	 * プロジェクトルートからの相対パスを応答する templateは含まない
	 * @return
	 */
	public String getNameInProject(){
		String result = this.plainFile.getAbsolutePath().replace(this.project.getRootDirectory().getPlainDirectory().getAbsolutePath(), "");
		return result;
	}

	/**
	 * コピー必須のファイルか否か
	 * @return
	 */
	public boolean isRequiredFile() {
		boolean result = true;
		String extension = NdFileUtil.getFileExtensionLowerCase(this.plainFile);
		if ("class".equals(extension)) {
			result = false;
		} else if ("java".equals(extension)) {
			result = plainFile.getName().startsWith(BdTemplateProject.REQUIRED_CLASS_NAME_PREFIX) 
					&& !plainFile.getParentFile().getName().equals(BdTemplateProject.G_PACKAGE_NAME);
		} else if ("html".equals(extension)) {
			result = plainFile.getName().startsWith(BdTemplateProject.REQUIRED_CLASS_NAME_PREFIX) 
					&& !plainFile.getParentFile().getName().equals(BdTemplateProject.G_PACKAGE_NAME);
		}
		return result;
	}

	/**
	 * 作成しないファイルか否か
	 * @return
	 */
	public boolean isIgnoredContents(){
		boolean result = false;
		for(String path : BdTemplateProject.IGNORED_DIIR_PATHS){
			if (this.plainFile.getAbsolutePath().contains(path)){
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * 収容ディレクトリを設定する
	 * @param directory
	 */
	public void setDirectory(BdTemplateDirectory directory){
		if (directory != null && this.directory != directory){
			this.directory = directory;
			directory.addFile(this);
		}
	}
	
	/**
	 * System.io.Fileを応答する
	 * @return
	 */
	public File getPlainFile(){
		return this.plainFile;
	}
	
	/**
	 * デバッグ
	 * @param tab
	 */
	public void debugPrint(String tab) {
		if (!NdLogger.getInstance().getDebugLogging()){
			return;
		}
		String line = tab + this.plainFile.getName() + "  [" + this.getClass().getSimpleName() + "]";
		line += this.isRequiredFile() ? " ....REQIRED" : "";
		line += this.isIgnoredContents() ? "....IGNORED" : "";
		NdLogger.getInstance().debug(line);
	}
}
