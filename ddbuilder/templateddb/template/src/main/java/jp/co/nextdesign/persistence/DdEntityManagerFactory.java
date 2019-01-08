/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

/**
 * EntityManagerFactoryをラップしたシングルトンオブジェクト
 * 最初はDdPersistnceと命名したが、
 * javax.persistence.Persistenceは、EntityManagerFactoryを生成・応答する責務を持つのに対し、
 * このクラスはjavax.persistence.EntityManagerFactoryを1つだけ保持し、
 * EntityManagerFactoryのライフサイクルを管理することが責務である。
 * 従って、DdPersistenceよりもDdEntityManagerFactoryが適切な名前とした。
 * Persistence is
 * Bootstrap class that is used to obtain an EntityManagerFactory in Java SE environments.
 * ----------------------------
 * EntityManagerFactory --- スレッドセーフ。生成コスト大。
 * EntityManager --- スレッドセーフではない。生成コスト小。
 * ======================================================== ここから
 * ここからは、DdEntityManagerクラスにつけていたコメントである。
 * DdEntityManagerはモデルとして冗長、jpa EntityManagerと違いが明白ではなかったので
 * DdEntityManagerは削除した。しかし、コメントは有用だったので、ここに移動・記載した。
 * 
 * javax EntityManagerのラッパークラス DdBaseServiceクラスのThreadLocalインスタンスとして生成される
 * jpa.EntityManagerとjpa.EntityTransactionをカプセル化したオブジェクト
 * ----------------------------------------------------------------------
 * EntityManager#getTransaction()は、closeするまでは同じTransactionインスタンスを応答する。
 * なので、インスタンス属性のtransactionは不要である。（必要な時に毎回getTransaction()すれば良いので）
 * しかし、StackOverflowにEclipseLinkび場合のバグとして異なるインスタンスを応答する、という書き込みもあったので、
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
 * データベースファイルはどこが使われるか (1) Jettyで起動するとproject-root/
 * jp_co_nextdesign_data (2) WTP/Tomcatの場合、eclipse-root/ jp_co_nextdesign_data
 * (3) war & Tomcat 配備の場合、tomcat-root/bin/ jp_co_nextdesign_data
 * -----------------------------------------------------------------------
 * ======================================================== ここまで
 * 
 * @author murayama
 *
 */
public class DdEntityManagerFactory {
	private static final String JPA_PERSISTENCE_UNIT_NAME = "nextdesign_pu"; //META-INF/persistence.xmlと整合すること
	private static DdEntityManagerFactory instance;
	private EntityManagerFactory emf;
	
	/** シングルトン */
	public static synchronized DdEntityManagerFactory getInstance(){
		if (instance == null){
			instance = new DdEntityManagerFactory();
		}
		return instance;
	}
	
	/** コンストラクタ */
	private DdEntityManagerFactory(){
	}
	
	/**
	 * アプリケーション開始時に  EntityManagerFactory (EMF) インスタンスを１つ作成する
	 * EntityManagerFactoryはスレッドセーフ。生成コストは高い。
	 * DdApplicationが使用するので、ここでは例外を送出しない。
	 * @throws Exception 
	 */
	public void open() throws Exception{
		try{
			this.emf = Persistence.createEntityManagerFactory(JPA_PERSISTENCE_UNIT_NAME);
		} catch(PersistenceException e) {
			this.emf = null;
			String msg = e.getCause().getMessage();
			msg += e.toString();
			throw new DdEntityManagerException("open: emf==null " + msg, this);
		} catch(Exception e) {
			this.emf = null;
			throw new DdEntityManagerException("open: emf==null " + e.toString(), this);
		}
	}

	/**
	 * EMインスタンスを生成し応答する
	 * EMFはスレッドセーフであるが、EMはスレッドセーフではない。
	 * @throws DdEntityManagerException
	 */
	public EntityManager getEntityManager() throws DdEntityManagerException{
		if (emf != null){
			return emf.createEntityManager();
		} else {
			throw new DdEntityManagerException("getEntityManagerFactory: emf==null", this);
		}
	}

	/**
	 * アプリケーション終了時にEMFインスタンスを終了させるため
	 * DdApplication#onDestroyが使用する。
	 * DdApplicationが使用するので、ここでは例外を送出しない。 getEntityManagerFactoryで送出する。
	 * @throws DdEntityManagerException 
	 */
	public void close() throws DdEntityManagerException{
		if (emf != null){
			emf.close();
		} else {
			throw new DdEntityManagerException("close: emf==null", this);
		}
	}
}
