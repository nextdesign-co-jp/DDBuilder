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
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaImportBigDecimal;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaImportDate;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaImportDdb;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaImportEditPage;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaImportListPage;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaImportManager;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaImportMultipleSelectPanel;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaImportService;
import jp.co.nextdesign.ddb.core.template.cases.BdCodeCaseJavaImportSingleSelectPanel;
import jp.co.nextdesign.ddb.core.user.BdUserAttribute;
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethod;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodParameter;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodResult;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * コードブロック Import
 * @author murayama
 *
 */
public class BdCodeBlockJavaImport extends BdBaseCodeBlock {

	/** コンストラクタ */
	public BdCodeBlockJavaImport(List<BdCodeLine> lineList){
		super(lineList);
	}
	
	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(BdUserClass c) {
		List<String> resultList = new ArrayList<String>();
		resultList.addAll(this.generateForKey(KEY_Manager, c));
		resultList.addAll(this.generateForKey(KEY_Service, c));
		resultList.addAll(this.generateForKey(KEY_EditPage, c));
		resultList.addAll(this.generateForKey(KEY_ListPage, c));
		resultList.addAll(this.generateForKey(KEY_Ddb, c));
		resultList.addAll(this.generateForKey(KEY_Date, c));
		resultList.addAll(this.generateForKey(KEY_BigDecimal, c));
		return resultList;
	}
	
	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(BdUserAttribute a) {
		List<String> resultList = new ArrayList<String>();
		resultList.addAll(this.generateForKey(KEY_SingleSelectPanel, a));
		resultList.addAll(this.generateForKey(KEY_MultipleSelectPanel, a));
		return resultList;
	}

	/** 指定されたCaseKey分の生成コードを応答する */
	private List<String> generateForKey(String key, BdUserAttribute a){
		List<String> resultList = new ArrayList<String>();
		BdBaseCodeCase codeCase = this.getCodeCaseByKey(key);
		if (codeCase != null){
			resultList.addAll(codeCase.generate(a));
		} else {
			//このクラスは共用なので、「ない」ケースは常にありえるので警告ログは出さない NdLogger.getInstance().info(key + " **IS UNKNOWN CodeCase**.");
		}
		return resultList;
	}

	/** 指定されたCaseKey分の生成コードを応答する */
	private List<String> generateForKey(String key, BdUserClass c){
		List<String> resultList = new ArrayList<String>();
		BdBaseCodeCase codeCase = this.getCodeCaseByKey(key);
		if (codeCase != null){
			resultList.addAll(codeCase.generate(c));
		} else {
			//このクラスは共用なので、「ない」ケースは常にありえるので警告ログは出さない NdLogger.getInstance().info(key + " **IS UNKNOWN CodeCase**.");
		}
		return resultList;
	}
	
	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(BdUserServiceMethod m) {
		List<String> resultList = new ArrayList<String>();
		resultList.addAll(this.generateForKey(KEY_Manager, m));
		resultList.addAll(this.generateForKey(KEY_Service, m));
		resultList.addAll(this.generateForKey(KEY_EditPage, m));
		resultList.addAll(this.generateForKey(KEY_ListPage, m));
		resultList.addAll(this.generateForKey(KEY_Ddb, m));
		resultList.addAll(this.generateForKey(KEY_Date, m));
		resultList.addAll(this.generateForKey(KEY_BigDecimal, m));
		return resultList;
	}
	
	/** 指定されたCaseKey分の生成コードを応答する */
	private List<String> generateForKey(String key, BdUserServiceMethod m){
		List<String> resultList = new ArrayList<String>();
		BdBaseCodeCase codeCase = this.getCodeCaseByKey(key);
		if (codeCase != null){
			resultList.addAll(codeCase.generate(m));
		} else {
			//このクラスは共用なので、「ない」ケースは常にありえるので警告ログは出さない NdLogger.getInstance().info(key + " **IS UNKNOWN CodeCase**.");
		}
		return resultList;
	}
	
	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(BdUserServiceMethodParameter p) {
		List<String> resultList = new ArrayList<String>();
		resultList.addAll(this.generateForKey(KEY_SingleSelectPanel, p));
		resultList.addAll(this.generateForKey(KEY_MultipleSelectPanel, p));
		return resultList;
	}

