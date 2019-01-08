/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
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
