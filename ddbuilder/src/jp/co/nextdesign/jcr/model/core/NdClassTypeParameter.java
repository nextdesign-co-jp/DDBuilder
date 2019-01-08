/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

import java.util.ArrayList;

import jp.co.nextdesign.jcr.model.typelink.NdAbstractTypeLink;

/**
 * クラス宣言 型引数
 * @author murayama
 *
 */
public class NdClassTypeParameter {

	/** 型名 */
	private String typeName;
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String name) {
		this.typeName = name;
	}
	
	/** 境界型リスト */
	private ArrayList<NdAbstractTypeLink> typeBoundList;
	public ArrayList<NdAbstractTypeLink> getTypeBoundList() {
		return typeBoundList;
	}
	public void addTypeBound(NdAbstractTypeLink newLink){
		if ((newLink != null) && (!this.typeBoundList.contains(newLink))){
			this.typeBoundList.add(newLink);
		}
	}
	
	/** コンストラクタ */
	public NdClassTypeParameter(){
		this.typeName = "";
		this.typeBoundList = new ArrayList<NdAbstractTypeLink>();
	}
}
