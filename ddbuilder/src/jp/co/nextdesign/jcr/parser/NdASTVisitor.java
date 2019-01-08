/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.parser;

import java.util.HashMap;
import java.util.Map;

import jp.co.nextdesign.jcr.model.core.NdAbstractClassifier;
import jp.co.nextdesign.jcr.model.core.NdAbstractMethod;
import jp.co.nextdesign.jcr.model.core.NdAnnotationType;
import jp.co.nextdesign.jcr.model.core.NdAttribute;
import jp.co.nextdesign.jcr.model.core.NdClass;
import jp.co.nextdesign.jcr.model.core.NdCompilationUnit;
import jp.co.nextdesign.jcr.model.core.NdEnum;
import jp.co.nextdesign.jcr.model.core.NdImportDeclaration;
import jp.co.nextdesign.jcr.model.core.NdInitializer;
import jp.co.nextdesign.jcr.model.core.NdInterface;
import jp.co.nextdesign.jcr.model.core.NdPackageDeclaration;
import jp.co.nextdesign.jcr.model.expression.NdClassInstanceCreation;
import jp.co.nextdesign.jcr.model.statement.NdAbstractStatement;
import jp.co.nextdesign.jcr.parser.reflector.NdAnnotationTypeDeclarationReflector;
import jp.co.nextdesign.jcr.parser.reflector.NdAnonymousClassDeclarationReflector;
import jp.co.nextdesign.jcr.parser.reflector.NdEnumDeclarationReflector;
import jp.co.nextdesign.jcr.parser.reflector.NdInitializerReflector;
import jp.co.nextdesign.jcr.parser.reflector.NdTypeDeclarationReflector;
import jp.co.nextdesign.util.logging.NdLogger;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * 抽象構文木（AST）に対するVisitorクラス（Visitorパターン）
 * @author murayama
 */
public class NdASTVisitor extends ASTVisitor {

	private NdTypeDeclarationReflector typeDeclarationReflector = new NdTypeDeclarationReflector();
	private NdAnonymousClassDeclarationReflector anonymousClassDeclarationReflector = new NdAnonymousClassDeclarationReflector();
	private NdAnnotationTypeDeclarationReflector annotationTypeDeclarationReflector = new NdAnnotationTypeDeclarationReflector();
	private NdEnumDeclarationReflector enumDeclarationReflector = new NdEnumDeclarationReflector();
	private NdInitializerReflector initializerReflector = new NdInitializerReflector();
	private NdCompilationUnit compilationUnit;
	/**
	 * 要素キャッシュ　クラス、内部クラス、無名クラス
	 */
	private Map<ASTNode, NdAbstractClassifier> classifierCache = new HashMap<ASTNode, NdAbstractClassifier>();
	/**
	 * 要素キャッシュ　属性フィールド
	 */
	private Map<ASTNode, NdAttribute> attributeCache = new HashMap<ASTNode, NdAttribute>();
	/**
	 * 要素キャッシュ　メソッド、初期化子
	 */
	private Map<ASTNode, NdAbstractMethod> methodCache = new HashMap<ASTNode, NdAbstractMethod>();
	/**
	 * 要素キャッシュ　ステートメント
	 */
	private Map<ASTNode, NdAbstractStatement> statementCache = new HashMap<ASTNode, NdAbstractStatement>();
	/**
	 * 要素キャッシュ　アノテーションタイプ
	 */
	private Map<ASTNode, NdAnnotationType> annotationCache = new HashMap<ASTNode, NdAnnotationType>();
	/**
	 * 要素キャッシュ　列挙型
	 */
	private Map<ASTNode, NdEnum> enumCache = new HashMap<ASTNode, NdEnum>();

	/**
	 * 無名クラス管理情報
	 */
	private static final String ANONYMOUS_CLASS_NAME_PREFIX = "無名クラス";
	private int anonymousClassNameSequence;
	
