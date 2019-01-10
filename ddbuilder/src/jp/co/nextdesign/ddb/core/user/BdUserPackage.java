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

import jp.co.nextdesign.ddb.ui.main.BdDdbRuleCheckException;
import jp.co.nextdesign.jcr.NdConstants;
import jp.co.nextdesign.jcr.model.core.NdAbstractClassifier;
import jp.co.nextdesign.jcr.model.core.NdClass;
import jp.co.nextdesign.jcr.model.core.NdCompilationUnit;
import jp.co.nextdesign.jcr.model.core.NdEnum;
import jp.co.nextdesign.jcr.model.core.NdInterface;
import jp.co.nextdesign.jcr.model.core.NdPackage;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * ユーザプロジェクトのパッケージ
 * @author murayama
 *
 */
public class BdUserPackage extends BdUserAbstractElement {

	private NdPackage ndPackage;
	private BdUserPackage parentPackage;
	private List<BdUserPackage> childPackageList;
	private List<BdUserClass> classList;
	protected BdUserProject userProject;
	private List<NdEnum> enumList;
	private List<NdInterface> interfaceList;
		
	//ルートパッケージか否か
	public boolean isRootPackage(){
		return false;
	}
	
	/**
	 * コンストラクタ
	 * @param userEntityModelPackage
	 */
	public BdUserPackage(NdPackage ndPackage, BdUserPackage parentPackage, BdUserProject userProject){
		super();
		this.ndPackage = ndPackage;
		this.parentPackage = parentPackage;
		this.userProject = userProject;
		this.childPackageList = new ArrayList<BdUserPackage>();
		this.classList = new ArrayList<BdUserClass>();
		this.enumList = new ArrayList<NdEnum>();
		this.interfaceList = new ArrayList<NdInterface>();
	}

	/**
	 * 処理対象のサービスクラスリストを応答する
	 * @return
	 */
	public List<BdUserClass> getAppendedServiceClassList(){
		List<BdUserClass> resultList = new ArrayList<BdUserClass>();
		for(BdUserClass c : this.classList){
			if (c.isAppendedServiceClass()){
				resultList.add(c);
			}
		}
		for(BdUserPackage child : this.childPackageList){
			resultList.addAll(child.getAppendedServiceClassList());
		}
		return resultList;
	}

	/**
	 * 処理対象(@Entiry付きのドメインクラス)のリストを応答する
	 * @return
	 */
	public List<BdUserClass> getEntityAnnotatedDomainClassList(){
		List<BdUserClass> resultList = new ArrayList<BdUserClass>();
		for(BdUserClass c : this.classList){
			if (c.isEntityAnnotatedDomainClass()){
				resultList.add(c);
			}
		}
		for(BdUserPackage child : this.childPackageList){
			resultList.addAll(child.getEntityAnnotatedDomainClassList());
		}
		return resultList;
	}

	/**
	 * JCR要素をラップする
	 */
	public void importNdProject() throws BdDdbRuleCheckException {
		for(NdPackage child : this.ndPackage.getChildPackageList()){
			BdUserPackage pkg = null;
			if (userProject.isDomainPackageFullPath(child.getDirectory().getAbsolutePath())){
				pkg = new BdUserDomainPackage(child, this, userProject);
			} else if(userProject.isServicePackageFullPath(child.getDirectory().getAbsolutePath())){
				pkg = new BdUserServicePackage(child, this, userProject);
			} else {
				pkg = new BdUserPackage(child, this, userProject);
			}
			this.childPackageList.add(pkg);
			pkg.importNdProject();
		}
		for(NdCompilationUnit unit : ndPackage.getCompilationUnitList()){
			for(NdAbstractClassifier classifier : unit.getClassifierList()){
				if (classifier instanceof NdClass){
					BdUserClass bdClass = new BdUserClass((NdClass)classifier, this);
					this.classList.add(bdClass);
					bdClass.checkDdbRule(); //例外送出も検討する
				} else if (classifier instanceof NdEnum){
					this.enumList.add((NdEnum)classifier);
				} else if (classifier instanceof NdInterface){
					this.interfaceList.add((NdInterface)classifier);
				}
			}
		}
	}
	
