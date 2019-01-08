/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.nextdesign.jcr.model.core.NdAnnotation;
import jp.co.nextdesign.jcr.model.core.NdAnnotationAttribute;
import jp.co.nextdesign.jcr.model.core.NdAttribute;
import jp.co.nextdesign.jcr.model.core.NdExtendedModifier;
import jp.co.nextdesign.jcr.model.core.NdModifier;
import jp.co.nextdesign.jcr.model.typelink.NdAbstractTypeLink;
import jp.co.nextdesign.jcr.model.typelink.NdParameterizedTypeLink;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * Java属性
 * @author murayama
 *
 */
public class BdUserAttribute extends BdUserAbstractElement {

	//DDB型分類キー
	public static final String KEY_JavaPrimitiveClass = "JavaPrimitiveClass";
	public static final String KEY_String = "String";
	public static final String KEY_Date = "Date";
	public static final String KEY_BigDecimal = "BigDecimal";
	public static final String KEY_Boolean = "Boolean";
	public static final String KEY_Enum = "Enum";
	public static final String KEY_Embedded = "Embedded";
	public static final String KEY_ManyToOne = "ManyToOne";
	public static final String KEY_OneToMany = "OneToMany";
	public static final String KEY_ManyToMany = "ManyToMany";
	public static final String KEY_Ignore = "Ignore";
	private String ddbKey = "";
	public static final List<String> SUPPORT_COLLECTION_CLASS_NAMES = new ArrayList<String>(Arrays.asList(
			"List", 
			"ArrayList"));

	private NdAttribute ndAttribute;
	private BdUserClass bdUserClass;
	private String getterName;
	private String setterName;
	public static final List<String> PRIMITIVE_OBJECT_TYPE_NAMES = new ArrayList<String>(Arrays.asList(
			"Boolean", "Character",	"Byte",	"Short",	"Integer",	"Long",	"Float",	"Double"));

	/**
	 * コンストラクタ
	 * @param ndAttribute
	 */
	public BdUserAttribute(NdAttribute ndAttribute, BdUserClass bdUserClass){
		super();
		this.ndAttribute = ndAttribute;
		this.bdUserClass = bdUserClass;
		this.buildGetterSetterName();
		this.decideDdbKey();
	}

	/** EagerFetch指定あるか否か */
	public boolean isEagerFetch(){
		return this.hasAnnotationAttribute("fetch",  "FetchType.EAGER");
	}

	/** LazyFetch指定あるか否か */
	public boolean isLazyFetch(){
		return this.hasAnnotationAttribute("fetch",  "FetchType.LAZY");
	}

	/** デフォルトフェッチか否か */
	public boolean isDefaultFetch(){
		return (! this.isEagerFetch()) && (! this.isLazyFetch());
	}

	/** 指定した名前と値をアノテーション属性として持つか否か */
	private boolean hasAnnotationAttribute(String name, String value){
		boolean result = false;
		if (name !=null && value != null){
			for(NdExtendedModifier modifier : this.ndAttribute.getModifierList()){
				if (modifier.isAnnoteation()){
					NdAnnotation annottion = (NdAnnotation)modifier;
					for(NdAnnotationAttribute ndAnnotationAttribute : annottion.getAnnotationAttributeList()){
						if (name.equals(ndAnnotationAttribute.getName()) && value.equals(ndAnnotationAttribute.getValue())){
							result = true;
							break;
						}
					}
				}
			}
		}
		return result;
	}

	/** 有効なDDB型か否か */
	public boolean isAvailableDdbTypeKey(){
		return (! "".equals(this.ddbKey)) && (! KEY_Ignore.equals(this.ddbKey));
	}

	/**
	 * DDB型分類キーを応答する
	 * @return
	 */
	public String getDdbTypeKey(){
		return this.ddbKey;
	}

