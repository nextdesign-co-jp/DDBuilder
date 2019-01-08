/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.documentation.project;

import jp.co.nextdesign.jcr.model.reference.NdAbstractReference;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * パッケージ依存情報
 * @author murayama
 */
public class NdPackageDependencyInfo {
	private String fromName;
	private String toName;
	private boolean isInverse = false;
	private int countOfAttributeReference;
	private int countOfClassInstanceCreationReference;
	private int countOfExtendsReference;
	private int countOfImplementsReference;
	private int countOfMethodParameterReference;
	private int countOfMethodReturnValueReference;
	private int countOfExtendsInterfaceReference;
	
	/**
	 * コンストラクタ
	 * @param fromName
	 * @param toName
	 */
	public NdPackageDependencyInfo(NdAbstractReference ref, boolean isInverse){
		this.fromName = "";
		this.toName = "";
		if (ref != null){
			String originalFromName = "";
			String originalToName = "";
			if (ref.getFrom() != null){
				originalFromName = ref.getFrom().getPackageName();
			}
			if (ref.getTo() != null){
				originalToName = ref.getTo().getPackageName();
			}
			if (!isInverse){
				this.fromName = originalFromName;
				this.toName = originalToName;
			} else {
				this.fromName = originalToName;
				this.toName = originalFromName;
			}
		}
		this.isInverse = isInverse;
		this.countOfAttributeReference = 0;
		this.countOfClassInstanceCreationReference = 0;
		this.countOfExtendsReference = 0;
		this.countOfImplementsReference = 0;
		this.countOfMethodParameterReference = 0;
		this.countOfMethodReturnValueReference = 0;
		this.countOfExtendsInterfaceReference = 0;
		
		if (NdLogger.getInstance().getDebugLogging()){
			String debugText = this.fromName + "(" + ref.getFrom().getName() + ")";
			debugText += " --> " + this.toName + "(" + ref.getTo().getName() + ")";
			debugText += ref.getReferencePointName();
			if (!this.isInverse){
				NdLogger.getInstance().debug("正 " + debugText);
			} else {
				NdLogger.getInstance().debug("逆 " + debugText);
			}
		}
	}
	
	/**
	 * fromのパッケージ名を応答する
	 * @return
	 */
	public String getFromName() {
		return fromName;
	}
	
	/**
	 * toのパッケージ名を応答する
	 * @return
	 */
	public String getToName() {
		return toName;
	}
	
	/**
	 * 逆依存か否かを応答する
	 * @return
	 */
	public boolean isInverse(){
		return this.isInverse;
	}
	
	/**
	 * 件数を加算する
	 * @param ref
	 */
	public void countup(NdAbstractReference ref){
		if (ref.isAsAttribute()){
			this.countOfAttributeReference++;
		} else if (ref.isAsClassInstanceCreation()) {
			this.countOfClassInstanceCreationReference++;
		} else if (ref.isAsExtends()) {
			this.countOfExtendsReference++;
		} else if (ref.isAsImplements()) {
			this.countOfImplementsReference++;
		} else if (ref.isAsMethodParameter()) {
			this.countOfMethodParameterReference++;
		} else if (ref.isAsMethodReturnValue()) {
			this.countOfMethodReturnValueReference++;
		} else if (ref.isAsExtendsInterface()) {
			this.countOfExtendsInterfaceReference++;
		}
	}
	
	/**
	 * 参照数の合計を応答する
	 * @return
	 */
	public int getTotal(){
		return this.countOfAttributeReference
		+ this.countOfClassInstanceCreationReference
		+ this.countOfExtendsReference
		+ this.countOfImplementsReference
		+ this.countOfMethodParameterReference
		+ this.countOfMethodReturnValueReference
		+ this.countOfExtendsInterfaceReference;
	}
	
	/**
	 * 属性参照による件数を応答する
	 * @return
	 */
	public int getCountOfAttributeReference() {
		return countOfAttributeReference;
	}
	
	/**
	 * new参照による件数を応答する
	 * @return
	 */
	public int getCountOfClassInstanceCreationReference() {
		return countOfClassInstanceCreationReference;
	}
	
	/**
	 * スーパークラス参照による件数を応答する
	 * @return
	 */
	public int getCountOfExtendsReference() {
		return countOfExtendsReference;
	}
	
	/**
	 * インタフェース実装参照による件数を応答する
	 * @return
	 */
	public int getCountOfImplementsReference() {
		return countOfImplementsReference;
	}
	
	/**
	 * メソッド引数参照による件数を応答する
	 * @return
	 */
	public int getCountOfMethodParameterReference() {
		return countOfMethodParameterReference;
	}
	
	/**
	 * メソッド戻り値参照による件数を応答する
	 * @return
	 */
	public int getCountOfMethodReturnValueReference() {
		return countOfMethodReturnValueReference;
	}

	/**
	 * スーパーインタフェース参照による件数を応答する
	 * @return
	 */
	public int getCountOfExtendsInterfaceReference() {
		return countOfExtendsInterfaceReference;
	}

	/**
	 * equals
	 * fromパッケージ名とtoパッケージ名が等しく、かつ、依存方向が同じ場合に同値とする 
	 */
	@Override
	public boolean equals(Object o){
		boolean result = false;
		if ((o != null) && (o instanceof NdPackageDependencyInfo)){
			NdPackageDependencyInfo otherInfo = (NdPackageDependencyInfo)o;
			String from1 = this.fromName;
			String from2 = otherInfo.fromName;
			String to1 = this.toName;
			String to2 = otherInfo.toName;
			if ((from1 != null) && (to1 != null) && (from2 != null) && (to2 != null)){
				if ((from1.equals(from2)) && (to1.equals(to2))){
					boolean b1 = this.isInverse;
					boolean b2 = otherInfo.isInverse;
					result = !((b1 || b2) && (!b1 || !b2));
				}
			}
		}
		return result;
	}
	
	/**
	 * hashCode
	 */
	@Override
	public int hashCode(){
		int result = -1;
		if (this.fromName != null){
			result = this.fromName.hashCode();
		}
		return result;
	}
}
