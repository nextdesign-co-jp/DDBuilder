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
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaServiceResultPageNaviOneToMany;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodResult;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * BdBaseCodeBlockを継承していることに注意
 * @author murayama
 *
 */
public class BdCodeBlockJavaServiceResultPageNavi extends BdBaseCodeBlock {

	/** コンストラクタ */
	public BdCodeBlockJavaServiceResultPageNavi(List<BdCodeLine> lineList){
		super(lineList);
	}
	
	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(BdUserServiceMethodResult r) {
		List<String> resultList = new ArrayList<String>();
		BdBaseCodeCase codeCase = this.getCodeCaseByKey(r.getDdbTypeKey());
		if (codeCase != null){
			resultList.addAll(codeCase.generate(r));
		} else {
			//NdLogger.getInstance().error(KEY_OneToMany + " **IS UNKNOWN CodeCase**.", null); //KEY_OneToMany以外も存在する
		}
		return resultList;
	}
	
	private static final String KEY_OneToMany = "OneToMany";
	
	/** 
	 * 各サブクラスでOverrideする
	 */
	@Override
	protected BdBaseCodeCase createCodeCase(String key, List<BdCodeLine> lineListOfCase){
		BdBaseCodeCase result = null;
		if (KEY_OneToMany.equals(key)){
			result = new BdCodeCaseJavaServiceResultPageNaviOneToMany(lineListOfCase);
		} else if (KEY_Nop.equals(key)){
		} else {
			key = (key == null || "".equals(key)) ? "CASEキーなし" : key;
			NdLogger.getInstance().error(this.getClass().getName() + " : [WARNING] Unknown Key = " + key, null);
		}
		return result;
	}
}
