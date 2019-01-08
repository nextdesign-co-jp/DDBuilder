/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.user;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.ddb.BdConstants;
import jp.co.nextdesign.ddb.BdMessageResource;
import jp.co.nextdesign.ddb.ui.main.BdDdbRuleCheckException;
import jp.co.nextdesign.jcr.NdConstants;
import jp.co.nextdesign.jcr.model.core.NdAbstractClassifier;
import jp.co.nextdesign.jcr.model.core.NdAnnotation;
import jp.co.nextdesign.jcr.model.core.NdAnnotationAttribute;
import jp.co.nextdesign.jcr.model.core.NdAttribute;
import jp.co.nextdesign.jcr.model.core.NdClass;
import jp.co.nextdesign.jcr.model.core.NdExtendedModifier;
import jp.co.nextdesign.jcr.model.core.NdImportDeclaration;
import jp.co.nextdesign.jcr.model.core.NdMethod;
import jp.co.nextdesign.jcr.model.core.NdMethodParameter;
import jp.co.nextdesign.jcr.model.reference.NdAbstractReference;
import jp.co.nextdesign.util.BdSqlReservedWord;
import jp.co.nextdesign.util.logging.BdDdbRuleCheckLogger;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * ユーザプロジェクトのドメインクラス
 * @author murayama
 *
 */
public class BdUserClass extends BdUserAbstractElement {

	private NdClass ndClass;
	private List<BdUserAttribute> attributeList;
	private List<BdUserMethod> methodList;
	private BdUserPackage bdPackage;
	private static final String TABLE_ANNOTATION = "@Table";
	private static final String NAME_ATTRIBUTE = "name";

	/**
	 * コンストラクタ
	 * @param ndClass
	 */
	public BdUserClass(NdClass ndClass, BdUserPackage bdPackage){
		super();
		this.ndClass = ndClass;
		this.bdPackage = bdPackage;
		this.loadAttributes();
		this.loadMethods();
		this.debugPrintReferences();
	}
	
	/** DDBルールチェックを行う */
	public void checkDdbRule() throws BdDdbRuleCheckException {
		//(1)SQL予約語使用チェック
		if (this.isEntityAnnotatedDomainClass()){
			String tableName = this.getTableName();
			if (BdSqlReservedWord.isReservedWord(tableName)){
				String log = BdMessageResource.get("error.userclass.sqlname1") + this.getName() + " TABLE NAME: " + tableName; // "クラス名エラー（SQL予約語使用不可）:" + this.getName() + " テーブル名: " + tableName;
				BdDdbRuleCheckLogger.getInstance().add(log);
				throw new BdDdbRuleCheckException(BdMessageResource.get("error.userclass.sqlname2")  + "\n" + log, null); //"ソース更新処理は中断しました。\n"
			}
		}
		//(2)@Entityかつextends BdBaseEntityかチェック
		if (!this.isGPackageClass() && !this.isDdbFixedClass()){
			boolean hasEntityAnnotation = this.isEntityAnnotatedDomainClass();
			boolean extendsDdBaseEntity = this.extendsDdBaseEntity();
			if (hasEntityAnnotation && !extendsDdBaseEntity){
				BdDdbRuleCheckLogger.getInstance().add(BdMessageResource.get("error.userclass.extends") + this.getName()); //"DdBaseEntityを継承していません : "
			} else 
				if (!hasEntityAnnotation && extendsDdBaseEntity){
					BdDdbRuleCheckLogger.getInstance().add(BdMessageResource.get("error.userclass.entityAnnotation") + this.getName()); //"@Entityがありません : "
				}
		}
		//(3)相手作クラスが同じManyToMany属性は最大1つ
		//(4)ManyToMany属性に対応するaddクラス名(), removeクラス名()メソッドが必要
		List<String> otherSizeClassNameList = new ArrayList<String>();
		for(BdUserAttribute a : this.getAttributeListByDdbTypeKey(BdUserAttribute.KEY_ManyToMany)){
			String otherSizeClassName = a.getCollectionElementTypeName();
			if (otherSizeClassNameList.contains(otherSizeClassName)){
				BdDdbRuleCheckLogger.getInstance().add(BdMessageResource.get("error.userclass.manyToMany1")  + this.getName()); //"関連先が同じ@ManyToManyは２つ以上宣言できません : "
			} else {
				otherSizeClassNameList.add(otherSizeClassName);
				String addMethodName = "add" + a.getCollectionElementTypeName();
				this.checkManyToManyRequiredMethod(addMethodName, a);
				String removeMethodName = "remove" + a.getCollectionElementTypeName();
				this.checkManyToManyRequiredMethod(removeMethodName, a);
			}
		}
	}
	
