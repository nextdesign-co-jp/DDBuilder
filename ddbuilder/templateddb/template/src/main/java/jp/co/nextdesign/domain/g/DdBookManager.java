/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.domain.g;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

//-- BLOCKSTART Import
//-- CASE Manager
import jp.co.nextdesign.domain.Book;
//-- BLOCKEND Import

import jp.co.nextdesign.domain.ddb.DdBaseManager;
import jp.co.nextdesign.domain.ddb.DdSortParam;

/**
 * ドメインクラスマネージャ テンプレート
 * @author murayama
 *
 */
public class DdBookManager extends DdBaseManager {

	/**
	 *  コンストラクタ
	 */
	public DdBookManager() {
	}

	/**
	 * 主キーで検索取得する
	 * @param id
	 * @return
	 */
	public Book getById(Long id){
		Book result = this.getEntityManager().find(Book.class, id);
		return result;
	}

	/**
	 * 永続化する
	 * @param book
	 */
	public void persist(Book book){
		if (book != null){
			getEntityManager().persist(book);
		}
	}

	/**
	 * 削除する
	 * @param book
	 */
	public boolean delete(Book book){
		boolean result = false;
		if (book != null){
			this.getEntityManager().remove(book);
			result = true;
		}
		return result;
	}

	/**
	 * mergeする
	 * @param book
	 * @return
	 */
	public Book merge(Book book){
		if (book != null){
			return this.getEntityManager().merge(book);
		}else{
			return null;
		}
	}

	/**
	 * 全件のリストを応答する
	 * @return
	 */
	public List<Book> getList(){
		return this.getList(0, 0);
	}

	/**
	 * 指定された行位置から最大pageSize分のリストを応答する
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List<Book> getList(long offset, long pageSize){
		return getList(offset, pageSize, null);
	}

	/**
	 * 指定された行位置から最大pageSize分のリストを応答する
	 * @param offset
	 * @param pageSize
	 * @param ddSortParam
	 * @return
	 */
	public List<Book> getList(long offset, long pageSize, DdSortParam ddSortParam){
		List<Book> resultList = new ArrayList<Book>();
		EntityManager em = this.getEntityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Book> query = criteriaBuilder.createQuery(Book.class);
		Root<Book> root = query.from(Book.class);
		CriteriaQuery<Book> select = query.select(root);
		if (ddSortParam != null) {
			if (ddSortParam.isAsc()){
				query.orderBy(criteriaBuilder.asc(root.get(ddSortParam.getAttributeName())));
			} else {
				query.orderBy(criteriaBuilder.desc(root.get(ddSortParam.getAttributeName())));
			}
		}
		TypedQuery<Book> typedQuery = em.createQuery(select);
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
		countQuery.select(criteriaBuilder.count(countQuery.from(Book.class)));
		return em.createQuery(countQuery).getSingleResult();
	}
}
