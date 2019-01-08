/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

import java.util.ArrayList;

/**
 * モディファイア
 * @author murayama
 *
 */
public class NdModifier extends NdExtendedModifier{

	/**
	 * モディファイアか否かを応答する
	 * @return
	 */
	@Override public boolean isModifier(){
		return true;
	}

	/**
	 * キーワードが一致するモディファイアを応答する
	 * @param keyword
	 * @return
	 */
	public static NdModifier getModifierByKeyword(String keyword) {
		NdModifier result = null;
		for(NdModifier m : availableKeywords){
			if (m.getKeyword().equals(keyword)){
				result = m;
				break;
			}
		}
		if(result == null){
			result = new NdModifier("不明なKeyword=" + keyword);
		}
		return result;
	}
	
	/**
	 * コンストラクタ
	 * @param keyword
	 */
	private NdModifier(String keyword){
		super(keyword);
	}
		
	/**
	 * publicか否かを応答する
	 * @return
	 */
	public boolean isPublic(){
		return PUBLIC.equals(this.getKeyword());
	}

	/**
	 * privateか否かを応答する
	 * @return
	 */
	public boolean isPrivate(){
		return PRIVATE.equals(this.getKeyword());
	}

	/**
	 * protctedか否かを応答する
	 * @return
	 */
	public boolean isProtected(){
		return PROTECTED.equals(this.getKeyword());
	}

	/**
	 * abstractか否かを応答する
	 * @return
	 */
	public boolean isAbstract(){
		return ABSTRACT.equals(this.getKeyword());
	}
	
	public static final String PUBLIC = "public";
	public static final String PROTECTED = "protected";
	public static final String PRIVATE = "private";
	public static final String STATIC = "static";
	public static final String ABSTRACT = "abstract";
	public static final String FINAL = "final";
	public static final String NATIVE = "native";
	public static final String SYNCHRONIZED = "synchronized";
	public static final String TRANSIENT = "transient";
	public static final String VOLATILE = "volatile";
	public static final String STRICTFP = "strictfp";

	/**
	 * モディファイア一覧	
	 */
	private static ArrayList<NdModifier> availableKeywords = new ArrayList<NdModifier>(){
		private static final long serialVersionUID = 1L;
		{
			add(new NdModifier(PUBLIC));
			add(new NdModifier(PROTECTED));
			add(new NdModifier(PRIVATE));
			add(new NdModifier(STATIC));
			add(new NdModifier(ABSTRACT));
			add(new NdModifier(FINAL));
			add(new NdModifier(NATIVE));
			add(new NdModifier(SYNCHRONIZED));
			add(new NdModifier(TRANSIENT));
			add(new NdModifier(VOLATILE));
			add(new NdModifier(STRICTFP));
		}
	};
}
