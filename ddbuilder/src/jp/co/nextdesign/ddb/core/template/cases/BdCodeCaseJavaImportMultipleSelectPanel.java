/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.template.cases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.co.nextdesign.ddb.core.template.BdCodeLine;
import jp.co.nextdesign.ddb.core.template.BdTemplateProject;
import jp.co.nextdesign.ddb.core.user.BdUserAttribute;
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodParameter;

public class BdCodeCaseJavaImportMultipleSelectPanel extends BdBaseCodeCase {

	/**
	 * コンストラクタ
	 * @param lineListOfCase
	 */
	public BdCodeCaseJavaImportMultipleSelectPanel(List<BdCodeLine> lineListOfCase){
		super(lineListOfCase);
	}

	/** 属性に対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserAttribute a){
		List<String> resultList = new ArrayList<String>();
		resultList.add("import " + a.getBdUserClass().getFullName() + ";");		
		BdUserClass collectionElementClass = a.getCollectionElementBdUserClass();
		if (collectionElementClass != null){
			resultList.add("import " + collectionElementClass.getFullName() + ";");
			resultList.add("import " + a.getBdUserClass().getGroupId() + BdTemplateProject.DDB_SERVICE_PACKAGE_AND_HEADER + collectionElementClass.getName() + "Service;");
		}
		Collections.sort(resultList);
		return resultList;
	}

	/** 引数に対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserServiceMethodParameter p){
		List<String> resultList = new ArrayList<String>();
		resultList.add("import " + p.getBdUserServiceMethod().getBdServiceClass().getBdUserClass().getFullName() + ";");		
		BdUserClass collectionElementClass = p.getCollectionElementBdUserClass();
		if (collectionElementClass != null){
			resultList.add("import " + collectionElementClass.getFullName() + ";");
			resultList.add("import " + p.getBdUserServiceMethod().getBdServiceClass().getBdUserClass().getGroupId() + BdTemplateProject.DDB_SERVICE_PACKAGE_AND_HEADER + collectionElementClass.getName() + "Service;");
		}
		Collections.sort(resultList);
		return resultList;
	}
}
