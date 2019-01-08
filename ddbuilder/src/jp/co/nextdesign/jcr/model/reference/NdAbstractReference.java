/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.reference;

import jp.co.nextdesign.jcr.model.NdName;
import jp.co.nextdesign.jcr.model.core.NdAbstractClassifier;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * 参照
 * @author murayama
 */
public abstract class NdAbstractReference {

	/**
	 * 参照の種類を応答する
	 * @return
	 */
	public boolean isAsAttribute() {
		return false;
	}
	
	/**
	 * 参照の種類を応答する
	 * @return
	 */
	public boolean isAsClassInstanceCreation(){
		return false;
	}
	
	/**
	 * 参照の種類を応答する
	 * @return
	 */
	public boolean isAsExtends(){
		return false;
	}
	
	/**
	 * 参照の種類を応答する
	 * @return
	 */
	public boolean isAsExtendsInterface(){
		return false;
	}
	
	/**
	 * 参照の種類を応答する
	 * @return
	 */
	public boolean isAsImplements(){
		return false;
	}
	
	/**
	 * 参照の種類を応答する
	 * @return
	 */
	public boolean isAsMethodParameter(){
		return false;
	}
	
	/**
	 * 参照の種類を応答する
	 * @return
	 */
	public boolean isAsMethodReturnValue(){
		return false;
	}

	/**
	 * referenceNameはtoを決定するための名前である。
	 * 最終的にはtoの名前と同じになる。
	 */
	protected NdName referenceName;
	protected NdAbstractClassifier from;
	protected NdAbstractClassifier to;
		
	/**
	 * 有効な参照/被参照であるか否かを応答する
	 * 参照リスト、被参照リストにはプロジェクト内で解決できない参照/被参照も含まれている
	 * @param ref
	 * @return
	 */
	public boolean isAvailableReference(){
		return (this.getFrom() != null) && (this.getTo() != null);
	}
		
	/**
	 * 参照箇所を応答する
	 * @return
	 */
	public abstract String getReferencePointName();
	
	/**
	 * 参照名
	 * @return
	 */
	public NdName getReferenceName() {
		return referenceName;
	}

	/**
	 * 参照元を応答する
	 * @return
	 */
	public NdAbstractClassifier getFrom() {
		return from;
	}
	
	/**
	 * 参照元を設定する
	 * @param from
	 */

	public void setFrom(NdAbstractClassifier from) {
		this.from = from;
	}
	
	/**
	 * 参照先を応答する
	 * @return
	 */

	public NdAbstractClassifier getTo() {
		return to;
	}
	
	/**
	 * 参照先を設定する
	 * @param to
	 */

	public void setTo(NdAbstractClassifier to) {
		this.to = to;
	}

	/**
	 * コンストラクタ
	 * @param referenceName
	 */
	protected NdAbstractReference(NdName referenceName){
		if (referenceName != null){
			this.referenceName = referenceName;
		} else {
			this.referenceName = new NdName();
		}
	}
	
	/**
	 * デバッグ
	 */
	public void debugPrint(String tab){
		if (NdLogger.getInstance().getDebugLogging()){
			String msg = "■参照名getSimple()=" + this.referenceName.getSimpleName();
			if (this.referenceName.isQualifiedName()){
				msg += " getQualofiedName()=" + this.referenceName.getQualifiedName();
			}
			msg += " 要素=" + this.getClass().getSimpleName();
			msg += " 方向=";
			if (from != null){
				msg += from.getQualifiedName();
			} else {
				msg += "null";
			}
			msg += " => ";
			if (to != null){
				msg += to.getQualifiedName();
			} else {
				msg += "null";
			}
			NdLogger.getInstance().debug(tab + msg);
		}
	}
}
