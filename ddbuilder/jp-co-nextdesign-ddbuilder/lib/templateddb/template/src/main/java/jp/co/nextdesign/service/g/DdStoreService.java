/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.service.g;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.domain.Book;
import jp.co.nextdesign.domain.g.DdBookManager;
import jp.co.nextdesign.domain.g.DdStoreManager;
import jp.co.nextdesign.domain.store.Store;
import jp.co.nextdesign.service.ddb.DdBaseService;

public class DdStoreService extends DdBaseService {

	/**
	 * 永続化する
	 * @param store
	 */
	public void persist(Store store) {
		try {
			startService();
			begin();
			DdStoreManager storeManager = new DdStoreManager();
			storeManager.persist(store);
			commit();
		} catch (Exception e) {
			rollback();
		} finally {
			endService();
		}
	}

	public Store merge(Store store) {
		Store result = null;
		try {
			startService();
			begin();
			DdStoreManager storeManager = new DdStoreManager();
			result = storeManager.merge(store);
			commit();
		} catch (Exception e) {
			e.printStackTrace();
			rollback();
		} finally {
			endService();
		}
		return result;
	}

	public List<Store> getList(){
		List<Store> resultList = new ArrayList<Store>();
		try {
			startService();
			DdStoreManager storeManager = new DdStoreManager();
			resultList = storeManager.getList();
		} catch (Exception e) {
			throwRuntimeException("getList", e);
		} finally {
			endService();
		}
		return resultList;
	}
}
