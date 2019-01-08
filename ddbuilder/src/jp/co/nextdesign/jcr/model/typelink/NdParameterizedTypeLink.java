/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.typelink;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.jcr.model.NdName;
import jp.co.nextdesign.util.logging.NdLogger;

import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.Type;


/**
 * Parameterized型
 * Type < Type {, Type } >
 * @author murayama
 */
public class NdParameterizedTypeLink extends NdAbstractTypeLink {

	private ArrayList<NdAbstractTypeLink> typeArgumentList = new ArrayList<NdAbstractTypeLink>();
	private static final String COMMA = ", ";
	
	/**
	 * 参照しているタイプ名リストを応答する
	 * @return
	 */
	@Override public List<NdName> getReferenceList(){
		List<NdName> resultList = new ArrayList<NdName>();
		resultList.add(this.getTypeName());
		for(NdAbstractTypeLink link : this.typeArgumentList){
			resultList.addAll(link.getReferenceList());
		}
		return resultList;
	}

	/**
	 * 表示用の型名を応答する
	 * 2015.12.23 Map<T1,T2>をやめてMap<String,Map<String,Date>>などと表示できるように修正した
	 */
	@Override public String getDisplayName(){
		String result = this.getSimpleOrQualifiedName() + "<";
		List<NdAbstractTypeLink> list = this.getTypeArgumentList();
		for(NdAbstractTypeLink typeLink : list){
			String pTypeName = "";
			if (typeLink.isParameterizedType()){
				NdParameterizedTypeLink parameterizedTypeLink = (NdParameterizedTypeLink)typeLink;
				pTypeName = parameterizedTypeLink.getDisplayName();
			} else {
				pTypeName = typeLink.getSimpleOrQualifiedName();
			}
			result += pTypeName + COMMA;
		}
		if (result.endsWith(COMMA)){
			result = result.substring(0, result.length() - COMMA.length());
		}
		return result + ">";
	}


	/**
	 * 型引数リストを応答する
	 * @return
	 */
	public ArrayList<NdAbstractTypeLink> getTypeArgumentList() {
		return typeArgumentList;
	}
	
	/**
	 * 型引数リストを設定する
	 * @param typeArgumentList
	 */
	public void setTypeArgumentList(ArrayList<NdAbstractTypeLink> typeArgumentList) {
		this.typeArgumentList = typeArgumentList;
	}

	/**
	 * ParameterizedTypeか否かを応答する
	 */
	@Override
	public boolean isParameterizedType(){
		return true;
	}
	
	/**
	 * コンストラクタ
	 * @param jdtType
	 */
	public NdParameterizedTypeLink(ParameterizedType jdtType){
		super(jdtType);
		this.typeArgumentList = new ArrayList<NdAbstractTypeLink>();
		if (jdtType != null){
			for (Object o : jdtType.typeArguments()){
				Type typeArg = (Type)o;
				NdAbstractTypeLink typeLinkArg = NdTypeLinkFactory.createTypeLink(typeArg);
				this.typeArgumentList.add(typeLinkArg);
			}
		}
	}

	/** デバッグ */
	public void debugPrint(String tab){
		if (!NdLogger.getInstance().getDebugLogging()) return;
		NdLogger.getInstance().debug(tab + this.getDebugTitle());
		for(int i=0; i < this.typeArgumentList.size(); i++){
			NdAbstractTypeLink typeArg = this.typeArgumentList.get(i);
			String tab2 = tab + "型引数[" + i + "]";
			typeArg.debugPrint(tab2);
		}
	}
}