	/**
	 * コンストラクタ
	 * @param compilationUnit
	 */
	public NdASTVisitor(NdCompilationUnit compilationUnit){
		super();
		this.compilationUnit = compilationUnit;
		this.anonymousClassNameSequence = 1;
	}
	
	/**
	 * visit ClassInstanceCreation インスタンス生成式
	 */
	@Override public boolean visit(ClassInstanceCreation node){
		NdAttribute attribute = this.findAttributeFromCache(node);
		if (attribute != null){
			NdClassInstanceCreation creation = new NdClassInstanceCreation(node);
			attribute.addClassInstanceCreation(creation);
		} else {
			NdAbstractStatement statement = this.findStatementFromCache(node);
			if (statement != null){
				NdClassInstanceCreation creation = new NdClassInstanceCreation(node);
				statement.addClassInstanceCreation(creation);
			} else {
				NdLogger.getInstance().error(this.getClass().getSimpleName() + "#visit インスタンス生成式:設定先不明エラー", null);
			}
		}
		return super.visit(node);
	}
	
	/**
	 * キャッシュにある親要素（NdAttribute）を見つけて応答する
	 * @param node
	 * @return
	 */
	private NdAttribute findAttributeFromCache(ASTNode node){
		NdAttribute result = null;
		if (node != null){
			if (this.attributeCache.containsKey(node)){
				result = this.attributeCache.get(node);
			}
			if (result == null){
				ASTNode parent = node.getParent();
				result = this.findAttributeFromCache(parent);
			}
		}
		return result;
	}

	/**
	 * キャッシュにある親要素（NdStatement/NdInitializer）を見つけて応答する
	 * @param node
	 * @return
	 */
	private NdAbstractStatement findStatementFromCache(ASTNode node){
		NdAbstractStatement result = null;
		if (node != null){
			if (this.statementCache.containsKey(node)){
				result = this.statementCache.get(node);
			}
			if (result == null){
				ASTNode parent = node.getParent();
				result = this.findStatementFromCache(parent);
			}
		}
		return result;
	}
	
	/**
	 * visit アノテーション型宣言
	 */
	@Override public boolean visit(AnnotationTypeDeclaration node){
		NdAnnotationType newAnnotationType = new NdAnnotationType(node);
		newAnnotationType.setIsAnnotationType(true);
		this.compilationUnit.addClassifier(newAnnotationType);	
		annotationTypeDeclarationReflector.reflect(node, newAnnotationType, this);
		return super.visit(node);
	}
	
	/**
	 * visit 列挙型宣言
	 */
	@Override public boolean visit(EnumDeclaration node){
		NdEnum newEnum = null;
		if (!isInnerType(node)){
			newEnum = new NdEnum(node);
			newEnum.setIsEnum(true);
			this.compilationUnit.addClassifier(newEnum);	
			enumDeclarationReflector.reflect(node, newEnum, this);
		} else {
			NdAbstractClassifier parentClass = this.getParentClassOfInnerClass(node);
			if (parentClass != null){
				NdEnum innerEnum = new NdEnum(node);
				innerEnum.setIsEnum(true);
				parentClass.addInnerEnum(innerEnum);
				this.compilationUnit.addClassifier(innerEnum);
				this.classifierCache.put(node, innerEnum);
				enumDeclarationReflector.reflect(node, innerEnum, this);
			} else {
				NdLogger.getInstance().error("処理不能なケース:内部列挙型処理のGetParent()==null " + this.getClass().getName(), null);
			}
		}
		return super.visit(node);
	}

	/**
	 * visit 初期化子
	 */
	@Override public boolean visit(Initializer node){
		NdAbstractClassifier parentClass = this.getParentClassOfInitializer(node);
		if (parentClass != null){
			NdInitializer initializer = new NdInitializer();
			parentClass.addInitializer(initializer);
			initializerReflector.reflect(node, initializer, this);
		} else {
			NdLogger.getInstance().error("処理不能なケース:初期化子処理のGetParent()==null " + this.getClass().getName(), null);
		}
		return super.visit(node);
	}
	
