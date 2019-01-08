/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model;

import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;

/**
 * 名前
 * （例） Main.javaファイルにMain, C1が定義されているとき
 * package pk1;
 * public class Main{
 *     private pkg1.C1.C11 var1; //限定名
 *     private C1.C11 var2; //限定名
 *     private C11 var3; //C11は型未解決でエラー
 *     private C1 var4; //単純名
 * }
 * class C1{
 *     class C11{
 *     }
 * }
 * @author murayama
 */
public class NdName {
	
	private String simpleName;
	private String qualifiedName;
	private boolean isQualifiedName = false;
	
	/**
	 * 名前（ドットを含まない文字列）を応答する
	 * @return
	 */
	public String getSimpleName() {
		return simpleName;
	}

	/**
	 * 名前（ドットを含まない文字列）を設定する
	 * @param simpleName
	 */
	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	/**
	 * 限定名（ドットを含む文字列）を応答する
	 * @return
	 */
	public String getQualifiedName() {
		return qualifiedName;
	}

	/**
	 * 限定名（ドットを含まない文字列）を設定する
	 * @param qualifiedName
	 */
	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}

	/**
	 * 限定名か否かを応答する
	 * @return
	 */
	public boolean isQualifiedName() {
		return isQualifiedName;
	}

	/**
	 * 限定名か否かを設定する
	 * @param isQualifiedName
	 */
	public void setIsQualifiedName(boolean isQualifiedName) {
		this.isQualifiedName = isQualifiedName;
	}

	/**
	 * コンストラクタ
	 */
	public NdName(){
		super();
		this.simpleName = "";
		this.qualifiedName = "";
		this.isQualifiedName = false;
	}
	
	/**
	 * コンストラクタ
	 * @param jdtName
	 */
	public NdName(Name jdtName){
		this();
		if (jdtName.isQualifiedName()){
			QualifiedName qualifiedName = (QualifiedName)jdtName;
			this.setIsQualifiedName(true);
			this.setQualifiedName(qualifiedName.getFullyQualifiedName());
			this.setSimpleName(qualifiedName.getName().getIdentifier());
		} else{
			SimpleName simpleName = (SimpleName)jdtName;
			this.setSimpleName(simpleName.getIdentifier());
		}
	}
	
	/**
	 * equals
	 */
	@Override
	public boolean equals(Object o){
		boolean result = false;
		if ((o != null) && (o instanceof NdName)){
			NdName otherName = (NdName)o;
			if (this.isQualifiedName){
				if (otherName.isQualifiedName){
					result = this.qualifiedName.equals(otherName.getQualifiedName());
				}
			} else {
				if (!otherName.isQualifiedName){
					result = this.simpleName.equals(otherName.getSimpleName());
				}
			}
		}
		return result;
	}
	
	/**
	 * hashCode
	 */
	@Override
	public int hashCode(){
		int result = 0;
		if (this.isQualifiedName){
			result = this.qualifiedName.hashCode();
		} else {
			result = this.simpleName.hashCode();
		}
		return result;
	}
}
