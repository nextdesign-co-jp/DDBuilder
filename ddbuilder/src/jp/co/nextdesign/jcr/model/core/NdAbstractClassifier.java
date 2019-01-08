/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.jcr.NdConstants;
import jp.co.nextdesign.jcr.model.NdName;
import jp.co.nextdesign.jcr.model.reference.NdAbstractReference;
import jp.co.nextdesign.jcr.model.reference.NdReferenceAsExtends;
import jp.co.nextdesign.jcr.model.reference.NdReferenceAsExtendsInterface;
import jp.co.nextdesign.jcr.model.reference.NdReferenceAsImplements;
import jp.co.nextdesign.jcr.model.typelink.NdAbstractTypeLink;
import jp.co.nextdesign.util.logging.NdLogger;

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;

/**
 * 分類子
 * クラス（トップレベル、ローカルクラス、内部クラス、無名クラス）、インタフェース、アノテーション、列挙型
 * @author murayama
 */
public abstract class NdAbstractClassifier extends NdCoreElement {

	private NdCompilationUnit compilationUnit;
	private boolean isInterface = false;
	private boolean isInnerClass = false;
	private boolean isAnonymousClass = false;
	private boolean isAnnotationType = false;
	private boolean isEnum = false;
	
	//スーパークラス・サブクラス
	private NdAbstractTypeLink superClassTypeLink;
	private NdAbstractClassifier superClass;
	private ArrayList<NdAbstractClassifier> subClassList = new ArrayList<NdAbstractClassifier>();
	//スーパーインタフェース・サブインタフェース
	private ArrayList<NdAbstractTypeLink> superInterfaceTypeLinkList = new ArrayList<NdAbstractTypeLink>();
	private ArrayList<NdAbstractClassifier> superInterfaceList = new ArrayList<NdAbstractClassifier>();;
	private ArrayList<NdAbstractClassifier> subInterfaceList = new ArrayList<NdAbstractClassifier>();
	//インタフェース・実装
	private ArrayList<NdAbstractTypeLink> implementInterfaceTypeLinkList = new ArrayList<NdAbstractTypeLink>();
	private ArrayList<NdAbstractClassifier> implementInterfaceList = new ArrayList<NdAbstractClassifier>();
	private ArrayList<NdAbstractClassifier> implementerList = new ArrayList<NdAbstractClassifier>();
	
	private ArrayList<NdClassTypeParameter> typeParameterList = new ArrayList<NdClassTypeParameter>();
	private ArrayList<NdMethod> methodList = new ArrayList<NdMethod>();
	private ArrayList<NdAttribute> attributeList = new ArrayList<NdAttribute>();
	private ArrayList<NdInitializer> initializerList = new ArrayList<NdInitializer>();
	private ArrayList<NdAbstractClassifier> innerClassList = new ArrayList<NdAbstractClassifier>();
	private ArrayList<NdAbstractClassifier> anonymousClassList = new ArrayList<NdAbstractClassifier>();
	private ArrayList<NdEnum> innerEnumList = new ArrayList<NdEnum>();
	private NdAbstractClassifier parentClassOfInnerClass;
	private NdAbstractClassifier parentClassOfAnonymousClass;
	private List<NdAbstractReference> referenceList = new ArrayList<NdAbstractReference>();
	private List<NdAbstractReference> inverseReferenceList = new ArrayList<NdAbstractReference>();
	
	/**
	 * コンストラクタ
	 * @param declaration
	 */
	protected NdAbstractClassifier(AbstractTypeDeclaration declaration){
		super();
		if ((declaration != null) && (declaration.getName() != null)){
			this.setName(declaration.getName().getIdentifier());
		}
	}
	
	/**
	 * コンストラクタ
	 * 無名クラスで使用する
	 */
	protected NdAbstractClassifier(){
		super();
	}
	
