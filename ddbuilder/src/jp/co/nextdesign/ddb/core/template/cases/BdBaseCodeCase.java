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
package jp.co.nextdesign.ddb.core.template.cases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.nextdesign.ddb.core.template.BdCodeLine;
import jp.co.nextdesign.ddb.core.user.BdUserAttribute;
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethod;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodParameter;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodResult;
import jp.co.nextdesign.jcr.NdConstants;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * CASEの基底クラス
 * @author murayama
 *
 */
public abstract class BdBaseCodeCase {

	public static final String KEY_All = "All";
	private List<BdCodeLine> codeLineList ;
	private String key;
	
	/**
	 * コンストラクタ
	 * @param lineListOfCase
	 */
	public BdBaseCodeCase(List<BdCodeLine> lineListOfCase){
		super();
		this.codeLineList = new ArrayList<BdCodeLine>(lineListOfCase);
	}
	
	/** クラスに対応した生成コードを応答する */
	public List<String> generate(BdUserClass c){
		return new ArrayList<String>(Arrays.asList("generate(c):未実装:" + this.getClass().getSimpleName()));
	}

	/** 属性に対応した生成コードを応答する */
	public List<String> generate(BdUserAttribute a){
		return new ArrayList<String>(Arrays.asList("generate(a):未実装:" + this.getClass().getSimpleName()));
	}
	
	/** サービスメソッドに対応した生成コードを応答する */
	public List<String> generate(BdUserServiceMethod m){
		return new ArrayList<String>(Arrays.asList("generate(m):未実装:" + this.getClass().getSimpleName()));
	}

	/** サービスメソッド引数に対応した生成コードを応答する */
	public List<String> generate(BdUserServiceMethodParameter p){
		return new ArrayList<String>(Arrays.asList("generate(p):未実装:" + this.getClass().getSimpleName()));
	}

	/** サービスメソッド戻り値に対応した生成コードを応答する */
	public List<String> generate(BdUserServiceMethodResult r){
		return new ArrayList<String>(Arrays.asList("generate(r):未実装:" + this.getClass().getSimpleName()));
	}

	public List<BdCodeLine> getCodeLineList(){
		return this.codeLineList;
	}
	
	/**
	 * デバッグ
	 * @param tab
	 */
	public void debugPrint(String tab){
		String line = tab + this.getClass().getSimpleName() + " key=" + key;
		NdLogger.getInstance().debug(line);
		for(BdCodeLine codeLine : this.getCodeLineList()){
			codeLine.debugPrint(tab + NdConstants.DEBUG_TAB);
		}
	}
}
