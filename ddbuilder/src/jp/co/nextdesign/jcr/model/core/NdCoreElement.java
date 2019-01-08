/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.util.logging.NdLogger;

/**
 * JClassReportにとって主要なJava要素の基底クラス
 * @author murayama
 */
public abstract class NdCoreElement extends NdAbstractNamedElement {

	private List<NdExtendedModifier> modifierList = new ArrayList<NdExtendedModifier>();
	private NdJavadoc javadoc;
	private int lineCountOfStatement = 0;
	
	/**
	 * 要素型の表示名を応答する
	 * @return
	 */
	public abstract String getDisplayTypeName();
		
	/**
	 * 先頭行
	 */
	private String headerLine;

	/**
	 * Modifier　修飾子を応答する public,protected,private,abstract,final, ...
	 * @return
	 */
	public List<NdExtendedModifier> getModifierList() {
		return modifierList;
	}
	
	/**
	 * Modifier　修飾子を設定する
	 * @param newModifier
	 */
	public void setModifierList(ArrayList<NdExtendedModifier> newModifierList) {
		if (newModifierList != null){
			this.modifierList = newModifierList;
		}
	}
	
	/**
	 * publicか否かを応答する
	 * @return
	 */
	public boolean isPublic(){
		boolean result = false;
		for(NdExtendedModifier m : modifierList){
			if (m.isModifier()){
				if (((NdModifier)m).isPublic()) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * privateか否かを応答する DDB add 2014.4.20
	 * @return
	 */
	public boolean isPrivate(){
		boolean result = false;
		for(NdExtendedModifier m : modifierList){
			if (m.isModifier()){
				if (((NdModifier)m).isPrivate()) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * デフォルトスコープか否かを応答する
	 * @return
	 */
	public boolean isDefault(){
		boolean result = true;
		for(NdExtendedModifier m : modifierList){
			if (m.isModifier()){
				NdModifier modifier = (NdModifier)m;
				if (modifier.isPublic()) {
					result = false;
					break;
				}
				if (modifier.isProtected()) {
					result = false;
					break;
				}
				if (modifier.isPrivate()) {
					result = false;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * abstractか否かを応答する
	 * @return
	 */
	public boolean isAbstract(){
		boolean result = false;
		for(NdExtendedModifier m : modifierList){
			if (m.isModifier()){
				if (((NdModifier)m).isAbstract()) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Javadocを応答する
	 * @return
	 */
	public NdJavadoc getJavadoc() {
		return javadoc;
	}
	
	/**
	 * Javadocを設定する
	 * @param javadoc
	 */
	public void setJavadoc(NdJavadoc javadoc) {
		this.javadoc = javadoc;
	}
	
	/**
	 * Javadoc部の行数を応答する
	 * @return
	 */
	public int getJavadocLineCount(){
		int result = 0;
		if (this.javadoc != null){
			result = this.javadoc.getLineCount();
		}
		return result;
	}
	
	/**
	 * ステートメント行数を応答する
	 * Javadoc部を含まない。
	 * @return
	 */
	public int getLineCountOfStatement() {
		return this.lineCountOfStatement;
	}
	
	/**
	 * ステートメント行数を設定する
	 * Javadoc部を含まない。
	 * @param lineCountOfStatement
	 */
	public void setLineCountOfStatement(int lineCountOfStatement) {
		this.lineCountOfStatement = lineCountOfStatement;
	}

	/**
	 * 先頭行を応答する
	 * @return
	 */
	public String getHeadLine() {
		return headerLine;
	}
	
	/**
	 * 先頭行を設定する
	 * @param header
	 */
	public void setHeaderLine(String header) {
		this.headerLine = header;
	}

	/**
	 * コンストラクタ
	 */
	public NdCoreElement(){
		super();
	}
	
	/**
	 * 主要情報の文字列表現を応答する
	 */
	protected String getTitle(){
		return "未実装";
	}
	
	/**
	 * デバッグ用
	 * @param tab
	 */
	protected void debugPrintCore(String tab){
		if (!NdLogger.getInstance().getDebugLogging()) return;
		NdLogger.getInstance().debug(tab + this.getName() + "のLineCountOfStatement=" + this.getLineCountOfStatement());
		NdLogger.getInstance().debug(tab + this.getName() + "の先頭行=" + this.getHeadLine());
		if (this.javadoc != null){
			NdLogger.getInstance().debug(tab + this.getName() + "のjavadoc lineCount=" + this.javadoc.getLineCount());
		} else {
			NdLogger.getInstance().debug(tab + this.getName() + "のjavadoc lineCount=なし");
		}
	}
}
