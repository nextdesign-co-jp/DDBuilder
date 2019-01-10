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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import jp.co.nextdesign.ddb.core.BdBuilderException;
import jp.co.nextdesign.ddb.core.template.BdTemplateProject;
import jp.co.nextdesign.ddb.ui.main.BdDdbRuleCheckException;
import jp.co.nextdesign.jcr.NdApplicationPropertyException;
import jp.co.nextdesign.jcr.model.core.NdAbstractClassifier;
import jp.co.nextdesign.jcr.model.core.NdEnum;
import jp.co.nextdesign.jcr.model.core.NdInterface;
import jp.co.nextdesign.jcr.model.core.NdModelException;
import jp.co.nextdesign.jcr.model.core.NdProject;
import jp.co.nextdesign.jcr.model.core.NdProjectManager;
import jp.co.nextdesign.jcr.parser.NdParserException;
import jp.co.nextdesign.util.BdSqlReservedWord;
import jp.co.nextdesign.util.NdUtilException;
import jp.co.nextdesign.util.logging.BdDdbRuleCheckLogger;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * ユーザプロジェクト
 * @author murayama
 *
 */
public class BdUserProject {

	private File rootFolder;
	private String artifactId;
	private String groupId;
	private NdProject ndProject; //JCR NdProject
	private BdUserRootPackage rootPackage;
	
	private BdUserDomainPackage domainPackage;
	private String domainPackageFullPath = "";
	private List<BdUserClass> entityAnnotatedDomainClassList;
	private List<BdUserClass> allDomainClassList;
	private List<NdEnum> allDomainEnumList;
	private List<NdInterface> allDomainInterfaceList;
	
	private BdUserServicePackage servicePackage;
	private String servicePackageFullPath = "";
	private List<BdUserClass> appendedServiceClassList;
	private List<BdUserClass> allServiceClassList;


	/**
	 * コンストラクタ
	 * BdBuildTaskが使用する。NdProjectを生成する前に使用する。
	 * @param rootFolder
	 * @param artifactId
	 * @param groupId
	 */
	public BdUserProject(File rootFolder, String artifactId, String groupId){
		this.rootFolder = rootFolder;
		this.artifactId = artifactId;
		this.groupId = groupId;
		this.domainPackageFullPath = this.getDomainPackageFullPath();
		this.servicePackageFullPath = this.getServicePackageFullPath();
	}

	/** ドメインモデルが全て揃ってからでなければできないチェックを行う */
	public void checkDdbRule(){
		//全ドメインクラスの全属性の型をチェック
		for (BdUserClass bdUserClass : this.getAllDomainClassList()){
			for(BdUserAttribute a : bdUserClass.getAttributeList()){
				if ("".equals(a.getDdbTypeKey())){
					BdDdbRuleCheckLogger.getInstance().add("**NOT SUPPRTED DOMAIN CLASS ATTRIBUTE TYPE2** : " + a.getNdAttributeType().getDisplayName()); //"ドメインクラス属性の未対応型2：" 
				} else if (a.isDdbEmbedded() || a.isDdbManyToOne()) {
					if (a.getAttributeTypeBdUserClass() == null){
						BdDdbRuleCheckLogger.getInstance().add("**NOT SUPPRTED DOMAIN CLASS ATTRIBUTE TYPE2** : " + bdUserClass.getName() +"->" + a.getNdAttributeType().getDisplayName()); //"ドメインクラス属性の未対応型2：" 
					}
				} else if (a.isDdbManyToMany() || a.isDdbOneToMany()) {
					if (a.getCollectionElementBdUserClass() == null){
						BdDdbRuleCheckLogger.getInstance().add("**NOT SUPPRTED DOMAIN CLASS ATTRIBUTE TYPE2** : " + bdUserClass.getName() +"->" + a.getNdAttributeType().getDisplayName()); //"ドメインクラス属性の未対応型2：" 
					}
				}
			}
		}
	}

	/** groupId */
	public String getGroupId(){
		return this.groupId != null ? this.groupId : "";
	}
	
