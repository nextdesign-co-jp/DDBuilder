/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.parser;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import jp.co.nextdesign.jcr.NdApplicationPropertyException;
import jp.co.nextdesign.jcr.model.core.NdCompilationUnit;
import jp.co.nextdesign.jcr.model.core.NdJavaCode;
import jp.co.nextdesign.util.logging.NdLogger;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * コンパイル単位パーサー
 * @author murayama
 */
public class NdParser {

	private ASTParser astParser;

	/**
	 * コンストラクタ
	 */
	public NdParser(){
		astParser = ASTParser.newParser(AST.JLS4);
	}
	
	/**
	 * ASTParser　動作オプションを設定する
	 * ASTParser#setSourceの前に毎回行う必要がある
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setParserOptions(){
		Map options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
		astParser.setCompilerOptions(options);
		//スタンドアロンJDT利用（setSource(char[])）の場合、Binding困難。
		//ASTparser#setResolveBinding(boolean)のJavadocの最後を読む。
		//従って次の２行はコメントアウトする 2011.10.5
		//astParser.setKind(ASTParser.K_COMPILATION_UNIT);
		//astParser.setResolveBindings(true);
	}

	/**
	 * パースする
	 * @param compilationUnit
	 * @throws NdParserException 
	 * @throws NdApplicationPropertyException 
	 */
	public void parse(NdCompilationUnit compilationUnit) throws NdApplicationPropertyException, NdParserException {
		if (compilationUnit != null){
			String fileName = compilationUnit.getFullPathName();
			NdSourceFile sourceFile = new NdSourceFile(fileName);
			compilationUnit.setLineCountOfFile(sourceFile.getLineCount()); //ファイル行数設定
			this.setParserOptions();
			astParser.setSource(sourceFile.getCharArray());
			CompilationUnit jdtCompilationUnit = (CompilationUnit)astParser.createAST(null);
			this.logProblems(jdtCompilationUnit, fileName);
			this.displayCompilationUnit(jdtCompilationUnit);
			setLineCountInfoTo(compilationUnit, jdtCompilationUnit); //行数情報を設定
			NdASTVisitor visitor = new NdASTVisitor(compilationUnit);
			jdtCompilationUnit.accept(visitor); //Visitorパターン開始
		}		
	}
	
	/**
	 * パースエラーをロギングする
	 * @param jdtCompilationUnit
	 * @param fileName
	 */
	private void logProblems(CompilationUnit jdtCompilationUnit, String fileName){
		IProblem[] problems = jdtCompilationUnit.getProblems();
		for(IProblem problem : problems){
			String msg = " message:" + problem.getMessage();
			if (problem.isError()) {
				msg = "[エラー]" + fileName +  msg;
			}
			if (problem.isWarning()){
				msg = "[警告]" + fileName + msg;
			}
			NdLogger.getInstance().error(msg, null);
		}
	}

	/**
	 * コンパイル単位内の行数情報を設定する
	 * @param ndUnit
	 * @param jdtUnit
	 */
	private void setLineCountInfoTo(NdCompilationUnit ndUnit, CompilationUnit jdtUnit){
		List<?> objectList = jdtUnit.getCommentList();
		int parsedJavadocLineCount = 0;
		int javadocLineCount = 0;
		int blockCommentLineCount = 0;
		int lineCommentLineCount = 0;
		for(Object object : objectList){
			if (object instanceof Comment){
				Comment comment = (Comment)object;
				int lineCount = this.countLine(comment, jdtUnit);
				if (comment.isDocComment()){
					javadocLineCount += lineCount;
					parsedJavadocLineCount += NdJavaCode.getLineListOf(comment.toString()).size();
				} else if (comment.isBlockComment()){
					blockCommentLineCount += lineCount;
				} else {
					lineCommentLineCount += lineCount;
				}
			}
		}
		//各行数を設定する
		ndUnit.setLineCountOfParsedJavadoc(parsedJavadocLineCount);
		ndUnit.setLineCountOfJavadocComment(javadocLineCount);
		ndUnit.setLineCountOfBlockComment(blockCommentLineCount);
		ndUnit.setLineCountOfLineComment(lineCommentLineCount);
		String code = jdtUnit.toString();
		ndUnit.setLineCountOfStatement(NdJavaCode.getLineListOf(code).size());
	}
	
	/**
	 * １つのコメントの行数を応答する
	 * @param comment
	 * @param jdtUnit
	 * @return
	 */
	private int countLine(Comment comment, CompilationUnit jdtUnit){
		int result = 0;
		if (comment.isLineComment()){
			result = 1;
		} else {
			int startPosition = comment.getStartPosition();
			int endPosition = startPosition + comment.getLength();
			result = jdtUnit.getLineNumber(endPosition) - jdtUnit.getLineNumber(startPosition) + 1;
		}
		return result;
	}
	
	private static DecimalFormat format = new DecimalFormat("0000");
	/**
	 * デバッグ用
	 * @param jdtCompilationUnit
	 */
	private void displayCompilationUnit(CompilationUnit jdtCompilationUnit){
		List<String> list = NdJavaCode.getLineListOf(jdtCompilationUnit.toString());
		for(int i=0; i<list.size(); i++){
			String log = "[" + this.getClass().getSimpleName() + "]";
			log += "[" + format.format(i) + "]";
			log += list.get(i);
			NdLogger.getInstance().debug(log);
		}
	}
}
