/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

import java.io.File;

/**
 * プロジェクト管理者（シングルトン）
 * @author murayama
 */
public class NdProjectManager {
	
	private static NdProjectManager instance;
	private NdProject currentProject;
	
	/**
	 * シングルトン
	 * @return
	 */
	public static synchronized NdProjectManager getInstance(){
		if (instance == null){
			instance = new NdProjectManager();
		}
		return instance;
	}
	
	/**
	 * コンストラクタ
	 */
	private NdProjectManager(){
		super();
	}
	
	/**
	 * 現在のプロジェクトを応答する
	 * @return
	 */
	public NdProject getCurrentProject() {
		return currentProject;
	}
	
	/**
	 * 現在のプロジェクトを設定する
	 * @param currentProject
	 */
	public void setCurrentProject(NdProject currentProject) {
		this.currentProject = currentProject;
	}

	/**
	 * プロジェクトを構築する
	 * @param topDirectory
	 * @return
	 * @throws NdModelException
	 */
	public NdProject createProject(File topDirectory) throws NdModelException{
		if (isAvailableDirectory(topDirectory)){
			this.currentProject = new NdProject();
			this.currentProject.load(topDirectory);
		} else {
			String message = "無効なディレクトリです。";
			if (topDirectory != null){
				message += " Path=" + topDirectory.getAbsolutePath();
			}
			throw new NdModelException(message);
		}
		return this.currentProject;
	}
	
	/**
	 * 正しいディレクトリか否かを応答する
	 * @param check
	 * @return
	 */
	private static boolean isAvailableDirectory(File check) {
		return (check != null) && (check.exists()) && (check.isDirectory());
	}
}
