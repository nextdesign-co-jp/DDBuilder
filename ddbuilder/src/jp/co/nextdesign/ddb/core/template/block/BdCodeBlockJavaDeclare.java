/*
 * DDBuilder
 * http://www.nextdesign.co.jp/ddd/index.html
 * Copyright 2015 NEXT DESIGN Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.co.nextdesign.ddb.core.template.block;

import java.util.List;

import jp.co.nextdesign.ddb.core.template.BdCodeLine;
import jp.co.nextdesign.ddb.core.template.cases.BdBaseCodeCase;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaFieldBigDecimal;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaFieldBoolean;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaFieldDate;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaFieldEmbedded;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaFieldEnum;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaFieldManyToMany;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaFieldManyToOne;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaFieldOneToMany;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaFieldJavaPrimitiveClass;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaFieldString;
import jp.co.nextdesign.ddb.core.user.BdUserAttribute;
import jp.co.nextdesign.util.logging.NdLogger;

public class BdCodeBlockJavaDeclare extends BdCodeBlockBaseField {

	/** コンストラクタ */
	public BdCodeBlockJavaDeclare(List<BdCodeLine> lineList){
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
			result = new BdCodeCaseJavaFieldJavaPrimitiveClass(lineListOfCase);
		} else if (BdUserAttribute.KEY_String.equals(key)){
			result = new BdCodeCaseJavaFieldString(lineListOfCase);
		} else if (BdUserAttribute.KEY_Date.equals(key)){
			result = new BdCodeCaseJavaFieldDate(lineListOfCase);
		} else if (BdUserAttribute.KEY_BigDecimal.equals(key)){
			result = new BdCodeCaseJavaFieldBigDecimal(lineListOfCase);
		} else if (BdUserAttribute.KEY_Boolean.equals(key)){
			result = new BdCodeCaseJavaFieldBoolean(lineListOfCase);
		} else if (BdUserAttribute.KEY_Enum.equals(key)){
			result = new BdCodeCaseJavaFieldEnum(lineListOfCase);
		} else if (BdUserAttribute.KEY_Embedded.equals(key)){
			result = new BdCodeCaseJavaFieldEmbedded(lineListOfCase);
		} else if (BdUserAttribute.KEY_ManyToOne.equals(key)){
			result = new BdCodeCaseJavaFieldManyToOne(lineListOfCase);
		} else if (BdUserAttribute.KEY_OneToMany.equals(key)){
			result = new BdCodeCaseJavaFieldOneToMany(lineListOfCase);
		} else if (BdUserAttribute.KEY_ManyToMany.equals(key)){
			result = new BdCodeCaseJavaFieldManyToMany(lineListOfCase);
		} else if (KEY_Nop.equals(key)){
		} else {
			key = (key == null || "".equals(key)) ? "CASEキーなし" : key;
			NdLogger.getInstance().error(this.getClass().getName() + " : [WARNING] Unknown Key = " + key, null);
		}
		return result;
	}
}
