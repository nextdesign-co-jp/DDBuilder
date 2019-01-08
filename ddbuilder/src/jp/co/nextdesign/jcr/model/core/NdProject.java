/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.co.nextdesign.jcr.NdApplicationPropertyException;
import jp.co.nextdesign.jcr.NdConstants;
import jp.co.nextdesign.jcr.loader.NdProjectLoader;
import jp.co.nextdesign.jcr.model.NdName;
import jp.co.nextdesign.jcr.parser.NdParser;
import jp.co.nextdesign.jcr.parser.NdParserException;
import jp.co.nextdesign.util.NdUtilException;
import jp.co.nextdesign.util.logging.NdLogger;


/**
 * プロジェクト
 * @author murayama
 */
public class NdProject {
	
	private List<NdCompilationUnit> allCompilationUnitList = new ArrayList<NdCompilationUnit>();
	private List<NdCoreElement> allCoreElementList = new ArrayList<NdCoreElement>();
	private NdPackage rootPackage;
	int nextParseIndex = 0; //parseNext()でコンパイル単位（ソース）を１つづつ処理するために使用するインデックス
	NdParser parser = new NdParser();
	
	/**
	 * 参照名（NdName）を解決して、参照先（分類子）を応答する
	 * 参照名（name）は、どこ（nameOwner）で使われたかが分かってはじめて解決できる
	 * @param name
	 * @param nameOwner
	 * @return
	 */
	public NdAbstractClassifier resolveName(NdName name, NdAbstractClassifier nameOwner){
		NdAbstractClassifier result = null;
		if ((name != null) && (nameOwner != null)){
		List<NdAbstractClassifier> sameNameClassifierList = this.getClassifierListByName(name.getSimpleName()); //単純名で検索			
		if (name.isQualifiedName()){
			//限定名の場合
			result = this.resolveIfFullQualifieredName(name, sameNameClassifierList);
			if (result == null){
				//限定の場合(2)
				result = this.resolveIfQualifieredNameExcludingPackageName(name, nameOwner, sameNameClassifierList);
			}
		} else {
			//単純名の場合
			result = this.resolveIfSimpleName(nameOwner, sameNameClassifierList);
		}
		}
		return result;
	}

	/**
	 * 限定名の場合の参照先を解決する（パッケージ名を含まない限定名のはず）
	 * @param referenceName
	 * @param nameOwner
	 * @param sameNameClassifierList
	 * @return
	 */
	private NdAbstractClassifier resolveIfQualifieredNameExcludingPackageName(NdName referenceName, NdAbstractClassifier nameOwner, List<NdAbstractClassifier> sameNameClassifierList){
		NdAbstractClassifier result = null;
		result = this.resolveByInnerClassList(referenceName, nameOwner);
		if (result == null){
			result = this.resolveByAnonymousClassList(referenceName, nameOwner);
		}
		return result;
	}

	/**
	 * 完全限定名の場合の参照先を解決する
	 * 相手の完全限定名と一致すれば解決
	 * @param referenceName
	 * @param sameNameClassifierList
	 * @return
	 */
	private NdAbstractClassifier resolveIfFullQualifieredName(NdName referenceName, List<NdAbstractClassifier> sameNameClassifierList){
		NdAbstractClassifier result = null;
		for(NdAbstractClassifier candidate : sameNameClassifierList){
			//完全限定名の場合、相手の完全限定名と一致すれば解決
			if (referenceName.getQualifiedName().equals(candidate.getQualifiedName())){
				result = candidate;
				break;
			}
		}
		return result;
	}
	
	/**
	 * 単純名の場合の参照先を解決する
	 * 名前が一致＆インポートまたは同一パッケージ内であれば解決
	 * @param nameOwner
	 * @param sameNameClassifierList
	 * @return
	 */
	private NdAbstractClassifier resolveIfSimpleName(NdAbstractClassifier nameOwner, List<NdAbstractClassifier> sameNameClassifierList){
		NdAbstractClassifier result = null;
		for(NdAbstractClassifier candidate : sameNameClassifierList){
			if ((nameOwner.isImporting(candidate) || nameOwner.isClassifierOfSamePackage(candidate))){
				result = candidate;
				break;
			}
		}
		return result;
	}

