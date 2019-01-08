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

public class BdCodeCaseJavaServiceMethodListPageMethodAll extends BdBaseCodeCase {
	/**
	 * コンストラクタ
	 * @param lineListOfCase
	 */
	public BdCodeCaseJavaServiceMethodListPageMethodAll(List<BdCodeLine> lineListOfCase){
		super(lineListOfCase);
	}

	/** クラスに対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserServiceMethod m){
		List<String> resultList = new ArrayList<String>();
		for(BdCodeLine line : this.getCodeLineList()){
			String customized = line.getLine().replaceAll("_info", m.getBdServiceClass().getBdUserClass().getLowerStartedName() + "_" + m.getName() + "_info");
			customized = customized.replaceAll("serviceClassName", m.getBdServiceClass().getName());
			customized = customized.replaceAll("serviceMethodTitle", m.getJavadocTitle());
			customized = customized.replaceAll("serviceMethodName", m.getName());
			customized = customized.replaceAll("returnValueTypeName", m.getReturnValueDisplayName());
			customized = customized.replaceAll("parameterNames", m.getMethodParametersDisplayName());
			customized = customized.replaceAll("BookServiceInvokePageMethod1", m.getBdServiceClass().getName() + "InvokePage" + m.getUpperStartedName());
			customized = customized.replaceAll("_nextPage", m.getBdServiceClass().getBdUserClass().getLowerStartedName() + "_" + m.getName() + "_nextPage");
			resultList.add(customized);
		}
		return resultList;
	}
}
