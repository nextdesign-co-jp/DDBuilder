/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.jcr.NdConstants;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * コンパイル単位（ソースコードファイルと１対１）
 * @author murayama
 */
public class NdCompilationUnit {

	private int lineCountOfFile; //ファイルの生の行数
	private int lineCountOfParsedJavadoc; //Javadoc(パース後のJavadoc行数)
	private int lineCountOfStatement; //ステートメント＋Javadoc(パース後のJavadoc行数)
	private int lineCountOfJavadocComment; //Javadoc(テキストエディタ的行数)
	private int lineCountOfLineComment; //行コメント
	private int lineCountOfBlockComment; //ブロックコメント
	private String fullPathName;
	private String simpleName;
	private List<NdAbstractClassifier> classifierList = new ArrayList<NdAbstractClassifier>();
	private NdPackageDeclaration packageDeclaration;
	private ArrayList<NdImportDeclaration> importDeclarationList = new ArrayList<NdImportDeclaration>();

	/**
	 * 分類子リストに追加する
	 */
	public void addClassifier(NdAbstractClassifier classifier){
		if ((classifier != null) && (!this.classifierList.contains(classifier))){
			this.classifierList.add(classifier);
			classifier.setCompilationUnit(this);
		}
	}

	/**
	 * 分類子リストを応答する
	 */
	public List<NdAbstractClassifier> getClassifierList(){
		return this.classifierList;
	}

	/**
	 * コンパイル単位内で名前が同じクラスを応答する
	 * @param targetName
	 * @return
	 */
	public List<NdAbstractClassifier> getClassByName(String targetName){
		List<NdAbstractClassifier> resultList = new ArrayList<NdAbstractClassifier>();

		if (targetName != null){
			for(NdAbstractClassifier c : this.classifierList){
				if (targetName.equals(c.getName())){
					resultList.add(c);
				}
			}
		}
		return resultList;
	}

	/**
	 * フルパス名を応答する
	 */
	public String getFullPathName() {
		return fullPathName;
	}
	
	/**
	 * 単純名を応答する（ソースファイル名から.javaを除いた文字列） 
	 * @return
	 */
	public String getSimpleName(){
		return this.simpleName;
	}

	/**
	 * コンストラクタ
	 */
	public NdCompilationUnit(String path, String name){
		super();
		this.fullPathName = "";
		this.simpleName = "";
		if ((path != null) && (name != null)){
			this.fullPathName = path + NdConstants.PATH_SEPARATOR + name;
			if (name.toUpperCase().endsWith(NdConstants.SOURCE_FILE_EXTENSION)){
				this.simpleName = name.substring(0, name.length() - NdConstants.SOURCE_FILE_EXTENSION.length());
			}
		}
	}

	/**
	 * package宣言を応答する
	 * @return
	 */
	public NdPackageDeclaration getPackageDeclaration() {
		return packageDeclaration;
	}

	/**
	 * package宣言を設定する
	 * @param newPackageDeclaration
	 */
	public void setPackageDeclaration(NdPackageDeclaration newPackageDeclaration) {
		this.packageDeclaration = newPackageDeclaration;
	}

	/**
	 * import宣言リストを応答する
	 * @return
	 */
	public ArrayList<NdImportDeclaration> getImportDeclarationList() {
		return importDeclarationList;
	}

	/**
	 * import宣言を追加する
	 * @param newImport
	 */
	public void addImportDeclaration(NdImportDeclaration newImport) {
		if ((newImport != null) && (!this.importDeclarationList.contains(newImport))){
			this.importDeclarationList.add(newImport);
		}
	}

	/**
	 * ソースファイルの行数を応答する（空行も含むエディタ式行数）
	 * @return
	 */
	public int getLineCountOfFile() {
		return lineCountOfFile;
	}

	/**
	 * ソースファイルの行数を設定する（空行も含むエディタ式行数）
	 * @param lineCountOfFile
	 */
	public void setLineCountOfFile(int lineCountOfFile) {
		this.lineCountOfFile = lineCountOfFile;
	}

	/**
	 * コンパイル単位内の全Javaステートメント行数（空行、LineComment、BlockCommentを除く）を応答する
	 * @return
	 */
	public int getLineCountOfStatement() {
		return lineCountOfStatement;
	}

	/**
	 * コンパイル単位内の全Javaステートメント行数
	 * ASTParser.createAST(null)後の行数
	 * 含む：ステートメント、Javadoc(パス後なのでテキストエディタ的行数よりも少ない)
	 * 除く：空行、LineComment、BlockComment
	 * @param lineCountOfStatement
	 */
	public void setLineCountOfStatement(int lineCountOfStatement) {
		this.lineCountOfStatement = lineCountOfStatement;
	}
	
	/** パーサー解釈後のJavadoc行数 */
	public int getLineCountOfParsedJavadoc() {
		return lineCountOfParsedJavadoc;
	}

	/** パーサー解釈後のJavadoc行数 */
	public void setLineCountOfParsedJavadoc(int lineCountOfParsedJavadoc) {
		this.lineCountOfParsedJavadoc = lineCountOfParsedJavadoc;
	}

	/**
	 * コンパイル単位内の全Javadocの行数を応答する
	 * @return
	 */
	public int getLineCountOfJavadocComment() {
		return lineCountOfJavadocComment;
	}

	/**
	 * コンパイル単位内の全Javadocの行数を設定する
	 * @param lineCountOfJavadocComment
	 */
	public void setLineCountOfJavadocComment(int lineCountOfJavadocComment) {
		this.lineCountOfJavadocComment = lineCountOfJavadocComment;
	}

	/**
	 * コンパイル単位内の全LineComment行数を応答する
	 * @return
	 */
	public int getLineCountOfLineComment() {
		return lineCountOfLineComment;
	}

	/**
	 * コンパイル単位内の全LineComment行数を設定する
	 * @param lineCountOfLineComment
	 */
	public void setLineCountOfLineComment(int lineCountOfLineComment) {
		this.lineCountOfLineComment = lineCountOfLineComment;
	}

	/**
	 * コンパイル単位内の全BlockComment行数を応答する
	 * @return
	 */
	public int getLineCountOfBlockComment() {
		return lineCountOfBlockComment;
	}

	/**
	 * コンパイル単位内の全BlockComment行数を設定する
	 * @param lineCountOfBlockComment
	 */
	public void setLineCountOfBlockComment(int lineCountOfBlockComment) {
		this.lineCountOfBlockComment = lineCountOfBlockComment;
	}

	/**
	 * デバッグ
	 */
	public void debugPrint(String tab){
		NdLogger logger = NdLogger.getInstance();
		if (!logger.getDebugLogging()) return;
		logger.debug(tab + "コンパイル単位=" + this.getFullPathName());
		String log = tab + "import=" + this.importDeclarationList.size();
		log += " FileLine=" + this.getLineCountOfFile();
		log += " Statement=" + this.getLineCountOfStatement();
		log += " Javadoc=" + this.getLineCountOfJavadocComment();
		log += " BlockComment=" + this.getLineCountOfBlockComment();
		log += " LineComment=" + this.getLineCountOfLineComment();
		logger.debug(log);
		for(NdAbstractClassifier c : this.classifierList){
			if (!c.hasParent()){
				c.debugPrint(tab + NdConstants.DEBUG_TAB);
			}
		}
	}
}
