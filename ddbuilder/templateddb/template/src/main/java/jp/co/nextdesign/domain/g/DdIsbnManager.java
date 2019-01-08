package jp.co.nextdesign.domain.g;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import jp.co.nextdesign.domain.Isbn;
import jp.co.nextdesign.domain.ddb.DdBaseManager;

public class DdIsbnManager extends DdBaseManager {

	/**
	 *  コンストラクタ
	 */
	public DdIsbnManager() {
	}

	/**
	 * 主キーで検索取得する
	 * @param id
	 * @return
	 */
	public Isbn getById(Long id){
		Isbn result = this.getEntityManager().find(Isbn.class, id);
		return result;
	}
	
	/**
	 * 永続化する
	 * @param isbn
	 */
	public void persist(Isbn isbn){
		if (isbn != null){
			getEntityManager().persist(isbn);
		}
	}
	
	/**
	 * 削除する
	 * @param isbn
	 */
	public boolean delete(Isbn isbn){
		boolean result = false;
		if (isbn != null){
			this.getEntityManager().remove(isbn);
			result = true;
		}
		return result;
	}
	
	/**
	 * mergeする
	 * @param isbn
	 * @return
	 */
	public Isbn merge(Isbn isbn){
		if (isbn != null){
			return this.getEntityManager().merge(isbn);
		}else{
			return null;
		}
	}

	/**
	 * 全件のリストを応答する
	 * @return
	 */
	public List<Isbn> getList(){
		return this.getList(0, 0);
	}

	/**
	 * 指定された行位置から最大pageSize分のリストを応答する
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List<Isbn> getList(long offset, long pageSize){
		List<Isbn> resultList = new ArrayList<Isbn>();
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Isbn> query = criteriaBuilder.createQuery(Isbn.class);
		Root<Isbn> root = query.from(Isbn.class);
		CriteriaQuery<Isbn> select = query.select(root);
		TypedQuery<Isbn> typedQuery = em.createQuery(select);
		if (pageSize > 0){
			typedQuery.setFirstResult((new Long(offset)).intValue());
			typedQuery.setMaxResults((new Long(pageSize)).intValue());
		}
		resultList = typedQuery.getResultList();
		return resultList;
	}
	
	/**
	 * 件数を応答する
	 * @return
	 */
	public Long getCount(){
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		countQuery.select(criteriaBuilder.count(countQuery.from(Isbn.class)));
		return em.createQuery(countQuery).getSingleResult();
	}
}