	/**
	 * 内部クラス名として解決する（解決した場合はretun != null）
	 * @param referenceName
	 * @param nameOwner
	 * @return
	 */
	private NdAbstractClassifier resolveByInnerClassList(NdName referenceName, NdAbstractClassifier nameOwner){
		NdAbstractClassifier result = null;
		String[] splitted = referenceName.getQualifiedName().split("\\.");
		if (splitted.length > 0){
			NdAbstractClassifier topClassifier = null;
			String topName = splitted[0];
			List<NdAbstractClassifier> list = this.getClassifierListByName(topName);
			for(NdAbstractClassifier c : list){
				if ((nameOwner.isImporting(c) || nameOwner.isClassifierOfSamePackage(c))){
					topClassifier = c;
					break;
				}
			}
			if (topClassifier != null){
				boolean found = true;
				NdAbstractClassifier parent = topClassifier;
				for (int i=1; i<splitted.length; i++){
					String nextName = splitted[i];
					NdAbstractClassifier inner = parent.getInnerClassBySimpleName(nextName);
					if (inner != null) {
						parent = inner;
					} else {
						found = false;
						break;
					}
				}
				if (found){
					result = parent;
				}
			}
		}
		return result;
	}
	
	/**
	 * 無名クラス名として解決する（解決した場合はretun != null）
	 * @param referenceName
	 * @param nameOwner
	 * @return
	 */
	private NdAbstractClassifier resolveByAnonymousClassList(NdName referenceName, NdAbstractClassifier nameOwner){
		NdAbstractClassifier result = null;
		String[] splitted = referenceName.getQualifiedName().split("\\.");
		if (splitted.length > 0){
			NdAbstractClassifier topClassifier = null;
			String topName = splitted[0];
			List<NdAbstractClassifier> list = this.getClassifierListByName(topName);
			for(NdAbstractClassifier c : list){
				if ((nameOwner.isImporting(c) || nameOwner.isClassifierOfSamePackage(c))){
					topClassifier = c;
					break;
				}
			}
			if (topClassifier != null){
				boolean found = true;
				NdAbstractClassifier parent = topClassifier;
				for (int i=1; i<splitted.length; i++){
					String nextName = splitted[i];
					NdAbstractClassifier anonymous = parent.getAnonymousClassBySimpleName(nextName);
					if (anonymous != null) {
						parent = anonymous;
					} else {
						found = false;
						break;
					}
				}
				if (found){
					result = parent;
				}
			}
		}
		return result;
	}
	
	/**
	 * プロジェクトに含まれる同じ名前の分類子（クラス/インタフェース）のリストを応答する
	 * @param name
	 * @return
	 */
	public List<NdAbstractClassifier> getClassifierListByName(String name){
		List<NdAbstractClassifier> resultList = new ArrayList<NdAbstractClassifier>();
		for(NdCompilationUnit unit : this.allCompilationUnitList){
			resultList.addAll(unit.getClassByName(name));
		}
		return resultList;
	}
	
	/**
	 * プロジェクトを分析し情報を作成する
	 */
	public void analyzeProject(){
		this.buildCoreElementList();
		this.buildCrossReferences();
		this.buildPackageDependency();
		this.buildInheritenceRelationship();
	}
	
	/**
	 * 継承関連を作成する
	 */
	private void buildInheritenceRelationship(){
		for(NdCompilationUnit unit : this.allCompilationUnitList){
			for(NdAbstractClassifier c : unit.getClassifierList()){
				c.buildInheritenceRelationship();
			}
		}
	}	
	