	/**
	 * 自分の配下のすべてのパッケージを応答する
	 * @return
	 */
	public List<BdUserPackage> getAllPackageList(){
		List<BdUserPackage> resultList = new ArrayList<BdUserPackage>();
		resultList.addAll(this.getChildPackageList());
		for(BdUserPackage pkg : this.getChildPackageList()){
			resultList.addAll(pkg.getAllPackageList());
		}
		return resultList;
	}
	
	/**
	 * domainパッケージか否か
	 * @return
	 */
	public boolean isDomainPackage() {
		return false;
	}

	/**
	 * serviceパッケージか否か
	 * @return
	 */
	public boolean isServicePackage() {
		return false;
	}

	/**
	 * ユーザプロジェクトを応答する
	 * @return
	 */
	public BdUserProject getUserProject(){
		if (this.isRootPackage()){
			return this.userProject;
		}else{
			return this.parentPackage.getUserProject();
		}
	}
	
	/**
	 * domain配下のパッケージか否か
	 * @return
	 */
	public boolean isInDomain(){
		if (isDomainPackage()){
			return true;
		}else if (isRootPackage()){
			return false;
		}else {
			return this.parentPackage.isInDomain();
		}
	}
	
	/**
	 * service配下のパッケージか否か
	 * @return
	 */
	public boolean isInService(){
		if (isServicePackage()){
			return true;
		}else if (isRootPackage()){
			return false;
		}else {
			return this.parentPackage.isInService();
		}
	}

	/**
	 * 子パッケージリストを応答する
	 * @return
	 */
	public List<BdUserPackage> getChildPackageList(){
		return this.childPackageList;
	}
		
	/**
	 * 親パッケージを応答する
	 * @return
	 */
	public BdUserPackage getParentPackage(){
		return this.parentPackage;
	}
	
	public List<BdUserClass> getClassList(){
		return this.classList;
	}
	
	/** このパッケージに含まれるすべてのクラスリストを応答する */
	public List<BdUserClass> getAllClassListUnderThisPackage(){
		List<BdUserClass> resultList = new ArrayList<BdUserClass>();
		for(BdUserClass c : this.classList){
			if (!c.isDdbFixedClass()){
				resultList.add(c);
			}
		}
		for(BdUserPackage child : this.childPackageList){
			resultList.addAll(child.getAllClassListUnderThisPackage());
		}
		return resultList;
	}

	/** このパッケージに含まれるすべてのEnumリストを応答する */
	public List<NdEnum> getAllEnumListUnderThisPackage(){
		List<NdEnum> resultList = new ArrayList<NdEnum>();
		for(NdEnum enm : this.enumList){
			if (!isDdbFixedPackageName(enm.getPackageName())){
				resultList.add(enm);
			}
		}
		for(BdUserPackage child : this.childPackageList){
			resultList.addAll(child.getAllEnumListUnderThisPackage());
		}
		return resultList;
	}

	/** このパッケージに含まれるすべてのInterfaceリストを応答する */
	public List<NdInterface> getAllInterfaceListUnderThisPackage(){
		List<NdInterface> resultList = new ArrayList<NdInterface>();
		for(NdInterface enm : this.interfaceList){
			if (!isDdbFixedPackageName(enm.getPackageName())){
				resultList.add(enm);
			}
		}
		for(BdUserPackage child : this.childPackageList){
			resultList.addAll(child.getAllInterfaceListUnderThisPackage());
		}
		return resultList;
	}

	/** パッケージ名がddbであるか否か */
	private boolean isDdbFixedPackageName(String packageName){
		return packageName != null && packageName.endsWith("ddb");
	}
	
	/**
	 * 絶対パス名を応答する
	 * @return
	 */
	public String getAbsolutePath(){
		return this.ndPackage.getDirectory().getAbsolutePath();
	}
	
	/**
	 * デバッグ
	 */
	public void debugPrint(String tab){
		NdLogger.getInstance().debug(tab + "PACKAGE NAME : " + this.ndPackage.getFullPackageName()); // "パッケージ名："
		for(BdUserClass bdClass : this.getClassList()){
			bdClass.debugPrint(tab + NdConstants.DEBUG_TAB);
		}
		for(BdUserPackage child : this.getChildPackageList()){
			child.debugPrint(tab + NdConstants.DEBUG_TAB);
		}
	}
}
