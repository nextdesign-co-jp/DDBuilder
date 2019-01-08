/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr;

import java.io.File;

/**
 * Jクラスレポート定数
 * @author murayama
 *
 */
public class NdConstants {
	
	public static final int VERSION_MAJOR = 8;
	public static final int VERSION_MINOR = 2;
	public static final String VERSION = VERSION_MAJOR + "." + VERSION_MINOR;

	public static final String TITLE = "Jクラスレポート" + VERSION;
	public static final String UNDEFINED_PATH = "選択してください";

	public static final String SOURCE_FILE_EXTENSION = ".JAVA";
	public static final String PATH_SEPARATOR = File.separator; //2012.9.7 mod
	public static final String DEBUG_TAB = "+---";
	
	public static final short FOOTER_FONT_SIZE = 9;
}