	/**
	 * 全パッケージの依存関係リストを作成する
	 * 事前条件：各分類子のクロスリファレンスが作成されていること（buildCrossReferences()が終了していること）
	 */
	private void buildPackageDependency(){
		if (this.rootPackage != null){
			this.rootPackage.buildDependency();
		}
	}
	
	/**
	 * 全コンパイル単位内の全分類子のクラスリファレンスを作成する
	 */
	private void buildCrossReferences(){
		for(NdCompilationUnit unit : this.allCompilationUnitList){
			for(NdAbstractClassifier c : unit.getClassifierList()){
				c.buildCrossReference();
			}
		}
	}
	
	/**
	 * 全ての分類子・属性・メソッドのリストを作成する
	 */
	private void buildCoreElementList(){
		List<NdAbstractNamedElement> namedElementList = this.rootPackage.getNamedElementList();
		for(NdAbstractNamedElement element : namedElementList){
			if (element instanceof NdAbstractClassifier){
				NdAbstractClassifier classifier = (NdAbstractClassifier)element;
				this.allCoreElementList.add(classifier);
				List<NdAttribute> attributeList = classifier.getAttributeList();
				for(NdAttribute a : attributeList){
					this.allCoreElementList.add(a);
				}
				List<NdMethod> methodList = classifier.getMethodList();
				for(NdMethod m : methodList){
					this.allCoreElementList.add(m);
				}
			}
		}
		Collections.sort(this.allCoreElementList, new NdCoreElementComparator());
	}
	
	/**
	 * 全ての分類子・属性・メソッドのリストを応答する
	 * @return
	 */
	public List<NdCoreElement> getAllCoreElementList(){
		return this.allCoreElementList;
	}

	/**
	 * 次のコンパイル単位をパースする
	 * @return
	 * @throws NdParserException
	 * @throws NdUtilException
	 * @throws NdApplicationPropertyException 
	 */
	public boolean parseNext() throws NdParserException, NdUtilException, NdApplicationPropertyException {
		boolean result = true;
		if (nextParseIndex < allCompilationUnitList.size()){
			NdCompilationUnit nextCompilationUnit = allCompilationUnitList.get(nextParseIndex);
			NdLogger.getInstance().info("=> ファイル読込み NdProject#parseNext[" + nextParseIndex + "] " + nextCompilationUnit.getFullPathName());
			parser.parse(nextCompilationUnit);
			nextParseIndex++;
		} else {
			result = false;
		}
		return result;
	}
	
	/**
	 * CompilationUnitリストを作成する（ロード終了処理）
	 * this#parsenext()で使用する
	 */
	private void buildAllCompilationUnitList(){
		if (this.rootPackage != null){
			this.allCompilationUnitList = this.rootPackage.getAllCompilationUnitList();
		}
	}
	
	/**
	 * ロードする
	 * @param topDirectory
	 */
	public void load(File topDirectory){
		NdProjectLoader loader = new NdProjectLoader();
		loader.load(topDirectory, this);
		this.buildAllCompilationUnitList();
	}
	
	/**
	 * 全コンパイル単位の数を応答する
	 * @return
	 */
	public int getAllCompilationUnitCount(){
		return this.allCompilationUnitList.size();
	}

	/**
	 * ルートパッケージを応答する
	 * @return
	 */
	public NdPackage getRootPackage() {
		return rootPackage;
	}
	
	/**
	 * ルートパッケージを設定する
	 * @param topPackage
	 */
	public void setRootPackage(NdPackage topPackage) {
		this.rootPackage = topPackage;
	}

	/**
	 * コンストラクタ
	 */
	public NdProject(){
		super();
	}
	
	/**
	 * デバッグ
	 * @param tab
	 */
	public void debugPrint(String tab){
		if (!NdLogger.getInstance().getDebugLogging()) return;
		NdLogger.getInstance().debug("プロジェクト=" + this.getRootPackage().getName());
		rootPackage.debugPrint(tab + NdConstants.DEBUG_TAB);
	}
}
