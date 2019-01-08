/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * クラス
 * トップレベルクラス（publicクラス）
 * ローカルクラス（非publicクラス）
 * 内部クラス
 * @author murayama
 */
public class NdClass extends NdAbstractClassifier {
	
	/**
	 * JPA @Entityクラスか否かを応答する DDB add 2014.4.16
	 * NdClassでoverrideする
	 * @return
	 */
	@Override
	public boolean isJpaEntityClass(){
		boolean result = false;
		if (!this.isInnerClass() && !this.isAnonymousClass()){
			for(NdExtendedModifier modifier : this.getModifierList()){
				if (modifier.isAnnoteation() && "@Entity".equals(modifier.getKeyword())){
					result = true;
				}
			}
		}
		return result;
	}
	
	/**
	 * 要素型の表示名を応答する
	 * @return
	 */
	public String getDisplayTypeName(){
		String result = "クラス";
		if (this.isInnerClass()){
			result = "内部クラス";
		} else if (this.isAnonymousClass()){
			result = "無名クラス";
		} else if (!this.isPublic()) {
			result = "<非public>" + result;
		}
		return result;
	}

	/**
	 * コンストラクタ
	 * @param node
	 */
	public NdClass(TypeDeclaration node){
		super(node);
	}
	
	/**
	 * コンストラクタ（無名クラスの場合に使用する）
	 */
	public NdClass(String anonymousName){
		super();
		this.setName(anonymousName);
	}
}
