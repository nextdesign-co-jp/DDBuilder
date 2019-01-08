/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.documentation;

import java.util.Random;

import jp.co.nextdesign.jcr.key.NdKeyManager;

/**
 * 評価版用の文字マスク
 * @author murayama
 */
public class NdMask {

	private static Random random;
	
	/**
	 * 評価モードの場合は、オリジナル文字列の1文字だけをマスクする
	 * @param original
	 * @return
	 */
	public static synchronized String mask(String original){
		String result = original;
		if (!NdKeyManager.getInstance().getKey().isAvailable()){
			if (original != null){
				int length = original.length();
				if (length > 0){
					char[] list = original.toCharArray();
					int pos = getRandom().nextInt(length);
					list[pos] = '■';
					result = "";
					for(int i=0; i<length; i++){
						result += list[i];
					}
				}
			}
		}
		return result;
	}

	/**
	 * 乱数オブジェクトを応答する
	 * @return
	 */
	private static Random getRandom(){
		if (random == null){
			random = new Random();
		}
		return random;
	}
	
//	public static void main(String[] args){
//		for (int i=0; i<10; i++){
//			String original = "SchoolTeacher";
//			String masked = NdMask.mask(original);
//			//System.out.println(original + "    length=" + original.length());
//			System.out.println(masked + "    length=" + masked.length());
//		}
//	}
}