	/** ユーザプロジェクト内の全てのサービスメソッドを応答する */
	public List<BdUserServiceMethod> getServiceMethodList(){
		List<BdUserServiceMethod> resultList = new ArrayList<BdUserServiceMethod>();
		for(BdUserClass bdUserClass : this.getAppendedServiceClassList()){	
			BdUserServiceClass bdUserServiceClass = new BdUserServiceClass(bdUserClass);
			resultList.addAll(bdUserServiceClass.getServiceMethodList());
		}
		return resultList;
	}

	/**
	 * 処理対象のドメインクラスの全リストを応答する
	 * @return
	 */
	public List<BdUserClass> getAppendedServiceClassList(){
		if (this.appendedServiceClassList == null){
			this.appendedServiceClassList = this.servicePackage.getAppendedServiceClassList();
		}
		return this.appendedServiceClassList;
	}

	/** サービスパッケージ配下のすべてのクラスリストを応答する */
	public List<BdUserClass> getAllServiceClassList(){
		if (this.allServiceClassList == null){
			this.allServiceClassList = this.servicePackage.getAllClassListUnderThisPackage();
		}
		return this.allServiceClassList;
	}

	/**
	 * 処理対象のドメインクラスの全リストを応答する
	 * @return
	 */
	public List<BdUserClass> getEntityAnnotatedDomainClassList(){
		if (this.entityAnnotatedDomainClassList == null){
			this.entityAnnotatedDomainClassList = this.domainPackage.getEntityAnnotatedDomainClassList();
		}
		return this.entityAnnotatedDomainClassList;
	}

	/** ドメインパッケージ配下のすべてのクラスリストを応答する */
	public List<BdUserClass> getAllDomainClassList(){
		if (this.allDomainClassList == null){
			this.allDomainClassList = this.domainPackage.getAllClassListUnderThisPackage();
		}
		return this.allDomainClassList;
	}

	/** ドメインパッケージ配下のすべてのEnumリストを応答する */
	public List<NdEnum> getAllDomainEnumList(){
		if (this.allDomainEnumList == null){
			this.allDomainEnumList = this.domainPackage.getAllEnumListUnderThisPackage();
		}
		return this.allDomainEnumList;
	}

	/** ドメインパッケージ配下のすべてのInterfaceリストを応答する */
	public List<NdInterface> getAllDomainInterfaceList(){
		if (this.allDomainInterfaceList == null){
			this.allDomainInterfaceList = this.domainPackage.getAllInterfaceListUnderThisPackage();
		}
		return this.allDomainInterfaceList;
	}

	/**
	 * クラス名でドメインモデルを検索し応答する
	 * @param name
	 * @return
	 */
	public BdUserClass getEntityAnnotatedDomainClassByName(String name){
		BdUserClass result = null;
		if (name != null ){
			boolean found = false;
			for(BdUserClass bdUserClass : getEntityAnnotatedDomainClassList()){
				if(name.equals(bdUserClass.getName())){
					result = bdUserClass;
					if (!found){
						found = true;
					} else {
						NdLogger.getInstance().error("DDBuilderでは、ドメインモデルに（パッケージが違っていても）同じ名前のクラスは定義できません。" + name, null);
					}
				}
			}
		}
		return result;
	}

	//2016.5.22時点で、このメソッド未使用だが、使用する可能性があるので残しておく。
	//このメソッドはEditPageのimport文生成ために必要（参照先のパッケージ名取得に必要）と考えたが
	//BdCodeCaseImportEditPage#generateで解決できていたので、未使用となっている。
	/** @Entityアノテーションなしのクラスも含めて名前で検索し応答する */
	public NdAbstractClassifier getDomainClassClassifierByName(String name){
		NdAbstractClassifier result = null;
		if (name != null){
			boolean found = false;
			for(BdUserClass bdUserClass : getAllDomainClassList()){
				if(name.equals(bdUserClass.getName())){
					result = (NdAbstractClassifier)bdUserClass.getNdClass();
					if (!found){
						found = true;
					} else {
						NdLogger.getInstance().error("DDBuilderでは、ドメインモデルに（パッケージが違っていても）同じ名前のクラスは定義できません。" + name, null);
					}
				}
			}
		}
		return result;
	}

