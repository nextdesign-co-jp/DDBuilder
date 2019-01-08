/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * 外部プロパティ情報
 * @author murayama
 */
public class NdApplicationProperty {
	
	private static NdApplicationProperty instance;
	private Properties properties;
	private static final String PROP_KEY_CHARACTER_CODE = "CharacterCode";
	private static final String PROP_KEY_METHOD_SIZE_WARNING = "MethodSizeWarning";
	private static final String PROP_KEY_METHOD_NEST_WARNING = "MethodNestWarning";
	private static final String PROP_KEY_SYSTEM_NAME = "SystemName";
	private static final String PROP_KEY_COMPANY_NAME = "CompanyName";
	private static final String PROP_KEY_PRODUCT = "Product";
	private static final String PROPERTIES_FILE_NAME = "Application.properties";
	
	/**
	 * シングルトン
	 * @return
	 * @throws NdApplicationPropertyException 
	 */
	public static synchronized NdApplicationProperty getInstance() throws NdApplicationPropertyException{
		if (instance == null){
			instance = new NdApplicationProperty();
		}
		return instance;
	}
	
	/**
	 * コンストラクタ
	 * プロパティファイルを読み込む
	 * @throws NdApplicationPropertyException 
	 */
	private NdApplicationProperty() throws NdApplicationPropertyException{
		properties = new Properties();
		this.load();
	}
	
	/**
	 * プロパティを読み込む
	 * @throws NdApplicationPropertyException
	 */
	public void load() throws NdApplicationPropertyException{
		try {
			InputStream inputStream = new FileInputStream(PROPERTIES_FILE_NAME);
			properties.load(inputStream);
		} catch(IOException ex) {
			throw new NdApplicationPropertyException("例外 load時 ", ex);
		}
	}
	
	/**
	 * プロパティを書き出す
	 * @throws NdApplicationPropertyException
	 */
	public void store() throws NdApplicationPropertyException{
		try {
			OutputStream outputStream = new FileOutputStream(PROPERTIES_FILE_NAME);
			properties.store(outputStream, "JClassReport Properties");
		} catch(IOException ex) {
			throw new NdApplicationPropertyException("例外 store時 ", ex);
		}
	}
	
	/**
	 * 製品モードか否かを応答する
	 * @return
	 */
	public boolean isProduct(){
		boolean result = false;
		if (this.properties != null){
			String value = this.properties.getProperty(PROP_KEY_PRODUCT);
			if ("1".equals(value)){
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * 文字コードを応答する
	 * @return
	 */
	public String getCharacterCode(){
		String result = null;
		if (this.properties != null){
			result = this.properties.getProperty(PROP_KEY_CHARACTER_CODE);
		}
		if (result == null){
			result = "UTF-8";
		}
		return result;
	}
	
	/**
	 * 文字コードをプロパティに設定する
	 * @param value
	 */
	public void setCharacterCode(String value){
		setStringProperty(PROP_KEY_CHARACTER_CODE, value);
	}
	
	/**
	 * システム名を応答する
	 * @return
	 */
	public String getSystemName(){
		String result = null;
		if (this.properties != null){
			result = this.properties.getProperty(PROP_KEY_SYSTEM_NAME);
		}
		if (result == null){
			result = "システム名";
		}
		return result;
	}
	
	/**
	 * システム名をプロパティに設定する
	 * @param value
	 */
	public void setSystemName(String value){
		setStringProperty(PROP_KEY_SYSTEM_NAME, value);
	}

	/**
	 * 会社名を応答する
	 * @return
	 */
	public String getCompanyName(){
		String result = null;
		if (this.properties != null){
			result = this.properties.getProperty(PROP_KEY_COMPANY_NAME);
		}
		if (result == null){
			result = "会社名";
		}
		return result;
	}
	
	/**
	 * 会社名をプロパティに設定する
	 * @param value
	 */
	public void setCompanyName(String value){
		setStringProperty(PROP_KEY_COMPANY_NAME, value);
	}
	
	/**
	 * メソッド行数の警告サイズを応答する
	 * @return
	 */
	public int getMethodSizeWarning(){
		int result = this.getIntProperty(PROP_KEY_METHOD_SIZE_WARNING);
		if (result < 0){
			result = 100;
		}
		return result;
	}
	
	/**
	 * メソッド行数の警告サイズをを設定する
	 * @param value
	 */
	public void setMethodSizeWarning(Integer value){
		this.setIntProperty(PROP_KEY_METHOD_SIZE_WARNING, value);
	}
	
	/**
	 * 警告ネストレベルを応答する
	 * @return
	 */
	public int getMethodNestWarning(){
		int result = this.getIntProperty(PROP_KEY_METHOD_NEST_WARNING);
		if (result < 0){
			result = 4;
		}
		return result;
	}
	
	/**
	 * 警告ネストレベルを設定する
	 * @param value
	 */
	public void setMethodNestWarning(Integer value){
		this.setIntProperty(PROP_KEY_METHOD_NEST_WARNING, value);
	}

	/**
	 * 整数型プロパティ値を読み取り応答する
	 * @param key
	 * @return
	 */
	private int getIntProperty(String key){
		int result = -1;
		if ((this.properties != null) && (key != null)){
			String val = this.properties.getProperty(key);
			if (val != null){
				try{
					result = Integer.parseInt(val);
				} catch(Exception ex) {
					result = -1;
				}
			}
		}
		return result;
	}
	
	/**
	 * 整数型プロパティを設定する
	 * @param key
	 * @param value
	 */
	private void setIntProperty(String key, Integer value){
		if (value >= 0){
			this.properties.setProperty(key, value.toString());
		}
	}

	/**
	 * 文字型プロパティを設定する
	 * @param key
	 * @param value
	 */
	private void setStringProperty(String key, String value){
		if ((value != null) && (!"".equals(value))){
			this.properties.setProperty(key, value);
		}
	}
	
	/**
	 * 自己テスト
	 * @param args
	 */
//	public static void main(String[] args){
//		try{
//			NdApplicationProperty prop = NdApplicationProperty.getInstance();
//			System.out.println("CharacterCode=" + prop.getCharacterCode());
//			System.out.println("MethodSizeWarning=" + prop.getMethodSizeWarning());
//			System.out.println("MethodNestWarning=" + prop.getMethodNestWarning());
//			System.out.println("SystemName=" + prop.getSystemName());
//			System.out.println("CompanyName=" + prop.getCompanyName());
//			
//			prop.setCharacterCode("UTF-8");
//			prop.setMethodNestWarning(5);
//			prop.setMethodSizeWarning(50);
//			prop.setSystemName("");
//			prop.setCompanyName("");
//			prop.store();
//			
//		} catch(Exception ex){
//			System.out.println("例外" + ex.toString());
//		}
//	}
}

