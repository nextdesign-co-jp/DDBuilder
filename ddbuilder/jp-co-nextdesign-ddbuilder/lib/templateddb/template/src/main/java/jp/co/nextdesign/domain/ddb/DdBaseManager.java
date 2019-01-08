/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.domain.ddb;

import javax.persistence.EntityManager;

import jp.co.nextdesign.service.ddb.DdThreadContext;

/**
 * すべての管理者クラスの基底クラス
 * @author murayama
 *
 */
public abstract class DdBaseManager {

	/**
	 * スレッドローカルなDdEntityManagerを取得して応答する
	 * もし、スレッドローカルのエンティティマネージャがnullの場合は、
	 * DdThreadContext.getEntityManager()が例外を送出するので、このメソッドでnullチェックは不要。
	 * @return
	 */
	protected EntityManager getEntityManager() {
		return DdThreadContext.getEntityManager();
	}
	
	/**
	 * コンテキストをクリアする
	 */
	public void clear(){
		this.getEntityManager().clear();
	}
}
