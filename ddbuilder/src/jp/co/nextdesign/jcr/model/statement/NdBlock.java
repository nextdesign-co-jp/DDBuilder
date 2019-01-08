/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.statement;

/**
 * Block
 * @author murayama
 */
public class NdBlock extends NdAbstractStatement {

	/**
	 * コンストラクタ
	 */
	protected NdBlock(){
		super();
	}

	/**
	 * このステートメント１つを何行と数えるかを応答する
	 * @return
	 */
	@Override public int getLineCount(){
		return 0;
	}
	
	/**
	 * このステートメントをネストとしてカウントするか否かを応答する
	 * @return
	 */
	@Override public boolean isNestLevelCountable(){
		return false;
	}

}
