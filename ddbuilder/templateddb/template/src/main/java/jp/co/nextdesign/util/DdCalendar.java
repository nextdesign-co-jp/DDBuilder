/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class DdCalendar {

	private static final TimeZone TIME_ZONE_JAPAN = TimeZone.getTimeZone("Asia/Tokyo");
	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static String getNow(){
		return getFormattedDate(DATE_FORMAT);
	}
	
	public static String getFormattedDate(SimpleDateFormat format){
		String result = "";
		if (format != null){
			Calendar cal = Calendar.getInstance();
			format.setTimeZone(TIME_ZONE_JAPAN);
			result = format.format(cal.getTime());
		}
		return result;
	}
}
