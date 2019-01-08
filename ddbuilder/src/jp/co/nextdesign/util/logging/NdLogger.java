/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.util.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.co.nextdesign.ddb.BdConstants;

/**
 * ログ
 * @author murayama
 */
public class NdLogger {

	private static final int MAX_LOG_SIZE = 1000;
	private ArrayList<String> logBuffer;
	private String logFileName;
	private static NdLogger instance;
	private boolean fileLogging;
	private boolean timeStampLogging;
	private boolean infoLogging;
	private boolean debugLogging;
	private boolean errorLogging;
	
	private List<String> userMessages = new ArrayList<String>();

	/** シングルトン */
	public static synchronized NdLogger getInstance(){
		if (instance == null){
			instance = new NdLogger();
		}
		return instance;
	}
	
	public void putUserMessage(String msg){
		this.userMessages.add(msg);
	}
	
	public List<String> getUserMessages(){
		return this.userMessages;
	}

	/** info出力 */
	public void info(String msg) {
		if (infoLogging){
			String log = "[INFO]" + getTimeStamp4LogLine() + msg;
			putLog(log);
		}
	}

	/** debug出力 */
	public void debug(String msg) {
		if (debugLogging){
			String log = "[DEBUG]" + getTimeStamp4LogLine() + msg;
			putLog(log);
		}
	}

	/** debug出力 */
	public void debug(List<String> msgList, String title) {
		if (debugLogging){
			putLog("[DEBUG]リスト表示ここから title=" + title);
			for(String msg : msgList){
				String log = "[DEBUG]" + getTimeStamp4LogLine() + msg;
				putLog(log);
			}
			putLog("[DEBUG]リスト表示ここまで");
		}
	}

	/** error出力 */
	public void error(String msg, Exception ex) {
		if (errorLogging){
			String log = "[ERROR]" + getTimeStamp4LogLine() + msg;
			if (ex != null){
				log += " 例外情報:" + ex.toString();
			}
			putLog(log);
			putUserMessage(log);
		}
	}

	/** バッファリングまたはシステム出力する */
	private void putLog(String log){
		if (fileLogging){
			logBuffer.add(log);
			if (logBuffer.size() > MAX_LOG_SIZE){
				flushLog();
			}
		}else{
			System.out.println(log);
		}
	}

	/** ログバッファの内容を物理ファイルに書き出す  */
	public void flushLog() {
		if (logBuffer.size() > 0){
			writeFile();
			logBuffer.clear();
		}
	}

	/** 物理ファイルに出力する */
	private void writeFile() {
		BufferedWriter out = null;
		try {
			//out = new BufferedWriter(new FileWriter(logFileName + getTimeStamp4LogFileName() + ".txt"));
			out = new BufferedWriter(new FileWriter(logFileName + ".txt"));
			for(int i=0; i<logBuffer.size(); i++){
				out.write(logBuffer.get(i) + BdConstants.CR);
			}
			out.flush();
		} catch(IOException ex) {
			System.out.println("NdException#writeFile" + ex.toString());
			//2011.4.11 Loggerでは例外を送出しない throw new NdUtilException("NdException#writeFile", ex);
		} finally {
			if (out != null){
				try{
					out.close();
				} catch(IOException ex){
					System.out.println("NdException#writeFile@close" + ex.toString());
					//2011.4.11 Loggerでは例外を送出しない throw new NdUtilException("NdException#writeFile@close", ex);
				}
			}
		}
	}

	/** コンストラクタ */
	private NdLogger(){
		super();
		fileLogging = false;
		timeStampLogging = false;
		infoLogging = false;
		debugLogging = false;
		errorLogging = false;
		logBuffer = new ArrayList<String>();
		logFileName = "Log";
	}

	/** ファイル出力するか否かを設定する */
	public void setFileLogging(boolean fileLogging) {
		this.fileLogging = fileLogging;
	}

	/** タイムスタンプを付加するか否かを設定する */
	public void setTimeStampLogging(boolean timeStampLogging) {
		this.timeStampLogging = timeStampLogging;
	}

	/** infoを出力するか否かを設定する */
	public void setInfoLogging(boolean infoLogging) {
		this.infoLogging = infoLogging;
	}

	/** debugを出力するか否かを設定する */
	public void setDebugLogging(boolean debugLogging) {
		this.debugLogging = debugLogging;
	}
	
	/** debugを出力するか否かを応答する */
	public boolean getDebugLogging(){
		return this.debugLogging;
	}

	/** errorを出力するか否かを設定する */
	public void setErrorLogging(boolean errorLogging) {
		this.errorLogging = errorLogging;
	}

	/**
	 * ログファイル名用のタイムスタンプを応答する
	 * ログファイルは日付を付けずに常に１ファイルとする。Nファイルだと削除が必要になる。
	 * コンパイル警告を出さないためにこのメソッドはコメントアウトした。2011.10.19
	 */
//	private String getTimeStamp4LogFileName(){
//		return "_" + getTimeStamp();
//	}

	/** ログ行用のタイムスタンプを応答する */
	private String getTimeStamp4LogLine(){
		String result = "";
		if (timeStampLogging){
			result = "[" + getTimeStamp() + "]";
		}
		return result;
	}

	/** タイムスタンプを応答する（SimpleDateFormatでは桁数を揃えられなかった） */
	private String getTimeStamp(){
		String result = "";
		Calendar cal = Calendar.getInstance();
		Integer yyyy = new Integer(cal.get(Calendar.YEAR));
		Integer mm = new Integer(cal.get(Calendar.MONTH) + 1);
		Integer dd = new Integer(cal.get(Calendar.DAY_OF_MONTH));
		Integer hh = new Integer(cal.get(Calendar.HOUR_OF_DAY));
		Integer min = new Integer(cal.get(Calendar.MINUTE));
		Integer sec = new Integer(cal.get(Calendar.SECOND));
		Integer msec = new Integer(cal.get(Calendar.MILLISECOND));
		DecimalFormat df2 = new DecimalFormat( "00" );
		DecimalFormat df3 = new DecimalFormat( "000" );
		result = yyyy.toString()
		+ df2.format(mm).toString()
		+ df2.format(dd).toString() + "_"
		+ df2.format(hh).toString()
		+ df2.format(min).toString() + "_"
		+ df2.format(sec).toString() + "_"
		+ df3.format(msec).toString();
		return result;
	}
}
