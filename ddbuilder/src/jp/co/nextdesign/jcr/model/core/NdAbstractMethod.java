/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.jcr.NdConstants;
import jp.co.nextdesign.jcr.model.NdName;
import jp.co.nextdesign.jcr.model.expression.NdClassInstanceCreation;
import jp.co.nextdesign.jcr.model.reference.NdAbstractReference;
import jp.co.nextdesign.jcr.model.reference.NdReferenceAsClassInstanceCreation;
import jp.co.nextdesign.jcr.model.reference.NdReferenceAsMethodParameter;
import jp.co.nextdesign.jcr.model.reference.NdReferenceAsMethodReturnValue;
import jp.co.nextdesign.jcr.model.statement.NdAbstractStatement;
import jp.co.nextdesign.jcr.model.typelink.NdAbstractTypeLink;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * メソッド
 * 1.行数について
 *   (1)super.lineCountOfStatementは[]ブロックを１行とカウントした時の行数
 *   (2)this.metricsStatementCountは[]ブロックをカウントしない
 * @author murayama
 */
public abstract class NdAbstractMethod extends NdCoreElement {

	private boolean isConstructor;
	private NdAbstractStatement bodyStatement;
	private ArrayList<NdMethodParameter> parameterList;
	private NdMethodReturnValue returnValue;
	//Metrics
	private int metricsStatementCount;
	private int metricsControlStatementCount;
	private int metricsMaxNestLevelNumber;

	/**
	 * 参照しているすべてのタイプ名のリストを応答する（Promitive型を除く）
	 * @return
	 */
	public List<NdAbstractReference> getReferenceList(){
		List<NdAbstractReference> resultList = new ArrayList<NdAbstractReference>();
		resultList.addAll(this.getReferenceListOfReturnValue());
		resultList.addAll(this.getReferenceListOfParameter());
		if (this.bodyStatement != null) {
			resultList.addAll(this.collectReferenceListOfClassInstanceCreation(this.bodyStatement));
		}
		return resultList;
	}
		
	/**
	 * インスタンス生成呼出しを検出し参照を追加する
	 * @param statement
	 * @return
	 */
	private List<NdAbstractReference> collectReferenceListOfClassInstanceCreation(NdAbstractStatement statement){
		List<NdAbstractReference> resultList = new ArrayList<NdAbstractReference>();
		for(NdClassInstanceCreation creation : statement.getClassInstanceCreationList()){
			NdAbstractTypeLink typeLink = creation.getCreationTypeLink();
			if (typeLink != null){
				NdReferenceAsClassInstanceCreation ref = new NdReferenceAsClassInstanceCreation(typeLink.getTypeName());
				resultList.add(ref);
			}
		}
		for(NdAbstractStatement child : statement.getChildStatementList()){
			resultList.addAll(collectReferenceListOfClassInstanceCreation(child));
		}
		return resultList;
	}
	
	/**
	 * 戻り値参照を応答する
	 * @return
	 */
	private List<NdAbstractReference> getReferenceListOfReturnValue(){
		List<NdAbstractReference> resultList = new ArrayList<NdAbstractReference>();
		if (!this.isConstructor){
			if ((returnValue != null) && (returnValue.getReturnValueType() != null)){
				List<NdName> list = returnValue.getReturnValueType().getReferenceList();
				for(NdName name : list) {
					resultList.add(new NdReferenceAsMethodReturnValue(name));
				}
			}
		}
		return resultList;
	}

	/**
	 * 引数参照を応答する
	 * @return
	 */
	private List<NdAbstractReference> getReferenceListOfParameter(){
		List<NdAbstractReference> resultList = new ArrayList<NdAbstractReference>();
		for(NdMethodParameter parameter : this.parameterList){
			if (parameter.getParameterType() != null){
				List<NdName> list = parameter.getParameterType().getReferenceList();
				for(NdName name : list){
					resultList.add(new NdReferenceAsMethodParameter(name));
				}
			}
		}
		return resultList;
	}

	/**
	 * メトリクス ステートメント行数を応答する
	 * @return
	 */
	public int getMetricsStatementCount() {
		return metricsStatementCount;
	}

