/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.user;

import java.util.List;

import jp.co.nextdesign.jcr.model.core.NdAbstractClassifier;
import jp.co.nextdesign.jcr.model.typelink.NdAbstractTypeLink;
import jp.co.nextdesign.jcr.model.typelink.NdParameterizedTypeLink;

/**
 * サービスメソッドの戻り値と引数の基底クラス
 * @author murayama
 *
 */
public abstract class BdUserServiceMethodValueBase {
	
	private String ddbKey = "";
	private NdAbstractTypeLink ndAbstractTypeLink;
	protected BdUserServiceMethod bdUserServiceMethod;

	/** コンストラクタ */
	public BdUserServiceMethodValueBase(NdAbstractTypeLink ndAbstractTypeLink, BdUserServiceMethod bdServiceMethod){
		super();
		this.ndAbstractTypeLink = ndAbstractTypeLink;
		this.bdUserServiceMethod = bdServiceMethod;
		this.decideDdbKey();
	}

	/** 
	 * DDB型分類キーを決める
	 * 属性型、引数型、戻り値型が持つBdDdbKeyクラスの導入を検討したが、
	 * BdUserServiceMethodValueBaseとBdUserAttributeではddbKeyの決定方法が違うので、導入は保留とする。
	 */
	private void decideDdbKey(){
		this.ddbKey = "";
		if (this.isIgnoreType()){
			this.ddbKey = BdUserAttribute.KEY_Ignore;
		} else if (this.isDdbJavaPrimitiveClass()){
			this.ddbKey = BdUserAttribute.KEY_JavaPrimitiveClass;
		} else if (this.isDdbString()){
			this.ddbKey = BdUserAttribute.KEY_String;
		} else if (this.isDdbDate()){
			this.ddbKey = BdUserAttribute.KEY_Date;
		} else if (this.isDdbBigDecimal()){
			this.ddbKey = BdUserAttribute.KEY_BigDecimal;
		} else if (this.isDdbBoolean()){
			this.ddbKey = BdUserAttribute.KEY_Boolean;
		} else if (this.isDdbEnum()){
			this.ddbKey = BdUserAttribute.KEY_Enum;
		} else if (this.isDdbEmbedded()){
			this.ddbKey = BdUserAttribute.KEY_Embedded;
		} else if (this.isDdbManyToOne()){
			this.ddbKey = BdUserAttribute.KEY_ManyToOne;
		} else if (this.isDdbOneToMany()){
			this.ddbKey = BdUserAttribute.KEY_OneToMany;
		} else if (this.isDdbManyToMany()){
			this.ddbKey = BdUserAttribute.KEY_ManyToMany;
		}
	}

	/** 有効なDDB型か否か */
	public boolean isAvailableDdbTypeKey(){
		return (! "".equals(this.ddbKey)) && (! BdUserAttribute.KEY_Ignore.equals(this.ddbKey));
	}

	/**
	 * DDB型分類キーを応答する
	 * @return
	 */
	public String getDdbTypeKey(){
		return this.ddbKey;
	}

	/** 無視する属性 2015.7.7時点では"serialVersionUID", "TEMS_PER_PAGE" がある */
	private boolean isIgnoreType(){
		return false;  //戻り値、引数では発生しない
	}
	
	//DDB ManyToMany型か否か
	public boolean isDdbManyToMany(){
		return false; //戻り値、引数では使用しない
	}

	//DDB 埋込み型 OneToOne型か否か
	public boolean isDdbEmbedded(){
		return false; //戻り値、引数では使用しない
	}

	//DDB OneToMany型か否か
	public boolean isDdbOneToMany(){
		return this.isDdbSupportingCollection();
	}
	//DDB ManyToOne型か否か
	public boolean isDdbManyToOne(){
		return this.ndAbstractTypeLink != null
				&& this.isSimpleType() 
				&& ! this.isJavaPrimitiveClass()
				&& ! this.isJavaStandardDataClass()
				&& ! this.isDdbEnum();
	}

	//DDB プリミティブのオブジェクト型か否か
	//String, Date, BigDecimal, Booleanは個別の生成パターンが必要なため除外する
	//BooleanはJavaPrimitiveClassだが固有の生成パターンが必要なためDDBJavaPrimitiveClassではない。
	public boolean isDdbJavaPrimitiveClass(){
		return this.ndAbstractTypeLink != null
				&& this.isSimpleType()
				&& this.isJavaPrimitiveClass()
				&& ! this.isDdbString()
				&& ! this.isDdbDate()
				&& ! this.isDdbBigDecimal()
				&& ! this.isDdbBoolean();
		}

	//Javaプリミティブラッパー型か否か
	private boolean isJavaPrimitiveClass() {
		String checkName = this.ndAbstractTypeLink.getTypeName().getSimpleName();
		return BdUserAttribute.PRIMITIVE_OBJECT_TYPE_NAMES.contains(checkName);
	}

	//Java標準ライブラリに内のクラス化否か
	private boolean isJavaStandardDataClass(){
		return isDdbDate() || isDdbString() || isDdbBigDecimal();
	}

	//DDB Boolean型か否か
	public boolean isDdbBoolean(){
		return this.ndAbstractTypeLink != null 
				&& this.isSimpleType() 
				&& "Boolean".equals(this.ndAbstractTypeLink.getTypeName().getSimpleName());
	}

