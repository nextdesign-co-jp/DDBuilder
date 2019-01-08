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
import jp.co.nextdesign.ddb.core.user.BdUserClass;

/**
 * BdCodeBlockDomainClassListPageHtmlEntityとBdCodeBlockDomainClassListPageJavaEntityの基底クラス
 * クラスを基にソースを生成する動きはHtmlとJava側で共通である
 * @author murayama
 *
 */
public abstract class BdCodeBlockBaseDomainClassListPageEntity extends BdBaseCodeBlock {

	/** コンストラクタ */
	public BdCodeBlockBaseDomainClassListPageEntity(List<BdCodeLine> lineList){
		super(lineList);
	}
	
	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(List<BdUserClass> cList) {
		List<String> resultList = new ArrayList<String>();
		BdBaseCodeCase codeCase = this.getCodeCase(BdBaseCodeCase.KEY_All);
		if (codeCase != null){
			for(BdUserClass c : cList){
				resultList.addAll(codeCase.generate(c));
			}
		}
		return resultList;
	}

	/**
	 * 属性からCodeCaseを応答する
	 * @return
	 */
	private BdBaseCodeCase getCodeCase(String caseKey){
		BdBaseCodeCase result = null;
		if (codeCaseMap.containsKey(caseKey)){
			result = codeCaseMap.get(caseKey);
		}
		return result;
	}
}
