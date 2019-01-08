/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.template.cases;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.ddb.core.template.BdCodeLine;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethod;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodParameter;

public class BdCodeCaseJavaInvokeParametersAll extends BdBaseCodeCase {

	/**
	 * コンストラクタ
	 * @param lineListOfCase
	 */
	public BdCodeCaseJavaInvokeParametersAll(List<BdCodeLine> lineListOfCase){
		super(lineListOfCase);
	}
	
	private static final String TAB = "				";
	private static final String COMMA = ",";
	
	/** 引数に対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserServiceMethod m){
		List<String> resultList = new ArrayList<String>();
		List<BdUserServiceMethodParameter> pList = m.getServiceMethodParameterList();
		if (pList.size() > 0){
			resultList.add(TAB + pList.get(0).getName());
		}
		for(int i=1; i<pList.size(); i++){
			resultList.add(TAB + COMMA + pList.get(i).getName());
		}
		return resultList;
	}
}

