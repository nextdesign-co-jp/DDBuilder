/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

/**
 * 初期化子
 * @author murayama
 */
public class NdInitializer extends NdAbstractMethod {

	private NdAbstractClassifier classifier;
	
	/**
	 * 要素型の表示名を応答する
	 * @return
	 */
	public String getDisplayTypeName(){
		return "初期化子";
	}

	/**
	 * 所属するクラス（双方向関連）を設定する
	 * @param classifier
	 */
	public void setClassifier(NdAbstractClassifier classifier){
		if ((classifier != null) && (this.classifier != classifier)){
			this.classifier = classifier;
			classifier.addInitializer(this);
		}
	}

	/**
	 * 所属するクラス（双方向関連）を応答する
	 * @return
	 */
	public NdAbstractClassifier getClassifier(){
		return this.classifier;
	}
}
