/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.parser.reflector;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.jcr.model.core.NdJavaCode;
import jp.co.nextdesign.jcr.model.core.NdMethodParameter;
import jp.co.nextdesign.jcr.model.core.NdMethodReturnValue;
import jp.co.nextdesign.jcr.model.statement.NdAbstractStatement;
import jp.co.nextdesign.jcr.model.statement.NdStatementFactory;
import jp.co.nextdesign.jcr.model.typelink.NdAbstractTypeLink;
import jp.co.nextdesign.jcr.model.typelink.NdTypeLinkFactory;
import jp.co.nextdesign.jcr.parser.NdASTVisitor;
import jp.co.nextdesign.util.logging.NdLogger;

import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

/**
 * メソッドに類似する要素の抽象リフレクタ
 * @author murayama
 */
public abstract class NdAbstractMethodReflector extends NdAbstractReflector {
	
	/**
	 * ブロック内のステートメントツリーを作成し応答する
	 * @param jdtBlock
	 * @param astVisitor
	 * @return
	 */
	protected NdAbstractStatement buildStatementTree(Block jdtBlock, NdASTVisitor astVisitor){
		NdAbstractStatement rootStatement = null;
		if (jdtBlock != null){
			rootStatement = NdStatementFactory.create(jdtBlock);
			astVisitor.putStatementCache(jdtBlock, rootStatement);
			List<?> jdtTopLevelStatementList = ((Block)jdtBlock).statements();
			this.addChildStatementListToParent(jdtTopLevelStatementList, rootStatement, astVisitor);
		}
		return rootStatement;
	}

	/**
	 * ブロック内の行数を応答する
	 * @param jdtBlock
	 * @return
	 */
	protected int getLineCountOfBody(Block jdtBlock){
		int result = 0;
		if (jdtBlock != null){
			String bodyString = jdtBlock.toString();
			List<String> lines = NdJavaCode.getLineListOf(bodyString);
			result = lines.size();
		}
		return result;
	}

	/**
	 * 子ステートメントリストをすべて親ステートメントに追加する
	 * @param jdtChildList
	 * @param parent
	 * @param astVisitor
	 */
	protected void addChildStatementListToParent(List<?> jdtChildList, NdAbstractStatement parent, NdASTVisitor astVisitor){
		for(Object jdtChild : jdtChildList){
			if (jdtChild instanceof Statement){
				this.addChildStatementToParent((Statement)jdtChild, parent, astVisitor);
			}
		}
	}

	/**
	 * 子ステートメントを親ステートメントに追加する
	 * @param jdtChild
	 * @param parent
	 * @param astVisitor
	 */
	protected void addChildStatementToParent(Statement jdtChild, NdAbstractStatement parent, NdASTVisitor astVisitor){
		if ((jdtChild != null) && (parent != null)){
			NdAbstractStatement child = NdStatementFactory.create(jdtChild);
			astVisitor.putStatementCache(jdtChild, child);
			parent.addChildStatement(child);
			List<?> grandChildList = this.getIncludingStatementList(jdtChild);
			this.addChildStatementListToParent(grandChildList, child, astVisitor);				
		}
	}

	/**
	 * ステートメントが含んでいる子ステートメントリストを応答する
	 * @param statement
	 * @return
	 */
	protected List<Statement> getIncludingStatementList(Statement statement){
		List<Statement> resultList = new ArrayList<Statement>();
		if (statement instanceof IfStatement){
			IfStatement ifStatement = (IfStatement)statement;
			resultList.add(ifStatement.getThenStatement());
			resultList.add(ifStatement.getElseStatement());
		} else if(hasGetBodyMethod(statement)){
			resultList = this.buildChildListByGetBodyMethod(statement);
		} else if(hasStatementsMethod(statement)){
			resultList = this.buildChildListByStatementsMethod(statement);
		}
		return resultList;
	}

