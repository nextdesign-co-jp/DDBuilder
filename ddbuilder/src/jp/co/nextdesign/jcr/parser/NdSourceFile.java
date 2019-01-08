/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.parser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import jp.co.nextdesign.jcr.NdApplicationProperty;
import jp.co.nextdesign.jcr.NdApplicationPropertyException;

/**
 * Javaソースファイル
 * @author murayama
 */
public class NdSourceFile {

	private String fileName;
	private char[] charArray = new char[0];
	private int lineCount;

	/**
	 * コンストラクタ
	 * @param fileName
	 * @throws NdParserException 
	 * @throws NdApplicationPropertyException 
	 */
	public NdSourceFile(String fileName) throws NdApplicationPropertyException, NdParserException {
		this.fileName = fileName;
		init();
	}

	/**
	 * 初期化処理
	 * @throws NdApplicationPropertyException 
	 * @throws NdParserException 
	 */
	private void init() throws NdApplicationPropertyException, NdParserException {
		String characterCode = NdApplicationProperty.getInstance().getCharacterCode();
		if (fileName != null){
			try{
				StringBuffer sb = new StringBuffer();
				FileInputStream fis = new FileInputStream(fileName);
				InputStreamReader isr = new InputStreamReader(fis, characterCode);
				BufferedReader br = new BufferedReader(isr);
				this.lineCount = 0;
				String line;
				while ((line = br.readLine()) != null){
					sb.append(line + "\n");
					this.lineCount++;
				}
				this.charArray = sb.toString().toCharArray();
				br.close();
			} catch(FileNotFoundException ex) {
				throw new NdParserException(this.getClass().getSimpleName() + "ファイル名不明=" + fileName, ex);
			} catch (UnsupportedEncodingException ex) {
				throw new NdParserException(this.getClass().getSimpleName() + "プロパティ（Application.properties）の文字コード名をチェック=" + characterCode, ex);
			} catch(IOException ex) {
				throw new NdParserException(this.getClass().getSimpleName() + "ファイル行読込み例外=" + fileName, ex);
			}
		}
	}

	/**
	 * 文字配列を応答する
	 * @return
	 */
	public char[] getCharArray() {
		return charArray;
	}

	/**
	 * 行数を応答する
	 * @return
	 */
	public int getLineCount() {
		return lineCount;
	}
}
