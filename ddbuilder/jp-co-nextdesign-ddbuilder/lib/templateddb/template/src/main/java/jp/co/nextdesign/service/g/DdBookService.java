/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.service.g;

import java.util.ArrayList;
import java.util.List;

//-- BLOCKSTART Import
//-- CASE Service
import jp.co.nextdesign.domain.Book;
//-- BLOCKEND Import

import jp.co.nextdesign.domain.g.DdBookManager;
import jp.co.nextdesign.service.ddb.DdBaseService;
import jp.co.nextdesign.service.ddb.DdBaseServiceException;
import jp.co.nextdesign.domain.ddb.DdSortParam;

/**
 * サービス
 * @author murayama
 *
 */
public class DdBookService extends DdBaseService {

	/**
	 * idで取得する
	 * @param id
	 * @return
	 */
	public Book getById(long id) throws DdBaseServiceException {
		Book result = null;
		try {
			startService();
			DdBookManager bookManager = new DdBookManager();
			result = bookManager.getById(id);
		} catch (Exception e) {
			throwBaseServiceException(this.getClass().getSimpleName() + "#persist", e);
		} finally {
			endService();
		}
		return result;
	}
	
	/**
	 * 永続化する
	 * @param book
	 */
	public void persist(Book book) throws DdBaseServiceException {
		try {
			startService();
			begin();
			DdBookManager bookManager = new DdBookManager();
			bookManager.persist(book);
			commit();
		} catch (Exception e) {
			rollback();
			throwBaseServiceException(this.getClass().getSimpleName() + "#persist", e);
		} finally {
			endService();
		}
	}

	/**
	 * mergeして永続化する
	 * @param book
	 * @return
	 * @throws DdBaseServiceException 
	 */
	public Book merge(Book book) throws DdBaseServiceException {
		Book result = null;
		try {
			startService();
			begin();
			DdBookManager bookManager = new DdBookManager();
			result = bookManager.merge(book);
			commit();
		} catch (Exception e) {
			rollback();
			throwBaseServiceException(this.getClass().getSimpleName() + "#merge", e);
		} finally {
			endService();
		}
		return result;
	}

	/**
	 * 削除する
	 * @param book
	 * @return
	 * @throws DdBaseServiceException
	 */
	public Boolean remove(Book book) throws DdBaseServiceException {
		Boolean result = false;
		try {
			startService();
			begin();
			DdBookManager bookManager = new DdBookManager();
			Book bookMerged = bookManager.merge(book);
			result = bookManager.delete(bookMerged);
			commit();
		} catch (Exception e) {
			rollback();
			throwBaseServiceException(this.getClass().getSimpleName() + "#remove", e);
		} finally {
			endService();
		}
		return result;
	}
	
	/**
	 * EntityManager#clear()
	 * Clear the persistence context, causing all managed entities to become detached. 
	 */
	public void clear(){
		try {
			startService();
			DdBookManager bookManager = new DdBookManager();
			bookManager.clear();
		} catch (Exception e) {
			this.throwRuntimeException(this.getClass().getSimpleName() + "#clear", e);
		} finally {
			endService();
		}
	}

	/**
	 * 全件数を応答する
	 * @return
	 */
	public Long getSize(){
		Long result = 0L;
		try {
			startService();
			DdBookManager bookManager = new DdBookManager();
			result = bookManager.getCount();
		} catch (Exception e) {
			throwRuntimeException(this.getClass().getSimpleName() + "#getSize", e);
		} finally {
			endService();
		}
		return result;
	}

	/**
	 * リストを応答する
	 * @return
	 */
	public List<Book> getList(){
		return this.getList(0, 0);
	}

	/**
	 * リストを応答する
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List<Book> getList(long offset, long pageSize){
		List<Book> resultList = new ArrayList<Book>();
		try{
			startService();
			DdBookManager bookManager = new DdBookManager();
			resultList = bookManager.getList(offset, pageSize);
		} catch (Exception e) {
			throwRuntimeException(this.getClass().getSimpleName() + "#getList", e);
		} finally {
			endService();
		}
		mergeAndDebugPrint(resultList); //mergeが必要
		return resultList;
	}

	/**
	 * リストを応答する
	 * @param offset
	 * @param pageSize
	 * @param ddSortParam
	 * @return
	 */
	public List<Book> getList(long offset, long pageSize, DdSortParam ddSortParam){
		List<Book> resultList = new ArrayList<Book>();
		try{
			startService();
			DdBookManager bookManager = new DdBookManager();
			resultList = bookManager.getList(offset, pageSize, ddSortParam);
		} catch (Exception e) {
			throwRuntimeException(this.getClass().getSimpleName() + "#getList", e);
		} finally {
			endService();
		}
		mergeAndDebugPrint(resultList); //mergeが必要
		return resultList;
	}

	/** 遅延ロード分も復元する */
	public Book loadFull(Book book){
		Book result = null;
		try{
			startService();
			DdBookManager bookManager = new DdBookManager();
			result = bookManager.merge(book);
			//-- BLOCKSTART Fetch
			//-- CASE Collection
			if (result.getStoreList() != null){
				result.getStoreList().size();
			}
			//-- CASE Simple
			if (result.getIsbn() != null){
				result.getIsbn().getId();
			}
			//-- BLOCKEND Fetch
		} catch (Exception e) {
			throwRuntimeException(this.getClass().getSimpleName() + "#loadFull", null);
		} finally {
			endService();
		}
		return result;
	}

	/** debug */
	private void mergeAndDebugPrint(List<Book> list){
		try{
			startService();
			DdBookManager bookManager = new DdBookManager();			
			for(Book book : list){
				Book mergedBook = bookManager.merge(book);
				mergedBook.debugPrint();
			}
		} catch (Exception e) {
			throwRuntimeException(this.getClass().getSimpleName() + "#mergeAndDebugPrint", e);
		} finally {
			endService();
		}
	}
}
