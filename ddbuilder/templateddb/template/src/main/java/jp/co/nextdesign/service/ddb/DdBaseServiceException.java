/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.service.ddb;

import jp.co.nextdesign.DdBaseException;

/**
 * サービス層で送出するすべての例外の基底クラス
 * @author murayama
 *
 */
public abstract class DdBaseServiceException extends DdBaseException {
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * @param msg
	 * @param thrower
	 */
	public DdBaseServiceException(String msg, Object thrower){
		super(msg, thrower);
	}
}