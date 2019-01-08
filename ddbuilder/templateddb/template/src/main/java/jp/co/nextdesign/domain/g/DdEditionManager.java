/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.domain.g;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import jp.co.nextdesign.domain.Edition;
import jp.co.nextdesign.domain.ddb.DdBaseManager;

public class DdEditionManager extends DdBaseManager {

	/**
	 *  コンストラクタ
	 */
	public DdEditionManager() {
	}

	/**
	 * 主キーで検索取得する
	 * @param id
	 * @return
	 */
	public Edition getById(Long id){
		Edition result = this.getEntityManager().find(Edition.class, id);
		return result;
	}
	
	/**
	 * 永続化する
	 * @param edition
	 */
	public void persist(Edition edition){
		if (edition != null){
			getEntityManager().persist(edition);
		}
	}
	
	public List<Edition> getList(){
		EntityManager em = this.getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Edition> query = cb.createQuery(Edition.class);
		Root<Edition> root = query.from(Edition.class);
		//query.where(cb.equal(root.get("name"), ""));
		List<Edition> resultList = em
				.createQuery(query)
				.getResultList();
		return resultList;
	}
}
