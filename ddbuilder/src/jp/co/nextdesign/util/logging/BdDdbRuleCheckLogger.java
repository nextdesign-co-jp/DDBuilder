/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.util.logging;

import java.util.ArrayList;
import java.util.List;

/**
 * ログ
 * ドメインとサービスに関するDDBuilderルールのチェックログ
 * BdBuilderTaskが使用する
 * @author murayama
 */
public class BdDdbRuleCheckLogger {

	private static BdDdbRuleCheckLogger instance = null;
	private List<String> logList;
	
	/** ログリストを応答する */
	public List<String> getLogList(){
		return this.logList;
	}
	
	/** ログを保持しているか否か */
	public boolean hasLog(){
		return ! this.logList.isEmpty();
	}
	
	/** 1件追加する */
	public void add(String log){
		this.logList.add(log);
	}

	/** 1件追加する（重複なし） */
	public void addWithoutDuplication(String log){
		if (! this.logList.contains(log)){
			this.logList.add(log);
		}
	}

	/** 全件クリアする */
	public void clear(){
		this.logList.clear();
	}
	
	/** シングルトン */
	public static synchronized BdDdbRuleCheckLogger getInstance(){
		if (instance == null){
			instance = new BdDdbRuleCheckLogger();
		}
		return instance;
	}
	
	/** コンストラクタ */
	private BdDdbRuleCheckLogger(){
		super();
		this.logList = new ArrayList<String>();
	}
}
