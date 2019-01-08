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
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaServiceFetchCollection;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaServiceFetchSimple;
import jp.co.nextdesign.ddb.core.user.BdUserAttribute;
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.util.logging.NdLogger;

public class BdCodeBlockJavaServiceFetch extends BdBaseCodeBlock {

	/** コンストラクタ */
	public BdCodeBlockJavaServiceFetch(List<BdCodeLine> lineList){
		super(lineList);
	}
	
	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(BdUserClass c) {
		List<String> resultList = new ArrayList<String>();
		for(BdUserAttribute a : c.getAttributeList()){
			if (a.isDdbEmbedded() || a.isDdbManyToOne() || a.isDdbOneToMany() || a.isDdbManyToMany()){
				BdBaseCodeCase codeCase = null;
				if (a.isDdbEmbedded() || a.isDdbManyToOne()){
					codeCase = this.getCodeCaseByKey(CASE_KEY_Simple);
				} else if (a.isDdbOneToMany() || a.isDdbManyToMany()){
					codeCase = this.getCodeCaseByKey(CASE_KEY_Collection);
				}
				if (codeCase != null){
					resultList.addAll(codeCase.generate(a));
				} else{
					NdLogger.getInstance().error(c.getName() + "." + a.getName() + "CodeCaseが見つからない", null);
				}
			}
		}
		return resultList;
	}
	
	//このBLOCKに含まれるCASEキー
	private static final String CASE_KEY_Collection = "Collection";
	private static final String CASE_KEY_Simple = "Simple";
	
	/** 
	 * 各サブクラスでOverrideする
	 */
	@Override
	protected BdBaseCodeCase createCodeCase(String key, List<BdCodeLine> lineListOfCase){
		BdBaseCodeCase result = null;
		if (CASE_KEY_Simple.equals(key)){
			result = new BdCodeCaseJavaServiceFetchSimple(lineListOfCase);
		} else if (CASE_KEY_Collection.equals(key)){
			result = new BdCodeCaseJavaServiceFetchCollection(lineListOfCase);
		} else if (KEY_Nop.equals(key)){
		} else {
			key = (key == null || "".equals(key)) ? "CASEキーなし" : key;
			NdLogger.getInstance().error(this.getClass().getName() + " : [WARNING] Unknown Key = " + key, null);
		}
		return result;
	}
}