	/**
	 * 指定された名前の非プライベートなメソッドを持つか否か DDB add 2014.4.19
	 * @param methosName
	 * @return
	 */
	public boolean hasUnPrivateMethod(String methosName){
		boolean result = false;
		if (methosName != null){
			for(NdMethod method : this.getMethodListWithoutConstructor()){
				if (!method.isPrivate()){
					if (methosName.equals(method.getName())){
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * JPA @Entityクラスか否かを応答する DDB add 2014.4.16 ddb
	 * NdClassでoverrideする
	 * @return
	 */
	public boolean isJpaEntityClass(){
		return false;
	}
	
	/**
	 * スーパークラス階層を応答する
	 * @return
	 */
	public List<NdAbstractClassifier> getSuperClassHierarchy(){
		List<NdAbstractClassifier> resultList = new ArrayList<NdAbstractClassifier>();
		if (this.superClass != null){
			resultList.add(this.superClass);
			resultList.addAll(this.superClass.getSuperClassHierarchy());
		}
		return resultList;
	}

	/**
	 * スーパークラスを応答する
	 * @return
	 */
	public NdAbstractClassifier getSuperClass(){
		return this.superClass;
	}
	
	/**
	 * スーパークラスを設定する
	 * @param c
	 */
	public void setSuperClass(NdAbstractClassifier c){
		if ((c != null) && ( this.superClass != c)){
			this.superClass = c;
			c.addSubClass(this);
		}
	}
	
	/**
	 * サブクラスを追加する
	 * @param c
	 */
	public void addSubClass(NdAbstractClassifier c){
		if ((c != null) && (!this.subClassList.contains(c))){
			this.subClassList.add(c);
			c.setSuperClass(this);
		}
	}
	
	/**
	 * サブクラスのリストを応答する
	 * @return
	 */
	public List<NdAbstractClassifier> getSubClassList(){
		return this.subClassList;
	}
	
	/**
	 * スーパーインタフェースのリストを応答する
	 * @return
	 */
	public List<NdAbstractClassifier> getSuperInterfaceList(){
		return this.superInterfaceList;	
	}
	
	/**
	 * スーパーインタフェースを追加する
	 * @param c
	 */
	public void addSuperInterface(NdAbstractClassifier c){
		if ((c != null) && (!this.superInterfaceList.contains(c))){
			this.superInterfaceList.add(c);
			c.addSubInterface(this);
		}
	}
	
	/**
	 * サブインタフェースを追加する
	 * @param c
	 */
	public void addSubInterface(NdAbstractClassifier c){
		if ((c != null) && (!this.subInterfaceList.contains(c))){
			this.subInterfaceList.add(c);
			c.addSuperInterface(this);
		}
	}
	
	/**
	 * サブインタフェースのリストを応答する
	 * @return
	 */
	public List<NdAbstractClassifier> getSubInterfaceList(){
		return this.subInterfaceList;
	}
	
	/**
	 * インタフェースを追加する
	 * @param c
	 */
	public void addImplementInterface(NdAbstractClassifier c){
		if ((c != null) && (!this.implementInterfaceList.contains(c))){
			this.implementInterfaceList.add(c);
			c.addImplementer(this);
		}
	}
	
	/**
	 * インタフェースのリストを応答する
	 * @return
	 */
	public List<NdAbstractClassifier> getImplementInterfaceList(){
		return this.implementInterfaceList;
	}
	
	/**
	 * 自分（インタフェース）の実装クラスを追加する
	 * @param c
	 */
	public void addImplementer(NdAbstractClassifier c){
		if ((c != null) && (!this.implementerList.contains(c))){
			this.implementerList.add(c);
			c.addImplementInterface(this);
		}
	}
	
	/**
	 * 自分（インタフェース）の実装クラスのリストを応答する
	 * @return
	 */
	public List<NdAbstractClassifier> getImplementerList(){
		return this.implementerList;
	}

	/**
	 * 継承関連を作成する
	 */
	public void buildInheritenceRelationship(){
		NdProject project = NdProjectManager.getInstance().getCurrentProject();
		if (this.superClassTypeLink != null) {
			List<NdName> list = this.superClassTypeLink.getReferenceList();
			if (list.size() > 0){
				NdName name = list.get(0);
				NdAbstractClassifier resolved = project.resolveName(name, this);
				if (resolved != null){
					this.setSuperClass(resolved);
				}
			}
		}
		for(NdAbstractTypeLink link : this.superInterfaceTypeLinkList){
			List<NdName> list = link.getReferenceList();
			if (list.size() > 0){
				NdName name = list.get(0);
				NdAbstractClassifier resolved = project.resolveName(name, this);
				if (resolved != null){
					this.addSuperInterface(resolved);
				}
			}
		}
		for(NdAbstractTypeLink link : this.implementInterfaceTypeLinkList){
			List<NdName> list = link.getReferenceList();
			if (list.size() > 0){
				NdName name = list.get(0);
				NdAbstractClassifier resolved = project.resolveName(name, this);
				if (resolved != null){
					this.addImplementInterface(resolved);
				}
			}
		}
	}
	
	/**
	 * 所属するコンパイル単位の中の代表分類子か否かを応答する
	 * 代表分類子=コンパイル単位の名前と分類子名が同じであること。
	 * @return
	 */
	public boolean isPrimaryClassifier(){
		boolean result = false;
		if (!this.hasParent()){
			String simpleName = this.getCompilationUnit().getSimpleName();
			result = this.getName().equals(simpleName);
		}
		return result;
	}

	/**
	 * 参照リストを応答する
	 * @return
	 */
	public List<NdAbstractReference> getReferenceList(){
		List<NdAbstractReference> resultList = new ArrayList<NdAbstractReference>(this.referenceList);
		resultList.addAll(this.collectReferenceOfInnerClass());
		resultList.addAll(this.collectReferenceOfAnonymousClass());
		return resultList;
	}
	
	/**
	 * クラスレポートの出力対象か否かを応答する
	 * @return
	 */
	public boolean isSubjectOfClassifierReport(){
		return !this.hasParent();
	}
	
	/**
	 * 親要素を持つか否かを応答する
	 * @return
	 */
	public boolean hasParent(){
		boolean result = false;
		if (this.isInnerClass){
			result = this.parentClassOfInnerClass != null;
		} else if (this.isAnonymousClass){
			result = this.parentClassOfAnonymousClass != null;
		}
		return result;
	}

	/**
	 * 自分の内部クラスが持つ参照リストを収集して応答する
	 * @return
	 */
	private List<NdAbstractReference> collectReferenceOfInnerClass(){
		List<NdAbstractReference> resultList = new ArrayList<NdAbstractReference>();
		for(NdAbstractClassifier inner : this.innerClassList){
			resultList.addAll(inner.referenceList);
			if (inner.innerClassList.size() > 0){
				resultList.addAll(inner.collectReferenceOfInnerClass());
			}
		}
		return resultList;
	}

	/**
	 * 自分の無名クラスが持つ参照リストを収集して応答する
	 * @return
	 */
	private List<NdAbstractReference> collectReferenceOfAnonymousClass(){
		List<NdAbstractReference> resultList = new ArrayList<NdAbstractReference>();
		for(NdAbstractClassifier anonymous : this.anonymousClassList){
			resultList.addAll(anonymous.referenceList);
			if (anonymous.anonymousClassList.size() > 0){
				resultList.addAll(anonymous.collectReferenceOfAnonymousClass());
			}
		}
		return resultList;
	}

	/**
	 * 被参照リストを応答する
	 * @return
	 */
	public List<NdAbstractReference> getInverseReferenceList(){
		List<NdAbstractReference> resultList = new ArrayList<NdAbstractReference>(this.inverseReferenceList);
		resultList.addAll(this.collectInverseReferenceOfInnerClass());
		resultList.addAll(this.collectInverseReferenceOfAnonymousClass());
		return resultList;
	}
	
	/**
	 * 自分の内部クラスが持つ被参照リストを収集して応答する
	 * @return
	 */
	private List<NdAbstractReference> collectInverseReferenceOfInnerClass(){
		List<NdAbstractReference> resultList = new ArrayList<NdAbstractReference>();
		for(NdAbstractClassifier inner : this.innerClassList){
			resultList.addAll(inner.inverseReferenceList);
			if (inner.innerClassList.size() > 0){
				resultList.addAll(inner.collectInverseReferenceOfInnerClass());
			}
		}
		return resultList;
	}
	
	/**
	 * 自分の無名クラスが持つ被参照リストを収集して応答する
	 * @return
	 */
	private List<NdAbstractReference> collectInverseReferenceOfAnonymousClass(){
		List<NdAbstractReference> resultList = new ArrayList<NdAbstractReference>();
		for(NdAbstractClassifier anonymous : this.anonymousClassList){
			resultList.addAll(anonymous.inverseReferenceList);
			if (anonymous.anonymousClassList.size() > 0){
				resultList.addAll(anonymous.collectInverseReferenceOfAnonymousClass());
			}
		}
		return resultList;
	}

	/**
	 * 分類子内の各要素が参照している参照先を集めて１つのリストとして作成する
	 */
	private List<NdAbstractReference> createReferenceList(){
		List<NdAbstractReference> resultList = new ArrayList<NdAbstractReference>();
		//extendsスーパークラス参照
		if (this.superClassTypeLink != null){
			NdAbstractReference ref = new NdReferenceAsExtends(this.superClassTypeLink.getTypeName());
			ref.setFrom(this);
			resultList.add(ref);
		}
		//extendsスーパーインタフェース参照
		for(NdAbstractTypeLink link : this.superInterfaceTypeLinkList){
			NdAbstractReference ref = new NdReferenceAsExtendsInterface(link.getTypeName());
			ref.setFrom(this);
			resultList.add(ref);
		}
		//implements参照
		for(NdAbstractTypeLink link : this.implementInterfaceTypeLinkList){
			NdAbstractReference ref = new NdReferenceAsImplements(link.getTypeName());
			ref.setFrom(this);
			resultList.add(ref);
		}
		//Attributes参照
		for(NdAttribute attribute : this.attributeList){
			List<NdAbstractReference> refList = attribute.getReferenceList();
			for (NdAbstractReference ref : refList){
				ref.setFrom(this);
				resultList.add(ref);
			}
		}
		//Methods参照
		for(NdMethod method : this.methodList){
			List<NdAbstractReference> refList = method.getReferenceList();
			for (NdAbstractReference ref : refList){
				ref.setFrom(this);
				resultList.add(ref);
			}
		}
		//Initializer参照
		for(NdInitializer initializer : this.initializerList){
			List<NdAbstractReference> refList = initializer.getReferenceList();
			for (NdAbstractReference ref : refList){
				ref.setFrom(this);
				resultList.add(ref);
			}
		}
		return resultList;
	}

	/**
	 * クロスリファレンスを作成する
	 * このメソッドは一度だけ使用される。パッケージレポートとクラスレポートでクロスリファレンスを使用しても実行は一度だけである。
	 * NdReportTask#doInBackground() -> NdProject#analyzeProject() -> このメソッド
	 */
	public void buildCrossReference(){
		this.referenceList = this.createReferenceList();
		NdProject project = NdProjectManager.getInstance().getCurrentProject();
		for(NdAbstractReference ref : this.referenceList){
			//参照先を解決する
			NdName name = ref.getReferenceName();
			NdAbstractClassifier nameOwner = ref.getFrom();
			NdAbstractClassifier resolved = project.resolveName(name, nameOwner);
			if (resolved != null){
				ref.setTo(resolved);
				ref.getTo().inverseReferenceList.add(ref);
			}
		}
	}
	
	/**
	 * 直下の内部クラスで指定された名前のものが存在すれば、それを応答する
	 * @param simpleName
	 * @return
	 */
	public NdAbstractClassifier getInnerClassBySimpleName(String simpleName){
		NdAbstractClassifier result = null;
		for(NdAbstractClassifier inner : this.innerClassList){
			if ((simpleName != null) && (!"".equals(simpleName))){
				if (simpleName.equals(inner.getName())){
					result = inner;
				}
			}
		}
		return result;
	}
	
	/**
	 * 直下の無名クラスで指定された名前のものが存在すれば、それを応答する
	 * @param simpleName
	 * @return
	 */
	public NdAbstractClassifier getAnonymousClassBySimpleName(String simpleName){
		NdAbstractClassifier result = null;
		for(NdAbstractClassifier anonymous : this.anonymousClassList){
			if ((simpleName != null) && (!"".equals(simpleName))){
				if (simpleName.equals(anonymous.getName())){
					result = anonymous;
				}
			}
		}
		return result;
	}

	/**
	 * 引数の分類子をインポートしているか否かを応答する
	 * @param candidate
	 * @return
	 */
	public boolean isImporting(NdAbstractClassifier candidate){
		boolean result = false;
		List<NdImportDeclaration> importList = this.getCompilationUnit().getImportDeclarationList();
		for(NdImportDeclaration importDeclaration : importList){
			NdName thisName = importDeclaration.getName();
			String thisCompareName = "";
			if (thisName.isQualifiedName()){
				thisCompareName = thisName.getQualifiedName();
			} else {
				thisCompareName = thisName.getSimpleName();
			}
			if (importDeclaration.isOnDemand()){
				String otherPackageName = candidate.getPackageName();
				if(thisCompareName.equals(otherPackageName)){
					//*記述で一致
					result = true;
				}
			} else {
				String otherFullName = candidate.getQualifiedName();
				if (thisCompareName.equals(otherFullName)){
					//シングル記述で一致
					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * この分類子と同じパッケージに所属するか否かを応答する
	 * @param candidate
	 * @return
	 */
	public boolean isClassifierOfSamePackage(NdAbstractClassifier candidate){
		boolean result = false;
		if (this.getPackageName().equals(candidate.getPackageName())){
			result = true;
		}
		return result;
	}

	/**
	 * パッケージ名を応答する
	 * @return
	 */
	public String getPackageName(){
		String result = "";
		if (this.getCompilationUnit() != null) {
			NdPackageDeclaration packageDeclaration = this.getCompilationUnit().getPackageDeclaration();
			if (packageDeclaration != null){
				result = packageDeclaration.getFullName();
			}
		}
		return result;
	}

	/**
	 * 完全限定名を応答する
	 * @return
	 */
	public String getQualifiedName(){
		return this.getPackageName() + "." + this.getQualifiedNameWithoutPackage();
	}

	/**
	 * パッケージ名は含まない限定名を応答する
	 * @return
	 */
	public String getQualifiedNameWithoutPackage(){
		String result = this.name;
		if (this.parentClassOfInnerClass != null){
			result = this.parentClassOfInnerClass.getQualifiedNameWithoutPackage() + "." + result;
		}
		if (this.parentClassOfAnonymousClass != null){
			result = this.parentClassOfAnonymousClass.getQualifiedNameWithoutPackage() + "." + result;
		}
		return result;
	}

	/**
	 * 収容されているファイル中の行数を応答する
	 * @return
	 */
	public int getCompilationUnitFileLineCount(){
		int result = 0;
		if (this.compilationUnit != null){
			result = this.compilationUnit.getLineCountOfFile();
		}
		return result;
	}

	/**
	 * 収容されているファイル中のステートメント行数を応答する
	 * @return
	 */
	public int getCompilationUnitStatementCount(){
		int result = 0;
		if (this.compilationUnit != null){
			result = this.compilationUnit.getLineCountOfStatement() - this.compilationUnit.getLineCountOfParsedJavadoc();
		}
		return result;
	}

	/**
	 * 収容されているファイル中のJavadoc行数を応答する
	 * @return
	 */
	public int getCompilationUnitJavadocLineCount(){
		int result = 0;
		if (this.compilationUnit != null){
			result = this.compilationUnit.getLineCountOfJavadocComment();
		}
		return result;
	}

	/**
	 * 収容されているファイル中の行コメント行数を応答する
	 * @return
	 */
	public int getCompilationUnitLineCommentLineCount(){
		int result = 0;
		if (this.compilationUnit != null){
			result = this.compilationUnit.getLineCountOfLineComment();
		}
		return result;
	}

	/**
	 * 収容されているファイル中のブロックコメント行数を応答する
	 * @return
	 */
	public int getCompilationUnitBlockCommentLineCount(){
		int result = 0;
		if (this.compilationUnit != null){
			result = this.compilationUnit.getLineCountOfBlockComment();
		}
		return result;
	}

	/**
	 * コンパイル単位との双方向関連を応答する
	 * @return
	 */
	public NdCompilationUnit getCompilationUnit(){
		return this.compilationUnit;
	}

	/**
	 * コンパイル単位との双方向関連を設定する
	 * @param compilationUnit
	 */
	public void setCompilationUnit(NdCompilationUnit compilationUnit){
		if ((compilationUnit != null) && (this.compilationUnit != compilationUnit)){
			this.compilationUnit = compilationUnit;
			this.compilationUnit.addClassifier(this);
		}
	}

	/**
	 * 型パラメータリストを応答する
	 */
	public ArrayList<NdClassTypeParameter> getTypeParameterList() {
		return typeParameterList;
	}

	/**
	 * 型パラメータを追加する
	 * @param newTypeParameter
	 */
	public void addTypeParameter(NdClassTypeParameter newTypeParameter){
		if ((newTypeParameter != null) && (!this.typeParameterList.contains(newTypeParameter))){
			this.typeParameterList.add(newTypeParameter);
		}
	}

	/**
	 * スーパークラス，スーパーインタフェースを応答する
	 */
	public NdAbstractTypeLink getSuperClassTypeLink() {
		return superClassTypeLink;
	}

	/**
	 * スーパークラス，スーパーインタフェースを設定する
	 * @param superClass
	 */
	public void setSuperClassTypeLink(NdAbstractTypeLink superClass) {
		this.superClassTypeLink = superClass;
	}
	
	/**
	 * スーパーインタフェースリストを応答する
	 * @return
	 */
	public ArrayList<NdAbstractTypeLink> getSuperInterfaceTypeLinkList() {
		return superInterfaceTypeLinkList;
	}

	/**
	 * スーパーインタフェースを追加する
	 * @param newLink
	 */
	public void addSuperInterfaceTypeLink(NdAbstractTypeLink newLink){
		if ((newLink != null) && (!this.superInterfaceTypeLinkList.contains(newLink))){
			this.superInterfaceTypeLinkList.add(newLink);
		}
	}

	/**
	 * 実装implemantsインタフェースリストを応答する
	 */
	public ArrayList<NdAbstractTypeLink> getImplementInterfaceTypeLinkList() {
		return implementInterfaceTypeLinkList;
	}

	/**
	 * 実装implemantsインタフェースリストに追加する
	 * @param newLink
	 */
	public void addImplementInterfaceTypeLink(NdAbstractTypeLink newLink){
		if ((newLink != null) && (!this.implementInterfaceTypeLinkList.contains(newLink))){
			this.implementInterfaceTypeLinkList.add(newLink);
		}
	}

	/**
	 * 親クラス・内部クラスの双方向関連（内部クラスリストを応答する）
	 * @return
	 */
	public ArrayList<NdAbstractClassifier> getInnerClassList() {
		return innerClassList;
	}

	/**
	 * 親クラス・内部クラスの双方向関連（内部クラスを追加する）
	 * @param newInnerClass
	 */
	public void addInnerClass(NdAbstractClassifier newInnerClass){
		if ((newInnerClass != null) && (!this.innerClassList.contains(newInnerClass))){
			this.innerClassList.add(newInnerClass);
			newInnerClass.setParentClassOfInnerClass(this);
		}
	}

	/**
	 * 親クラス・内部クラスの双方向関連（親クラスを応答する）
	 */
	public NdAbstractClassifier getParentClassOfInnerClass() {
		return parentClassOfInnerClass;
	}

	/**
	 * 親クラス・内部クラスの双方向関連（親クラスを設定する）
	 * @param parentClassOfInnerClass
	 */
	public void setParentClassOfInnerClass(NdAbstractClassifier parentClassOfInnerClass) {
		if ((parentClassOfInnerClass != null) && (this.parentClassOfInnerClass != parentClassOfInnerClass)){
			this.parentClassOfInnerClass = parentClassOfInnerClass;
			parentClassOfInnerClass.addInnerClass(this);
		}
	}

	/**
	 * 親クラス・無名クラスの双方向関連（内部クラスリストを応答する）
	 * @return
	 */
	public ArrayList<NdAbstractClassifier> getAnonymousClassList() {
		return anonymousClassList;
	}

	/**
	 * 親クラス・無名クラスの双方向関連（内部クラスを追加する）
	 * @param newAnonymousClass
	 */
	public void addAnonymousClass(NdAbstractClassifier newAnonymousClass){
		if ((newAnonymousClass != null) && (!this.anonymousClassList.contains(newAnonymousClass))){
			this.anonymousClassList.add(newAnonymousClass);
			newAnonymousClass.setParentClassOfAnonymousClass(this);
		}
	}
	
	/**
	 * 親クラス・無名クラスの双方向関連（親クラスを応答する）
	 */
	public NdAbstractClassifier getParentClassOfAnonymousClass() {
		return parentClassOfAnonymousClass;
	}

	/**
	 * 親クラス・無名クラスの双方向関連（親クラスを設定する）
	 * @param parentClassOfAnonymousClass
	 */
	public void setParentClassOfAnonymousClass(NdAbstractClassifier parentClassOfAnonymousClass) {
		if ((parentClassOfAnonymousClass != null) && (this.parentClassOfAnonymousClass != parentClassOfAnonymousClass)){
			this.parentClassOfAnonymousClass = parentClassOfAnonymousClass;
			parentClassOfAnonymousClass.addAnonymousClass(this);
		}
	}
	
	/**
	 * 親クラス・クラス内定義の列挙型の双方向関連（内部クラスリストを応答する）
	 * @return
	 */
	public ArrayList<NdEnum> getInnerEnumList() {
		return innerEnumList;
	}

	/**
	 * 親クラス・クラス内定義の列挙型の双方向関連（内部クラスを追加する）
	 * @param newInnerEnum
	 */
	public void addInnerEnum(NdEnum newInnerEnum){
		if ((newInnerEnum != null) && (!this.innerEnumList.contains(newInnerEnum))){
			this.innerEnumList.add(newInnerEnum);
			newInnerEnum.setParentClass(this);
		}
	}

	/**
	 * メソッドリストに追加する
	 */
	public void addMethod(NdMethod newMethod){
		if ((newMethod != null) && (!this.methodList.contains(newMethod))){
			this.methodList.add(newMethod);
			newMethod.setClassifier(this);
		}
	}

	/**
	 * メソッドリストを応答する
	 * @return
	 */
	public ArrayList<NdMethod> getMethodList(){
		return this.methodList;
	}

	/**
	 * メソッドリスト（コンストラクタ以外）を応答する
	 * @return
	 */
	public ArrayList<NdMethod> getMethodListWithoutConstructor(){
		ArrayList<NdMethod> resultList = new ArrayList<NdMethod>();
		for(int i=0; i<this.methodList.size(); i++){
			NdMethod m = this.methodList.get(i);
			if (!m.isConstructor()){
				resultList.add(m);
			}
		}
		return resultList;
	}

	/**
	 * コンストラクタリストを応答する
	 * @return
	 */
	public ArrayList<NdMethod> getConstructorList(){
		ArrayList<NdMethod> resultList = new ArrayList<NdMethod>();
		for(int i=0; i<this.methodList.size(); i++){
			NdMethod m = this.methodList.get(i);
			if (m.isConstructor()){
				resultList.add(m);
			}
		}
		return resultList;
	}

	/**
	 * 初期化子リストに追加する
	 */
	public void addInitializer(NdInitializer newInitializer){
		if ((newInitializer != null) && (!this.initializerList.contains(newInitializer))){
			this.initializerList.add(newInitializer);
			newInitializer.setClassifier(this);
		}
	}

	/**
	 * 初期化子リストを応答する
	 * @return
	 */
	public ArrayList<NdInitializer> getInitializerList(){
		return this.initializerList;
	}
	
	/**
	 * 最大メソッドの行数を応答する
	 * NdMethod#getMetricsStatementCountはブロック { } をカウントしない値なので、
	 * ここではgetLineCountOfStatementを使用する
	 * @return
	 */
	public int getMaxMethodStepCount(){
		int result = 0;
		for(NdMethod m : this.methodList){
			if (result < m.getLineCountOfStatement()){
				result = m.getLineCountOfStatement();
			}
		}
		return result;
	}

	/**
	 * 最深ネストレベルを応答する
	 * @return
	 */
	public int getMaxMethodNestLevel(){
		int result = 0;
		for(NdMethod m : this.methodList){
			if (result < m.getMetricsMaxNestLevelNumber()){
				result = m.getMetricsMaxNestLevelNumber();
			}
		}
		return result;
	}

	/**
	 * 最大制御文数を応答する
	 * @return
	 */
	public int getMaxMethodControlStepCount(){
		int result = 0;
		for(NdMethod m : this.methodList){
			if (result < m.getMetricsControlStatementCount()){
				result = m.getMetricsControlStatementCount();
			}
		}
		return result;
	}

	/**
	 * 属性リストに追加する
	 */
	public void addAttribute(NdAttribute attribute){
		if ((attribute != null) && (!this.attributeList.contains(attribute))){
			this.attributeList.add(attribute);
			attribute.setClassifier(this);
		}
	}

	/**
	 * 属性リストを応答する
	 * @return
	 */
	public ArrayList<NdAttribute> getAttributeList(){
		return this.attributeList;
	}

	/**
	 * ステートメント行数を応答する
	 * 分類子の場合は含んでいるメソッド、属性から求める
	 */
	@Override public int getLineCountOfStatement(){
		int result = 0;
		for(NdAttribute a : this.attributeList){
			result += a.getLineCountOfStatement();
		}
		for(NdMethod m : this.methodList){
			result += m.getLineCountOfStatement();
		}
		return result;
	}

	/**
	 * 主要情報の文字列表現を応答する
	 */
	@Override
	protected String getTitle(){
		String result = "";
		for(NdExtendedModifier m : this.getModifierList()){
			result += m.getKeyword() + " ";
		}
		result += this.getName();
		if (this.typeParameterList.size() > 0){
			result += "<";
			for(NdClassTypeParameter param : this.typeParameterList){
				result += param.getTypeName() + ", ";
				if (result.endsWith(", ")){
					result = result.substring(0, result.length() - 2);
				}
			}
			result += ">";
		}
		if (this.superClassTypeLink != null){
			result += " extends " + superClassTypeLink.getDisplayName();
		}
		if (this.implementInterfaceTypeLinkList.size() > 0){
			result += " implements";
			for (NdAbstractTypeLink link : this.implementInterfaceTypeLinkList){
				result += " " + link.getDisplayName() + ",";
			}
			if (result.endsWith(",")){
				result = result.substring(0, result.length() - 1);
			}
		}
		return result;
	}

	/**
	 * インタフェースか否かを応答する
	 * @return
	 */
	public boolean isInterface() {
		return isInterface;
	}

	/**
	 * インタフェースか否かを設定する
	 * @param isInterface
	 */
	public void setIsInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}

	/**
	 * 内部クラスか否かを応答する
	 * @return
	 */
	public boolean isInnerClass(){
		return this.isInnerClass;
	}
	
	/**
	 * 内部クラスか否かを設定する
	 * @param b
	 */
	public void setIsInnerClass(boolean b){
		this.isInnerClass = b;
	}
	
	/**
	 * 無名クラスか否かを応答する
	 * @return
	 */
	public boolean isAnonymousClass(){
		return this.isAnonymousClass;
	}
	
	/**
	 * 無名クラスか否かを設定する
	 * @param b
	 */
	public void setIsAnonymousClass(boolean b){
		this.isAnonymousClass = b;
	}

	/**
	 * アノテーションか否かを応答する
	 * @return
	 */
	public boolean isAnnotationType(){
		return this.isAnnotationType;
	}
	
	/**
	 * アノテーションか否かを設定する
	 * @param b
	 */
	public void setIsAnnotationType(boolean b){
		this.isAnnotationType = b;
	}
	
	/**
	 * 列挙型か否かを応答する
	 * @return
	 */
	public boolean isEnum(){
		return this.isEnum;
	}
	
	/**
	 * 列挙型か否かを設定する
	 * @param b
	 */
	public void setIsEnum(boolean b){
		this.isEnum = b;
	}

	/**
	 * デバッグ
	 */
	public void debugPrint(String tab){
		if (!NdLogger.getInstance().getDebugLogging()) return;
		NdLogger.getInstance().debug(tab + getTitle4Debug());
		this.debugPrintCore(tab);
		String tab2 = tab + NdConstants.DEBUG_TAB;
		
		NdLogger.getInstance().debug(tab + this.getName() + "のスーパークラス【リンク解決済み】");
		if (this.superClass != null){
			NdLogger.getInstance().debug(tab + this.superClass.getPackageName() + "." + this.superClass.getQualifiedNameWithoutPackage());
		}
		NdLogger.getInstance().debug(tab + this.getName() + "のサブクラス【リンク解決済み】");
		for(NdAbstractClassifier c : this.subClassList){
			NdLogger.getInstance().debug(tab + c.getPackageName() + "." + c.getQualifiedNameWithoutPackage());
		}

		NdLogger.getInstance().debug(tab + this.getName() + "のスーパーインタフェース【リンク解決済み】");
		for(NdAbstractClassifier c : this.superInterfaceList){
			NdLogger.getInstance().debug(tab + c.getPackageName() + "." + c.getQualifiedNameWithoutPackage());
		}
		NdLogger.getInstance().debug(tab + this.getName() + "のサブインタフェース【リンク解決済み】");
		for(NdAbstractClassifier c : this.subInterfaceList){
			NdLogger.getInstance().debug(tab + c.getPackageName() + "." + c.getQualifiedNameWithoutPackage());
		}

		NdLogger.getInstance().debug(tab + this.getName() + "のインタフェース【リンク解決済み】");
		for(NdAbstractClassifier c : this.implementInterfaceList){
			NdLogger.getInstance().debug(tab + c.getPackageName() + "." + c.getQualifiedNameWithoutPackage());
		}
		NdLogger.getInstance().debug(tab + this.getName() + "の実装【リンク解決済み】");
		for(NdAbstractClassifier c : this.implementerList){
			NdLogger.getInstance().debug(tab + c.getPackageName() + "." + c.getQualifiedNameWithoutPackage());
		}
		NdLogger.getInstance().debug(tab + this.getName() + "のスーパークラス階層");
		List<NdAbstractClassifier> superClassHiearchyList = this.getSuperClassHierarchy();
		for(int i=0; i<superClassHiearchyList.size(); i++){
			NdAbstractClassifier c = superClassHiearchyList.get(i);
			NdLogger.getInstance().debug(tab + c.getPackageName() + "." + c.getQualifiedNameWithoutPackage());
		}
		
		NdLogger.getInstance().debug(tab + this.getName() + "の継承");
		if (this.superClassTypeLink != null){
			this.superClassTypeLink.debugPrint(tab2 + "継承=");
		}
		NdLogger.getInstance().debug(tab + this.getName() + "の実装インタフェース");
		for(NdAbstractTypeLink link : this.implementInterfaceTypeLinkList){
			link.debugPrint(tab2 + "実装=");
		}
		NdLogger.getInstance().debug(tab + this.getName() + "の属性");
		for(NdAttribute a : this.attributeList){
			a.debugPrint(tab2);			
		}
		NdLogger.getInstance().debug(tab + this.getName() + "のメソッド");
		for(NdMethod m : this.methodList){
			m.debugPrint(tab2);			
		}
		NdLogger.getInstance().debug(tab + this.getName() + "の内部クラス");
		for(NdAbstractClassifier inner : this.innerClassList){
			inner.debugPrint(tab2);
		}
		NdLogger.getInstance().debug(tab + this.getName() + "の無名クラス");
		for(NdAbstractClassifier anonymous : this.anonymousClassList){
			anonymous.debugPrint(tab2);
		}
		NdLogger.getInstance().debug(tab + this.getName() + "の中に定義された列挙型");
		for(NdAbstractClassifier innerEnum : this.innerEnumList){
			innerEnum.debugPrint(tab2);
		}
		NdLogger.getInstance().debug(tab + this.getName() + "の参照リスト（内部クラス、無名クラス分も含む）");
		for(NdAbstractReference ref : this.getReferenceList()){
			ref.debugPrint(tab2);
		}
		NdLogger.getInstance().debug(tab + this.getName() + "の被参照リスト（内部クラス、無名クラス分も含む）");
		for(NdAbstractReference ref : this.getInverseReferenceList()){
			ref.debugPrint(tab2);
		}
	}

	/**
	 * debugPrint用
	 * @return
	 */
	private String getTitle4Debug(){
		String result = "";
		if (this.isPublic()){
			result = "<公開>";
		} else if (this.isDefault()){
			result = "<デフォルト>";
		} else {
			result = "<非公開>";
		}
		result += "<" + this.getDisplayTypeName() + ">";
		result += "getName()=" + this.getName();
		result += " （パッケージ宣言+内部クラス階層による）限定名=" + this.getQualifiedName();
		return result;
	}
}
