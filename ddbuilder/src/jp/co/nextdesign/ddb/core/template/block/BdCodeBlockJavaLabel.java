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

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.ddb.core.template.BdCodeLine;
import jp.co.nextdesign.ddb.core.template.cases.BdBaseCodeCase;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaLabelEntityTitle;
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
public class BdCodeBlockJavaLabel extends BdBaseCodeBlock {

	/** コンストラクタ */
	public BdCodeBlockJavaLabel(List<BdCodeLine> lineList){
		super(lineList);
	}
	
	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(BdUserClass c) {
		List<String> resultList = new ArrayList<String>();
		BdBaseCodeCase codeCase = this.getCodeCaseByKey(KEY_EntityTitle);
		if (codeCase != null){
			List<BdUserAttribute> aList = new ArrayList<BdUserAttribute>(c.getAttributeListByDdbTypeKey(BdUserAttribute.KEY_OneToMany));
			aList.addAll(c.getAttributeListByDdbTypeKey(BdUserAttribute.KEY_ManyToMany));
			for(BdUserAttribute a : aList){
				resultList.addAll(codeCase.generate(a));
			}
		} else {
			NdLogger.getInstance().error(KEY_EntityTitle + " **IS UNKNOWN CodeCase**.", null);
		}
		return resultList;
	}

	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(BdUserServiceMethod m) {
		List<String> resultList = new ArrayList<String>();
		BdBaseCodeCase codeCase = this.getCodeCaseByKey(KEY_EntityTitle);
		if (codeCase != null){
			List<BdUserServiceMethodParameter> pList = new ArrayList<BdUserServiceMethodParameter>(m.getParameterListByDdbTypeKey(BdUserAttribute.KEY_OneToMany));
			pList.addAll(m.getParameterListByDdbTypeKey(BdUserAttribute.KEY_ManyToMany));
			for(BdUserServiceMethodParameter p : pList){
				resultList.addAll(codeCase.generate(p));
			}
		} else {
			NdLogger.getInstance().error(KEY_EntityTitle + " **IS UNKNOWN CodeCase**.", null);
		}
		return resultList;
	}

	private static final String KEY_EntityTitle = "EntityTitle";
	
	/** 
	 * 各サブクラスでOverrideする
	 */
	@Override
	protected BdBaseCodeCase createCodeCase(String key, List<BdCodeLine> lineListOfCase){
		BdBaseCodeCase result = null;
		if (KEY_EntityTitle.equals(key)){
			result = new BdCodeCaseJavaLabelEntityTitle(lineListOfCase);
		} else if (KEY_Nop.equals(key)){
		} else {
			key = (key == null || "".equals(key)) ? "CASEキーなし" : key;
			NdLogger.getInstance().error(this.getClass().getName() + " : [WARNING] Unknown Key = " + key, null);
		}
		return result;
	}
}
