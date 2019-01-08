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
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaInvokeHeadNotVoid;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethod;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * BdBaseCodeBlockを継承していることに注意
 * @author murayama
 *
 */
public class BdCodeBlockJavaInvokeHead extends BdBaseCodeBlock {

	/** コンストラクタ */
	public BdCodeBlockJavaInvokeHead(List<BdCodeLine> lineList){
		super(lineList);
	}

	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(BdUserServiceMethod m) {
		List<String> resultList = new ArrayList<String>();
		BdBaseCodeCase codeCase = null;
		if (m.isReturnVoid()){
			codeCase = this.getCodeCaseByKey(KEY_Void);
		} else {
			codeCase = this.getCodeCaseByKey(KEY_NotVoid);
		}
		if (codeCase != null){
			resultList.addAll(codeCase.generate(m));
		} else {
			NdLogger.getInstance().error(KEY_Void + " AND " + KEY_NotVoid + " **IS UNKNOWN CodeCase**.", null);
		}
		return resultList;
	}

	private static final String KEY_Void = "Void";
	private static final String KEY_NotVoid = "NotVoid";

	/** 
	 * 各サブクラスでOverrideする
	 */
	@Override
	protected BdBaseCodeCase createCodeCase(String key, List<BdCodeLine> lineListOfCase){
		BdBaseCodeCase result = null;
		if (KEY_Void.equals(key)){
			result = new BdCodeCaseJavaInvokeHeadNotVoid(lineListOfCase);
		} else if (KEY_NotVoid.equals(key)){
			result = new BdCodeCaseJavaInvokeHeadNotVoid(lineListOfCase);
		} else if (KEY_Nop.equals(key)){
		} else {
			key = (key == null || "".equals(key)) ? "CASEキーなし" : key;
			NdLogger.getInstance().error(this.getClass().getName() + " : [WARNING] Unknown Key = " + key, null);
		}
		return result;
	}
}
