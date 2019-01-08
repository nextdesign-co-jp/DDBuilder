/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.service.ddb;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.persistence.EntityTransaction;
import javax.persistence.OptimisticLockException;
import javax.persistence.RollbackException;

import jp.co.nextdesign.DdBaseException;
import jp.co.nextdesign.DdRuntimeException;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * すべてのサービスの基底クラス
 * ThreadLocalにDdEntityManagerを持つ
 * このクラスではjpa EntityManagerを直接は使用しない、DdEntityManagerを使用する
 * @author murayama
 *
 */
public abstract class DdBaseService {
	private static Logger logger = LoggerFactory.getLogger(DdBaseService.class);

	/*
	 * ----------------------------------------------------------------------
	 * EntityManager#getTransaction()は、closeするまでは同じTransactionインスタンスを応答する。
	 * なので、インスタンス属性のtransactionは不要である。（必要な時に毎回getTransaction()すれば良いので）
	 * しかし、StackOverflowにEclipseLinkの場合のバグとして異なるインスタンスを応答する、という書き込みもあったので、
	 * 一応、インスタンス属性のtransactionを保留する。
	 * ----------------------------------------------------------------------
	 * ここで、複数のトランザクションやネストしたトランザクションはどう実現するのかを調べたので、その時の情報を書き残す。
	 * エンティティマネージャのタイプとトランザクションマネージャの組み合わせには 
	 * (1)コンテナ管理--------JTA
	 * (2)アプリケーション管理---JTA 
	 * (3)アプリケーション管理---リソースローカル 
	 * がある。 JavaSE環境の場合は、(3)に限られる。 
	 * JTA か リソースローカル かは、persistence.xmlで指定する。 
	 * （例）<persistence-unit name="nextdesign_pu" transaction-type="RESOURCE_LOCAL">
	 * Quickでは複数/ネストのトランザクションは考慮しない。 
	 * というよりも、どう実現するのか難しい。
	 * 複数/ネストのトランザクションはどう実現するのか？に関して簡単なテストで試したところ、
	 * entityManager#getTransaction()は同じインスタンスを応答するが、
	 * EntityManagerFactory#createEntityManagerは実行する毎に新しいインスタンスが生成され応答される。
	 * なので、複数のEntityManagerと各EntityManagerに１つのEntityTransactionで管理するのかもしれない。
	 * ----------------------------------------------------------------------
	 * データベースファイルはどこが使われるか 
	 * (1) Jettyで起動するとproject-root/jp_co_nextdesign_data 
	 * (2) Eclipse WTP/Tomcatの場合、eclipseワークスペース/jp_co_nextdesign_data
	 * (3) war & Tomcat 配備の場合、tomcat-root/bin/jp_co_nextdesign_data
	 * -----------------------------------------------------------------------
	 */
	//JPAトランザクション
	private EntityTransaction tx;
	
	/** サービスの初期化処理  */
	protected void startService() {
		DdThreadContext.init();
	}

	/** サービスの終了処理 */
	protected void endService() {
		try{
			DdThreadContext.getEntityManager().close();
			DdThreadContext.remove();
		} catch(Exception e) {
			throwRuntimeException(this.getClass().getSimpleName() + "#endService", e);
		}
	}

	/** トランザクション開始 */
	protected void begin() throws DdPersistenceOperationException{
		this.tx = DdThreadContext.getEntityManager().getTransaction();
		if (tx != null){
			tx.begin();
		} else {
			throw new DdPersistenceOperationException(this.getClass().getSimpleName() + "#begin: tx is NULL.", this);
		}
	}

	/** トランザクションコミット */
	protected void commit() throws DdPersistenceOperationException{
		if (tx != null){
			DdThreadContext.getEntityManager().flush();
			DdThreadContext.getEntityManager().clear();
			tx.commit();
		} else {
			throw new DdPersistenceOperationException(this.getClass().getSimpleName() + "#commit: tx is NULL.", this);
		}
	}

	/** 
	 * トランザクションロールバック 
	 * try-catch の catch の中で使用するので、
	 * catch の中でさらに try-catch しないで済むように、
	 * このメソッドはDdRuntimeExceptionを送出するようにした。 
	 */
	protected void rollback() {
		if (tx != null){
			try{
				tx.rollback();
			} catch(Exception e){
				logger.info("rollback例外:" + e.getClass().getName());
			}
		} else {
			throwRuntimeException(this.getClass().getSimpleName() + "#tx is null.", null);
		}
	}

	/** 非チェック例外を送出する */
	protected void throwRuntimeException(String msg, Exception e) {
		msg = "\n" + msg;
		if (e != null){
			if (e instanceof DdBaseException) {
				msg += "\n[アプリケーション例外]" + ((DdBaseException)e).getApplMessage();
			} else {
				msg += "\n[例外情報]";
			}
			msg += this.getStackTraceOf(e);
		}
		logger.error(msg);
		e.printStackTrace();
		throw new DdRuntimeException(msg);
	}
	
	/**
	 * Exception#printStackTrace文字列を取得する
	 * @param ex
	 * @return
	 */
	private String getStackTraceOf(Exception ex) {
		String result = "";
		if (ex != null) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			ex.printStackTrace(printWriter);
			result += stringWriter.toString();
		}
		return result;
	}
	
	/**
	 * 制約違反の例外に起因するものか否か
	 * @param e
	 * @return
	 */
	protected boolean isConstraintViolationException(Throwable e){
		boolean result = false;
		if (e != null){
			if (e instanceof ConstraintViolationException){
				result = true;
			} else {
				result = isConstraintViolationException(e.getCause());
			}
		}
		return result;
	}
	
	/**
	 * サブクラスから使用する共通例外処理
	 * ログ出力する。
	 * rollback()はしない。
	 * @throws DdBaseServiceException 
	 */
	protected void throwBaseServiceException(String msg, Exception ex) throws DdBaseServiceException{
		if (ex != null){
			ex.printStackTrace();
			logger.error(msg + "\n" + this.getStackTraceOf(ex));
			if (ex instanceof RollbackException) {
				if (this.isConstraintViolationException(ex)){
					throw new DdConstraintViolationException(msg, ex);
				} else {
					throw new DdServiceOtherException(msg, ex);
				}
			} if (ex instanceof OptimisticLockException){
				throw new DdOptimisticLockException(msg, ex);
			} else {
				throw new DdServiceOtherException(msg, ex);
			}
		}
	}
}
