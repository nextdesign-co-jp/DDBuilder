/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.documentation.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.co.nextdesign.jcr.NdApplicationPropertyException;
import jp.co.nextdesign.jcr.documentation.NdDocumentationException;
import jp.co.nextdesign.jcr.documentation.NdMask;
import jp.co.nextdesign.jcr.documentation.xls.NdSheet;
import jp.co.nextdesign.jcr.documentation.xls.NdWorkbook;
import jp.co.nextdesign.jcr.model.core.NdPackage;
import jp.co.nextdesign.jcr.model.core.NdProject;

/**
 * プロジェクトレポート　依存シート
 * @author murayama
 */
public class NdPackageDependencySheet extends NdBaseProjectReportSheet {

	private static final int SHEET_NO = 3;
	private static final String DEPENDENCY_LINE_KEY = "DependencyLine";

	private static final String COL_FROM = "From";
	private static final String COL_ROLE = "Role";
	private static final String COL_TO = "To";
	private static final String COL_TOTAL = "Total";
	private static final String COL_AS_ATTRIBUTE = "AsAttribute";
	private static final String COL_AS_CLASS_INSTANCE_CREATION = "AsClassInstanceCreation";
	private static final String COL_AS_EXTENDS = "AsExtends";
	private static final String COL_AS_IMPLEMENTS = "AsImplements";
	private static final String COL_AS_METHOD_PARAMETER = "AsMethodParameter";
	private static final String COL_AS_METHOD_RETURN_VALUE = "AsMethodReturnValue";
	private static final String COL_AS_EXTENDS_INTERFACE = "AsExtendsInterface";

	private static final String DEFAULT_PACKAGE_NAME = "DefaultPackage";
	private static final String DEPEND_TO = "---->";
	private static final String DEPENDED_BY = "<----";
	
	/**
	 * 依存シートを作成する
	 * @throws NdDocumentationException
	 * @throws NdApplicationPropertyException
	 */
	public void buildSheet()throws NdDocumentationException, NdApplicationPropertyException{
		NdSheet sheet = workbook.getSheetAt(SHEET_NO);
		if (sheet != null){
			List<NdPackageDependencyInfo> list = getInfoList();
			this.addEmptyRows(list.size() - 1, sheet, DEPENDENCY_LINE_KEY);
			this.setDependencyRow(list, sheet);
			return;
		}
	}
	
	/**
	 * プロジェクト内の全パッケージの依存情報を収集して応答する
	 * @return
	 */
	private List<NdPackageDependencyInfo> getInfoList(){
		List<NdPackageDependencyInfo> resultList = new ArrayList<NdPackageDependencyInfo>();
		if ((project != null) && (project.getRootPackage() != null)){
			resultList.addAll(this.collectInfoList(project.getRootPackage()));
		}
		Collections.sort(resultList, new NdPackageDependecyInfoComparator());
		return resultList;
	}
	
	/**
	 * 子パッケージの依存情報を再帰的に収集する
	 * @param pkg
	 * @return
	 */
	private List<NdPackageDependencyInfo> collectInfoList(NdPackage pkg){
		List<NdPackageDependencyInfo> resultList = new ArrayList<NdPackageDependencyInfo>();
		if (pkg != null){
			resultList.addAll(pkg.getDependencyList());
		}
		for(NdPackage child : pkg.getChildPackageList()){
			resultList.addAll(this.collectInfoList(child));
		}
		return resultList;
	}
	
	/**
	 * 各行に情報を設定する
	 * @param elementList
	 * @param sheet
	 * @throws NdDocumentationException
	 */
	private void setDependencyRow(List<NdPackageDependencyInfo> list, NdSheet sheet) throws NdDocumentationException{
		int setRowIndex = sheet.getRowIndexByKey(DEPENDENCY_LINE_KEY);
		int fromIndex = sheet.getColumnIndexByKey(COL_FROM);
		int roleIndex = sheet.getColumnIndexByKey(COL_ROLE);
		int toIndex = sheet.getColumnIndexByKey(COL_TO);
		int totalIndex = sheet.getColumnIndexByKey(COL_TOTAL);
		int asAttributeIndex = sheet.getColumnIndexByKey(COL_AS_ATTRIBUTE);
		int asClassInstanceCreationIndex = sheet.getColumnIndexByKey(COL_AS_CLASS_INSTANCE_CREATION);
		int asExtendsIndex = sheet.getColumnIndexByKey(COL_AS_EXTENDS);
		int asImplementsIndex = sheet.getColumnIndexByKey(COL_AS_IMPLEMENTS);
		int asMethodParameterIndex = sheet.getColumnIndexByKey(COL_AS_METHOD_PARAMETER);
		int asMethodReturnValueIndex = sheet.getColumnIndexByKey(COL_AS_METHOD_RETURN_VALUE);
		int asExtendsInterfaceIndex = sheet.getColumnIndexByKey(COL_AS_EXTENDS_INTERFACE);
		for(int i=0; i<list.size(); i++){
			NdPackageDependencyInfo info = list.get(i);
			String fromPackageName = NdMask.mask(info.getFromName());
			if ("".equals(fromPackageName)){
				fromPackageName = DEFAULT_PACKAGE_NAME;
			}
			String role = DEPEND_TO;
			if (info.isInverse()){
				role = DEPENDED_BY;
			}
			String toPackageName = NdMask.mask(info.getToName());
			if ("".equals(toPackageName)){
				toPackageName = DEFAULT_PACKAGE_NAME;
			}
			int total = info.getTotal();
			int asAttribute = info.getCountOfAttributeReference();
			int asClassInstaqnceCreation = info.getCountOfClassInstanceCreationReference();
			int asExtends = info.getCountOfExtendsReference();
			int asImplements = info.getCountOfImplementsReference();
			int asMethodParameter = info.getCountOfMethodParameterReference();
			int asMethodReturnValue = info.getCountOfMethodReturnValueReference();
			int asExtendsInterface = info.getCountOfExtendsInterfaceReference();
			sheet.setValue(setRowIndex, fromIndex, fromPackageName);
			sheet.setValue(setRowIndex, roleIndex, role);
			sheet.setValue(setRowIndex, toIndex, toPackageName);
			sheet.setValue(setRowIndex, totalIndex, total);
			sheet.setValue(setRowIndex, asAttributeIndex, asAttribute);
			sheet.setValue(setRowIndex, asClassInstanceCreationIndex, asClassInstaqnceCreation);
			sheet.setValue(setRowIndex, asExtendsIndex, asExtends);
			sheet.setValue(setRowIndex, asImplementsIndex, asImplements);
			sheet.setValue(setRowIndex, asMethodParameterIndex, asMethodParameter);
			sheet.setValue(setRowIndex, asMethodReturnValueIndex, asMethodReturnValue);
			sheet.setValue(setRowIndex, asExtendsInterfaceIndex, asExtendsInterface);
			setRowIndex++;
		}
	}

	/**
	 * コンストラクタ
	 * @param workbook
	 * @param project
	 * @throws NdApplicationPropertyException 
	 */
	public NdPackageDependencySheet(NdWorkbook workbook, NdProject project) throws NdApplicationPropertyException{
		super(workbook, project);
	}
}
