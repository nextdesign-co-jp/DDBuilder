/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.template.cases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.co.nextdesign.ddb.core.template.BdCodeLine;
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethod;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethodResult;
import jp.co.nextdesign.jcr.model.core.NdAbstractClassifier;
import jp.co.nextdesign.jcr.model.core.NdImportDeclaration;
import jp.co.nextdesign.jcr.model.reference.NdAbstractReference;

public class BdCodeCaseJavaImportEditPage extends BdBaseCodeCase {

	/**
	 * コンストラクタ
	 * @param lineListOfCase
	 */
	public BdCodeCaseJavaImportEditPage(List<BdCodeLine> lineListOfCase){
		super(lineListOfCase);
	}

	/** クラスに対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserClass c){
		List<String> resultList = new ArrayList<String>();
		String groupId = c.getGroupId();
		resultList.add("import " + c.getFullName() + ";"); //自身 Book
		for(NdAbstractReference  ref : c.getNdClass().getReferenceList()){
			NdAbstractClassifier referTo = ref.getTo();
			if (isUserSpecificClass(referTo, groupId)){
				if (!isDomainDdbClass(referTo.getQualifiedName(), groupId)){
					String importLine = "import " + referTo.getQualifiedName() + ";";
					if (!resultList.contains(importLine)){
						resultList.add(importLine);
					}
				}
			}
		}
		Collections.sort(resultList);
		return resultList;
	}
	
	/** ユーザパッケージ内のクラスか否か */
	private boolean isUserSpecificClass(NdAbstractClassifier referTo, String groupId){
		return referTo != null && referTo.getPackageName() != null && referTo.getPackageName().startsWith(groupId);
	}
	
	/** DDB宣言のクラスか否か */
	private boolean isDomainDdbClass(String fullName, String groupId){
		return fullName != null && fullName.startsWith(groupId + ".domain.ddb");
	}
	
	/** サービスクラスに対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserServiceMethod m){
		List<String> resultList = new ArrayList<String>();
		String groupId = m.getBdServiceClass().getBdUserClass().getGroupId();
		resultList.add("import " + m.getBdServiceClass().getBdUserClass().getFullName() + ";"); //自身 Book
		for(NdAbstractReference  ref : m.getBdServiceClass().getBdUserClass().getNdClass().getReferenceList()){
			NdAbstractClassifier referTo = ref.getTo();
			if (isUserSpecificClass(referTo, groupId)){
				if (!isDomainDdbClass(referTo.getQualifiedName(), groupId)){
					String importLine = "import " + referTo.getQualifiedName() + ";";
					if (!resultList.contains(importLine)){
						resultList.add(importLine);
					}
				}
			}
		}
		Collections.sort(resultList);
		return resultList;
	}
	
	/** サービス実行結果に対応した生成コードを応答する */
	@Override
	public List<String> generate(BdUserServiceMethodResult r){
		List<String> resultList = new ArrayList<String>();
		BdUserServiceMethod m = r.getBdUserServiceMethod();
		String groupId = m.getBdServiceClass().getBdUserClass().getGroupId();
		//importしているdomainパッケージ内のクラスをimport文に追加する
		for(NdImportDeclaration importDeclaration : m.getBdServiceClass().getBdUserClass().getNdClass().getCompilationUnit().getImportDeclarationList()){
			String importClassFullName = importDeclaration.getName().getQualifiedName();
			if (importClassFullName.startsWith(groupId + ".domain")){
				resultList.add("import " + importClassFullName + ";");
			}
		}
		Collections.sort(resultList);
		return resultList;
	}

}
