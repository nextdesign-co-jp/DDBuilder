/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign;

/**
 * チェックされる例外
 * ドメインモデル層、永続化層のオブジェクトが送出する。
 * すべてサービス層でキャッチされ、チェックされない例外DdRuntimeExceptionに置き換えられて再送出される。
 * @author murayama
 *
 */
public abstract class DdBaseException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private String applMessage;
	
	/**
	 * コンストラクタ
	 * @param msg　thrower情報で送出元クラスはわかるので、簡単なメッセージでよい。メソッド名はあってもなくてもよい。
	 * @param thrower
	 */
	public DdBaseException(String msg, Object thrower){
		super();
		if (thrower != null){
			applMessage = this.getClass().getSimpleName();
			applMessage += "\n[送出者]" + thrower.getClass().getSimpleName();
			applMessage += "\n[メッセージ]" + msg;
		}else{
			applMessage = this.getClass().getSimpleName();
			applMessage += "\n[送出者]thrower is null";
			applMessage += "\n[メッセージ]" + msg;
		}
	}
	
	@Override
	public String toString() {
		return  applMessage;
	}

	public String getApplMessage() {
		return applMessage;
	}

	public void setApplMessage(String applMessage) {
		this.applMessage = applMessage;
	}	
}
