/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

import java.util.List;

import org.eclipse.jdt.core.dom.Javadoc;

/**
 * Javadoc
 * @author murayama
 */
public class NdJavadoc {

	private int lineCount;
	private String blockString;
	private String firstLine;
	
	/**
	 * 文字表現を応答する
	 * @return
	 */
	public String getBlockString() {
		return blockString;
	}

	/**
	 * 行数を応答する
	 * @return
	 */
	public int getLineCount() {
		return lineCount;
	}
	
	/**
	 * 最初の１行を応答する
	 * @return
	 */
	public String getFirstLine(){
		return this.firstLine;
	}

	/**
	 * コンストラクタ
	 */
	public NdJavadoc(Javadoc jdtJavadoc){
		super();
		if (jdtJavadoc != null){
			this.blockString = jdtJavadoc.toString();
			if (this.blockString != null){
				this.lineCount = NdJavaCode.getLineListOf(blockString).size();
				this.firstLine = "";
				List<?> tagList = jdtJavadoc.tags();
				if ((tagList != null) && (tagList.size() > 0)){
					this.firstLine = tagList.get(0).toString();
					this.firstLine = this.firstLine.replaceAll("\n", "");
				}
			} else {
				this.blockString = "";
				this.lineCount = 0;
				this.firstLine = "";
			}
		}
	}
}
