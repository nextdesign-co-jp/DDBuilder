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
import jp.co.nextdesign.util.BdStringHelper;
import jp.co.nextdesign.util.BdStringHelperReplacementPattern;

public class BdCodeCaseHtmlServiceMethodListPageMethodAll extends BdBaseCodeCase {
	
	/**
	 * コンストラクタ
	 * @param lineListOfCase
	 */
	public BdCodeCaseHtmlServiceMethodListPageMethodAll(List<BdCodeLine> lineListOfCase){
		super(lineListOfCase);
	}

	/** クラスに対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserServiceMethod m){
		List<BdStringHelperReplacementPattern> replacementPatternList = this.createReplacementPatternList(m);
		List<String> resultList = new ArrayList<String>();
		for(BdCodeLine line : this.getCodeLineList()){
			resultList.add(BdStringHelper.replace(line.getLine(), replacementPatternList));
		}
		return resultList;
	}
	
	/** 文字置換パターンを作成する */
	private List<BdStringHelperReplacementPattern> createReplacementPatternList(BdUserServiceMethod m){
		List<BdStringHelperReplacementPattern> resultList = new ArrayList<BdStringHelperReplacementPattern>();
		resultList.add(new BdStringHelperReplacementPattern("serviceMethod", m.getBdServiceClass().getName() + m.getUpperStartedName()));
		resultList.add(new BdStringHelperReplacementPattern("BookService", m.getBdServiceClass().getName()));
		resultList.add(new BdStringHelperReplacementPattern("書籍検索", m.getJavadocTitle()));
		resultList.add(new BdStringHelperReplacementPattern("method1", m.getName()));
		resultList.add(new BdStringHelperReplacementPattern("returnValue", BdStringHelper.toHtmlEscaped(m.getReturnValueDisplayName())));
		resultList.add(new BdStringHelperReplacementPattern("parameters", BdStringHelper.toHtmlEscaped(m.getMethodParametersDisplayName())));
		return resultList;
	}
}