	/** ManyToMany属性に必要なadd, removeメソッドの有無をチェックする */
	private boolean checkManyToManyRequiredMethod(String requiredMethodName, BdUserAttribute a){
		boolean result = false;
		String otherSideClassName = a.getCollectionElementTypeName();
		BdUserMethod bdMethod = this.getBdMethodByName(requiredMethodName);
		if (bdMethod != null){
			List<NdMethodParameter> ndParameterList = bdMethod.getNdMethod().getParameterList();
			result = ndParameterList.size() == 1 && otherSideClassName.equals(ndParameterList.get(0).getParameterType().getTypeName().getSimpleName());
		}
		if (!result){
			//"@ManyToMany属性には次のメソッドが必要です : "
			BdDdbRuleCheckLogger.getInstance().add(
					BdMessageResource.get("error.userclass.manyToMany2") + requiredMethodName + 
					"(" + otherSideClassName + " param)" + this.getName());
		}
		return result;
	}
	
	/** メソッド名が一致するメソッドを応答する */
	public BdUserMethod getBdMethodByName(String name){
		BdUserMethod result = null;
		if (name != null){
			for(BdUserMethod m : this.getPublicMethodList()){
				if (name.equals(m.getNdMethod().getName())){
					result = m;
					break;
				}
			}
		}
		return result;
	}

	/** publicなメソッドリストを応答する（コンストラクタを除く） */
	public List<BdUserMethod> getPublicMethodList(){
		return this.methodList;
	}

	/**
	 * JPAが使用するテーブル名
	 * @return
	 */
	public String getTableName(){
		String result = this.getName();
		for(NdExtendedModifier modifier : this.ndClass.getModifierList()){
			if (modifier.isAnnoteation() && TABLE_ANNOTATION.equals(modifier.getKeyword())){
				NdAnnotation annotation = (NdAnnotation)modifier;
				for(NdAnnotationAttribute attr : annotation.getAnnotationAttributeList()){
					if (attr.getName() != null && NAME_ATTRIBUTE.equals(attr.getName().toLowerCase())){
						if (attr.getValue() != null){
							result = attr.getValue().replaceAll("\"", "");
							break;
						}
					}
				}
			}
		}
		return result;
	}

	/** ユーザパッケージを応答する */
	public BdUserPackage getBdUserPackage(){
		return this.bdPackage;
	}

	/** groupId */
	public String getGroupId(){
		return (this.bdPackage != null && this.bdPackage.getUserProject() != null) ? this.bdPackage.getUserProject().getGroupId() : "";
	}

	/** NdClassを応答する */
	public NdClass getNdClass(){
		return this.ndClass;
	}

	/** すべてのimport文を応答する(importキーワードを除いたもの) */
	public List<String> getImportList(){
		List<String> resultList = new ArrayList<String>();
		List<NdImportDeclaration> list = this.ndClass.getCompilationUnit().getImportDeclarationList();
		for(NdImportDeclaration ddd : list){
			resultList.add(ddd.getName().getQualifiedName());
		}
		return resultList;
	}

	/** パッケージ名を応答する */
	public String getPackageName(){
		return this.ndClass != null ? this.ndClass.getPackageName() : "";
	}

	/**
	 * DDB分類Keyに該当する属性のリストを応答する
	 * @param ddbTypeKey
	 * @return
	 */
	public List<BdUserAttribute> getAttributeListByDdbTypeKey(String ddbTypeKey){
		List<BdUserAttribute> resultList = new ArrayList<BdUserAttribute>();
		if (ddbTypeKey != null){
			for(BdUserAttribute a : this.getAttributeList()){
				if (ddbTypeKey.equals(a.getDdbTypeKey())){
					resultList.add(a);
				}
			}
		}
		return resultList;
	}

	/**
	 * Javadocからタイトルを応答する
	 * @return
	 */
	public String getJavadocTitle(){
		return formatJavadoc(ndClass.getJavadoc());
	}

