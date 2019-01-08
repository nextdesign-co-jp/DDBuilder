/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.domain.g;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import jp.co.nextdesign.domain.Author;
import jp.co.nextdesign.domain.ddb.DdBaseManager;

public class DdAuthorManager extends DdBaseManager {

	/**
	 *  コンストラクタ
	 */
	public DdAuthorManager() {
	}

	/**
	 * 主キーで検索取得する
	 * @param id
	 * @return
	 */
	public Author getById(Long id){
		Author result = this.getEntityManager().find(Author.class, id);
		return result;
	}
	
	/**
	 * 永続化する
	 * @param author
	 */
	public void persist(Author author){
		if (author != null){
			getEntityManager().persist(author);
		}
	}
	
	public List<Author> getList(){
		EntityManager em = this.getEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Author> query = cb.createQuery(Author.class);
		Root<Author> root = query.from(Author.class);
		//query.where(cb.equal(root.get("name"), ""));
		List<Author> resultList = em
				.createQuery(query)
				.getResultList();
		return resultList;
	}
}
