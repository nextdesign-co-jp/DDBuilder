/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.template.block;

import java.util.List;

import jp.co.nextdesign.ddb.core.template.BdCodeLine;
import jp.co.nextdesign.ddb.core.template.cases.BdBaseCodeCase;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaBeforeAfterListManyToMany;
import jp.co.nextdesign.ddb.core.user.BdUserAttribute;
import jp.co.nextdesign.util.logging.NdLogger;

public class BdCodeBlockJavaBeforeAfterList extends BdCodeBlockBaseField {

	/** コンストラクタ */
	public BdCodeBlockJavaBeforeAfterList(List<BdCodeLine> lineList){
		super(lineList);
	}
	
	/** 
	 * 各サブクラスでOverrideする
	 * このBLOCKは、BdDdbAttributeに定義されたDDB属性分類型のキー名をCASEキーとする
	 */
	@Override
	protected BdBaseCodeCase createCodeCase(String key, List<BdCodeLine> lineListOfCase){
		BdBaseCodeCase result = null;
		if (BdUserAttribute.KEY_ManyToMany.equals(key)){
			result = new BdCodeCaseJavaBeforeAfterListManyToMany(lineListOfCase);
		} else if (KEY_Nop.equals(key)){
		} else {
			key = (key == null || "".equals(key)) ? "CASEキーなし" : key;
			NdLogger.getInstance().error(this.getClass().getName() + " : [WARNING] Unknown Key = " + key, null);
		}
		return result;
	}
}