	/** 
	 * DDB型分類キーを決める
	 * 属性型、引数型、戻り値型が持つBdDdbKeyクラスの導入を検討したが、
	 * BdUserServiceMethodValueBaseとBdUserAttributeではddbKeyの決定方法が違うので、導入は保留とする。
	 */
	private void decideDdbKey(){
		this.ddbKey = "";
		if (this.isIgnoreType()){
			this.ddbKey = KEY_Ignore;
		} else if (this.isDdbJavaPrimitiveClass()){
			this.ddbKey = KEY_JavaPrimitiveClass;
		} else if (this.isDdbString()){
			this.ddbKey = KEY_String;
		} else if (this.isDdbDate()){
			this.ddbKey = KEY_Date;
		} else if (this.isDdbBigDecimal()){
			this.ddbKey = KEY_BigDecimal;
		} else if (this.isDdbBoolean()){
			this.ddbKey = KEY_Boolean;
		} else if (this.isDdbEnum()){
			this.ddbKey = KEY_Enum;
		} else if (this.isDdbEmbedded()){
			this.ddbKey = KEY_Embedded;
		} else if (this.isDdbManyToOne()){
			this.ddbKey = KEY_ManyToOne;
		} else if (this.isDdbOneToMany()){
			this.ddbKey = KEY_OneToMany;
		} else if (this.isDdbManyToMany()){
			this.ddbKey = KEY_ManyToMany;
		}
	}

	/** 無視する属性 2015.7.7時点では"serialVersionUID", "TEMS_PER_PAGE" がある */
	private boolean isIgnoreType(){
		return this.isStatic() 
				|| this.isFinal()
				|| this.hasAnnotationKeywords(new String[]{"@Transient"});
	}

	/** staticな属性か否か */
	private boolean isStatic(){
		return this.hasModifier("static");
	}

	/** finalな属性か否か */
	private boolean isFinal(){
		return this.hasModifier("final");
	}

