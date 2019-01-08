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
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethod;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodResult;

public class BdCodeCaseJavaImportBigDecimal extends BdBaseCodeCase {

	/**
	 * コンストラクタ
	 * @param lineListOfCase
	 */
	public BdCodeCaseJavaImportBigDecimal(List<BdCodeLine> lineListOfCase){
		super(lineListOfCase);
	}

	/** 属性に対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserClass c){
		List<String> resultList = new ArrayList<String>();
		for(String line : c.getImportList()){
			if (line != null && line.endsWith(".BigDecimal")){
				resultList.add("import " + line + ";");
			}
		}
		return resultList;
	}

	/** サービスメソッドに対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserServiceMethod m){
		List<String> resultList = new ArrayList<String>();
		for(String line : m.getBdServiceClass().getImportList()){
			if (line != null && line.endsWith(".BigDecimal")){
				resultList.add("import " + line + ";");
			}
		}
		return resultList;
	}

	/** サービスメソッド戻り型に対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserServiceMethodResult r){
		List<String> resultList = new ArrayList<String>();
		if (r.isDdbBigDecimal()){
			for(BdCodeLine line : this.getCodeLineList()){
				resultList.add(line.getLine());
			}
		}
		return resultList;
	}
}
