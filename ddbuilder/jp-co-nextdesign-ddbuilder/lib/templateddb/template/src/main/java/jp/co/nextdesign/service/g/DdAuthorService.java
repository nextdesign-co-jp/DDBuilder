/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.service.g;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.domain.Author;
import jp.co.nextdesign.domain.g.DdAuthorManager;
import jp.co.nextdesign.service.ddb.DdBaseService;

public class DdAuthorService extends DdBaseService {

	/**
	 * 永続化する
	 * @param author
	 */
	public void persist(Author author) {
		try {
			startService();
			begin();
			DdAuthorManager authorManager = new DdAuthorManager();
			authorManager.persist(author);
			commit();
		} catch (Exception e) {
			rollback();
		} finally {
			endService();
		}
	}
	
	public List<Author> getList(){
		List<Author> resultList = new ArrayList<Author>();
		try {
			startService();
			DdAuthorManager authorManager = new DdAuthorManager();
			resultList = authorManager.getList();
		} catch (Exception e) {
			throwRuntimeException("getList", e);
		} finally {
			endService();
		}
		return resultList;
	}
}