	/** 指定された語をモディファイアに持つか否か */
	private boolean hasModifier(String keyword){
		boolean result = false;
		if (keyword != null){
			for(NdExtendedModifier m : this.ndAttribute.getModifierList()){
				if (m instanceof NdModifier){
					if (keyword.equals(((NdModifier)m).getKeyword())){
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}
	
	/** 無視する型か否か */
	public boolean isDdbIgnore(){
		return KEY_Ignore.equals(this.getDdbTypeKey());
	}

	//DDB ManyToMany型か否か
	public boolean isDdbManyToMany(){
		return this.isDdbSupportingCollection() && this.hasAnnotationKeywords(new String[]{"@ManyToMany"});
	}

	//DDB OneToMany型か否か
	public boolean isDdbOneToMany(){
		return this.isDdbSupportingCollection() && this.hasAnnotationKeywords(new String[]{"@OneToMany"});
	}

	//サポートするCollectionか否か
	//Collectionクラス名は対象のものに限る
	//ジェネリクス型に限る
	//型引数は1つに限る
	//要素型のチェックはしていない
	private boolean isDdbSupportingCollection(){
		boolean result = false;
		if (this.ndAttribute != null && this.isParameterizedType()){
			NdAbstractTypeLink typeLink = this.getNdAttributeType();
			if (typeLink instanceof NdParameterizedTypeLink){
				NdParameterizedTypeLink pLink = (NdParameterizedTypeLink)typeLink;
				List<NdAbstractTypeLink> arguments = pLink.getTypeArgumentList();
				if (arguments != null && arguments.size() == 1){
					String cllectionClassName = "";
					if (pLink.getTypeName().isQualifiedName()){
						cllectionClassName = pLink.getTypeName().getQualifiedName();
					} else{
						cllectionClassName = pLink.getTypeName().getSimpleName();
					}
					result = SUPPORT_COLLECTION_CLASS_NAMES.contains(cllectionClassName);
				}
			}
		}
		return result;
	}

	/** 属性型がドメインクラスの場合のBdUserClass */
	private BdUserClass attributeTypeBdUserClass = null;

	/** 属性型がドメインクラスの場合そのBdUserClassを応答する */
	public synchronized BdUserClass getAttributeTypeBdUserClass(){
		if (this.attributeTypeBdUserClass == null){
			String typeName = this.getNdAttributeType().getSimpleOrQualifiedName();
			if (this.getBdUserClass() != null && this.getBdUserClass().getBdUserPackage() != null){
				BdUserProject userProject = this.getBdUserClass().getBdUserPackage().getUserProject();
				if (userProject != null){
					this.attributeTypeBdUserClass = userProject.getEntityAnnotatedDomainClassByName(typeName);
				}
			}
		}
		return this.attributeTypeBdUserClass;
	}

	/** 属性がコレクション型でその要素がドメインクラスである場合のBdUserClass */
	private BdUserClass collectionElementTypeBdUserClass = null;

	/** 属性がコレクション型でその要素がドメインクラスである場合のBdUserClassを応答する */
	public synchronized BdUserClass getCollectionElementBdUserClass(){
		if (this.collectionElementTypeBdUserClass == null){
			String collectionElementTypeName = this.getCollectionElementTypeName();
			if (this.getBdUserClass() != null && this.getBdUserClass().getBdUserPackage() != null){
				BdUserProject userProject = this.getBdUserClass().getBdUserPackage().getUserProject();
				if (userProject != null){
					this.collectionElementTypeBdUserClass = userProject.getEntityAnnotatedDomainClassByName(collectionElementTypeName);
				}
			}
		}
		return this.collectionElementTypeBdUserClass;
	}

	/** List<T>のTのクラス名を応答する */
	public String getCollectionElementTypeName(){
		String result = "";
		if (this.isDdbSupportingCollection()){
			NdParameterizedTypeLink pLink = (NdParameterizedTypeLink)this.getNdAttributeType();
			result = pLink.getTypeArgumentList().get(0).getSimpleOrQualifiedName();
		}
		return result;
	}

	//DDB ManyToOne型か否か
	public boolean isDdbManyToOne(){
		return this.ndAttribute != null
				&& this.isSimpleType() 
				&& ! this.isDdbJavaPrimitiveClass()
				&& this.hasAnnotationKeywords(new String[]{"@ManyToOne"});
	}

	//DDB 埋込み型 OneToOne型か否か
	public boolean isDdbEmbedded(){
		return this.ndAttribute != null
				&& this.isSimpleType() 
				&& ! this.isDdbJavaPrimitiveClass()
				&& this.hasAnnotationKeywords(new String[]{"@OneToOne", "@Embedded"});
	}

	//DDB プリミティブのオブジェクト型か否か
	//String, Date, BigDecimal, Booleanは個別の生成パターンが必要でプリミティブではない
	//BooleanはJavaPrimitiveClassだが固有の生成パターンが必要なためDDBJavaPrimitiveClassではない。
	public boolean isDdbJavaPrimitiveClass(){
		return this.ndAttribute != null
				&& this.isSimpleType()
				&& this.isJavaPrimitiveClass()
				&& ! this.isDdbString()
				&& ! this.isDdbDate()
				&& ! this.isDdbBigDecimal()
				&& ! this.isDdbBoolean();
	}

	//Javaプリミティブラッパー型か否か
	private boolean isJavaPrimitiveClass() {
		String checkName = this.getNdAttributeType().getTypeName().getSimpleName();
		return PRIMITIVE_OBJECT_TYPE_NAMES.contains(checkName);
	}

	//DDB Boolean型か否か
	public boolean isDdbBoolean(){
		return this.ndAttribute != null 
				&& this.isSimpleType() 
				&& "Boolean".equals(this.getNdAttributeType().getTypeName().getSimpleName());
	}

	//DDB Enum型か否か
	public boolean isDdbEnum(){
		return this.hasAnnotationKeywords(new String[]{"@Enumerated"});
	}

	//DDB 文字列型か否か
	public boolean isDdbString(){
		return this.ndAttribute != null 
				&& this.isSimpleType() 
				&& "String".equals(this.getNdAttributeType().getTypeName().getSimpleName());
	}

	//DDB 日付型か否か
	public boolean isDdbDate(){
		return this.ndAttribute != null 
				&& this.isSimpleType() 
				&& "Date".equals(this.getNdAttributeType().getTypeName().getSimpleName());
	}

	//DDB BigDecimal型か否か
	public boolean isDdbBigDecimal(){
		return this.ndAttribute != null 
				&& this.isSimpleType() 
				&& "BigDecimal".equals(this.getNdAttributeType().getTypeName().getSimpleName());
	}

	//指定されたキーワードのアノテーションを持つか否か
	private boolean hasAnnotationKeywords(String[] keywords){
		boolean result = false;
		if (this.ndAttribute.getModifierList() != null){
			for(NdExtendedModifier modifier : this.ndAttribute.getModifierList()){
				if (modifier instanceof NdAnnotation){
					NdAnnotation annotation = (NdAnnotation)modifier;
					for(String keyword : keywords){
						if (keyword.equals(annotation.getKeyword())){
							result = true;
							break;
						}
					}
				}
				if (result) break; 
			}
		}
		return result;
	}

	/** 属性タイプを応答する */
	public NdAbstractTypeLink getNdAttributeType(){
		return this.ndAttribute.getAttributeType();
	}

	/** 属性定義行を応答する */
	public String getHeadLine(){
		return this.ndAttribute.getHeadLine();
	}

	/** Javadocからタイトルを応答する */
	public String getJavadocTitle(){
		return formatJavadoc(ndAttribute.getJavadoc());
	}

	/** 属性名を応答する */
	public String getName(){
		return this.ndAttribute != null ? this.ndAttribute.getName() : "";
	}

	/** SimpleType型の型名を応答する */
	public String getSimpleOrQualifiedName(){
		return this.isSimpleType() ? this.getNdAttributeType().getSimpleOrQualifiedName() : "Unknown";
	}

	/** 先頭を大文字にした名前を応答する */
	public String getUpperStartedName(){
		String result = this.getName();
		if (result.length() > 1){
			result = result.substring(0, 1).toUpperCase() + result.substring(1);
		} else if(result.length() == 1){
			result = result.toUpperCase();
		}
		return result;
	}

	/** Beans仕様のgetter名を応答する */
	public String getGetterName(){
		return this.getterName;
	}

	/**
	 * Beans仕様のsetter名を応答する */
	public String getSetterName(){
		return this.setterName;
	}

	/** Bean仕様のgetter/setter名を設定する */
	private void buildGetterSetterName(){
		this.getterName = "";
		this.setterName = "";
		String body = this.getUpperStartedName();
		//ケース1
		if (!"".equals(body)){
			this.getterName = "get" + body;
			this.setterName = "set" + body;
		}
		//ケース2 Booleanはケース1
		if (this.isPrimitiveType()){
			if ("boolean".equals(ndAttribute.getAttributeType().getSimpleOrQualifiedName())){
				if (body.startsWith("Is")){
					String body2 = body;
					if (body.length() > 2){
						body2 = body.substring(2);
					}
					this.getterName = "is" + body2;
					this.setterName = "set" + body2;
				}else{
					this.getterName = "is" + body;
				}
			}
		}
	}

	/** 所属するクラス(BdUserClass)を応答する */
	public BdUserClass getBdUserClass() {
		return bdUserClass;
	}

	//NdAttributeに委譲
	public boolean isArrayType(){
		return this.ndAttribute.getAttributeType().isArrayType();
	}

	public boolean isParameterizedType(){
		return this.ndAttribute.getAttributeType().isParameterizedType();
	}

	public boolean isPrimitiveType(){
		return this.ndAttribute.getAttributeType().isPrimitiveType();
	}

	public boolean isQualifiedType(){
		return this.ndAttribute.getAttributeType().isQualifiedType();
	}

	public boolean isSimpleType(){
		return this.ndAttribute.getAttributeType().isSimpleType();
	}

	public boolean isWildcardType(){
		return this.ndAttribute.getAttributeType().isWildcardType();
	}

	/**
	 * デバッグ
	 */
	public void debugPrint(String tab){
		NdAbstractTypeLink typeLink = ndAttribute.getAttributeType();
		String msg = " : " + typeLink.getSimpleOrQualifiedName();
		if (typeLink.isParameterizedType()){
			List<NdAbstractTypeLink> list = ((NdParameterizedTypeLink)typeLink).getTypeArgumentList();
			for(NdAbstractTypeLink link : list){
				msg += "<" + link.getSimpleOrQualifiedName() + ">";
			}
		}
		if (this.ndAttribute.getAttributeType().isArrayType()){
			msg += " [配列]";
		} else if (this.ndAttribute.getAttributeType().isParameterizedType()) {
			msg += " [型引数型]";
		} else if (this.ndAttribute.getAttributeType().isPrimitiveType()) {
			msg += " [プリミティブ]";
		} else if (this.ndAttribute.getAttributeType().isQualifiedType()) {
			msg += " [出現しないはず]";
		} else if (this.ndAttribute.getAttributeType().isSimpleType()) {
			msg += " [シンプル]";
		} else if (this.ndAttribute.getAttributeType().isWildcardType()) {
			msg += " [ワイルドカード]";
		} else {
			msg += " [不明]";
		}
		msg += "[" + this.getDdbTypeKey() + "]";
		msg += " title=" + this.getJavadocTitle();

		NdLogger.getInstance().debug(tab + "ATTRIBUTE: " + this.getName() + " " + msg + "[" + this.getterName + "/" + this.setterName + "]");
	}
}
