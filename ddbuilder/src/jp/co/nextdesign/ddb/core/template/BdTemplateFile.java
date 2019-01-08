/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
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
