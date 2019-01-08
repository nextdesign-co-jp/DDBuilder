/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.view.ddb;

import java.io.Serializable;

/**
 * サービスメソッド一覧に表示するためのサービスメソッド情報
 * @author murayama
 *
 */
public class DdServiceMethodInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String serviceClassName;
	private String serviceMethodTitle;
	private String serviceMethodName;
	private String returnValueTypeName;
	private String parameterNames;
	private DdBaseEditPage nextPage;
	
	/** コンストラクタ */
	public DdServiceMethodInfo(
			String serviceClassName, 
			String serviceMethodTitle, 
			String serviceMethodName,
			String returnValueTypeName,
			String parameterNames){
		super();
		this.serviceMethodTitle = serviceMethodTitle;
		this.serviceClassName = serviceClassName;
		this.serviceMethodName = serviceMethodName;
		this.returnValueTypeName = returnValueTypeName;
		this.parameterNames = parameterNames;
	}

	public String getServiceMethodTitle() {
		return serviceMethodTitle;
	}

	public String getServiceClassName() {
		return serviceClassName;
	}

	public String getServiceMethodName() {
		return serviceMethodName;
	}

	public String getReturnValueTypeName() {
		return returnValueTypeName;
	}

	public String getParameterNames() {
		return parameterNames;
	}

	public DdBaseEditPage getNextPage() {
		return nextPage;
	}

	public void setNextPage(DdBaseEditPage nextPage) {
		this.nextPage = nextPage;
	}
}
