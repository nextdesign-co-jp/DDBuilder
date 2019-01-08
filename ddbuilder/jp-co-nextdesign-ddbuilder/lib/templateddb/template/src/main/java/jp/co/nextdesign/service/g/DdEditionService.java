/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.service.g;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.domain.Edition;
import jp.co.nextdesign.domain.g.DdEditionManager;
import jp.co.nextdesign.service.ddb.DdBaseService;

public class DdEditionService extends DdBaseService {

	/**
	 * 永続化する
	 * @param edition
	 */
	public void persist(Edition edition) {
		try {
			startService();
			DdEditionManager editionManager = new DdEditionManager();
			editionManager.persist(edition);
		} catch (Exception e) {
			throwRuntimeException("persist", e);
		} finally {
			endService();
		}
	}

	public List<Edition> getList(){
		List<Edition> resultList = new ArrayList<Edition>();
		try {
			startService();
			DdEditionManager editionManager = new DdEditionManager();
			resultList = editionManager.getList();
		} catch (Exception e) {
			throwRuntimeException("getList", e);
		} finally {
			endService();
		}
		return resultList;
	}
}