	/**
	 * 初期化子の親クラスをクラスキャッシュから取得する
	 * 正しい収容先（親）が見つかるまで順次親を検査する
	 * 2011.10.7 親がアノテーション、列挙型はありえないようだ。特に理由はないが、コードはこのままにしておく。
	 * @param node
	 * @return
	 */
	private NdAbstractClassifier getParentClassOfInitializer(ASTNode node){
		NdAbstractClassifier result = null;
		boolean foundAsType = false;
		boolean foundAsAnonymous = false;
		boolean foundAsEnum = false;
		ASTNode parentNode = node.getParent();
		while ((!foundAsType) && (!foundAsAnonymous) && (!foundAsEnum) && (parentNode != null)){
			if ((parentNode.getNodeType() == ASTNode.TYPE_DECLARATION)
			|| (parentNode.getNodeType() == ASTNode.ANONYMOUS_CLASS_DECLARATION)){
				foundAsType = true;
				break;
			} else if(parentNode.getNodeType() == ASTNode.ANNOTATION_TYPE_DECLARATION){
				foundAsAnonymous = true;
				break;
			} else if(parentNode.getNodeType() == ASTNode.ENUM_DECLARATION){
				foundAsEnum = true;
				break;
			} else {
				if (parentNode != null){
					parentNode = parentNode.getParent();
				}
			}
		}
		if (foundAsType){
			if (this.classifierCache.containsKey(parentNode)){
				result = this.classifierCache.get(parentNode);
			}
		} else if (foundAsAnonymous){
			if (this.annotationCache.containsKey(parentNode)){
				result = this.annotationCache.get(parentNode);
			}
		} else if (foundAsEnum){
			if (this.enumCache.containsKey(parentNode)){
				result = this.enumCache.get(parentNode);
			}
		}
		return result;
	}

	/**
	 * 無名クラス用の仮名を応答する
	 * @return
	 */
	private String getNewAnonymousName(){
		String result = ANONYMOUS_CLASS_NAME_PREFIX + this.anonymousClassNameSequence;
		this.anonymousClassNameSequence++;
		return result;
	}
	
	/**
	 * visit 無名クラス定義
	 */
	@Override public boolean visit(AnonymousClassDeclaration node){
		NdAbstractClassifier parentClass = this.getParentClassOfAnonymousClass(node);
		if (parentClass != null){
			NdClass anonymousClass = new NdClass(this.getNewAnonymousName());
			this.classifierCache.put(node, anonymousClass);
			anonymousClass.setIsAnonymousClass(true);
			parentClass.addAnonymousClass(anonymousClass);
			this.compilationUnit.addClassifier(anonymousClass);
			anonymousClassDeclarationReflector.reflect(node, anonymousClass, this);
		} else {
			NdLogger.getInstance().error("処理不能なケース:無名クラス処理のGetParent()==null " + this.getClass().getName(), null);
		}
		return super.visit(node);
	}

	/**
	 * visit インポート宣言
	 */
	@Override public boolean visit(ImportDeclaration node){
		NdImportDeclaration newImport = new NdImportDeclaration(node);
		this.compilationUnit.addImportDeclaration(newImport);
		return super.visit(node);
	}

	/**
	 * visit パッケージ宣言
	 */
	@Override public boolean visit(PackageDeclaration node){
		NdPackageDeclaration newDeclaration = new NdPackageDeclaration(node);
		this.compilationUnit.setPackageDeclaration(newDeclaration);
		return super.visit(node);
	}

