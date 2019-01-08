/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.documentation.classifier;

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
import jp.co.nextdesign.jcr.model.core.NdEnum;
import jp.co.nextdesign.jcr.model.core.NdJavadoc;
import jp.co.nextdesign.jcr.model.core.NdMethod;

/**
 * クラスレポート　詳細シート
 * @author murayama
 */
public class NdDetailSheet extends NdBaseClassifierReportSheet {

	private static final int SHEET_NO = 1;
	private static final String LINE_KEY_TITLE = "Title";
	private static final String LINE_KEY_CLASSIFIER = "Classifier";
	private static final String LINE_KEY_SUPER_CLASS = "SuperClass";
	private static final String LINE_KEY_INTERFACE = "Interface";
	private static final String LINE_KEY_SUPER_INTERFACE = "SuperInterface";
	private static final String LINE_KEY_SUB_INTERFACE = "SubInterface";
	private static final String LINE_KEY_IMPLEMENTER = "Implementer";
	private static final String LINE_KEY_SUB_CLASS = "SubClass";
	private static final String LINE_KEY_ATTRIBUTE = "Attribute";
	private static final String LINE_KEY_CONSTRUCTOR = "Constructor";
	private static final String LINE_KEY_METHOD = "Method";
	private static final String LINE_KEY_INNER_CLASS = "InnerClass";
	private static final String LINE_KEY_ENUM = "Enum";
	private static final String COL_KEY_TITLE = "Name";
	private static final String COL_KEY_DECLARATION = "Declaration";
	private static final int COL_NUMBER_INDEX = 0;

	/**
	 * 詳細シートを作成する
	 * @throws NdDocumentationException
	 * @throws NdApplicationPropertyException
	 */
	public void buildSheet()throws NdDocumentationException, NdApplicationPropertyException{
		NdSheet sheet = workbook.getSheetAt(SHEET_NO);
		if (sheet != null){
			this.setTitleRow(sheet);
			this.setClassifierRow(sheet);
			this.setSuperClassRow(sheet);
			this.setInterfaceRow(sheet);
			this.setSubClassRow(sheet);
			this.setSuperInterfaceRow(sheet);
			this.setSubInterfaceRow(sheet);
			this.setImplementerRow(sheet);
			this.setAttributeRow(sheet);
			this.setConstructorRow(sheet);
			this.setMethodRow(sheet);
			this.setInnerClassRow(sheet);
			this.setEnumRow(sheet);
		}
	}

	/**
	 * コンストラクタ
	 * @param workbook
	 * @param classifier
	 */
	protected NdDetailSheet(NdWorkbook workbook, NdAbstractClassifier classifier){
		super(workbook, classifier);
	}

	/**
	 * 列インデックスを取得して応答する
	 * @param lineKey
	 * @param colKey
	 * @param sheet
	 * @return
	 * @throws NdDocumentationException
	 */
	private int getColimnIndexByKey(String lineKey, String colKey, NdSheet sheet) throws NdDocumentationException{
		String key = lineKey + colKey;
		return sheet.getColumnIndexByKey(key);
	}

	/**
	 * 行インデックスを取得して応答する
	 * @param lineKey
	 * @param sheet
	 * @return
	 * @throws NdDocumentationException
	 */
	private int getRowIndexByKey(String lineKey, NdSheet sheet) throws NdDocumentationException{
		String key = lineKey + NdSheet.LINE_KEY_SUFFIX;
		return sheet.getRowIndexByKey(key);
	}

	/**
	 * タイトル行を設定する
	 * @param sheet
	 * @throws NdDocumentationException
	 */
	private void setTitleRow(NdSheet sheet) throws NdDocumentationException{
		int rowIndex = this.getRowIndexByKey(LINE_KEY_TITLE, sheet);
		int columnIndex = this.getColimnIndexByKey(LINE_KEY_TITLE, COL_KEY_TITLE, sheet);
		sheet.setValue(rowIndex, columnIndex, this.classifier.getQualifiedName());
	}

	/**
	 * クラス/インタフェース宣言行を設定する
	 * @param sheet
	 * @throws NdDocumentationException
	 */
	private void setClassifierRow(NdSheet sheet) throws NdDocumentationException{
		int rowIndex = this.getRowIndexByKey(LINE_KEY_CLASSIFIER, sheet);
		int columnIndex = this.getColimnIndexByKey(LINE_KEY_CLASSIFIER, COL_KEY_DECLARATION, sheet);
		sheet.setValue(rowIndex, columnIndex, NdMask.mask(this.classifier.getHeadLine()));
	}
	
