/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.service.g;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.domain.Isbn;
import jp.co.nextdesign.domain.g.DdIsbnManager;
import jp.co.nextdesign.service.ddb.DdBaseService;

/**
 * サービス テンプレート
 * @author murayama
 *
 */
public class DdIsbnService extends DdBaseService {

	/**
	 * 永続化する
	 * @param isbn
	 */
	public void persist(Isbn isbn) {
		try {
			startService();
			begin();
			DdIsbnManager isbnManager = new DdIsbnManager();
			isbnManager.persist(isbn);
			commit();
		} catch (Exception e) {
			rollback();
		} finally {
			endService();
		}
	}

	/**
	 * mergeして永続化する
	 * @param isbn
	 * @return
	 */
	public Isbn merge(Isbn isbn) {
		Isbn result = null;
		try {
			startService();
			begin();
			DdIsbnManager isbnManager = new DdIsbnManager();
			result = isbnManager.merge(isbn);
			commit();
		} catch (Exception e) {
			rollback();
		} finally {
			endService();
		}
		return result;
	}
	
	public Boolean remove(Isbn isbn){
		Boolean result = false;
		try {
			startService();
			begin();
			DdIsbnManager isbnManager = new DdIsbnManager();
			Isbn isbnMerged = isbnManager.merge(isbn);
			isbnManager.delete(isbnMerged);
			commit();
			result = true;
		} catch (Exception e) {
			rollback();
		} finally {
			endService();
		}
		return result;
	}

	public void clear() {
		try {
			startService();
			DdIsbnManager isbnManager = new DdIsbnManager();
			isbnManager.clear();
		} catch (Exception e) {
			throwRuntimeException("clear", e);
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
			DdIsbnManager isbnManager = new DdIsbnManager();
			result = isbnManager.getCount();
		} catch (Exception e) {
			throwRuntimeException("getSize", e);
		} finally {
			endService();
		}
		return result;
	}
	
	/**
	 * リストを応答する
	 * @return
	 */
	public List<Isbn> getList() {
		return this.getList(0, 0);
	}
	
	/**
	 * リストを応答する
	 * @param offset
	 * @param pageSize
	 * @return
	 */
	public List<Isbn> getList(long offset, long pageSize) {
		List<Isbn> resultList = new ArrayList<Isbn>();
		try{
			startService();
			DdIsbnManager isbnManager = new DdIsbnManager();
			resultList = isbnManager.getList(offset, pageSize);
		} catch (Exception e) {
			throwRuntimeException("getList", e);
		} finally {
			endService();
		}
		mergeAndDebugPrint(resultList); //mergeが必要
		return resultList;
	}
	
	/** 遅延ロード分も復元する */
	public Isbn loadFull(Isbn isbn){
		Isbn result = null;
		try{
			startService();
			DdIsbnManager isbnManager = new DdIsbnManager();
			result = isbnManager.merge(isbn);
		} catch (Exception e) {
			throwRuntimeException("loadFull", e);
		} finally {
			endService();
		}
		return result;
	}
	
	/** debug */
	private void debugPrint(List<Isbn> list){
		for(Isbn isbn : list){
			isbn.debugPrint();
		}
	}
	
	/** debug */
	private void mergeAndDebugPrint(List<Isbn> list){
		try{
			startService();
			DdIsbnManager isbnManager = new DdIsbnManager();			
			for(Isbn isbn : list){
				Isbn mergedIsbn = isbnManager.merge(isbn);
				mergedIsbn.debugPrint();
			}
		} catch (Exception e) {
			throwRuntimeException("mergeandDebug", e);
		} finally {
			endService();
		}
	}
}
