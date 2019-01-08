/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.service.ddb;

/**
 * サービス層で発生するチェック例外
 * begin,commit,rollback,startService,endServiceで発生する
 * @author murayama
 *
 */
public class DdPersistenceOperationException extends DdBaseServiceException {
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * @param msg
	 * @param thrower
	 */
	public DdPersistenceOperationException(String msg, Object thrower){
		super(msg, thrower);
	}
}
