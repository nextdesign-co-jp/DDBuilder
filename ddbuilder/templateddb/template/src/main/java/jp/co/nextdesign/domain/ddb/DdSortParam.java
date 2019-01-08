/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.domain.ddb;

/**
 * OrderBy項目
 * @author murayama
 *
 */
public class DdSortParam {
	
	/** 属性名 */
	private String attributeName = "";
	
	/** 昇順・降順 */
	private DdSortOrder orderType = DdSortOrder.ASC;

	/**
	 * コンストラクタ
	 * @param attributeName
	 * @param orderType
	 */
	public DdSortParam(String attributeName, DdSortOrder orderType){
		super();
		this.attributeName = attributeName;
		this.orderType = orderType;
	}
	
	/** 属性名を応答する */
	public String getAttributeName(){
		return this.attributeName;
	}
	
	/** 昇順か否か */
	public boolean isAsc(){
		return DdSortOrder.ASC == this.orderType;
	}
}
