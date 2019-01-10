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

import jp.co.nextdesign.jcr.model.core.NdMethodParameter;
import jp.co.nextdesign.jcr.model.typelink.NdAbstractTypeLink;
import jp.co.nextdesign.util.logging.BdDdbRuleCheckLogger;

/**
 * サービスメソッドの1つ1つのパラメター
 * @author murayama
 *
 */
public class BdUserServiceMethodParameter extends BdUserServiceMethodValueBase {
	
	private NdMethodParameter ndMethodParameter;

	/** コンストラクタ */
	public BdUserServiceMethodParameter(NdMethodParameter ndMethodParameter, BdUserServiceMethod bdServiceMethod){
		super(ndMethodParameter.getParameterType(), bdServiceMethod);
		this.ndMethodParameter = ndMethodParameter;
	}
	
	/** 戻り値型のルールチェックを行う */
	public void checkDdbRule(){
		if ("".equals(this.getDdbTypeKey())){
			BdDdbRuleCheckLogger.getInstance().add(createRuleCheckLog());
		} else if (this.isDdbEmbedded() || this.isDdbManyToOne()) {
			BdUserProject userProject = this.getBdUserServiceMethod().getBdServiceClass().getBdUserClass().getBdUserPackage().getUserProject();
			if ((userProject.getDomainClassClassifierByName(this.getSimpleOrQualifiedName()) == null) 
					&& (userProject.getDomainEnumClassifierByName(this.getSimpleOrQualifiedName()) == null)) {
				BdDdbRuleCheckLogger.getInstance().add(createRuleCheckLog());
			}
		} else if (this.isDdbManyToMany() || this.isDdbOneToMany()) {
			BdUserProject userProject = this.getBdUserServiceMethod().getBdServiceClass().getBdUserClass().getBdUserPackage().getUserProject();
			if ((userProject.getDomainClassClassifierByName(this.getCollectionElementTypeName()) == null) && (userProject.getDomainEnumClassifierByName(this.getCollectionElementTypeName()) == null)) {
				BdDdbRuleCheckLogger.getInstance().add(createRuleCheckLog());
			}
		}
	}

	//メソッド引数チェックエラー時のログ情報を作成する
	private String createRuleCheckLog(){
		//"サービスメソッド引数の未対応型："
		return "**NOT SUPPORTED TYPE : SERVICE METHOD PARAMETER**:" 
				+ this.getBdUserServiceMethod().getBdServiceClass().getName() + "->" + this.getBdUserServiceMethod().getName() + "->"
				+ this.getMethodParameterType().getDisplayName();
	}

	/** 引数タイプを応答する */
	public NdAbstractTypeLink getMethodParameterType(){
		return this.ndMethodParameter.getParameterType();
	}
	
	/** NdNdMethodParameterを応答する */
	public NdMethodParameter getNdMethodParameter(){
		return this.ndMethodParameter;
	}

	/** 引数名を応答する */
	@Override
	public String getName(){
		return this.ndMethodParameter != null ? this.ndMethodParameter.getName() : "";
	}
	
	/** 引数のタイトルを応答する */
	@Override
	public String getTitle(){
		return this.bdUserServiceMethod != null ? this.bdUserServiceMethod.getParameterTitle(this.getName()) : "";
	}
}