	/** 指定されたCaseKey分の生成コードを応答する */
	private List<String> generateForKey(String key, BdUserServiceMethodParameter p){
		List<String> resultList = new ArrayList<String>();
		BdBaseCodeCase codeCase = this.getCodeCaseByKey(key);
		if (codeCase != null){
			resultList.addAll(codeCase.generate(p));
		} else {
			//このクラスは共用なので、「ない」ケースは常にありえるので警告ログは出さない NdLogger.getInstance().info(key + " **IS UNKNOWN CodeCase**.");
		}
		return resultList;
	}

	/**
	 * ソースを生成する
	 */
	@Override
	public List<String> generate(BdUserServiceMethodResult r) {
		List<String> resultList = new ArrayList<String>();
		resultList.addAll(this.generateForKey(KEY_EditPage, r));
		resultList.addAll(this.generateForKey(KEY_Ddb, r));
		resultList.addAll(this.generateForKey(KEY_Date, r));
		resultList.addAll(this.generateForKey(KEY_BigDecimal, r));
		return resultList;
	}

	/** 指定されたCaseKey分の生成コードを応答する */
	private List<String> generateForKey(String key, BdUserServiceMethodResult r){
		List<String> resultList = new ArrayList<String>();
		BdBaseCodeCase codeCase = this.getCodeCaseByKey(key);
		if (codeCase != null){
			resultList.addAll(codeCase.generate(r));
		} else {
			//このクラスは共用なので、「ない」ケースは常にありえるので警告ログは出さない NdLogger.getInstance().info(key + " **IS UNKNOWN CodeCase**.");
		}
		return resultList;
	}

	private static final String KEY_Manager = "Manager";
	private static final String KEY_Service = "Service";
	private static final String KEY_EditPage = "EditPage";
	private static final String KEY_SingleSelectPanel = "SingleSelectPanel";
	private static final String KEY_MultipleSelectPanel = "MultipleSelectPanel";
	private static final String KEY_ListPage = "ListPage";
	private static final String KEY_Ddb = "Ddb";
	private static final String KEY_Date = "Date";
	private static final String KEY_BigDecimal = "BigDecimal";
	
	/** 
	 * 各サブクラスでOverrideする
	 */
	@Override
	protected BdBaseCodeCase createCodeCase(String key, List<BdCodeLine> lineListOfCase){
		BdBaseCodeCase result = null;
		if (KEY_Manager.equals(key)){
			result = new BdCodeCaseJavaImportManager(lineListOfCase);
		} else if (KEY_Service.equals(key)){
			result = new BdCodeCaseJavaImportService(lineListOfCase);
		} else if (KEY_EditPage.equals(key)){
			result = new BdCodeCaseJavaImportEditPage(lineListOfCase);
		} else if (KEY_SingleSelectPanel.equals(key)){
			result = new BdCodeCaseJavaImportSingleSelectPanel(lineListOfCase);
		} else if (KEY_MultipleSelectPanel.equals(key)){
			result = new BdCodeCaseJavaImportMultipleSelectPanel(lineListOfCase);
		} else if (KEY_ListPage.equals(key)){
			result = new BdCodeCaseJavaImportListPage(lineListOfCase);
		} else if (KEY_Ddb.equals(key)){
			result = new BdCodeCaseJavaImportDdb(lineListOfCase);
		} else if (KEY_Date.equals(key)){
			result = new BdCodeCaseJavaImportDate(lineListOfCase);
		} else if (KEY_BigDecimal.equals(key)){
			result = new BdCodeCaseJavaImportBigDecimal(lineListOfCase);
		} else if (KEY_Nop.equals(key)){
		} else {
			key = (key == null || "".equals(key)) ? "CASEキーなし" : key;
			NdLogger.getInstance().error(this.getClass().getName() + " : [WARNING] Unknown Key = " + key, null);
		}
		return result;
	}
}
