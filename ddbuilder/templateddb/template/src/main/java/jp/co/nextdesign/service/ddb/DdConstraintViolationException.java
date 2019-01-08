/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.service.ddb;

/**
 * サービス層で送出するチェック例外 javax.persistence.ConstraintViolationException
 * @author murayama
 *
 */
public class DdConstraintViolationException extends DdBaseServiceException {
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * @param msg
	 * @param thrower
	 */
	public DdConstraintViolationException(String msg, Object thrower){
		super(msg, thrower);
	}
}
