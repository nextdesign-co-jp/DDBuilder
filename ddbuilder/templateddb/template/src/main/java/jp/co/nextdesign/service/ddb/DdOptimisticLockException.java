/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.service.ddb;

import jp.co.nextdesign.DdBaseException;

/**
 * サービス層で送出するチェック例外 javax.persistence.OptimisticLockException
 * @author murayama
 *
 */
public class DdOptimisticLockException extends DdBaseServiceException {
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * @param msg
	 * @param thrower
	 */
	public DdOptimisticLockException(String msg, Object thrower){
		super(msg, thrower);
	}
}
