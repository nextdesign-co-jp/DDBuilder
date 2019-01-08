/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.jcr.NdConstants;
import jp.co.nextdesign.jcr.documentation.project.NdPackageDependencyInfo;
import jp.co.nextdesign.jcr.model.reference.NdAbstractReference;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * パッケージ
 * @author murayama
 */
public class NdPackage extends NdAbstractNamedElement {
	
	private File directory;
	private NdPackage parentPackage = null;
	private ArrayList<NdPackage> childPackageList = new ArrayList<NdPackage>();;
	private ArrayList<NdCompilationUnit> compilationUnitList = new ArrayList<NdCompilationUnit>();
	private List<NdPackageDependencyInfo> dependencyList = new ArrayList<NdPackageDependencyInfo>();
	
	/**
	 * このパッケージ以下すべてのパッケージのリストを応答する DDB 2014.4.15
	 * ddbではgetNamedElementList()を使わなかった
	 * @return
	 */
	public List<NdPackage> getAllPackageList(){
		List<NdPackage> resultList = new ArrayList<NdPackage>();
		resultList.add(this);
		for(NdPackage child : getChildPackageList()){
			resultList.addAll(child.getAllPackageList());
		}
		return resultList;
	}
	
	/**
	 * このパッケージの依存先リストを応答する
	 * @return
	 */
	public List<NdPackageDependencyInfo> getDependencyList() {
		return dependencyList;
	}

	/**
	 * パッケージ相互の依存関係情報を作成する
	 */
	public void buildDependency(){
		NdLogger.getInstance().debug("###### " + this.getName());
		for(NdCompilationUnit unit : this.compilationUnitList){
			List<NdAbstractClassifier> cList = unit.getClassifierList();
			for(NdAbstractClassifier c : cList){
				if (c.isSubjectOfClassifierReport()){
					List<NdAbstractReference> refList = c.getReferenceList();
					this.setDependencyList(refList, false);
					List<NdAbstractReference> inverseRefList = c.getInverseReferenceList();
					this.setDependencyList(inverseRefList, true);
				}
			}
		}
		for(NdPackage child : this.childPackageList){
			child.buildDependency();
		}
	}
	
	/**
	 * 依存情報リストに設定する
	 * 存在する場合はカウントアップ、存在しない場合は追加する
	 * @param refList
	 * @param isInverse
	 */
	private void setDependencyList(List<NdAbstractReference> refList, boolean isInverse){
		for(NdAbstractReference ref : refList){
			if (ref.isAvailableReference()){
				NdPackageDependencyInfo newInfo = new NdPackageDependencyInfo(ref, isInverse);
				int found = this.dependencyList.indexOf(newInfo);
				if (found < 0){
					newInfo.countup(ref);
					this.dependencyList.add(newInfo);
				} else {
					this.dependencyList.get(found).countup(ref);
				}
			}
		}
	}
	
	/**
	 * パッケージ名（ディレクトリ構成ベース）
	 * NdCompilationUnitのNdPackageDeclarationと必ずしも一致しない
	 * @return
	 */
	public String getFullPackageName(){
		String result = this.getName();
		if (this.parentPackage != null){
			result = this.parentPackage.getFullPackageName() + "." + result;
		}
		return result;
	}
	
	/**
	 * 配下のすべてのパッケージ、分類子のリストを応答する
	 * Classifierのリストを作るコードとPackageリストを作るコードは分けなかった。
	 * 理由はこのメソッドは再帰的に使用されるので、問題を複雑にしないため。
	 * @return
	 */
	public List<NdAbstractNamedElement> getNamedElementList(){
		List<NdAbstractNamedElement> resultList = new ArrayList<NdAbstractNamedElement>();
		resultList.add(this);
		for(NdCompilationUnit unit : this.compilationUnitList){
			List<NdAbstractClassifier> list = unit.getClassifierList();
			for(NdAbstractClassifier c : list){
				resultList.add(c);
			}
		}
		for(NdPackage childPackage : this.childPackageList){
			resultList.addAll(childPackage.getNamedElementList());
		}
		return resultList;
	}
	
	/**
	 * 直下のコンパイル単位リストに追加する
	 * @param compilationUnit
	 */
	public void addCompilationUnit(NdCompilationUnit compilationUnit){
		if ((compilationUnit != null) && (!this.compilationUnitList.contains(compilationUnit))){
			this.compilationUnitList.add(compilationUnit);
		}
	}
	
	/**
	 * 直下のコンパイル単位リストを応答する
	 * @return
	 */
	public List<NdCompilationUnit> getCompilationUnitList(){
		return this.compilationUnitList;
	}

	/**
	 * このパッケージ配下の全コンパイル単位リストを応答する
	 * @see NdProject#endLoading()
	 * @return
	 */
	public List<NdCompilationUnit> getAllCompilationUnitList(){
		ArrayList<NdCompilationUnit> resultList = new ArrayList<NdCompilationUnit>(this.compilationUnitList);
		for(int i=0; i<this.childPackageList.size(); i++){
			NdPackage child = this.childPackageList.get(i);
			resultList.addAll(child.getAllCompilationUnitList());
		}
		return resultList;
	}
		
	/**
	 * ディレクトリを応答する
	 * @return
	 */
	public File getDirectory() {
		return directory;
	}
	
	/**
	 * ディレクトリを設定する
	 * @param directory
	 */
	public void setDirectory(File directory) {
		this.directory = directory;
	}
	
	/**
	 * 親パッケージを応答する
	 * @return
	 */
	public NdPackage getParentPackage() {
		return parentPackage;
	}
	
	/**
	 * 親パッケージを設定する
	 * @param parentPackage
	 */
	public void setParentPackage(NdPackage parentPackage) {
		if (parentPackage != null){
			this.parentPackage = parentPackage;
			this.parentPackage.addChildPackage(this);
		}
	}
	
	/**
	 * 子パッケージリストを応答する
	 * @return
	 */
	public List<NdPackage> getChildPackageList() {
		return childPackageList;
	}
	
	/**
	 * 子パッケージリストに追加する
	 * @param newChildPackage
	 */
	public void addChildPackage(NdPackage newChildPackage){
		if ((newChildPackage != null) && (!this.childPackageList.contains(newChildPackage))){
			this.childPackageList.add(newChildPackage);
			newChildPackage.setParentPackage(this);
		}
	}
	
	/**
	 * コンストラクタ
	 * @param directory
	 */
	public NdPackage(File directory){
		super();
		if ((directory != null) && (directory.isDirectory())){
			this.setName(directory.getName());
			this.directory = directory;
		}
	}
	
	/**
	 * デバッグ
	 * @param tab
	 */
	public void debugPrint(String tab){
		if (!NdLogger.getInstance().getDebugLogging()) return;
		NdLogger.getInstance().debug(tab + "パッケージ=" + this.getName());
		for (NdCompilationUnit unit : this.getCompilationUnitList()){
			unit.debugPrint(tab + NdConstants.DEBUG_TAB);
		}
		for(NdPackage child : this.getChildPackageList()){
			child.debugPrint(tab + NdConstants.DEBUG_TAB);			
		}
	}
}