	/** ドメインクラスか否か */
	public boolean isUserDomainClass(){
		String fullName = this.getFullName();
		return fullName.contains(".domain.") && (! fullName.contains(".ddb.")) && (! fullName.contains(".g."));
	}
	/**
	 * 属性リストを構築する
	 */
	private void loadAttributes(){
		this.attributeList = new ArrayList<BdUserAttribute>();
		for(NdAttribute ndAttribute : ndClass.getAttributeList()){
			BdUserAttribute attribute = new BdUserAttribute(ndAttribute, this);
			if (attribute.isAvailableDdbTypeKey()){
				if (hasGetterSetter(attribute)){
					this.attributeList.add(attribute);
				} else {
					if (this.isUserDomainClass() &&  (! attribute.isDdbIgnore())) {
						//"ドメインクラスで非privateなgetter/setterがない属性：" 
						BdDdbRuleCheckLogger.getInstance().add(BdMessageResource.get("error.userclass.getterSetter") + this.getName() + "->" + attribute.getNdAttributeType().getDisplayName());
					}
				}
			} else {
				if (this.isUserDomainClass() &&  (! attribute.isDdbIgnore())) {
					//"ドメインクラス属性の未対応型："
					BdDdbRuleCheckLogger.getInstance().add(BdMessageResource.get("error.userclass.unsupportedAttribute") + this.getName() + "->" + attribute.getNdAttributeType().getDisplayName());
				}
				//ここでは全てのドメインクラスは揃っていないので、List<T>のTがドメインクラスか否かのチェックはできない。TのチェックはBdUserproject#checkDdbRoleで行う。
			}
		}
	}

	/**
	 * メソッドリストを構築する
	 */
	private void loadMethods(){
		this.methodList = new ArrayList<BdUserMethod>();
		for(NdMethod ndMethod : ndClass.getMethodList()){
			BdUserMethod method = new BdUserMethod(ndMethod, this);
			if (method.getNdMethod().isPublic() && !method.getNdMethod().isConstructor()){
				this.methodList.add(method);
			}
		}
	}

	/**
	 * publicか否か
	 * @return
	 */
	public boolean isPublic(){
		return this.ndClass.isPublic();
	}

	/**
	 * クラス名を応答する
	 * @return
	 */
	public String getName(){
		return this.ndClass.getName();
	}

	/** 先頭小文字のクラス名を応答する */
	public String getLowerStartedName(){
		String result = this.getName();
		if (result.length() > 1){
			result = result.substring(0, 1).toLowerCase() + result.substring(1);
		}
		return result;
	}

	/**
	 * パッケージ名付きクラス名を応答する
	 * @return
	 */
	public String getFullName(){
		return this.ndClass.getQualifiedName();
	}

	/**
	 * 指定されたNdClassインスタンスは自分のインスタンスか否か
	 * @param c
	 * @return
	 */
	public boolean isThisNdClass(NdClass c){
		return this.ndClass == c;
	}

	/**
	 * 属性リストを応答する
	 * @return
	 */
	public List<BdUserAttribute> getAttributeList(){
		return this.attributeList;
	}

	//	/**
	//	 * スーパークラスから継承した属性を含めた全属性のリストを応答する
	//	 * @return
	//	 */
	//	public List<BdAttribute> getAttributeListWithInherited(){
	//		List<BdAttribute> resultList = new ArrayList<BdAttribute>();
	//		List<NdAbstractClassifier> list = ndClass.getSuperClassHierarchy();
	//		list.add(0, this.ndClass);
	//		for(int i = (list.size() - 1); i >= 0; i--){
	//			NdAbstractClassifier classifier = list.get(i);
	//			if (classifier instanceof NdClass){
	//				NdClass ndClass = (NdClass)classifier;
	//				
	//				this.bdPackage.getUserProject();
	//				//////////////BdClass userDomainClass = this.domainPackage.findByNdClass(ndClass);
	//				
	//				
	//				if (userDomainClass != null && (!userDomainClass.isDomainRootClass())){
	//					resultList.addAll(userDomainClass.getAttributeList());
	//				}
	//			}
	//		}
	//		return resultList;
	//	}

	//	/**
	//	 * domain配下のクラスか否か
	//	 * @return
	//	 */
	//	public boolean isInDomain(){
	//		return this.bdPackage.isInDoamin();
	//	}

