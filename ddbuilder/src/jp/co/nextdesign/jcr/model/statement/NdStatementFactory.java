/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.statement;

import jp.co.nextdesign.util.logging.NdLogger;

import org.eclipse.jdt.core.dom.Statement;

/**
 * NdAbstractStatemntのサブクラスのインスタンスのファクトリ
 * @author murayama
 */
public class NdStatementFactory {

	/**
	 * JDT StatementからNdStatementインスタンスを生成する
	 * @param jdtStatement
	 * @return
	 */
	public static NdAbstractStatement create(Statement jdtStatement){
		NdAbstractStatement result = null;
		String targetName = "jp.co.nextdesign.jcr.model.statement.Nd" + jdtStatement.getClass().getSimpleName();
		try{
			Class<?> classObject = Class.forName(targetName);
			result = (NdAbstractStatement)classObject.newInstance();
		} catch (Exception ex) {
			NdLogger.getInstance().error("", ex);
			result = new NdUnknownStatement();
		}
		return result;
	}
}