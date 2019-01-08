/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign;

/**
 * チェックされない例外
 * サービス層が送出する。
 * Wicketのデフォルトエラーページが表示される。
 * @author murayama
 *
 */
public class DdRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 * @param messageOnWicketErrorPage
	 */
	public DdRuntimeException(String messageOnWicketErrorPage){
		super(messageOnWicketErrorPage);
	}
}
