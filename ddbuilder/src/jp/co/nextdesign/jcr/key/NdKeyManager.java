/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.key;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import jp.co.nextdesign.jcr.NdConstants;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * ネクストデザインキーマネージャ
 * @author murayama
 */
public class NdKeyManager {

	private static final String KEY_NAME = "key.nextdesign";
	private static NdKeyManager instance;
	private NdAbstractKey key;
	
	/**
	 * シングルトン
	 * @return
	 */
	public static synchronized NdKeyManager getInstance(){
		if (instance == null){
			instance = new NdKeyManager();
			instance.loadKey();
		}
		return instance;
	}
	
	/**
	 * 現キーを応答する
	 * @return
	 */
	public NdAbstractKey getKey(){
		return this.key;
	}
	
	/**
	 * キーファイルを読み現キーとして設定する
	 */
	private void loadKey(){
		this.key = new NdNullKey();
		FileInputStream fileIn = null;
		ObjectInputStream in = null;
		try {
			fileIn = new FileInputStream(KEY_NAME);
			in = new ObjectInputStream(fileIn);
			this.key = (NdStandardKey)in.readObject();
		} catch (FileNotFoundException ex){
			//キーなし
		} catch (Exception ex) {
			NdLogger.getInstance().error("キーロードエラー", ex);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (fileIn != null) {
					fileIn.close();
				}
			} catch (Exception ex) {
				NdLogger.getInstance().error("キーロードエラー2", ex);
			}
		}
	}

	/**
	 * 引数のキーをファイル出力する
	 * @param newKey
	 */
	public void write(NdStandardKey newKey){
		FileOutputStream fileOut = null;
		ObjectOutputStream out = null;
		try {
			File outputFile = new File(KEY_NAME);
			fileOut = new FileOutputStream(outputFile);
			out = new ObjectOutputStream(fileOut);
			out.writeObject(newKey);
		} catch (IOException ex) {
			NdLogger.getInstance().error("キー保存エラー", ex);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (fileOut != null) {
					fileOut.close();
				}
			} catch (IOException ex) {
				NdLogger.getInstance().error("キー保存エラー2", ex);
			}
		}
	}
	
	/**
	 * 作成用
	 * @param args
	 */
	public static void main(String[] args){
		/*
		 * フッター用は日本語24文字が限界
		 */
		int version = NdConstants.VERSION_MAJOR;
		String userName = "無償版"; //設定ダイアログにだけ表示される会社名
		String userName4Footer = "NextDesign"; //フッターに表示される会社名

		NdStandardKey key = new NdStandardKey(version, userName, userName4Footer);
		NdKeyManager manager = NdKeyManager.getInstance();
		manager.write(key);		
		manager.loadKey();
		NdAbstractKey newKey = manager.getKey();
		System.out.println("完了");
		System.out.println("ユーザ名=" + newKey.getUserName());
		System.out.println("フッター用ユーザ名=" + newKey.getUserName4Footer());
		System.out.println("バージョン=" + newKey.getVersion());
		System.out.println("生成日=" + newKey.getCreation());
		return;
	}
}