	//2016.5.22時点で、このメソッド未使用だが、getDomainClassClassifierByNameと同様の経緯で残しておく。
	/** 列挙型を名前で検索し応答する */
	public NdAbstractClassifier getDomainEnumClassifierByName(String name){
		NdAbstractClassifier result = null;
		if (name != null){
			boolean found = false;
			for(NdAbstractClassifier enumClassifier : getAllDomainEnumList()){
				if(name.equals(enumClassifier.getName())){
					result = enumClassifier;
					if (!found){
						found = true;
					} else {
						NdLogger.getInstance().error("DDBuilderでは、ドメインモデルに（パッケージが違っていても）同じ名前のEnumは定義できません。" + name, null);
					}
				}
			}
		}
		return result;
	}

	//2016.5.22時点で、このメソッド未使用だが、getDomainClassClassifierByNameと同様の経緯で残しておく。
	/** インタフェースを名前で検索し応答する */
	public NdAbstractClassifier getDomainInterfaceClassifierByName(String name){
		NdAbstractClassifier result = null;
		if (name != null){
			boolean found = false;
			for(NdAbstractClassifier interfaceClassifier : getAllDomainInterfaceList()){
				if(name.equals(interfaceClassifier.getName())){
					result = interfaceClassifier;
					if (!found){
						found = true;
					} else {
						NdLogger.getInstance().error("DDBuilderでは、ドメインモデルに（パッケージが違っていても）同じ名前のインタフェースは定義できません。" + name, null);
					}
				}
			}
		}
		return result;
	}

	/**
	 * プロジェクトルートのパスを応答する 
	 * @return
	 */
	public String getProjectRootFullPath(){
		return 	this.rootFolder.getAbsolutePath() + File.separator + this.artifactId;
	}

	/**
	 * src/main/javaのjavaのフルパスを応答する
	 * @return
	 */
	public String getSourceDirectoryFullPath(){
		return this.getProjectRootFullPath()
				+ File.separator + BdTemplateProject.MAVEN_STANDARD_DIRECTORY_LAYOUT.replaceAll("\\.",  Matcher.quoteReplacement(File.separator));
	}

	/**
	 * ルートパッケージ(例:jp.co.abcのjp)のSystem.io.Fileを応答する
	 * (JCRを使用しないで取得するメソッド)
	 * @return
	 * @throws BdBuilderException 
	 */
	private  File getRootPackageDirectory() throws BdBuilderException{
		File result = null;
		String rootPackageName = "";
		String[] splitted = this.groupId.split("\\.");
		if (splitted != null && splitted.length > 0){
			rootPackageName =  splitted[0];
			String path = this.getSourceDirectoryFullPath() 	+ File.separator + rootPackageName;
			File check = new File(path);
			if (check.exists()){
				result = check;
			}
		}
		if (result == null){
			throw new BdBuilderException("NOT FOUND : RootPackageDirectory");
		}
		return result;
	}

	/**
	 * domainパッケージのディレクトリパスを応答する
	 * @return
	 */
	private String getDomainPackageFullPath(){
		return this.getSourceDirectoryFullPath()
				+ File.separator + this.groupId.replaceAll("\\.", Matcher.quoteReplacement(File.separator))
				+ File.separator + BdTemplateProject.DOMAIN_PACKAGE_NAME;
	}

	/**
	 * domainパッケージのパス名か否か 
	 * BdPackage, BdDomainPackageを生成する段階で使用する
	 * @param path
	 * @return
	 */
	protected boolean isDomainPackageFullPath(String path){
		return domainPackageFullPath.equals(path);
	}

