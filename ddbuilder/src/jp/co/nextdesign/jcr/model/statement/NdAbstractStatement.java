/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.statement;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.jcr.NdConstants;
import jp.co.nextdesign.jcr.model.expression.NdClassInstanceCreation;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * ステートメント
 * 1.elseはステートメントとして抽出されない。ifステートメントにthenStatements()とelseStatements()がある。
 * 2.[ ]はBlockステートメントである。ただし、メトリクス計算時には「行カウントしない」、「ネストレベルカウントしない」ことにする。
 * 3.switchステートメントのstatements()は、すべてのswitchcaseステートメントとその中の最上位のステートメントを含む。
 *   つまり、switchcaseはstatements()もgetBody()も持たない。
 * @author murayama
 */
public abstract class NdAbstractStatement {

	private ArrayList<NdAbstractStatement> childStatementList = new ArrayList<NdAbstractStatement>();
	private NdAbstractStatement parentStatement;
	protected List<NdClassInstanceCreation> classInstanceCreationList = new ArrayList<NdClassInstanceCreation>();
	
	/**
	 * インスタンス生成式を追加する
	 * @param creation
	 */
	public void addClassInstanceCreation(NdClassInstanceCreation creation){
		if (creation != null){
			this.classInstanceCreationList.add(creation);
		}
	}
	
	/**
	 * ステートメントに含まれるインスタンス生成式を応答する
	 * @return
	 */
	public List<NdClassInstanceCreation> getClassInstanceCreationList() {
		return classInstanceCreationList;
	}
	
	/**
	 * ステートメント同士の親子関連（双方向）の子ステートメントリストを応答する
	 * @return
	 */
	public ArrayList<NdAbstractStatement> getChildStatementList(){
		return this.childStatementList;
	}
	
	/**
	 * ステートメント同士の親子関連（双方向）に親ステートメントを設定する
	 * @param child
	 */
	public void addChildStatement(NdAbstractStatement child){
		if ((child != null) && (!this.childStatementList.contains(child))){
			this.childStatementList.add(child);
			child.setParentStatement(this);
		}
	}
	
	/**
	 * 親ステートメントを応答する
	 * @return
	 */
	public NdAbstractStatement getParentStatement() {
		return parentStatement;
	}

	/**
	 * 親ステートメントを設定する
	 * @param parentStatement
	 */
	public void setParentStatement(NdAbstractStatement parentStatement) {
		if ((parentStatement != null) && (this.parentStatement != parentStatement)) {
			this.parentStatement = parentStatement;
			this.parentStatement.addChildStatement(this);
		}
	}

	/**
	 * 制御文か否かを応答する
	 * @return
	 */
	public boolean isControlStatement() {
		return false;
	}
	
	/**
	 * このステートメント１つを何行と数えるかを応答する
	 * @return
	 */
	public int getLineCount(){
		return 1;
	}
	
	/**
	 * このステートメントをネストダウンとしてカウントするか否かを応答する
	 * @return
	 */
	public boolean isNestLevelCountable(){
		return true;
	}

	/**
	 * Object#toString()
	 */
	@Override public String toString(){
		return this.getClass().getSimpleName();
	}

	/**
	 * コンストラクタ
	 */
	protected NdAbstractStatement(){
		super();
	}
	
	/** デバッグ用 */
	public void debugPrint(String tab){
		if (!NdLogger.getInstance().getDebugLogging()) return;
		NdLogger.getInstance().debug(tab + this.toString());
		for(NdAbstractStatement child : this.childStatementList){
			child.debugPrint(tab + NdConstants.DEBUG_TAB);
		}
	}
}
