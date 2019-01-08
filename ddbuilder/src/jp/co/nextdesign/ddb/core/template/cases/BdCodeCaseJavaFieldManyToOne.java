/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.template.cases;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.ddb.core.template.BdCodeLine;
import jp.co.nextdesign.ddb.core.user.BdUserAttribute;
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodParameter;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodResult;
import jp.co.nextdesign.util.BdStringHelper;
import jp.co.nextdesign.util.BdStringHelperReplacementPattern;

public class BdCodeCaseJavaFieldManyToOne extends BdBaseCodeCase {
	/**
	 * コンストラクタ
	 * @param lineListOfCase
	 */
	public BdCodeCaseJavaFieldManyToOne(List<BdCodeLine> lineListOfCase){
		super(lineListOfCase);
	}

	/** 属性に対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserAttribute a){
		List<String> lineList = new ArrayList<String>();
		List<BdStringHelperReplacementPattern> replacementPatternList = this.getReplacementPatternList(a);
		for(BdCodeLine codeLine : this.getCodeLineList()){
			lineList.add(BdStringHelper.replace(codeLine.getLine(), replacementPatternList));
		}
		return lineList;
	}

	/** 文字置換パターンを作成する */
	private List<BdStringHelperReplacementPattern> getReplacementPatternList(BdUserAttribute a){
		List<BdStringHelperReplacementPattern> resultList = new ArrayList<BdStringHelperReplacementPattern>();
		resultList.add(new BdStringHelperReplacementPattern("book", a.getBdUserClass().getLowerStartedName()));
		resultList.add(new BdStringHelperReplacementPattern("Book", a.getBdUserClass().getName()));
		BdUserClass typeBdUserClass = a.getAttributeTypeBdUserClass();
		if (typeBdUserClass != null){
			resultList.add(new BdStringHelperReplacementPattern("AuthorSingleSelectPanel", a.getUpperStartedName() + "SingleSelectPanel"));
			resultList.add(new BdStringHelperReplacementPattern("author", a.getName()));
			resultList.add(new BdStringHelperReplacementPattern("Author", typeBdUserClass.getName()));
		}
		return resultList;
	}

	/** 引数に対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserServiceMethodParameter p){
		List<String> lineList = new ArrayList<String>();
		List<BdStringHelperReplacementPattern> replacementPatternList = this.getReplacementPatternList(p);
		for(BdCodeLine codeLine : this.getCodeLineList()){
			lineList.add(BdStringHelper.replace(codeLine.getLine(), replacementPatternList));
		}
		return lineList;
	}

	/** 文字置換パターンを作成する */
	private List<BdStringHelperReplacementPattern> getReplacementPatternList(BdUserServiceMethodParameter p){
		List<BdStringHelperReplacementPattern> resultList = new ArrayList<BdStringHelperReplacementPattern>();
		BdUserClass typeBdUserClass = p.getParameterTypeBdUserClass();
		if (typeBdUserClass != null){
			resultList.add(new BdStringHelperReplacementPattern("著者", p.getTitle()));
			resultList.add(new BdStringHelperReplacementPattern("paramAuthor", p.getName()));
			resultList.add(new BdStringHelperReplacementPattern("authorSingleSelectPanel", p.getName() + "SingleSelectPanel"));
			resultList.add(new BdStringHelperReplacementPattern("authorModalWindow", p.getName() +  "ModalWindow"));
			resultList.add(new BdStringHelperReplacementPattern("Author", typeBdUserClass.getName()));
			resultList.add(new BdStringHelperReplacementPattern("author", p.getName()));
		}
		return resultList;
	}


	/** サービスメソッド戻り値に対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserServiceMethodResult r){
		List<String> resultList = new ArrayList<String>();
		List<BdStringHelperReplacementPattern> replacementPatternList = this.createReplacementPatternList(r);
		for(BdCodeLine codeLine : this.getCodeLineList()){
			resultList.add(BdStringHelper.replace(codeLine.getLine(), replacementPatternList));
		}
		return resultList;
	}
	
	/** 文字置換パターンを作成する */
	private List<BdStringHelperReplacementPattern> createReplacementPatternList(BdUserServiceMethodResult r){
		List<BdStringHelperReplacementPattern> resultList = new ArrayList<BdStringHelperReplacementPattern>();
		BdUserClass returnBdUserClass = r.getParameterTypeBdUserClass();
		if (returnBdUserClass != null){
			resultList.add(new BdStringHelperReplacementPattern("//final", "final"));
			resultList.add(new BdStringHelperReplacementPattern("//item", "item"));
			resultList.add(new BdStringHelperReplacementPattern("book", r.getLowerStartedSimpleOrCollectionElementTypeName()));
			resultList.add(new BdStringHelperReplacementPattern("Book", r.getSimpleOrCollectionElementTypeName()));
		}
		return resultList;
	}
}
