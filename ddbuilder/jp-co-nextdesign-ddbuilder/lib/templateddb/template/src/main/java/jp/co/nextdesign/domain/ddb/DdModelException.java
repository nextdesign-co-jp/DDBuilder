/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.domain.ddb;

import jp.co.nextdesign.DdBaseException;

/**
 * ドメインモデル層で使用するチェックされる例外
 * @author murayama
 *
 */
public class DdModelException extends DdBaseException {
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * @param msg
	 * @param thrower
	 */
	public DdModelException(String msg, Object thrower){
		super(msg, thrower);
	}
}
