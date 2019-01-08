/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.user;

import jp.co.nextdesign.jcr.model.core.NdMethodReturnValue;
import jp.co.nextdesign.util.logging.BdDdbRuleCheckLogger;

/**
 * サービスメソッドの戻り値
 * @author murayama
 *
 */
public class BdUserServiceMethodResult extends BdUserServiceMethodValueBase {

	private NdMethodReturnValue ndReturnValue;
	
	/** コンストラクタ */
	public BdUserServiceMethodResult(NdMethodReturnValue ndReturnValue, BdUserServiceMethod bdServiceMethod){
		super(ndReturnValue.getReturnValueType(), bdServiceMethod);
		this.ndReturnValue = ndReturnValue;
	}
	
	/** 戻り値型のルールチェックを行う */
	public void checkDdbRule(){
		if ("".equals(this.getDdbTypeKey())){
			BdDdbRuleCheckLogger.getInstance().add(createRuleCheckLog());
		} else if (this.isDdbEmbedded() || this.isDdbManyToOne()) {
			BdUserProject userProject = this.getBdUserServiceMethod().getBdServiceClass().getBdUserClass().getBdUserPackage().getUserProject();
			if ((userProject.getDomainClassClassifierByName(this.getSimpleOrQualifiedName()) == null) && (userProject.getDomainEnumClassifierByName(this.getSimpleOrQualifiedName()) == null)) {
				BdDdbRuleCheckLogger.getInstance().add(createRuleCheckLog());
			}
		} else if (this.isDdbManyToMany() || this.isDdbOneToMany()) {
			BdUserProject userProject = this.getBdUserServiceMethod().getBdServiceClass().getBdUserClass().getBdUserPackage().getUserProject();
			if ((userProject.getDomainClassClassifierByName(this.getCollectionElementTypeName()) == null) && (userProject.getDomainEnumClassifierByName(this.getCollectionElementTypeName()) == null)) {
				BdDdbRuleCheckLogger.getInstance().add(createRuleCheckLog());
			}
		}
	}

	//メソッド戻り値チェックエラー時のログ情報を作成する
	private String createRuleCheckLog(){
		//"サービスメソッド戻り型の未対応型："
		return "**NOT SUPPORTED TYPE : USER SERVICE METHOD RETURN VALUE**:" 
				+ this.getBdUserServiceMethod().getBdServiceClass().getName() + "->" + this.getBdUserServiceMethod().getName() + "->"
				+ this.getNdReturnValue().getReturnValueType().getDisplayName();
	}

	/** NdMethodReturnValueを応答する */
	public NdMethodReturnValue getNdReturnValue() {
		return ndReturnValue;
	}

	/** 戻り値のタイトルを応答する */
	@Override
	public String getTitle(){
		return this.bdUserServiceMethod != null ? this.bdUserServiceMethod.getReturnTitle() : "";
	}
	
	/** 型名を応答する（コレクションの場合は要素型名） */
	public String getSimpleOrCollectionElementTypeName(){
		String result = "";
		if(this.isDdbOneToMany()){
			result = this.getCollectionElementTypeName();
		} else {
			result = this.getSimpleOrQualifiedName();
		}
		return result;
	}
	
	/** 先頭小文字の型名を応答する（コレクションの場合は要素型名） */
	public String getLowerStartedSimpleOrCollectionElementTypeName(){
		String result = this.getSimpleOrCollectionElementTypeName();
		if (result.length() > 1){
			result = result.substring(0, 1).toLowerCase() + result.substring(1);
		}
		return result;
	}
}