	/**
	 * getBody()メソッドを持つステートメントか否かを応答する
	 * @param statement
	 * @return
	 */
	protected boolean hasGetBodyMethod(Statement statement){
		boolean result = false;
		if((statement instanceof DoStatement)
		|| (statement instanceof EnhancedForStatement)
		|| (statement instanceof ForStatement)
		|| (statement instanceof LabeledStatement)
		|| (statement instanceof SynchronizedStatement)
		|| (statement instanceof TryStatement)
		|| (statement instanceof WhileStatement)){
			result = true;
		}
		return result;
	}

	/**
	 * statements()メソッドを持つステートメントか否かを応答する
	 * @param statement
	 * @return
	 */
	protected boolean hasStatementsMethod(Statement statement){
		boolean result = false;
		if((statement instanceof Block)
		|| (statement instanceof SwitchStatement)){
			result = true;
		}
		return result;
	}

	/**
	 * statements()メソッドで子ステートメントリストを作成し応答する
	 * @param jdtStatement
	 * @return
	 */
	protected List<Statement> buildChildListByStatementsMethod(Statement jdtStatement){
		List<Statement> resultList = new ArrayList<Statement>();
		try {
			java.lang.reflect.Method methodObject = jdtStatement.getClass().getMethod("statements", (Class<?>[])null);
			List<?> childList = (List<?>)methodObject.invoke(jdtStatement, (Object[])null);
			for(Object child : childList){
				if (child instanceof Statement){
					resultList.add((Statement)child);
				}
			}
		} catch(Exception ex){
			NdLogger.getInstance().error(this.getClass().getSimpleName() + "#buildChildListByStatementsMethod()例外", ex);
		}
		return resultList;
	}

	/**
	 * getBody()メソッドで子ステートメントリストを作成し応答する
	 * @param jdtStatement
	 * @return
	 */
	protected List<Statement> buildChildListByGetBodyMethod(Statement jdtStatement){
		List<Statement> resultList = new ArrayList<Statement>();
		try {
			java.lang.reflect.Method methodObject = jdtStatement.getClass().getMethod("getBody", (Class<?>[])null);
			Object invoked = methodObject.invoke(jdtStatement, (Object[])null);
			if (invoked instanceof Block){
				List<?> childList = ((Block)invoked).statements();
				for(Object child : childList){
					if (child instanceof Statement){
						resultList.add((Statement)child);
					}
				}
			}
		} catch(Exception ex){
			NdLogger.getInstance().error(this.getClass().getSimpleName() + "#buildChildListByGetBodyMethod()例外", ex);
		}
		return resultList;
	}

	/**
	 * メソッド名を応答する
	 * @param node
	 * @return
	 */
	protected String getMethodName(MethodDeclaration node){
		String result = "";
		if (node.getName() != null){
			result = node.getName().getIdentifier();
		}
		return result;
	}

	/**
	 * Parameterリストを応答する
	 * @param node
	 * @return
	 */
	protected ArrayList<NdMethodParameter> getParameterList(MethodDeclaration node){
		ArrayList<NdMethodParameter> resultList = new ArrayList<NdMethodParameter>();
		for(Object o : node.parameters()){
			if (o instanceof SingleVariableDeclaration){
				SingleVariableDeclaration declaration = (SingleVariableDeclaration)o;
				NdMethodParameter newParameter = new NdMethodParameter();
				resultList.add(newParameter);
				if (declaration.getName() != null){
					newParameter.setName(declaration.getName().toString());
				}
				if (declaration.getType() != null){
					NdAbstractTypeLink typeLink = NdTypeLinkFactory.createTypeLink(declaration.getType());
					newParameter.setParameterType(typeLink);
				}
				newParameter.setVarargs(declaration.isVarargs());
			}
		}
		return resultList;
	}

	/**
	 * ReturnValueを応答する
	 * @param node
	 * @return
	 */
	protected NdMethodReturnValue getReturnValue(MethodDeclaration node){
		NdMethodReturnValue result = new NdMethodReturnValue();
		if (node.getReturnType2() != null){
			NdAbstractTypeLink typeLink = NdTypeLinkFactory.createTypeLink(node.getReturnType2());
			result.setReturnValueType(typeLink);
		}
		return result;
	}
}