	//DDB Enum型か否か
	public boolean isDdbEnum(){
		boolean result = false;
		if (this.ndAbstractTypeLink != null && this.isSimpleType()){
			BdUserProject userProject = this.getUserProject();
			NdAbstractClassifier found = userProject.getDomainEnumClassifierByName(this.ndAbstractTypeLink.getTypeName().getSimpleName());
			result = found != null;
		}
		return result;
	}

	//DDB 文字列型か否か
	public boolean isDdbString(){
		return this.ndAbstractTypeLink != null 
				&& this.isSimpleType() 
				&& "String".equals(this.ndAbstractTypeLink.getTypeName().getSimpleName());
	}

	//DDB 日付型か否か
	public boolean isDdbDate(){
		return this.ndAbstractTypeLink != null 
				&& this.isSimpleType() 
				&& "Date".equals(this.ndAbstractTypeLink.getTypeName().getSimpleName());
	}
	
	//DDB BigDecimal型か否か
	public boolean isDdbBigDecimal(){
		return this.ndAbstractTypeLink != null 
				&& this.isSimpleType() 
				&& "BigDecimal".equals(this.ndAbstractTypeLink.getTypeName().getSimpleName());
	}
	
	/** SimpleType型の型名を応答する */
	public String getSimpleOrQualifiedName(){
		return this.isSimpleType() ? this.ndAbstractTypeLink.getTypeName().getSimpleName() : "Unknown";
	}

	//サポートするCollectionか否か
	//Collectionクラス名は対象のものに限る
	//ジェネリクス型に限る
	//型引数は1つに限る
	//要素型のチェックはしていない
	private boolean isDdbSupportingCollection(){
		boolean result = false;
		if (this.ndAbstractTypeLink != null && this.isParameterizedType()){
			if (this.ndAbstractTypeLink instanceof NdParameterizedTypeLink){
				NdParameterizedTypeLink pLink = (NdParameterizedTypeLink)this.ndAbstractTypeLink;
				List<NdAbstractTypeLink> arguments = pLink.getTypeArgumentList();
				if (arguments != null && arguments.size() == 1){
					String cllectionClassName = "";
					if (pLink.getTypeName().isQualifiedName()){
						cllectionClassName = pLink.getTypeName().getQualifiedName();
					} else{
						cllectionClassName = pLink.getTypeName().getSimpleName();
					}
					result = BdUserAttribute.SUPPORT_COLLECTION_CLASS_NAMES.contains(cllectionClassName);
				}
			}
		}
		return result;
	}
	
	//NdAbstractTypeLinkに委譲
	public boolean isArrayType(){
		return this.ndAbstractTypeLink.isArrayType();
	}

	public boolean isParameterizedType(){
		return this.ndAbstractTypeLink.isParameterizedType();
	}

	public boolean isPrimitiveType(){
		return this.ndAbstractTypeLink.isPrimitiveType();
	}

	public boolean isQualifiedType(){
		return this.ndAbstractTypeLink.isQualifiedType();
	}

	public boolean isSimpleType(){
		return this.ndAbstractTypeLink.isSimpleType();
	}

	public boolean isWildcardType(){
		return this.ndAbstractTypeLink.isWildcardType();
	}

	/** 戻り値／引数がドメインクラスの場合のBdUserClass */
	private BdUserClass parameterTypeBdUserClass = null;

	/** 戻り値／引数がドメインクラスの場合そのBdUserClassを応答する */
	public synchronized BdUserClass getParameterTypeBdUserClass(){
		if (this.parameterTypeBdUserClass == null){
			String typeName = this.ndAbstractTypeLink.getTypeName().getSimpleName();
				BdUserProject userProject = this.getUserProject();
				if (userProject != null){
					this.parameterTypeBdUserClass = userProject.getEntityAnnotatedDomainClassByName(typeName);
				}
		}
		return this.parameterTypeBdUserClass;
	}

	/** 戻り値／引数がコレクション型でその要素がドメインクラスである場合のBdUserClass */
	private BdUserClass collectionElementTypeBdUserClass = null;

	/** 戻り値／引数がコレクション型でその要素がドメインクラスである場合のBdUserClassを応答する */
	public synchronized BdUserClass getCollectionElementBdUserClass(){
		if (this.collectionElementTypeBdUserClass == null){
			String collectionElementTypeName = this.getCollectionElementTypeName();
			BdUserProject userProject = this.getUserProject();
			if (userProject != null){
				this.collectionElementTypeBdUserClass = userProject.getEntityAnnotatedDomainClassByName(collectionElementTypeName);
			}
		}
		return this.collectionElementTypeBdUserClass;
	}
	
	//ユーザプロジェクトを応答する
	private BdUserProject getUserProject(){
		return this.bdUserServiceMethod.getBdServiceClass().getBdUserClass().getBdUserPackage().getUserProject();
	}
	
	/** List<T>のTのクラス名を応答する */
	public String getCollectionElementTypeName(){
		String result = "";
		if (this.isDdbSupportingCollection()){
			NdParameterizedTypeLink pLink = (NdParameterizedTypeLink)this.ndAbstractTypeLink;
			result = pLink.getTypeArgumentList().get(0).getSimpleOrQualifiedName();
		}
		return result;
	}
	
	/** BdUserServiceMethodを応答する */
	public BdUserServiceMethod getBdUserServiceMethod(){
		return this.bdUserServiceMethod;
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
	
	/** 名前を応答する */
	public String getName(){
		return "";
	}
	
	/** 戻り値／引数のタイトルを応答する */
	public abstract String getTitle();
	
}
