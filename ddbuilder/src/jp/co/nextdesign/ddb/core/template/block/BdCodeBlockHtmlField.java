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
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseHtmlFieldBigDecimal;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseHtmlFieldBoolean;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseHtmlFieldDate;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseHtmlFieldEmbedded;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseHtmlFieldEnum;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseHtmlFieldManyToMany;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseHtmlFieldManyToOne;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseHtmlFieldOneToMany;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseHtmlFieldJavaPrimitiveClass;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseHtmlFieldString;
import jp.co.nextdesign.ddb.core.user.BdUserAttribute;
import jp.co.nextdesign.util.logging.NdLogger;

public class BdCodeBlockHtmlField extends BdCodeBlockBaseField {

	/** コンストラクタ */
	public BdCodeBlockHtmlField(List<BdCodeLine> lineList){
		super(lineList);
	}
	
	/** 
	 * 各サブクラスでOverrideする
	 * このBLOCKは、BdDdbAttributeに定義されたDDB属性分類型のキー名をCASEキーとする
	 */
	@Override
	protected BdBaseCodeCase createCodeCase(String key, List<BdCodeLine> lineListOfCase){
		BdBaseCodeCase result = null;
		if (BdUserAttribute.KEY_JavaPrimitiveClass.equals(key)){
			result = new BdCodeCaseHtmlFieldJavaPrimitiveClass(lineListOfCase);
		} else if (BdUserAttribute.KEY_String.equals(key)){
			result = new BdCodeCaseHtmlFieldString(lineListOfCase);
		} else if (BdUserAttribute.KEY_Date.equals(key)){
			result = new BdCodeCaseHtmlFieldDate(lineListOfCase);
		} else if (BdUserAttribute.KEY_BigDecimal.equals(key)){
			result = new BdCodeCaseHtmlFieldBigDecimal(lineListOfCase);
		} else if (BdUserAttribute.KEY_Boolean.equals(key)){
			result = new BdCodeCaseHtmlFieldBoolean(lineListOfCase);
		} else if (BdUserAttribute.KEY_Enum.equals(key)){
			result = new BdCodeCaseHtmlFieldEnum(lineListOfCase);
		} else if (BdUserAttribute.KEY_Embedded.equals(key)){
			result = new BdCodeCaseHtmlFieldEmbedded(lineListOfCase);
		} else if (BdUserAttribute.KEY_ManyToOne.equals(key)){
			result = new BdCodeCaseHtmlFieldManyToOne(lineListOfCase);
		} else if (BdUserAttribute.KEY_OneToMany.equals(key)){
			result = new BdCodeCaseHtmlFieldOneToMany(lineListOfCase);
		} else if (BdUserAttribute.KEY_ManyToMany.equals(key)){
			result = new BdCodeCaseHtmlFieldManyToMany(lineListOfCase);
		} else if (KEY_Nop.equals(key)){
		} else {
			key = (key == null || "".equals(key)) ? "CASEキーなし" : key;
			NdLogger.getInstance().error(this.getClass().getName() + " : [WARNING] Unknown Key = " + key, null);
		}
		return result;
	}
}