	/**
	 * 指定された属性の非privateなgetter/setterを持っているか否か
	 * @param ndAttribute
	 * @return
	 */
	private boolean hasGetterSetter(BdUserAttribute attribute){
		return ndClass.hasUnPrivateMethod(attribute.getGetterName()) && ndClass.hasUnPrivateMethod(attribute.getSetterName());
	}

	/**
	 * DDBが対象とするサービスクラスか否か
	 * @return
	 */
	public boolean isAppendedServiceClass(){
		return (! this.ndClass.isAbstract())
				&& (! this.ndClass.isInterface())
				&& (! this.isDdbFixedClass())
				&& (! this.isGPackageClass());
	}

	/**
	 * DDBが対象とする@Entityが付いたドメインクラスか否か
	 * @return
	 */
	public boolean isEntityAnnotatedDomainClass(){
		return (! this.ndClass.isAbstract())
				&& (! this.ndClass.isInterface())
				&& this.ndClass.isJpaEntityClass()
				&& (! this.isDdbFixedClass())
				&& (! this.isGPackageClass());
	}

	/** ddbパッケージ内のDDB固定クラスか否か */
	public boolean isDdbFixedClass(){
		return this.getPackageName() != null && this.getPackageName().endsWith("ddb");
	}

	/** gパッケージ内のDDB生成クラスか否か */
	public boolean isGPackageClass(){
		return this.getPackageName() != null && this.getPackageName().endsWith("g");
	}

	/** DdBaseEntityを継承しているか否か */
	public boolean extendsDdBaseEntity(){
		boolean result = false;
		for(NdAbstractClassifier superClassifier : ndClass.getSuperClassHierarchy()){
			if (BdConstants.DD_BASE_ENTITY_CLASS_NAME.equals(superClassifier.getName())){
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * デバッグ
	 */
	public void debugPrint(String tab){
		String msg = "CLASS：" + this.ndClass.getQualifiedName();
		msg += " title=" + this.getJavadocTitle();
		if (this.ndClass.isJpaEntityClass()){
			msg += " [@Entity]";
		}else{
			msg += " [NOT @Entity]";
		}
		NdLogger.getInstance().debug(tab + msg);
		//////////////////NdLogger.getInstance().debug("--- 自分自身の属性リスト ---");
		for(BdUserAttribute attr : this.attributeList){
			attr.debugPrint(tab + NdConstants.DEBUG_TAB);
		}
		//		NdLogger.getInstance().debug("--- 継承分を含む属性リスト ---");
		//		for(BdUserAttribute attr : this.getAttributeListWithInherited()){
		//			attr.debugPrint();
		//		}
	}

	//デバッグ用:参照関連を表示する
	private void debugPrintReferences(){
		if ("Book".equals(this.ndClass.getName()) || "Store".equals(this.ndClass.getName())){
			List<NdAbstractReference> refList = ndClass.getReferenceList();
			for(NdAbstractReference ref : refList){
				String msg = "BdUserClass():c=" + ndClass.getName() + " name=" + ref.getReferenceName().getSimpleName() + " タイプ=" ;
				msg += ref.isAsAttribute() ? "Attribute" : "";
				msg += ref.isAsClassInstanceCreation() ? "ClassInstanceCreation" : "";
				msg += ref.isAsExtends() ? "Extends" : "";
				msg += ref.isAsExtendsInterface() ? "ExtendsInterface" : "";
				msg += ref.isAsImplements() ? "Implements" : "";
				msg += ref.isAsMethodParameter() ? "MethodParameter" : "";
				msg += ref.isAsMethodReturnValue() ? "MethodReturnValue" : "";
				//msg += ref.isAvailableReference() ? "AvailableReference" : "";
				msg += " [";
				NdAbstractClassifier refTo = ref.getTo();
				NdAbstractClassifier refFrom = ref.getFrom();
				if (refFrom != null){
					msg += refFrom.getQualifiedName();
				}else{
					msg += "null";
				}
				msg += " --> ";
				if (refTo != null){
					msg += refTo.getQualifiedName();
				}else{
					msg += "null";
				}
				msg += "]";
				//				if (refFrom != null && refTo != null){  //String等の場合はrefToはnullなので出力は省略する
				//					NdLogger.getInstance().error(msg, null);
				//				}
			}
		}
	}
}
