/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.user;

import jp.co.nextdesign.jcr.NdConstants;
import jp.co.nextdesign.jcr.model.core.NdPackage;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * serviceパッケージ (例jp.co.abc.service)
 * serviceパッケージはパッケージを継承し、固有の振る舞いをもつ。
 * @author murayama
 *
 */
public class BdUserServicePackage extends BdUserPackage {

	/**
	 * コンストラクタ
	 * @param ndPackage
	 * @param parentPackage
	 */
	public BdUserServicePackage(NdPackage ndPackage, BdUserPackage parentPackage, BdUserProject userProject){
		super(ndPackage, parentPackage, userProject);
	}
	
	/**
	 * serviceパッケージか否か
	 * @return
	 */
	@Override
	public boolean isServicePackage() {
		return true;
	}
	
	@Override
	public void debugPrint(String tab){
		NdLogger.getInstance().debug(tab + "[SERVICE PACKAGE INFO start]");
		NdLogger.getInstance().debug(tab + this.getAbsolutePath());
		for(BdUserClass c : this.getClassList()){
			c.debugPrint(tab + NdConstants.DEBUG_TAB);
		}
		for(BdUserPackage child : this.getChildPackageList()){
			child.debugPrint(tab + NdConstants.DEBUG_TAB);
		}
		NdLogger.getInstance().debug(tab + "[SERVICE PACKAGE INFO end]");
	}
}
