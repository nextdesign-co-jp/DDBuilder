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
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaUpdatePageUpdate;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaUpdatePageUpdateSelected;
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
public class BdCodeBlockJavaUpdatePage extends BdBaseCodeBlock {

	/** コンストラクタ */
	public BdCodeBlockJavaUpdatePage(List<BdCodeLine> lineList){
		super(lineList);
	}
	
	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(BdUserClass c) {
		List<String> resultList = new ArrayList<String>();
		BdBaseCodeCase codeCase = this.getCodeCaseByKey(KEY_Update);
		if (codeCase != null){
			List<BdUserAttribute> aList = new ArrayList<BdUserAttribute>(c.getAttributeListByDdbTypeKey(BdUserAttribute.KEY_OneToMany));
			aList.addAll(c.getAttributeListByDdbTypeKey(BdUserAttribute.KEY_ManyToMany));
			for(BdUserAttribute a : aList){
				resultList.addAll(codeCase.generate(a));
			}
		} else {
			NdLogger.getInstance().error(KEY_Update + " **IS UNKNOWN CodeCase**.", null);
		}
		return resultList;
	}
	
	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(BdUserServiceMethod m) {
		List<String> resultList = new ArrayList<String>();
		resultList.addAll(this.generateForKey(KEY_Update, m));
		resultList.addAll(this.generateForKey(KEY_UpdateSelected, m));
		return resultList;
	}
	
	/** 指定されたCaseKey分の生成コードを応答する */
	private List<String> generateForKey(String key, BdUserServiceMethod m){
		List<String> resultList = new ArrayList<String>();
		BdBaseCodeCase codeCase = this.getCodeCaseByKey(key);
		if (codeCase != null){
			for(BdUserServiceMethodParameter p :  this.getParameterListForKey(key, m)){
				resultList.addAll(codeCase.generate(p));
			}
		} else {
			//このクラスは共用なので、「ない」ケースは常にありえるので警告ログは出さない NdLogger.getInstance().info(key + " **IS UNKNOWN CodeCase**.");
		}
		return resultList;
	}
	
	/**
	 *  KEY_Update対象の引数リスト、またはKEY_UpdateSelected対象の引数リストを応答する
	 *  KEY_UpdateとKEY_UpdateSelectedは別々のCodeBlockにすべきかもしれないが、ここでは、このメソッドを追加する方式とした。
	 */
	private List<BdUserServiceMethodParameter> getParameterListForKey(String key, BdUserServiceMethod m){
		List<BdUserServiceMethodParameter> resultList = null;
		if (KEY_Update.equals(key)){
			resultList = new ArrayList<BdUserServiceMethodParameter>(m.getParameterListByDdbTypeKey(BdUserAttribute.KEY_OneToMany));
		} else if (KEY_UpdateSelected.equals(key)){
			resultList = new ArrayList<BdUserServiceMethodParameter>(m.getParameterListByDdbTypeKey(BdUserAttribute.KEY_ManyToOne));
		}
		return resultList;
	}

	private static final String KEY_Update = "Update";
	private static final String KEY_UpdateSelected = "UpdateSelected";
	
	/** 
	 * 各サブクラスでOverrideする
	 */
	@Override
	protected BdBaseCodeCase createCodeCase(String key, List<BdCodeLine> lineListOfCase){
		BdBaseCodeCase result = null;
		if (KEY_Update.equals(key)){
			result = new BdCodeCaseJavaUpdatePageUpdate(lineListOfCase);
		} else if (KEY_UpdateSelected.equals(key)){
			result = new BdCodeCaseJavaUpdatePageUpdateSelected(lineListOfCase);
		} else if (KEY_Nop.equals(key)){
		} else {
			key = (key == null || "".equals(key)) ? "CASEキーなし" : key;
			NdLogger.getInstance().error(this.getClass().getName() + " : [WARNING] Unknown Key = " + key, null);
		}
		return result;
	}
}
