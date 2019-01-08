/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.service.ddb;

import jp.co.nextdesign.DdBaseException;

/**
 * OptimisticLockException
 * ConstraintViolationException
 * 現時点で上記2つ以外に発生した例外をこの例外クラスでさ送出する。
 * @author murayama
 *
 */
public class DdServiceOtherException extends DdBaseServiceException {
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * @param msg
	 * @param thrower
	 */
	public DdServiceOtherException(String msg, Object thrower){
		super(msg, thrower);
	}
}