/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.view.ddb;

import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class DdBaseEditPage extends DdBasePage {
	private static final long serialVersionUID = 1L;

	protected DdOperationTypes operationType;

	/** コンストラクタ */
	public DdBaseEditPage(DdOperationTypes operationType, DdBasePage backPage){
		super();
		this.operationType = operationType;
		this.backPage = backPage;
	}

	/** コンストラクタ */
	public DdBaseEditPage(final PageParameters parameters){
		super(parameters);
	}
	
	/** 最初の編集ページか否か。submit時にpersist/mergeを実行するか否か */
	protected boolean isStartPageOfEditing(){
		return !(this.backPage instanceof DdBaseEditPage);
	}
}
