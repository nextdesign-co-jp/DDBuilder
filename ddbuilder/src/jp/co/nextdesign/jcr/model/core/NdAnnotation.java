/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

import java.util.List;

/**
 * アノテーション（モディファイアと同列に記述される１つ１つのアノテーション）
 * @author murayama
 */
public class NdAnnotation extends NdExtendedModifier {

	private List<NdAnnotationAttribute> annotationAttributeList;
	
	/**
	 * アノテーションか否かを応答する
	 * @return
	 */
	@Override public boolean isAnnoteation(){
		return true;
	}
	
	/**
	 * キーワードを応答する
	 */
	@Override public String getKeyword(){
		return "@" + this.keyword;
	}

	/**
	 * コンストラクタ
	 * @param keyword
	 */
	public NdAnnotation(String keyword, List<NdAnnotationAttribute> annotationAttributeList){
		super(keyword);
		this.annotationAttributeList = annotationAttributeList;
	}
	
	/**
	 * 属性リストを応答する
	 * @return
	 */
	public List<NdAnnotationAttribute> getAnnotationAttributeList(){
		return this.annotationAttributeList;
	}
}
