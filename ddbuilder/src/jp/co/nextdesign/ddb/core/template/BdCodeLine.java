/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.template;

import java.io.File;

import jp.co.nextdesign.ddb.core.template.block.BdBaseCodeBlock;
import jp.co.nextdesign.util.logging.NdLogger;

public class BdCodeLine {
	
	private static final String JAVA_BLOCK_START = "//-- BLOCKSTART";
	private static final String JAVA_BLOCK_END = "//-- BLOCKEND";
	private static final String JAVA_CASE = "//-- CASE";
	private static final String HTML_BLOCK_START = "<!-- BLOCKSTART";
	private static final String HTML_BLOCK_END = "<!-- BLOCKEND";
	private static final String HTML_CASE = "<!-- CASE";
	private static final String HTML_COMMENT_END = "-->";

	private String line;
	private String trimedLine;
	private int lineIndex; //テンプレートソースファイル内での行インデックス
	private BdBaseCodeBlock codeBlock; //単方向参照
	private File plainFile;
	
	public BdCodeLine(String line, int lineIndex, File plainFile){
		super();
		this.line = line;
		this.trimedLine = this.line.trim();
		this.lineIndex = lineIndex;
		this.plainFile = plainFile;
	}
	
	public boolean hasCodeBlock(){
		return this.codeBlock != null;
	}
	
	public BdBaseCodeBlock getCodeBlock(){
		return this.codeBlock;
	}
	
	public void setCodeBlock(BdBaseCodeBlock codeBlock){
		this.codeBlock = codeBlock;
	}
	
	public String getKey(){
		String result = "";
		if (this.isDdbMarkLine()){
			result = this.trimedLine.replace(JAVA_BLOCK_END, "");
			result = result.replace(HTML_BLOCK_END, "");
			result = result.replace(JAVA_CASE, "");
			result = result.replace(HTML_CASE, "");
			result = result.replace(HTML_COMMENT_END,  "");
		}
		result = result.trim();
		if ("".equals(result)){
			NdLogger.getInstance().error(this.plainFile.getName() + "[" + (this.lineIndex + 1) +"] : 不明なKey : " + this.trimedLine,  null);
		}
		return result;
	}
	
	
	public boolean isDdbMarkLine() {
		return this.isBlockStartLine()
				|| this.isBlockEndLine()
				|| this.isCaseLine();
	}

	/**
	 * DDBSか否か
	 * @return
	 */
	public boolean isBlockStartLine(){
		return this.trimedLine.startsWith(JAVA_BLOCK_START)  || this.trimedLine.startsWith(HTML_BLOCK_START) ;
 	}

	/**
	 * DDBEか否か
	 * @return
	 */
	public boolean isBlockEndLine(){
		return  this.trimedLine.startsWith(JAVA_BLOCK_END)  || this.trimedLine.startsWith(HTML_BLOCK_END) ;
	}
	
	/**
	 * CASEか否か
	 * @return
	 */
	public boolean isCaseLine(){
		return  this.trimedLine.startsWith(JAVA_CASE)  || this.trimedLine.startsWith(HTML_CASE) ;
 	}
	
	public String getLine(){
		return this.line;
	}

	public int getLineIndex() {
		return lineIndex;
	}

	public void setLineIndex(int lineIndex) {
		this.lineIndex = lineIndex;
	}

	/**
	 * デバッグ
	 * @param tab
	 */
	public void debugPrint(String tab){
		String line = tab + this.line;
		NdLogger.getInstance().debug(line);
	}	
}
