/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.statement;

/**
 * ForStatement
 * @author murayama
 */
public class NdForStatement extends NdAbstractStatement {

	/**
	 * コンストラクタ
	 */
	protected NdForStatement(){
		super();
	}

	/**
	 * 制御文か否かを応答する
	 * @return
	 */
	@Override public boolean isControlStatement() {
		return true;
	}
}