	/**
	 * メトリクス 制御ステートメント行数を応答する
	 * @return
	 */
	public int getMetricsControlStatementCount() {
		return metricsControlStatementCount;
	}

	/**
	 * メトリクス ネストレベル数を応答する
	 * @return
	 */
	public int getMetricsMaxNestLevelNumber() {
		return metricsMaxNestLevelNumber;
	}

	/**
	 * メトリクスを計算する
	 */
	public void calculateMetrics(){
		this.metricsStatementCount = 0;
		this.metricsControlStatementCount = 0;
		this.metricsMaxNestLevelNumber = 0;
		if (this.bodyStatement != null) {
			this.digStatementTree(this.bodyStatement, 0);
		}
	}

	/**
	 * メトリクスを計算する（ステートメントツリーを走査する）
	 * @param statement
	 * @param nestCount
	 */
	private void digStatementTree(NdAbstractStatement statement, int nestCount){
		this.metricsStatementCount += statement.getLineCount();
		if (statement.isControlStatement()){
			this.metricsControlStatementCount++;
		}
		if (statement.isNestLevelCountable()){
			nestCount++;
			if (this.metricsMaxNestLevelNumber < nestCount){
				this.metricsMaxNestLevelNumber = nestCount;
			}
		}
		for(NdAbstractStatement child : statement.getChildStatementList()){
			digStatementTree(child, nestCount);
		}
	}

	/**
	 * メソッドボディを応答する
	 * メソッドボディはステートメントツリーのルートステートメントである
	 * @return
	 */
	public NdAbstractStatement getBodyStatement() {
		return bodyStatement;
	}

	/**
	 * メソッドボディを設定する
	 * @param bodyStatement
	 */
	public void setBodyStatement(NdAbstractStatement bodyStatement) {
		this.bodyStatement = bodyStatement;
	}

	/**
	 * このメソッドがコンストラクタか否かを応答する
	 * @return
	 */
	public boolean isConstructor(){
		return this.isConstructor;
	}

	/**
	 * このメソッドがコンストラクタか否かを設定する
	 * @param b
	 */
	public void setIsConstructor(boolean b){
		this.isConstructor = b;
	}

	/**
	 * パラメタリストを設定する
	 * @param parameterList
	 */
	public void setParameterList(ArrayList<NdMethodParameter> parameterList) {
		this.parameterList = parameterList;
	}

	/**
	 * パラメタリストを応答する
	 * @return
	 */
	public ArrayList<NdMethodParameter> getParameterList(){
		return this.parameterList;
	}

	/**
	 * 戻り値を応答する
	 * @return
	 */
	public NdMethodReturnValue getReturnValue() {
		return returnValue;
	}

	/**
	 * 戻り値を設定する
	 * @param returnValue
	 */
	public void setReturnValue(NdMethodReturnValue returnValue) {
		this.returnValue = returnValue;
	}

	/**
	 * コンストラクタ
	 */
	public NdAbstractMethod(){
		super();
		this.parameterList = new ArrayList<NdMethodParameter>();
		this.returnValue = NdMethodReturnValue.UNDEFINED;
		this.isConstructor = false;
	}

	/** デバッグ */
	public void debugPrint(String tab){
		if (!NdLogger.getInstance().getDebugLogging()) return;
		if (this.isConstructor()){
			NdLogger.getInstance().debug(tab + "コンストラクタ=" + this.getName());
		} else {
			NdLogger.getInstance().debug(tab + "メソッド=" + this.getName());
		}
		this.debugPrintCore(tab);
		NdLogger.getInstance().debug(tab + "メトリクス:ステートメント数=" + this.metricsStatementCount);
		NdLogger.getInstance().debug(tab + "メトリクス:制御ステートメント数=" + this.metricsControlStatementCount);
		NdLogger.getInstance().debug(tab + "メトリクス:ネストレベル=" + this.metricsMaxNestLevelNumber);
		String tab2 = tab + NdConstants.DEBUG_TAB;
		this.getReturnValue().debugPrint(tab2);
		for (int i=0; i< this.parameterList.size(); i++){
			NdMethodParameter parameter = this.parameterList.get(i);
			parameter.debugPrint(tab2 + "引数[" + i + "]=");
		}
	}
}
