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
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethod;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodResult;

/**
 * コードケース Import Ddb
 * @author murayama
 *
 */
public class BdCodeCaseJavaImportDdb extends BdBaseCodeCase {

	/**
	 * コンストラクタ
	 * @param lineListOfCase
	 */
	public BdCodeCaseJavaImportDdb(List<BdCodeLine> lineListOfCase){
		super(lineListOfCase);
	}

	/** クラスに対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserClass c){
		List<String> resultList = new ArrayList<String>();
		String groupId = c.getGroupId();
		for(BdCodeLine codeLine : this.getCodeLineList()){
			String line = codeLine.getLine();
			if (line.startsWith("import jp.co.nextdesign")){
				line = line.replaceAll("import jp.co.nextdesign", "import " + groupId);
			}
			resultList.add(line);
		}
		return resultList;
	}

	/** サービスクラスに対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserServiceMethod m){
		List<String> resultList = new ArrayList<String>();
		String groupId = m.getBdServiceClass().getBdUserClass().getGroupId();
		for(BdCodeLine codeLine : this.getCodeLineList()){
			String line = codeLine.getLine();
			if (line.startsWith("import jp.co.nextdesign")){
				line = line.replaceAll("import jp.co.nextdesign", "import " + groupId);
			}
			resultList.add(line);
		}
		return resultList;
	}

	/** サービス実行結果に対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserServiceMethodResult r){
		List<String> resultList = new ArrayList<String>();
		String groupId = r.getBdUserServiceMethod().getBdServiceClass().getBdUserClass().getGroupId();
		for(BdCodeLine codeLine : this.getCodeLineList()){
			String line = codeLine.getLine();
			if (line.startsWith("import jp.co.nextdesign")){
				line = line.replaceAll("import jp.co.nextdesign", "import " + groupId);
			}
			resultList.add(line);
		}
		return resultList;
	}
}
