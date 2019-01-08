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
import jp.co.nextdesign.ddb.core.user.BdUserAttribute;
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.util.BdStringHelper;
import jp.co.nextdesign.util.BdStringHelperReplacementPattern;

public class BdCodeCaseJavaEditPageOkManyToMany extends BdBaseCodeCase {
	/**
	 * コンストラクタ
	 * @param lineListOfCase
	 */
	public BdCodeCaseJavaEditPageOkManyToMany(List<BdCodeLine> lineListOfCase){
		super(lineListOfCase);
	}

	/** 属性に対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserAttribute a){
		List<String> resultList = new ArrayList<String>();
		List<BdStringHelperReplacementPattern> replacementPatternList = this.createReplacementPatternList(a);
		for(BdCodeLine codeLine : this.getCodeLineList()){
			resultList.add(BdStringHelper.replace(codeLine.getLine(), replacementPatternList));
		}
		return resultList;
	}

	/** 文字置換パターンを作成する */
	private List<BdStringHelperReplacementPattern> createReplacementPatternList(BdUserAttribute a){
		List<BdStringHelperReplacementPattern> resultList = new ArrayList<BdStringHelperReplacementPattern>();
		resultList.add(new BdStringHelperReplacementPattern("book", a.getBdUserClass().getLowerStartedName()));
		BdUserClass collectionElementClass = a.getCollectionElementBdUserClass();
		if (collectionElementClass != null){
			resultList.add(new BdStringHelperReplacementPattern("ddbRemoveStoreList", "ddbRemove" + a.getUpperStartedName()));
			resultList.add(new BdStringHelperReplacementPattern("StoreService", collectionElementClass.getName() + "Service"));
			resultList.add(new BdStringHelperReplacementPattern("storeService", collectionElementClass.getLowerStartedName() + "Service"));
			resultList.add(new BdStringHelperReplacementPattern("getStoreList", "get" + a.getUpperStartedName()));
			resultList.add(new BdStringHelperReplacementPattern("Store", collectionElementClass.getName()));
			resultList.add(new BdStringHelperReplacementPattern("store", collectionElementClass.getLowerStartedName()));
		}
		return resultList;
	}
}