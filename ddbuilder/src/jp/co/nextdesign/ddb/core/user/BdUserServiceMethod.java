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
package jp.co.nextdesign.ddb.core.user;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.jcr.model.core.NdMethod;
import jp.co.nextdesign.jcr.model.core.NdMethodParameter;
import jp.co.nextdesign.jcr.model.core.NdMethodReturnValue;
import jp.co.nextdesign.util.logging.BdDdbRuleCheckLogger;

/**
 * ユーザ定義のサービスメソッド
 * @author murayama
 *
 */
public class BdUserServiceMethod  extends BdUserAbstractElement {

	private BdUserMethod bdUserMethod;
	private BdUserServiceClass bdServiceClass;
	private BdUserServiceMethodResult bdServiceMethodResult;
	private List<BdUserServiceMethodParameter> serviceMethodParameterList;
	
	/**
	 * コンストラクタ
	 */
	public BdUserServiceMethod(BdUserMethod bdUserMethod, BdUserServiceClass bdServiceClass){
		super();
		this.bdUserMethod = bdUserMethod;
		this.bdServiceClass = bdServiceClass;
		this.bdServiceMethodResult = new BdUserServiceMethodResult(this.bdUserMethod.getNdMethod().getReturnValue(), this);
		this.buildMethodParameterList();
	}
	
	/** 引数と戻り値型のルールチェックを行う */
	public void checkDdbRule(){
		for(BdUserServiceMethodParameter p : this.getServiceMethodParameterList()){
			p.checkDdbRule();
		}
		if (! this.isReturnVoid()) {
			this.bdServiceMethodResult.checkDdbRule();
		}
	}

	/** BdUserMethodを応答する */
	public BdUserMethod getBdUserMethod() {
		return bdUserMethod;
	}

	/** 戻り値を応答する */
	public BdUserServiceMethodResult getBdServiceMethodResult() {
		return bdServiceMethodResult;
	}

	/** NdMethodを応答する */
	public NdMethod getNdMethod(){
		return this.bdUserMethod.getNdMethod();
	}
	
	/** 引数リストを応答する */
	public List<BdUserServiceMethodParameter> getServiceMethodParameterList(){
		return this.serviceMethodParameterList;
	}
	
	/** サービスメソッドの引数リストを作成する */
	private void buildMethodParameterList(){
		this.serviceMethodParameterList = new ArrayList<BdUserServiceMethodParameter>();
		for(NdMethodParameter ndMethodParameter : this.bdUserMethod.getNdMethod().getParameterList()){
			this.serviceMethodParameterList.add(new BdUserServiceMethodParameter(ndMethodParameter, this));
		}
	}
	
	private static final String DELIMITER = ", ";
	/** 引数リストの表示文字列形を応答する */
	public String getMethodParametersDisplayName(){
		String result = "(";
		for(NdMethodParameter param : this.bdUserMethod.getNdMethod().getParameterList()){
			result += param.getParameterType().getDisplayName() + DELIMITER;
		}
		if (result.endsWith(DELIMITER)){
			result = result.substring(0, result.length() - DELIMITER.length());
		}
		return result + ")";
	}
	
	/** 戻り値がvoidまたはVoidか否か */
	public boolean isReturnVoid(){
		String returnValueDisplayName = this.getReturnValueDisplayName();
		return "void".equals(returnValueDisplayName) || "Void".equals(returnValueDisplayName);
	}
	
	/** 戻り型を応答する */
	public NdMethodReturnValue getNdReturnValue(){
		return this.bdUserMethod.getNdMethod().getReturnValue();
	}
	
	/** 戻り型の表示名を応答する */
	public String getReturnValueDisplayName(){
		return this.bdUserMethod.getNdMethod().getReturnValue().getReturnValueType().getDisplayName();
	}
	
	/**
	 * メソッド名を応答する
	 * @return
	 */
	public String getName(){
		return this.bdUserMethod.getNdMethod().getName();
	}

	/** 先頭大文字のメソッド名を応答する */
	public String getUpperStartedName(){
		String result = this.getName();
		if (result.length() > 1){
			result = result.substring(0, 1).toUpperCase() + result.substring(1);
		}
		return result;
	}
	
	/**
	 * Javadocからタイトルを応答する
	 * @return
	 */
	public String getJavadocTitle(){
		return formatJavadoc(this.bdUserMethod.getNdMethod().getJavadoc());
	}

	/** BdServiceClassを応答する */
	public BdUserServiceClass getBdServiceClass() {
		return bdServiceClass;
	}
	
	/** メソッド宣言行を応答する */
	public String getHeadLine(){
		return this.bdUserMethod.getNdMethod().getHeadLine();
	}

	/** 引数のタイトルを応答する */
	public String getParameterTitle(String paramName){
		return this.bdUserMethod != null ? this.bdUserMethod.getParameterTitle(paramName) : "";
	}
	
	/** 戻り値のタイトルを応答する */
	public String getReturnTitle(){
		return this.bdUserMethod != null ? this.bdUserMethod.getReturnTitle() : "";
	}

	/** DDBKeyに一致する引数リストを応答する */
	public List<BdUserServiceMethodParameter> getParameterListByDdbTypeKey(String ddbTypeKey){
		List<BdUserServiceMethodParameter> resultList = new ArrayList<BdUserServiceMethodParameter>();
		if (ddbTypeKey != null){
			for(BdUserServiceMethodParameter p : this.getServiceMethodParameterList()){
				if (ddbTypeKey.equals(p.getDdbTypeKey())){
					resultList.add(p);
				}
			}
		}
		return resultList;
	}

}
