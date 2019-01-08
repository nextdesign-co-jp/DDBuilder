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
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseHtmlModalDiv;
import jp.co.nextdesign.ddb.core.user.BdUserAttribute;
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethod;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodParameter;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * BdBaseCodeBlockを継承していることに注意
 * @author murayama
 *
 */
public class BdCodeBlockHtmlModal extends BdBaseCodeBlock {

	/** コンストラクタ */
	public BdCodeBlockHtmlModal(List<BdCodeLine> lineList){
		super(lineList);
	}
	
	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(BdUserClass c) {
		List<String> resultList = new ArrayList<String>();
		BdBaseCodeCase codeCase = this.getCodeCaseByKey(KEY_Div);
		if (codeCase != null){
			List<BdUserAttribute> aList = new ArrayList<BdUserAttribute>(c.getAttributeListByDdbTypeKey(BdUserAttribute.KEY_ManyToOne));
			aList.addAll(c.getAttributeListByDdbTypeKey(BdUserAttribute.KEY_OneToMany));
			aList.addAll(c.getAttributeListByDdbTypeKey(BdUserAttribute.KEY_ManyToMany));
			for(BdUserAttribute a : aList){
				resultList.addAll(codeCase.generate(a));
			}
		} else {
			NdLogger.getInstance().error(KEY_Div + " **IS UNKNOWN CodeCase**.", null);
		}
		return resultList;
	}

	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(BdUserServiceMethod m) {
		List<String> resultList = new ArrayList<String>();
		BdBaseCodeCase codeCase = this.getCodeCaseByKey(KEY_Div);
		if (codeCase != null){
			List<BdUserServiceMethodParameter> pList = new ArrayList<BdUserServiceMethodParameter>(m.getParameterListByDdbTypeKey(BdUserAttribute.KEY_ManyToOne));
			pList.addAll(m.getParameterListByDdbTypeKey(BdUserAttribute.KEY_OneToMany));
			pList.addAll(m.getParameterListByDdbTypeKey(BdUserAttribute.KEY_ManyToMany));
			for(BdUserServiceMethodParameter p : pList){
				resultList.addAll(codeCase.generate(p));
			}
		} else {
			NdLogger.getInstance().error(KEY_Div + " **IS UNKNOWN CodeCase**.", null);
		}
		return resultList;
	}

	private static final String KEY_Div = "Div";
	
	/** 
	 * 各サブクラスでOverrideする
	 */
	@Override
	protected BdBaseCodeCase createCodeCase(String key, List<BdCodeLine> lineListOfCase){
		BdBaseCodeCase result = null;
		if (KEY_Div.equals(key)){
			result = new BdCodeCaseHtmlModalDiv(lineListOfCase);
		} else if (KEY_Nop.equals(key)){
		} else {
			key = (key == null || "".equals(key)) ? "CASEキーなし" : key;
			NdLogger.getInstance().error(this.getClass().getName() + " : [WARNING] Unknown Key = " + key, null);
		}
		return result;
	}
}