	/**
	 * visit タイプ宣言
	 */
	@Override public boolean visit(TypeDeclaration node){
		NdAbstractClassifier newClass = null;
		if (!isInnerType(node)){
			if (!node.isInterface()){
				newClass = new NdClass(node);
			} else {
				newClass = new NdInterface(node);
			}
			this.compilationUnit.addClassifier(newClass);	
			this.classifierCache.put(node, newClass);
			typeDeclarationReflector.reflect(node, newClass, this);
		} else {
			NdAbstractClassifier parentClass = this.getParentClassOfInnerClass(node);
			if (parentClass != null){
				NdClass innerClass = new NdClass(node);
				innerClass.setIsInnerClass(true);
				parentClass.addInnerClass(innerClass);
				this.compilationUnit.addClassifier(innerClass);
				this.classifierCache.put(node, innerClass);
				typeDeclarationReflector.reflect(node, innerClass, this);
			} else {
				NdLogger.getInstance().error("処理不能なケース:内部クラス処理のGetParent()==null " + this.getClass().getName(), null);
			}
		}
		return super.visit(node);
	}

	/**
	 * 内部クラスか否か or クラス内に定義された列挙型か否かを応答する
	 * @param node
	 * @return
	 */
	private boolean isInnerType(AbstractTypeDeclaration node){
		boolean result = true;
		ASTNode parentNode = node.getParent();
		if ((parentNode != null) && (parentNode.getNodeType() == ASTNode.COMPILATION_UNIT)){
			result = false;
		}
		return result;
	}
	
	/**
	 * 内部クラスの親をクラスキャッシュから取得して応答する
	 * 正しい収容先を見つける
	 * @param node
	 * @return
	 */
	private NdAbstractClassifier getParentClassOfInnerClass(ASTNode node){
		NdAbstractClassifier result = null;
		ASTNode parentNode = node.getParent();
		if ((parentNode != null) && (parentNode.getNodeType() == ASTNode.TYPE_DECLARATION)){
			if (this.classifierCache.containsKey(parentNode)){
				result = this.classifierCache.get(parentNode);
			}
		}
		return result;
	}
	
	/**
	 * 無名クラスの親をクラスキャッシュから取得して応答する
	 * 正しい収容先が見つかるまで順次親を検査する
	 * @param node
	 * @return
	 */
	private NdAbstractClassifier getParentClassOfAnonymousClass(ASTNode node){
		NdAbstractClassifier result = null;
		boolean found = false;
		ASTNode parentNode = node.getParent();
		while ((!found) && (parentNode != null)){
			if (parentNode.getNodeType() == ASTNode.TYPE_DECLARATION){
				found = true;
				break;
			} else {
				if (parentNode != null){
					parentNode = parentNode.getParent();
				}
			}
		}
		if (found){
			if (this.classifierCache.containsKey(parentNode)){
				result = this.classifierCache.get(parentNode);
			}
		}
		return result;
	}
	
	/**
	 * 属性をキャッシュする
	 * @param key
	 * @param value
	 */
	public void putAttributeCache(ASTNode key, NdAttribute value){
		if (!this.attributeCache.containsKey(key)){
			this.attributeCache.put(key, value);
		}
	}

	/**
	 * メソッド、初期化子をキャッシュする
	 * @param key
	 * @param value
	 */
	public void putMethodCache(ASTNode key, NdAbstractMethod value){
		if (!this.methodCache.containsKey(key)){
			this.methodCache.put(key, value);
		}
	}

	/**
	 * ステートメントをキャッシュする
	 * @param key
	 * @param value
	 */
	public void putStatementCache(ASTNode key, NdAbstractStatement value){
		if (!this.statementCache.containsKey(key)){
			this.statementCache.put(key, value);
		}
	}

	/**
	 * アノテーションタイプをキャッシュする
	 * @param key
	 * @param value
	 */
	public void putAnnotationCache(ASTNode key, NdAnnotationType value){
		if (!this.annotationCache.containsKey(key)){
			this.annotationCache.put(key, value);
		}
	}

	/**
	 * 列挙型をキャッシュする
	 * @param key
	 * @param value
	 */
	public void putEnumCache(ASTNode key, NdEnum value){
		if (!this.enumCache.containsKey(key)){
			this.enumCache.put(key, value);
		}
	}	
}
