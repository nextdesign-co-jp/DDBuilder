/*
 * DDBuilder
 * http://www.nextdesign.co.jp/ddd/index.html
 * Copyright 2015 NEXT DESIGN Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