	/**
	 * serviceパッケージのディレクトリパスを応答する
	 * @return
	 */
	private String getServicePackageFullPath(){
		return this.getSourceDirectoryFullPath()
				+ File.separator + this.groupId.replaceAll("\\.", Matcher.quoteReplacement(File.separator))
				+ File.separator + BdTemplateProject.SERVICE_PACKAGE_NAME;
	}

	/**
	 * serviceパッケージのパス名か否か 
	 * BdPackage, BdServicePackageを生成する段階で使用する
	 * @param path
	 * @return
	 */
	protected boolean isServicePackageFullPath(String path){
		return servicePackageFullPath.equals(path);
	}

	/**
	 * JCRパースの前処理　BdBuilderTask#doInBackgroundから使用する
	 * @throws NdModelException
	 * @throws BdBuilderException 
	 */
	public void createNdProject() throws NdModelException, BdBuilderException{
		File inputDirectory = this.getRootPackageDirectory();
		this.ndProject = NdProjectManager.getInstance().createProject(inputDirectory);
	}

	/**
	 * JCR要素をラップしてBdプロジェクトを構築する
	 * @throws BdBuilderException
	 * @throws BdDdbRuleCheckException
	 */
	public void importNdProject() throws BdBuilderException, BdDdbRuleCheckException{
		if (this.ndProject != null){
			this.rootPackage = new BdUserRootPackage(ndProject.getRootPackage(),  this);
			this.rootPackage.importNdProject();
			this.domainPackage = this.findDomainPackage();
			this.servicePackage = this.findServicePackage();
		}
	}

	/**
	 * domainパッケージを検索して応答する
	 * @return
	 * @throws BdBuilderException 
	 */
	private BdUserDomainPackage findDomainPackage() throws BdBuilderException{
		BdUserDomainPackage result = null;
		for(BdUserPackage pkg : this.rootPackage.getAllPackageList()){
			if (pkg.isDomainPackage()){
				result = (BdUserDomainPackage)pkg;
				break;
			}
		}
		if (result == null){
			throw new BdBuilderException("NOT FOUND domain package.");
		}
		return result;
	}

	/**
	 * serviceパッケージを検索して応答する
	 * @return
	 * @throws BdBuilderException 
	 */
	private BdUserServicePackage findServicePackage() throws BdBuilderException{
		BdUserServicePackage result = null;
		for(BdUserPackage pkg : this.rootPackage.getAllPackageList()){
			if (pkg.isServicePackage()){
				result = (BdUserServicePackage)pkg;
				break;
			}
		}
		if (result == null){
			throw new BdBuilderException("NOT FOUND service package.");
		}
		return result;
	}

	/**
	 * 関連などを解決する
	 */
	public void analyzeProject(){
		this.ndProject.analyzeProject();
	}

	/**
	 * NdProjectを応答する
	 * @return
	 */
	public NdProject getNdProject(){
		return this.ndProject;
	}

	/**
	 * コンパイル対象ファイル数を応答する delegate to NdProject
	 * @return
	 */
	public int getAllCompilationUnitCount(){
		return this.ndProject.getAllCompilationUnitCount();
	}

	/**
	 * 次の1ファイルをパースする delegate to NdProject
	 * @return boolean
	 * @throws NdParserException
	 * @throws NdApplicationPropertyException
	 * @throws NdUtilException
	 */
	public boolean parseNext() throws NdParserException, NdApplicationPropertyException, NdUtilException{
		return this.ndProject.parseNext();
	}

	/**
	 * デバッグ情報
	 */
	public void debugPrint(){
		//		NdLogger.getInstance().debug("**********************************************");
		//		NdLogger.getInstance().debug("USER PROJECT(JCR)");
		//		NdLogger.getInstance().debug("**********************************************");
		//		this.ndProject.debugPrint("");

		NdLogger.getInstance().debug("**********************************************");
		NdLogger.getInstance().debug("USER PROJECT(DDB)");
		NdLogger.getInstance().debug("**********************************************");	
		this.domainPackage.debugPrint("");
	}
}
