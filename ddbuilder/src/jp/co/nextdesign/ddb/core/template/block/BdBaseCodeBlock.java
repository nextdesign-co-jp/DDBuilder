/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.template.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.nextdesign.ddb.core.template.BdCodeLine;
import jp.co.nextdesign.ddb.core.template.cases.BdBaseCodeCase;
import jp.co.nextdesign.ddb.core.user.BdUserAttribute;
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethod;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodParameter;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodResult;
import jp.co.nextdesign.jcr.NdConstants;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * テンプレート中の BLOCKSTART～ENDBLOCKを現す
 * CodeCaseを1つ以上もつ。コード行は直接には持たない。
 * @author murayama
 *
 */
public class BdBaseCodeBlock {
	
	protected Map<String, BdBaseCodeCase> codeCaseMap;
	private String key = "";
	private int startIndex = -1;
	private int endIndex = -1;
	
	protected final String KEY_Nop = "nop";
	
	/**
	 * コンストラクタ
	 * @param lineList
	 */
	public BdBaseCodeBlock(List<BdCodeLine> lineList){
		super();
		if (lineList != null && lineList.size() > 0){
			lineList.get(0).setCodeBlock(this); //参照設定
			this.key = lineList.get(0).getKey();
			this.startIndex = lineList.get(0).getLineIndex();
			this.endIndex = lineList.get(lineList.size()-1).getLineIndex();
		}
		this.codeCaseMap = new HashMap<String, BdBaseCodeCase>();
	}
	
	/**
	 * ソースを生成する(サブクラスで実装する)
	 * @param c
	 * @return
	 */
	public List<String> generate(BdUserClass c) {
		NdLogger.getInstance().error("ERROR-LINE " + this.getClass().getSimpleName() + " : generate(c)IS NOT IMPLEMENTED.", null);
		return new ArrayList<String>();
	}
	
	/**
	 * ソースを生成する(サブクラスで実装する)
	 * @param a
	 * @return
	 */
	public List<String> generate(BdUserAttribute a) {
		NdLogger.getInstance().error("ERROR-LINE " + this.getClass().getSimpleName() + " : generate(a)IS NOT IMPLEMENTED.", null);
		return new ArrayList<String>();
	}

	/**
	 * ソースを生成する(サブクラスで実装する)
	 * @param cList
	 * @return
	 */
	public List<String> generate(List<BdUserClass> cList) {
		NdLogger.getInstance().error("ERROR-LINE " + this.getClass().getSimpleName() + " : generate(cList)IS NOT IMPLEMENTED.", null);
		return new ArrayList<String>();
	}

	/**
	 * ソースを生成する(サブクラスで実装する)
	 * @param p
	 * @return
	 */
	public List<String> generate(BdUserServiceMethodParameter p) {
		NdLogger.getInstance().error("ERROR-LINE " + this.getClass().getSimpleName() + " : generate(p)IS NOT IMPLEMENTED.", null);
		return new ArrayList<String>();
	}

	/**
	 * ソースを生成する(サブクラスで実装する)
	 * @param m
	 * @return
	 */
	public List<String> generate(BdUserServiceMethod m) {
		NdLogger.getInstance().error("ERROR-LINE " + this.getClass().getSimpleName() + " : generate(m)IS NOT IMPLEMENTED.", null);
		return new ArrayList<String>();
	}
	
	/**
	 * ソースを生成する(サブクラスで実装する)
	 * @param cList
	 * @return
	 */
	public List<String> generate4Service(List<BdUserServiceMethod> mList) {
		NdLogger.getInstance().error("ERROR-LINE " + this.getClass().getSimpleName() + " : generate4Service(mList)IS NOT IMPLEMENTED.", null);
		return new ArrayList<String>();
	}

	/**
	 * ソースを生成する(サブクラスで実装する)
	 * @param r
	 * @return
	 */
	public List<String> generate(BdUserServiceMethodResult r) {
		NdLogger.getInstance().error("ERROR-LINE " + this.getClass().getSimpleName() + " : generate(r)IS NOT IMPLEMENTED.", null);
		return new ArrayList<String>();
	}

	/** Caseリストを作成する */
	public void buildCodeCaseList(List<BdCodeLine> lineListOfBlock){
		this.codeCaseMap = new HashMap<String, BdBaseCodeCase>();
		List<BdCodeLine> lineListOfCase = new ArrayList<BdCodeLine>();
		String latestKey = "";
		for(BdCodeLine line : lineListOfBlock){
			if(line.isCaseLine()){
				if (lineListOfCase.size() > 0){
					BdBaseCodeCase codeCase = this.createCodeCase(latestKey, lineListOfCase);
					if (codeCase != null){
						this.addCodecase(latestKey, codeCase);
					}
					lineListOfCase.clear();
				}
				latestKey = line.getKey();
			} if (! line.isDdbMarkLine())
				lineListOfCase.add(line);
		}
		if (lineListOfCase.size() > 0){
			BdBaseCodeCase codeCase = this.createCodeCase(latestKey, lineListOfCase);
			if (codeCase != null){
				this.addCodecase(latestKey, codeCase);
			}
		}
	}
	
	/** CodeCaseを追加する */
	private void addCodecase(String key, BdBaseCodeCase codeCase){
		try{
			if (!this.codeCaseMap.containsKey(key)){
				this.codeCaseMap.put(key, codeCase);
			}
		}catch(Exception ex){
		}
	}

	/** 文字keyからCodeCaseを応答する */
	protected BdBaseCodeCase getCodeCaseByKey(String caseKey){
		BdBaseCodeCase result = null;
		if (codeCaseMap.containsKey(caseKey)){
			result = codeCaseMap.get(caseKey);
		}
		return result;
	}
	
	/** デバッグ用 */
	public Map<String, BdBaseCodeCase> getCodeCaseMap(){
		return this.codeCaseMap;
	}

	/** 各サブクラスでOverrideする */
	protected BdBaseCodeCase createCodeCase(String key, List<BdCodeLine> lineListOfCase){
		return null;
	}
	
	private String getKey() {
		return key;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	/**
	 * デバッグ
	 * @param tab
	 */
	public void debugPrint(String tab) {
			String line = tab + this.getClass().getSimpleName() + " key=" + this.getKey() + " [" + this.getStartIndex() + "-" + this.getEndIndex() + "]";
			NdLogger.getInstance().debug(line);
			for(BdBaseCodeCase codeCase : this.codeCaseMap.values()){
				codeCase.debugPrint(tab + NdConstants.DEBUG_TAB);
			}
	}
}
