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
import jp.co.nextdesign.jcr.model.reference.NdReferenceAsAttribute;
import jp.co.nextdesign.jcr.model.reference.NdReferenceAsClassInstanceCreation;
import jp.co.nextdesign.jcr.model.typelink.NdAbstractTypeLink;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * UML属性
 * @author murayama
 *
 */
public class NdAttribute extends NdCoreElement {

	private NdAbstractTypeLink attributeType;
	private NdAbstractClassifier classifier;
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
	 * 属性に含まれるインスタンス生成式を応答する
	 * @return
	 */
	public List<NdClassInstanceCreation> getClassInstanceCreationList() {
		return classInstanceCreationList;
	}

	
	/**
	 * 要素型の表示名を応答する
	 * @return
	 */
	public String getDisplayTypeName(){
		return "属性";
	}
	
	/**
	 * 参照しているすべてのタイプ名のリストを応答する（Promitive型を除く）
	 * @return
	 */
	public List<NdAbstractReference> getReferenceList(){
		List<NdAbstractReference> resultList = new ArrayList<NdAbstractReference>();
		if (attributeType != null){
			List<NdName> list = attributeType.getReferenceList();
			for(NdName name : list){
				resultList.add(new NdReferenceAsAttribute(name));
			}
		}
		for(NdClassInstanceCreation creation : this.classInstanceCreationList){
			NdAbstractTypeLink typeLink = creation.getCreationTypeLink();
			if (typeLink != null){
				NdReferenceAsClassInstanceCreation ref = new NdReferenceAsClassInstanceCreation(typeLink.getTypeName());
				resultList.add(ref);
			}
		}
		return resultList;
	}

	/**
	 * 所属するクラス（双方向関連）を設定する
	 * @param classifier
	 */
	public void setClassifier(NdAbstractClassifier classifier){
		if ((classifier != null) && (this.classifier != classifier)){
			this.classifier = classifier;
			classifier.addAttribute(this);
		}
	}
	
	/**
	 * 所属するクラス（双方向関連）を応答する
	 * @return
	 */
	public NdAbstractClassifier getClassifier(){
		return this.classifier;
	}

	/**
	 * 属性の型を応答する
	 * @return
	 */
	public NdAbstractTypeLink getAttributeType() {
		return attributeType;
	}
	
	/**
	 * 属性の型を設定する
	 * @param attributeTypeLink
	 */
	public void setAttributeType(NdAbstractTypeLink attributeTypeLink) {
		this.attributeType = attributeTypeLink;
	}

	/**
	 * コンストラクタ
	 */
	public NdAttribute(){
		super();
		this.attributeType = NdAbstractTypeLink.UNDEFINED;
	}
	
	/**
	 * 主要情報の文字列表現を応答する
	 */
	@Override
	protected String getTitle(){
		String result = "";
		for(NdExtendedModifier m : this.getModifierList()){
			result += m.getKeyword() + " ";
		}
		result += this.attributeType.getDisplayName() + " ";
		result += this.getName();
		return result;
	}

	/** デバッグ */
	public void debugPrint(String tab){
		if (!NdLogger.getInstance().getDebugLogging()) return;
		NdLogger.getInstance().debug(tab + "属性=" + this.getName());
		this.debugPrintCore(tab);
		this.getAttributeType().debugPrint(tab + NdConstants.DEBUG_TAB);
	}
}
