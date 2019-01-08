package jp.co.nextdesign.jcr;

/**
 * プロパティ関係の例外
 * @author murayama
 */
public class NdApplicationPropertyException extends NdBaseException {
	
	private static final long serialVersionUID = 1L;

	/** コンストラクタ */
	public NdApplicationPropertyException(String applMessage, Exception origin){
		super(applMessage, origin);
	}
	
	/** コンストラクタ */
	public NdApplicationPropertyException(String applMessage){
		super(applMessage);
	}

}