	/**
	 * スーパークラス行を設定する
	 * @param sheet
	 * @throws NdDocumentationException
	 */
	private void setSuperClassRow(NdSheet sheet) throws NdDocumentationException{
		List<NdAbstractClassifier> hierarchy = this.classifier.getSuperClassHierarchy();
		List<NdCoreElement> elementList = new ArrayList<NdCoreElement>();
		for (int i=hierarchy.size()-1; i>=0; i--){
			elementList.add(hierarchy.get(i));
		}
		this.setElementRowWithoutJavadoc(elementList, sheet, LINE_KEY_SUPER_CLASS);
	}

	/**
	 * インタフェース行を設定する
	 * @param sheet
	 * @throws NdDocumentationException
	 */
	private void setInterfaceRow(NdSheet sheet) throws NdDocumentationException{
		List<NdAbstractClassifier> list = this.classifier.getImplementInterfaceList();
		List<NdCoreElement> elementList = new ArrayList<NdCoreElement>();
		for (int i=0; i<list.size(); i++){
			elementList.add(list.get(i));
		}
		this.setElementRowWithoutJavadoc(elementList, sheet, LINE_KEY_INTERFACE);
	}
	
	/**
	 * サブクラス一覧行を設定する
	 * @param sheet
	 * @throws NdDocumentationException
	 */
	private void setSubClassRow(NdSheet sheet) throws NdDocumentationException{
		List<NdAbstractClassifier> list = this.classifier.getSubClassList();
		List<NdCoreElement> elementList = new ArrayList<NdCoreElement>();
		for (int i=0; i<list.size(); i++){
			elementList.add(list.get(i));
		}
		this.setElementRowWithoutJavadoc(elementList, sheet, LINE_KEY_SUB_CLASS);
	}

	/**
	 * スーパーインタフェース一覧行を設定する
	 * @param sheet
	 * @throws NdDocumentationException
	 */
	private void setSuperInterfaceRow(NdSheet sheet) throws NdDocumentationException{
		List<NdAbstractClassifier> list = this.classifier.getSuperInterfaceList();
		List<NdCoreElement> elementList = new ArrayList<NdCoreElement>();
		for (int i=0; i<list.size(); i++){
			elementList.add(list.get(i));
		}
		this.setElementRowWithoutJavadoc(elementList, sheet, LINE_KEY_SUPER_INTERFACE);
	}

	/**
	 * サブインタフェース一覧行を設定する
	 * @param sheet
	 * @throws NdDocumentationException
	 */
	private void setSubInterfaceRow(NdSheet sheet) throws NdDocumentationException{
		List<NdAbstractClassifier> list = this.classifier.getSubInterfaceList();
		List<NdCoreElement> elementList = new ArrayList<NdCoreElement>();
		for (int i=0; i<list.size(); i++){
			elementList.add(list.get(i));
		}
		this.setElementRowWithoutJavadoc(elementList, sheet, LINE_KEY_SUB_INTERFACE);
	}
	
	/**
	 * 実装クラス一覧行を設定する
	 * @param sheet
	 * @throws NdDocumentationException
	 */
	private void setImplementerRow(NdSheet sheet) throws NdDocumentationException{
		List<NdAbstractClassifier> list = this.classifier.getImplementerList();
		List<NdCoreElement> elementList = new ArrayList<NdCoreElement>();
		for (int i=0; i<list.size(); i++){
			elementList.add(list.get(i));
		}
		this.setElementRowWithoutJavadoc(elementList, sheet, LINE_KEY_IMPLEMENTER);
	}

	/**
	 * 属性行を設定する
	 * @param sheet
	 * @throws NdDocumentationException
	 */
	private void setAttributeRow(NdSheet sheet) throws NdDocumentationException{
		List<NdAttribute> attributeList = this.classifier.getAttributeList();
		List<NdCoreElement> elementList = new ArrayList<NdCoreElement>();
		for (int i=0; i<attributeList.size(); i++){
			elementList.add(attributeList.get(i));
		}
		this.setElementRowWithJavadoc(elementList, sheet, LINE_KEY_ATTRIBUTE);
	}

	/**
	 * コンストラクタ行を設定する
	 * @param sheet
	 * @throws NdDocumentationException
	 */
	private void setConstructorRow(NdSheet sheet) throws NdDocumentationException{
		List<NdMethod> methodList = this.classifier.getMethodList();
		List<NdCoreElement> elementList = new ArrayList<NdCoreElement>();
		for(int i=0; i<methodList.size(); i++){
			if (methodList.get(i).isConstructor()){
				elementList.add(methodList.get(i));
			}
		}
		this.setElementRowWithJavadoc(elementList, sheet, LINE_KEY_CONSTRUCTOR);
	}
	
