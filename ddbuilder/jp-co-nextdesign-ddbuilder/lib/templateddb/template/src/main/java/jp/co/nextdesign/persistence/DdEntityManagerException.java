/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.persistence;

import jp.co.nextdesign.DdBaseException;

/**
 * 永続化層javax.persistence.EntityManager関係で発生したチェック例外
 * @author murayama
 *
 */
public class DdEntityManagerException extends DdBaseException {
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * @param msg
	 * @param thrower
	 */
	public DdEntityManagerException(String msg, Object thrower){
		super(msg, thrower);
	}
}
