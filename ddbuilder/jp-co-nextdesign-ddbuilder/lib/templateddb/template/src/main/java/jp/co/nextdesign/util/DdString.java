/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.util;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class DdString {

	/** 指定長までにする */
	public static String adjustLength(String in, int maxLength){
		String result = "";
		if (in != null) {
			if (in.length() > maxLength){
				result = in.substring(0, maxLength);
			} else {
				result= in;
			}
		}
		return result;
	}

	/** nullケースを除外する */
	public static String omitNull(String in){
		if (in != null){
			return in;
		}
		return "";
	}

	/** nullまたは空文字列か */
	public static boolean isNullOrEmpty(String in){
		return in == null || "".equals(in);
	}

	/** 文字列を正規化する（全角/半角、lower-case） */
	public static String normalize(String in){
		String result = Normalizer.normalize(in, Normalizer.Form.NFKC);
		result = result.toLowerCase();
		result = result.replaceAll(" ", "");
		return result;
	}

	/** 検索用 */
	public static List<String> splitIntoNormalizedNames(String in){
		List<String> resultList = new ArrayList<String>();
		String work = normalize(in);
		String[] names = work.split(",");
		for(String name : names){
			if ( ! isNullOrEmpty(name)){
				resultList.add(name);
			}
		}
		return resultList;
	}
	
	/** 最大長までで切り捨てる。切り捨てた場合は最後に「…」を付ける */
	public static String toShortMessage(String param, int maxLength){
		String result = "";
		if (param != null){
			if (param.length() > maxLength){
				result = param.substring(0, maxLength) + "…";
			} else {
				result = param;
			}
		}
		return result;
	}
}
