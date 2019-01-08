/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.service.ddb;

import javax.persistence.EntityManager;

import jp.co.nextdesign.DdRuntimeException;
import jp.co.nextdesign.persistence.DdEntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * スレッド別のコンテキスト
 * EntityManagerをスレッドローカルに保存、取得、削除する責務を持つ
 * EntityManagerの振る舞い（getTransactionなど）を代理で
 * サービスが生成し削除する
 * 各メソッドは、static synchronized とすべきと考えた。2014.4.7
 * 
 * 次のように、１つのリクエストの間は同じEntityManagerが使用される。
 * XxxEditPageのonSubmitでは、そのページが最初のEditPageである場合のみXxxService#merge(o)等とする。
 * 最初のEditPageとは、XxxEditPage → YyyEditPageのように遷移するときのXxxEditPageのことを指す。
 * 最初のEditPageではない場合には、XxxService#merge(o)は実行されないので、XxxService#endService()も実行されない。
 * 従って、DdThreadLocal#remove()も実行されないので、一連の編集操作中は、XxxEditPageが終了するまで同じEntityManagerを使用できる。
 * @author murayama
 *
 */
public class DdThreadContext {
	private static Logger logger = LoggerFactory.getLogger(DdThreadContext.class);
	
	//JPAエンティティマネージャ格納用スレッドローカル
	//ThreadLocalはstaticでなければならない。
	//インスタンス属性ではないし、静的&スレッド毎という捉え方をするのだろう。
	private static ThreadLocal<EntityManager> threadLocal;

	/** スレッドローカル変数を初期化する */
	public static synchronized void init(){
		threadLocal = new ThreadLocal<EntityManager>(){
			protected EntityManager initialValue(){
				return null;
			}
		};
		try {
			threadLocal.set(DdEntityManagerFactory.getInstance().getEntityManager());
		} catch(Exception e) {
			threadLocal.set(null);
			throwRuntimeExcption("DdThreadContext#init: em is NULL.");
		}
	}

	
	/** スレッドローカル変数のEntityManagerを応答する */
	public static synchronized EntityManager getEntityManager(){
		EntityManager result = threadLocal.get();
		if (result == null){
			throwRuntimeExcption("DdThreadContext#getEntityManager: em is NULL.");
		}
		return result;
	}
	
	/** スレッドローカル変数を削除する */
	public static synchronized void remove(){
		if (threadLocal != null){
			threadLocal.remove();
		} else {
			throwRuntimeExcption("DdThreadContext#remove: threadLocal is NULL.");
		}
	}
	
	/** このクラスのメソッドはRuntimeExceptionを送出する */
	private static void throwRuntimeExcption(String msg){
		logger.error(msg);
		throw new DdRuntimeException(msg);			
	}
}
