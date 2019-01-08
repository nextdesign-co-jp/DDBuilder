/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.loader;

import java.io.File;

import jp.co.nextdesign.jcr.NdConstants;
import jp.co.nextdesign.jcr.model.core.NdCompilationUnit;
import jp.co.nextdesign.jcr.model.core.NdPackage;
import jp.co.nextdesign.jcr.model.core.NdProject;

/**
 * プロジェクトローダ
 * @author murayama
 */
public class NdProjectLoader {
	
	/**
	 * ディレクトリ以下の内容をプロジェクトに登録する
	 * @param topDirectory
	 * @param project
	 */
	public void load(File topDirectory, NdProject project){
		if (project != null){
			NdPackage rootPackage = null;
			if (isAvailableDirectory(topDirectory)){
				rootPackage = new NdPackage(topDirectory);
				project.setRootPackage(rootPackage);
				this.digDirectory(rootPackage, project);
			}
		}
	}
	
	/**
	 * ディレクトリを掘り下げパッケージ構成を再帰的に作成する
	 * @param parentPackage
	 * @param project
	 */
	private void digDirectory(NdPackage parentPackage, NdProject project){
		File parentDirectory = parentPackage.getDirectory();
		if (parentDirectory != null){
			loadCompilationUnits(parentPackage);
			String[] childNameList = parentDirectory.list();
			for(String childName : childNameList){
				String childFullPath = parentDirectory.getPath() + NdConstants.PATH_SEPARATOR + childName;
				File child = new File(childFullPath);
				if (child.isDirectory()){
					NdPackage childPackage = new NdPackage(child);
					parentPackage.addChildPackage(childPackage);
					digDirectory(childPackage, project);
				}
			}
		}
	}

	/**
	 * コンパイル単位をパッケージに登録する
	 * @param targetPackage
	 */
	private void loadCompilationUnits(NdPackage targetPackage){
		if (targetPackage != null){
			File directory = targetPackage.getDirectory();
			if (directory != null){
				String[] childNameList = directory.list();
				for(String childName : childNameList){
					if (isSourceFileName(childName)){
						String path = directory.getAbsolutePath();
						NdCompilationUnit newUnit = new NdCompilationUnit(path, childName);
						targetPackage.addCompilationUnit(newUnit);
					}
				}
			}
		}
	}
	
	/**
	 * Javaソースファイル名（コンパイル単位）か否かを応答する
	 * @param name
	 * @return
	 */
	private boolean isSourceFileName(String name){

		
		//テストフィルターここから
//		if (!name.startsWith("Z")){
//			return false;
//		}
		//テストフィルターここまで

		
		return (name != null) && (name.toUpperCase().endsWith(NdConstants.SOURCE_FILE_EXTENSION));
	}
	
	/**
	 * 有効なディレクトリか否かを応答する
	 * @param check
	 * @return
	 */
	private boolean isAvailableDirectory(File check){
		return (check != null) && (check.exists()) && (check.isDirectory());
	}
	
	/**
	 * コンストラクタ
	 */
	public NdProjectLoader(){
		super();
	}
}
