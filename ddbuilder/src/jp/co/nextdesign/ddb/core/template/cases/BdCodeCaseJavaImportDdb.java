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
