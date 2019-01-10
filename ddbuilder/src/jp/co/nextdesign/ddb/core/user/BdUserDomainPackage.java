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

import jp.co.nextdesign.jcr.NdConstants;
import jp.co.nextdesign.jcr.model.core.NdPackage;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * domainパッケージ (例jp.co.abc.domain)
 * domainパッケージはパッケージを継承し、固有の振る舞いをもつ。
 * @author murayama
 *
 */
public class BdUserDomainPackage extends BdUserPackage {

	/**
	 * コンストラクタ
	 * @param ndPackage
	 * @param parentPackage
	 */
	public BdUserDomainPackage(NdPackage ndPackage, BdUserPackage parentPackage, BdUserProject userProject){
		super(ndPackage, parentPackage, userProject);
	}
	
	/**
	 * domainパッケージか否か
	 * @return
	 */
	@Override
	public boolean isDomainPackage() {
		return true;
	}
	
	@Override
	public void debugPrint(String tab){
		NdLogger.getInstance().debug(tab + "[DOMAIN PACKAGE INFO start]");
		NdLogger.getInstance().debug(tab + this.getAbsolutePath());
		for(BdUserClass c : this.getClassList()){
			c.debugPrint(tab + NdConstants.DEBUG_TAB);
		}
		for(BdUserPackage child : this.getChildPackageList()){
			child.debugPrint(tab + NdConstants.DEBUG_TAB);
		}
		NdLogger.getInstance().debug(tab + "[DOMAIN PACKAGE INFO end]");
	}
}
