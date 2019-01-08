/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.ui.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import jp.co.nextdesign.ddb.BdMessageResource;
import jp.co.nextdesign.ddb.ui.BdUiException;
import jp.co.nextdesign.util.NdUtilException;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * ビルド設定（プロジェクトフォルダ,テンプレートフォルダ,GroupId,ArtifactId情報）
 * @author murayama
 */
public class BdBuilderSetting implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String SERIALIZE_FILE_NAME = "BuilderSetting.dat";
	private File userProjectFolder = null;
	private File templateProjectFolder = null;
	private String groupId;
	private String artifactId;
		
	/** グループID */
	public String getGroupId() {
		return groupId != null ? groupId : "";
	}

	/** グループID */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/** アーティファクトID */
	public String getArtifactId() {
		return artifactId != null ? artifactId : "";
	}

	/** アーティファクトID */
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	/**
	 * プロジェクトフォルダディレクトリ、テンプレートフォルダディレクトリがともに有効か否かを応答する
	 * @return
	 */
	public boolean isValidSetting(){
		boolean result = false;
		if ((userProjectFolder != null) && (userProjectFolder.exists()) && (userProjectFolder.isDirectory())){
			if ((templateProjectFolder != null) && (templateProjectFolder.exists()) && (templateProjectFolder.isDirectory())){
				result = groupId != null && !groupId.isEmpty() && artifactId != null && !artifactId.isEmpty();
			}
		}
		return result;
	}
	
	private static final String[] INVALID_CHARS = { "\\", "/", "@", "-" };
	private static final String PERIOD = ".";
	/**
	 * 入力情報をチェックしてメッセージを応答する
	 * @return
	 */
	public String checkSetting(){
		String result = "";
		if ((userProjectFolder == null) || (!userProjectFolder.exists()) || (!userProjectFolder.isDirectory())){
			result = BdMessageResource.get("error.settings.userProjectFolder"); //"ユーザプロジェクトフォルダの指定が不正です";
		} else if ((templateProjectFolder == null) || (!templateProjectFolder.exists()) || (!templateProjectFolder.isDirectory())){
			result = BdMessageResource.get("error.settings.templateProjectFolder"); //"テンプレートフォルダの指定が不正です";
		} else if (groupId == null || groupId.isEmpty()) {
			result = BdMessageResource.get("error.settings.groupId1"); //"groupIdを入力してください";
		} else if (artifactId == null || artifactId.isEmpty()){
			result = BdMessageResource.get("error.settings.artifactId1"); //"artifactIdを入力してください";
		}
		if (result.isEmpty()){
			for (String s : INVALID_CHARS) {
				if (groupId.contains(s)){
					result = BdMessageResource.get("error.settings.groupId2"); //"groupIdが不正な文字を含んでいます";
					break;
				}
			}
		}
		if (result.isEmpty()){
			if (groupId.startsWith(PERIOD) || groupId.endsWith(PERIOD)) {
				result = BdMessageResource.get("error.settings.groupId3"); //"groupIdが不正な文字で開始/終了しています";
			}
		}
		if (result.isEmpty()){
			if (groupId.contains(PERIOD + PERIOD)) {
				result = BdMessageResource.get("error.settings.groupId4"); //"groupIdが不正な文字「..」を含んでいます";
			}
		}
		if (result.isEmpty()){
			for (String s : INVALID_CHARS) {
				if (artifactId.contains(s)){
					result = BdMessageResource.get("error.settings.artifactId2"); //"artifactIdが不正な文字を含んでいます";
					break;
				}
			}
		}
		if (result.isEmpty()){
			if (artifactId.contains(PERIOD)) {
				result = BdMessageResource.get("error.settings.artifactId2"); //"artifactIdが不正な文字を含んでいます";
			}
		}
		return result;
	}

	/** ユーザプロジェクトフォルダを設定する */
	public void setUserProjectFolder(File sourceDirectory) {
		this.userProjectFolder = sourceDirectory;
	}
	
	/** ユーザプロジェクトフォルダを応答する */
	public File getUserProjectFolder() {
		File result = null;
		if ((userProjectFolder != null) && (userProjectFolder.exists()) && (userProjectFolder.isDirectory())){
			result = userProjectFolder;
		}
		return result;
	}

	/** テンプレートプロジェクトフォルダを設定する */
	public void setTemplateProjectFolder(File templateFolderDirectory) {
		this.templateProjectFolder = templateFolderDirectory;
	}

	/** テンプレートプロジェクトフォルダを応答する */
	public File getTemplateProjectFolder() {
		File result = null;
		if ((templateProjectFolder != null) && (templateProjectFolder.exists()) && (templateProjectFolder.isDirectory())){
			result = templateProjectFolder;
		}
		return result;
	}

	/** コンストラクタ */
	private BdBuilderSetting() {
		super();
	}

	/** 復元する  */
	public static synchronized BdBuilderSetting retrieveLastSetting() throws BdUiException, NdUtilException {
		BdBuilderSetting result = null;
		FileInputStream fileIn = null;
		ObjectInputStream in = null;
		try {
			fileIn = new FileInputStream(SERIALIZE_FILE_NAME);
			in = new ObjectInputStream(fileIn);			
			result = (BdBuilderSetting)in.readObject();
			NdLogger.getInstance().info("BdBuilderSetting#getFileHistory:RETRIEVED"); //復元成功
		} catch (FileNotFoundException ex) {
			result = new BdBuilderSetting(); //空の履歴
		} catch (IOException ex) {
			throw new BdUiException("BdBuilderSetting#retrieveHistory:IOException", ex);
		} catch (ClassNotFoundException ex) {
			throw new BdUiException("BdBuilderSetting#retrieveHistory:ClassNotFoundException", ex);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				throw new BdUiException("BdBuilderSetting#retrieveHistory:@close in", ex);
			} finally {
				if (fileIn != null) {
					try{
						fileIn.close();
					} catch (IOException ex){
						throw new BdUiException("BdBuilderSetting#retrieveHistory:@close fileIn", ex);
					}
				}
			}
		}
		//2016.11.2 テンプレートフォルダを固定にした ver1.0公開用
		result.setTemplateProjectFolder(new File("templateddb"));
		return result;
	}

	/** 保存する  */
	public void save() throws NdUtilException, BdUiException {
		NdLogger.getInstance().debug("BdBuilderSetting#save");
		File genertedFile = new File(SERIALIZE_FILE_NAME);
		FileOutputStream fileOut = null;
		ObjectOutputStream out = null;
		try {
			fileOut = new FileOutputStream(genertedFile);
			out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
		} catch (IOException ex) {
			throw new BdUiException("BdBuilderSetting#save", ex);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
				throw new BdUiException("BdBuilderSetting#save:@close out", ex);
			} finally {
				if (fileOut != null) {
					try{
						fileOut.close();
					}catch(Exception ex){
						throw new BdUiException("BdBuilderSetting#save:@close fileOut", ex);
					}
				}
			}
		}
	}
}