	/**
	 * メソッド行を設定する
	 * @param sheet
	 * @throws NdDocumentationException
	 */
	private void setMethodRow(NdSheet sheet) throws NdDocumentationException{
		List<NdMethod> methodList = this.classifier.getMethodList();
		List<NdCoreElement> elementList = new ArrayList<NdCoreElement>();
		for(int i=0; i<methodList.size(); i++){
			if (!methodList.get(i).isConstructor()){
				elementList.add(methodList.get(i));
			}
		}
		this.setElementRowWithJavadoc(elementList, sheet, LINE_KEY_METHOD);
	}

	/**
	 * 内部クラス行を設定する
	 * @param sheet
	 * @throws NdDocumentationException
	 */
	private void setInnerClassRow(NdSheet sheet) throws NdDocumentationException{
		List<NdAbstractClassifier> innerClassList = this.classifier.getInnerClassList();
		List<NdCoreElement> elementList = new ArrayList<NdCoreElement>();
		for(int i=0; i<innerClassList.size(); i++){
			elementList.add(innerClassList.get(i));
		}
		this.setElementRowWithJavadoc(elementList, sheet, LINE_KEY_INNER_CLASS);
	}

	/**
	 * 列挙型行を設定する
	 * @param sheet
	 * @throws NdDocumentationException
	 */
	private void setEnumRow(NdSheet sheet) throws NdDocumentationException{
		List<NdEnum> enumList = this.classifier.getInnerEnumList();
		List<NdCoreElement> elementList = new ArrayList<NdCoreElement>();
		for(int i=0; i<enumList.size(); i++){
			elementList.add(enumList.get(i));
		}
		this.setElementRowWithJavadoc(elementList, sheet, LINE_KEY_ENUM);
	}
	
	/**
	 * 属性、コンストラクタ、メソッド、内部クラス、列挙型行を設定する（Javadocなし）
	 * @param list
	 * @param sheet
	 * @param lineKey
	 * @throws NdDocumentationException
	 */
	private void setElementRowWithoutJavadoc(List<NdCoreElement> list, NdSheet sheet, String lineKey) throws NdDocumentationException{
		this.setElementRow(list, sheet, lineKey, false);
	}

	/**
	 * 属性、コンストラクタ、メソッド、内部クラス、列挙型行を設定する（Javadoc付き）
	 * @param list
	 * @param sheet
	 * @param lineKey
	 * @throws NdDocumentationException
	 */
	private void setElementRowWithJavadoc(List<NdCoreElement> list, NdSheet sheet, String lineKey) throws NdDocumentationException{
		this.setElementRow(list, sheet, lineKey, true);
	}
	
	/**
	 * 属性、コンストラクタ、メソッド、内部クラス、列挙型行を設定する（共通のメソッド）
	 * @param list
	 * @param sheet
	 * @param lineKey
	 * @param withJavadoc
	 * @throws NdDocumentationException
	 */
	private void setElementRow(List<NdCoreElement> list, NdSheet sheet, String lineKey, boolean withJavadoc) throws NdDocumentationException{
		int startRowIndex = this.getRowIndexByKey(lineKey, sheet);
		int declarationColumnIndex = this.getColimnIndexByKey(lineKey, COL_KEY_DECLARATION, sheet);
		if (list.size() > 0){
			if (list.size() > 1){
				this.addEmptyRows(list.size() - 1, sheet, lineKey + NdSheet.LINE_KEY_SUFFIX);
			}
			for(int i=0; i<list.size(); i++){
				int newRowIndex = startRowIndex + i;
				sheet.setValue(newRowIndex, COL_NUMBER_INDEX, i+1);
				NdCoreElement a = list.get(i);
				String declaration = "";
				if (withJavadoc){
					declaration = this.javadocToText(a.getJavadoc()) + NdMask.mask(a.getHeadLine());
				} else {
					declaration = NdMask.mask(a.getHeadLine());
				}
				sheet.setValue(newRowIndex, declarationColumnIndex, declaration);
			}
		}
	}

	/**
	 * Javadoc文字列表現を応答する
	 * @param javadoc
	 * @return
	 */
	private String javadocToText(NdJavadoc javadoc){
		String result = "";
		if (javadoc != null){
			result = javadoc.getBlockString();
		}
		return result;
	}
}
