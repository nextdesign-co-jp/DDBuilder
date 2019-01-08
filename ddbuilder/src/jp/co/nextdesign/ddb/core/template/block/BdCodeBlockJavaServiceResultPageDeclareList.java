/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.template.block;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.ddb.core.template.BdCodeLine;
import jp.co.nextdesign.ddb.core.template.cases.BdBaseCodeCase;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaServiceResultPageDeclareListAll;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaServiceResultPageDeclareListDate;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodResult;
import jp.co.nextdesign.util.logging.NdLogger;

public class BdCodeBlockJavaServiceResultPageDeclareList extends BdCodeBlockBaseField {

	/** コンストラクタ */
	public BdCodeBlockJavaServiceResultPageDeclareList(List<BdCodeLine> lineList){
		super(lineList);
	}

	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(BdUserServiceMethodResult r) {
		List<String> resultList = new ArrayList<String>();
		if (r.isDdbDate()){
			resultList.addAll(this.generateForKey(KEY_Date, r));
		}
		if (r.isDdbBigDecimal()){
			resultList.addAll(this.generateForKey(KEY_BigDecimal, r));
		}
		resultList.addAll(this.generateForKey(KEY_All, r));
		return resultList;
	}

	private List<String> generateForKey(String key, BdUserServiceMethodResult r){
		List<String> resultList = new ArrayList<String>();
		BdBaseCodeCase codeCase = this.getCodeCaseByKey(key);
		if (codeCase != null){
			resultList.addAll(codeCase.generate(r));
		} else {
			NdLogger.getInstance().error(key + " **IS UNKNOWN CodeCase**.", null);
		}
		return resultList;
	}

	private static final String KEY_All = "All";
	private static final String KEY_Date = "Date";
	private static final String KEY_BigDecimal = "BigDecimal";

	/** 
	 * 各サブクラスでOverrideする
	 */
	@Override
	protected BdBaseCodeCase createCodeCase(String key, List<BdCodeLine> lineListOfCase){
		BdBaseCodeCase result = null;
		if (KEY_All.equals(key)){
			result = new BdCodeCaseJavaServiceResultPageDeclareListAll(lineListOfCase);
		} else if (KEY_Date.equals(key)){
			result = new BdCodeCaseJavaServiceResultPageDeclareListDate(lineListOfCase);
		} else if (KEY_Nop.equals(key)){
		} else {
			key = (key == null || "".equals(key)) ? "CASEキーなし" : key;
			NdLogger.getInstance().error(this.getClass().getName() + " : [WARNING] Unknown Key = " + key, null);
		}
		return result;
	}
}
