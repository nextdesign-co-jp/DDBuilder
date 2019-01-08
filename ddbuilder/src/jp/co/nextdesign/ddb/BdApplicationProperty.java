/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * 外部プロパティ情報
 * 2016.8.20時点で、このクラスはNdApplicationPropertyと全く同じ。（BdXxxxとNdXxxxの違いのみ）
 * DDBuilder起動時にBdApplicationPropertyがロードされ、JCR部が実行されるとNdApplicationPropertyがロードされる。（二度ロードされる）
 * ロード対象は同じApplication.propertiesである。DDB開発では基本方針としてJCRを変更しないので、このように二重に存在している。
 * DDBApplication.propertiesのような別ファイルにすべきかもしれないし、NdApplicationPropertyだけにしても良いかもしれない。
 * ただし、DDB固有のプロパティが必要になった場合は、別ファイルにすべきと思うが、2016.8.20時点では、このままの作りにしておく。
 * 
 * 2017.1.18 Application.propertiesはJCR専用とし、DDBApplication.propertiesをDDBuilder専用として追加した。
 * @author murayama
 */
public class BdApplicationProperty {
	
	private static BdApplicationProperty instance;
	private Properties properties;
	private static final String PROP_KEY_CHARACTER_CODE = "CharacterCode";
	private static final String PROP_KEY_METHOD_SIZE_WARNING = "MethodSizeWarning";
	private static final String PROP_KEY_METHOD_NEST_WARNING = "MethodNestWarning";
	private static final String PROP_KEY_SYSTEM_NAME = "SystemName";
	private static final String PROP_KEY_COMPANY_NAME = "CompanyName";
	private static final String PROP_KEY_HOME_PAGE_CLASS_NAME = "HomePageClassName";
	private static final String PROP_KEY_PRODUCT = "Product";
	private static final String PROPERTIES_FILE_NAME = "DDBApplication.properties";
	
	/**
	 * シングルトン
	 * @return
	 * @throws BdApplicationPropertyException 
	 */
	public static synchronized BdApplicationProperty getInstance() throws BdApplicationPropertyException{
		if (instance == null){
			instance = new BdApplicationProperty();
		}
		return instance;
	}
	
	/**
	 * コンストラクタ
	 * プロパティファイルを読み込む
	 * @throws BdApplicationPropertyException 
	 */
	private BdApplicationProperty() throws BdApplicationPropertyException{
		properties = new Properties();
		this.load();
	}
	
	//次の2つをtrueにしてもNdLoggerはBdMainWindow#initLoggingの結果に従うので出力されない場合もある
	public static boolean LOG_USER_PROJECT_DUMP = false; //UserProjectのログ出力
	public static boolean LOG_TEMPLATE_PROJECT_DUMP = false; //TemplateProjectのログ出力
	
	public static boolean LOG_ENTITY_AND_ALL_DOMAIN_CLASS_LIST = false; //UserProjectのallDomainClassList, entityAnnotatedDomainClassListのログ出力
	public static boolean LOG_SERVICE_CLASS_LIST = false; //UserProjectのallServiceClassListのログ出力
	public static boolean LOG_GENERATING_FILE_NAME = false; //生成ファイル名のログ出力

	/**
	 * プロパティを読み込む
	 * @throws BdApplicationPropertyException
	 */
	public void load() throws BdApplicationPropertyException{
		try {
			InputStream inputStream = new FileInputStream(PROPERTIES_FILE_NAME);
			properties.load(inputStream);
		} catch(IOException ex) {
			throw new BdApplicationPropertyException("[EXCEPTION] loading ", ex);
		}
	}
	
	/**
	 * プロパティを書き出す
	 * @throws BdApplicationPropertyException
	 */
	public void store() throws BdApplicationPropertyException{
		try {
			OutputStream outputStream = new FileOutputStream(PROPERTIES_FILE_NAME);
			properties.store(outputStream, "DDBuilder Properties");
		} catch(IOException ex) {
			throw new BdApplicationPropertyException("[EXCEPTION] storing ", ex);
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
		if (result == null || result.length() < 1 || EMPTY_VALUE.equals(result)){
			result = BdMessageResource.get("setting.value.systemname"); //"システム名";
		}
		return result;
	}
	
	/**
	 * システム名をプロパティに設定する
	 * @param value
	 */
	public void setSystemName(String value){
		if (value == null || value.length() < 1){
			value = EMPTY_VALUE;
		}
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
		if (result == null || result.length() < 1 || EMPTY_VALUE.equals(result)){
			result = BdMessageResource.get("setting.value.companyname"); //"会社名";
		}
		return result;
	}
	
	/**
	 * 会社名をプロパティに設定する
	 * @param value
	 */
	public void setCompanyName(String value){
		if (value == null || value.length() < 1){
			value = EMPTY_VALUE;
		}
		setStringProperty(PROP_KEY_COMPANY_NAME, value);
	}

	//Propertyの値として空文字列は設定できないので、未指定の場合はこの定数を設定値として使用する。2017.1.8
	private static final String EMPTY_VALUE = "-";
	
	/**
	 * ホームページクラス名を応答する
	 * @return
	 */
	public String getHomePageClassName(){
		String result = null;
		if (this.properties != null){
			result = this.properties.getProperty(PROP_KEY_HOME_PAGE_CLASS_NAME);
		}
		if (result == null || result.length() < 1 || EMPTY_VALUE.equals(result)){
			result = "";
		}
		return result;
	}
	
	/**
	 * ホームページクラス名をプロパティに設定する
	 * @param value
	 */
	public void setHomePageClassName(String value){
		if (value == null || value.length() < 1){
			value = EMPTY_VALUE;
		}
		setStringProperty(PROP_KEY_HOME_PAGE_CLASS_NAME, value);
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
//			BdApplicationProperty prop = BdApplicationProperty.getInstance();
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
//			System.out.println("[EXCEPTION]" + ex.toString());
//		}
//	}
}
