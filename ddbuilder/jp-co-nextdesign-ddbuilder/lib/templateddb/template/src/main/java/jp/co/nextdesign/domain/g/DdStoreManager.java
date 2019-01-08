/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.domain.g;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import jp.co.nextdesign.domain.Book;
import jp.co.nextdesign.domain.ddb.DdBaseManager;
import jp.co.nextdesign.domain.store.Store;

public class DdStoreManager extends DdBaseManager {

	/**
	 *  コンストラクタ
	 */
	public DdStoreManager() {
	}

	/**
	 * 主キーで検索取得する
	 * @param id
	 * @return
	 */
	public Store getById(Long id){
		Store result = this.getEntityManager().find(Store.class, id);
		return result;
	}
	
	/**
	 * 永続化する
	 * @param bookStore
	 */
	public void persist(Store bookStore){
		if (bookStore != null){
			getEntityManager().persist(bookStore);
		}
	}

	public Store merge(Store store){
		if (store != null){
			return this.getEntityManager().merge(store);
		}else{
			return null;
		}
	}

	public List<Store> getList(){
		EntityManager em = this.getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Store> query = cb.createQuery(Store.class);
		Root<Store> root = query.from(Store.class);
		//query.where(cb.equal(root.get("name"), ""));
		List<Store> resultList = em
				.createQuery(query)
				.getResultList();
		return resultList;
	}
}
