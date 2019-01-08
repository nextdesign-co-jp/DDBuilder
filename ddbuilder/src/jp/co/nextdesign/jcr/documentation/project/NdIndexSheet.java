/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.documentation.project;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.jcr.NdApplicationPropertyException;
import jp.co.nextdesign.jcr.documentation.NdDocumentationException;
import jp.co.nextdesign.jcr.documentation.NdMask;
import jp.co.nextdesign.jcr.documentation.xls.NdSheet;
import jp.co.nextdesign.jcr.documentation.xls.NdWorkbook;
import jp.co.nextdesign.jcr.model.core.NdAbstractClassifier;
import jp.co.nextdesign.jcr.model.core.NdAttribute;
import jp.co.nextdesign.jcr.model.core.NdCoreElement;
import jp.co.nextdesign.jcr.model.core.NdExtendedModifier;
import jp.co.nextdesign.jcr.model.core.NdMethod;
import jp.co.nextdesign.jcr.model.core.NdProject;

/**
 * プロジェクトレポート　索引シート
 * @author murayama
 */
public class NdIndexSheet extends NdBaseProjectReportSheet {

	private static final int SHEET_NO = 2;
	private static final String INDEX_LINE_KEY = "IndexLine";
	private static final String COL_NO = "No";
	private static final String COL_ELEMENT_NAME = "ElementName";
	private static final String COL_MODIFIERS = "Modifiers";
	private static final String COL_TYPE_NAME = "TypeName";
	private static final String COL_ELEMENT_TYPE = "ElementType";
	private static final String COL_CLASSIFIER_NAME = "ClassifierName";
	private static final String COL_JAVADOC = "Javadoc";
	private static final String TYPE_NAME_SUFFIX = "名";

	/**
	 * メトリクスシートを作成する
	 * @throws NdDocumentationException
	 * @throws NdApplicationPropertyException
	 */
	public void buildSheet()throws NdDocumentationException, NdApplicationPropertyException{
		NdSheet sheet = workbook.getSheetAt(SHEET_NO);
		if (sheet != null){
			List<NdCoreElement> fullList = this.project.getAllCoreElementList();
			List<NdCoreElement> elementList = this.removeAnonymousClass(fullList);
			this.addEmptyRows(elementList.size() - 1, sheet, INDEX_LINE_KEY);
			this.setIndexRow(elementList, sheet);
		}
	}
	
	/**
	 * 無名クラスを除いたリストを応答する
	 * @param fullList
	 * @return
	 */
	private List<NdCoreElement> removeAnonymousClass(List<NdCoreElement> fullList){
		List<NdCoreElement> resultList = new ArrayList<NdCoreElement>();
		for(int i=0; i<fullList.size(); i++){
			boolean isAnonymous = false;
			if (fullList.get(i) instanceof NdAbstractClassifier){
				if (((NdAbstractClassifier)fullList.get(i)).isAnonymousClass()){
					isAnonymous = true;
				}
			}
			if (!isAnonymous){
				resultList.add(fullList.get(i));
			}
		}
		return resultList;
	}

	/**
	 * 各行に情報を設定する
	 * @param elementList
	 * @param sheet
	 * @throws NdDocumentationException
	 */
	private void setIndexRow(List<NdCoreElement> elementList, NdSheet sheet) throws NdDocumentationException{
		int setRowIndex = sheet.getRowIndexByKey(INDEX_LINE_KEY);
		for(int i=0; i<elementList.size(); i++){
			NdCoreElement element = elementList.get(i);
			String elementName = "";
			String modifiers = this.getModifiers(element);
			String typeName = "";
			String elementType = "";
			String classifierName = "";
			String linkFileName = "";
			String javadocFirstLine = "";
			if (element.getJavadoc() != null){
				javadocFirstLine = element.getJavadoc().getFirstLine();
			}
			if (element instanceof NdAttribute){
				NdAttribute attribute = (NdAttribute)element;
				elementName = NdMask.mask(attribute.getName());
				if (attribute.getAttributeType() != null){
					typeName = attribute.getAttributeType().getDisplayName();
				}
				elementType = element.getDisplayTypeName() + TYPE_NAME_SUFFIX;
				NdAbstractClassifier c = attribute.getClassifier();
				if (c != null){
					classifierName = attribute.getClassifier().getQualifiedName();
					if (c.isSubjectOfClassifierReport()){
						linkFileName = this.getClassifierReportFileName(c);;
					}
				}
			} else if (element instanceof NdMethod) {
				NdMethod method = (NdMethod)element;
				if ((method.getReturnValue() != null) && (method.getReturnValue().getReturnValueType() != null)){
					typeName = method.getReturnValue().getReturnValueType().getDisplayName();
				}
				elementName = NdMask.mask(method.getName());
				elementType = element.getDisplayTypeName() + TYPE_NAME_SUFFIX;
				NdAbstractClassifier c = method.getClassifier();
				if (c != null){
					classifierName = method.getClassifier().getQualifiedName();
					if (c.isSubjectOfClassifierReport()){
						linkFileName = this.getClassifierReportFileName(c);
					}
				}
			} else if (element instanceof NdAbstractClassifier){
				NdAbstractClassifier classifier = (NdAbstractClassifier)element;
				elementName = NdMask.mask(classifier.getName());
				elementType = element.getDisplayTypeName() + TYPE_NAME_SUFFIX;
				if (classifier.isSubjectOfClassifierReport()) {
					linkFileName = this.getClassifierReportFileName(classifier);
				}
				classifierName = classifier.getQualifiedName();
			}
			sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(COL_NO), (i+1));
			sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(COL_ELEMENT_NAME), elementName);
			sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(COL_MODIFIERS), modifiers);
			sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(COL_TYPE_NAME), typeName);
			sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(COL_ELEMENT_TYPE), elementType);
			sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(COL_CLASSIFIER_NAME), classifierName);
			sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(COL_JAVADOC), javadocFirstLine);
			//ハイパーリンクを設定する
			if (!"".equals(linkFileName)){
				sheet.setHyperlink(setRowIndex, sheet.getColumnIndexByKey(COL_CLASSIFIER_NAME), linkFileName);
			}
			setRowIndex++;
		}
	}

	private static final String CR = "\n";

	/**
	 * 修飾子をすべて取り出し文字列として応答する
	 * @param element
	 * @return
	 */
	private String getModifiers(NdCoreElement element){
		String result = "";
		List<NdExtendedModifier> modifierList = element.getModifierList();
		for (int i=0; i<modifierList.size(); i++){
			result += modifierList.get(i).getKeyword() + "\n";
		}
		if (result.endsWith(CR)){
			result = result.substring(0, result.length() - CR.length());
		}
		return result;
	}

	/**
	 * コンストラクタ
	 * @param workbook
	 * @param project
	 * @throws NdApplicationPropertyException 
	 */
	public NdIndexSheet(NdWorkbook workbook, NdProject project) throws NdApplicationPropertyException{
		super(workbook, project);
	}
}
