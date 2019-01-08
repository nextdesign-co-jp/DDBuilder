/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.documentation.project;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.jcr.NdApplicationProperty;
import jp.co.nextdesign.jcr.NdApplicationPropertyException;
import jp.co.nextdesign.jcr.documentation.NdDocumentationException;
import jp.co.nextdesign.jcr.documentation.NdMask;
import jp.co.nextdesign.jcr.documentation.xls.NdSheet;
import jp.co.nextdesign.jcr.documentation.xls.NdWorkbook;
import jp.co.nextdesign.jcr.model.core.NdAbstractClassifier;
import jp.co.nextdesign.jcr.model.core.NdAbstractNamedElement;
import jp.co.nextdesign.jcr.model.core.NdCompilationUnit;
import jp.co.nextdesign.jcr.model.core.NdPackage;
import jp.co.nextdesign.jcr.model.core.NdProject;

/**
 * プロジェクトレポート　メトリクスシート
 * @author murayama
 */
public class NdMetricsSheet extends NdBaseProjectReportSheet{

	//警告レベル
	private int nestWarning = 0;
	private int methodSizeWarning = 0;
	
	private static final int SHEET_NO = 1;
	private static final String REPORT_LINE_KEY = "ReportLine";
	private static final String KEY_PACKAGE_NAME = "PackageName";
	private static final String KEY_SUB_PACKAGE_COUNT = "SubPackageCount";
	private static final String KEY_CLASSIFIER_COUNT = "ClassifierCount";
	private static final String KEY_IS_PUBLIC = "IsPublic";
	private static final String KEY_ABSTRACT_INTERFACE = "AbstractInterface";
	private static final String KEY_CLASSIFIER_NAME = "ClassifierName";
	private static final String KEY_TOTAL_STEP_COUNT = "TotalStepCount";
	private static final String KEY_REAL_STEP_COUNT = "RealStepCount";
	private static final String KEY_JAVADOC_STEP_COUNT = "JavadocStepCount";
	private static final String KEY_COMMENT_STEP_COUNT = "CommentStepCount";
	private static final String KEY_METHOD_COUNT = "MethodCount";
	private static final String KEY_CONSTRUCTOR_COUNT = "ConstructorCount";
	private static final String KEY_INNER_CLASS_COUNT = "InnerClassCount";
	private static final String KEY_ANONYMOUS_CLASS_COUNT = "AnonymousClassCount";
	private static final String KEY_INITIALIZER_COUNT = "InitializerCount";
	private static final String KEY_INNER_ENUM_COUNT = "InnerEnumCount";
	private static final String KEY_METHOD_MAX_STEP_COUNT = "MethodMaxStepCount";
	private static final String KEY_METHOD_MAX_NEST_LEVEL = "MethodMaxNestCount";
	private static final String KEY_METHOD_CONTROL_STEP_COUNT = "MethodControlStepCount";
	private static final String KEY_ATTRIBUTE_COUNT = "AttributeCount";

	/**
	 * メトリクスシートを作成する
	 * @throws NdDocumentationException
	 * @throws NdApplicationPropertyException
	 */
	public void buildSheet()throws NdDocumentationException, NdApplicationPropertyException{
		NdSheet sheet = workbook.getSheetAt(SHEET_NO);
		if (sheet != null){
			List<NdMetricsLine> metricsLineList = this.createProjectReportLineList();
			this.addEmptyRows(metricsLineList.size(), sheet, REPORT_LINE_KEY);
			this.setDetailRow(metricsLineList, sheet);
		}
	}
	
	/**
	 * 詳細行に値を設定する（合計の集計と合計行の設定を含む）
	 * @param metricsRowList
	 * @param sheet
	 * @throws NdDocumentationException 
	 */
	private void setDetailRow(List<NdMetricsLine> metricsRowList, NdSheet sheet) throws NdDocumentationException{
		int sumOfClassifierCount = 0;
		int sumOfTotalStepCount = 0;
		int sumOfRealStepCount = 0;
		int sumOfJavadocStepCount = 0;
		int sumOfCommentStepCount = 0;
		int sumOfMethodCount = 0;
		int sumOfAttributeCount = 0;
		int sumOfConstructorCount = 0;
		int sumOfInnerClassCount = 0;
		int sumOfAnonymousClassCount = 0;
		int sumOfInnerEnumCount = 0;
		int sumOfInitializerCount = 0;
		int setRowIndex = sheet.getRowIndexByKey(REPORT_LINE_KEY);
		for(int i=0; i<metricsRowList.size(); i++){
			NdMetricsLine reportLine = metricsRowList.get(i);
			if (reportLine.isPackage){
				this.setPackageRow(setRowIndex, reportLine, sheet);
				sumOfClassifierCount += this.toSummable(reportLine.classifierCount);
			} else {
				this.setClassifierRow(setRowIndex, reportLine, sheet);
				sumOfTotalStepCount += this.toSummable(reportLine.totalStepCount);
				sumOfRealStepCount += this.toSummable(reportLine.realStepCount);
				sumOfJavadocStepCount += this.toSummable(reportLine.javadocStepCount);
				sumOfCommentStepCount += this.toSummable(reportLine.commentStepCount);
				sumOfMethodCount += this.toSummable(reportLine.methodCount);
				sumOfAttributeCount += this.toSummable(reportLine.attributeCount);
				sumOfConstructorCount += this.toSummable(reportLine.constructorCount);
				sumOfInnerClassCount += this.toSummable(reportLine.innerClassCount);
				sumOfAnonymousClassCount += this.toSummable(reportLine.anonymousClassCount);
				sumOfInnerEnumCount += this.toSummable(reportLine.innerEnumCount);
				sumOfInitializerCount += this.toSummable(reportLine.initializerCount);
			}
			setRowIndex++;
		}
		sheet.setValue(setRowIndex, 0, "集計");
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_CLASSIFIER_COUNT), sumOfClassifierCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_TOTAL_STEP_COUNT), sumOfTotalStepCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_REAL_STEP_COUNT), sumOfRealStepCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_JAVADOC_STEP_COUNT), sumOfJavadocStepCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_COMMENT_STEP_COUNT), sumOfCommentStepCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_METHOD_COUNT), sumOfMethodCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_ATTRIBUTE_COUNT), sumOfAttributeCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_CONSTRUCTOR_COUNT), sumOfConstructorCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_INNER_CLASS_COUNT), sumOfInnerClassCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_ANONYMOUS_CLASS_COUNT), sumOfAnonymousClassCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_INNER_ENUM_COUNT), sumOfInnerEnumCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_INITIALIZER_COUNT), sumOfInitializerCount);
	}

	/** JTable設定用 add 2014.4.25 ver8 */
	public static final int INDEX_PACKAGE_NAME = 0;
	public static final int INDEX_SUB_PACKAGE_COUNT = 1;
	public static final int INDEX_CLASSIFIER_COUNT = 2;
	public static final int INDEX_IS_PUBLIC = 3;
	public static final int INDEX_ABSTRACT_INTERFACE = 4;
	public static final int INDEX_CLASSIFIER_NAME = 5;
	public static final int INDEX_TOTAL_STEP_COUNT = 6;
	public static final int INDEX_REAL_STEP_COUNT = 7;
	public static final int INDEX_JAVADOC_STEP_COUNT = 8;
	public static final int INDEX_COMMENT_STEP_COUNT = 9;
	public static final int INDEX_ATTRIBUTE_COUNT = 10;
	public static final int INDEX_METHOD_COUNT = 11;
	public static final int INDEX_CONSTRUCTOR_COUNT = 12;
	public static final int INDEX_INNER_CLASS_COUNT = 13;
	public static final int INDEX_ANONYMOUS_CLASS_COUNT = 14;
	public static final int INDEX_INNER_ENUM_COUNT = 15;
	public static final int INDEX_INITIALIZER_COUNT = 16;
	public static final int INDEX_METHOD_MAX_STEP_COUNT = 17;
	public static final int INDEX_METHOD_MAX_NEST_LEVEL = 18;
	public static final int INDEX_METHOD_CONTROL_STEP_COUNT = 19;
	
	/**
	 * JTable設定用データ配列を応答する add 2014.4.25 ver8
	 * @param metricsRowList
	 * @return
	 * @throws NdDocumentationException
	 */
	public Object[][] getTableData(List<NdMetricsLine> metricsRowList) throws NdDocumentationException{
		int ROW_SIZE = metricsRowList.size() + 1;
		Object[][] result = new Object[ROW_SIZE][20];
		int sumOfClassifierCount = 0;
		int sumOfTotalStepCount = 0;
		int sumOfRealStepCount = 0;
		int sumOfJavadocStepCount = 0;
		int sumOfCommentStepCount = 0;
		int sumOfMethodCount = 0;
		int sumOfAttributeCount = 0;
		int sumOfConstructorCount = 0;
		int sumOfInnerClassCount = 0;
		int sumOfAnonymousClassCount = 0;
		int sumOfInnerEnumCount = 0;
		int sumOfInitializerCount = 0;
		for(int i=0; i<metricsRowList.size(); i++){
			NdMetricsLine reportLine = metricsRowList.get(i);
			Object[] rowData = new Object[20];
			rowData[INDEX_PACKAGE_NAME] = reportLine.packageName;
			rowData[INDEX_SUB_PACKAGE_COUNT] = reportLine.subPackageCount;
			rowData[INDEX_CLASSIFIER_COUNT] = reportLine.classifierCount;
			rowData[INDEX_IS_PUBLIC] = reportLine.isPublic;
			rowData[INDEX_ABSTRACT_INTERFACE] = reportLine.abstract_interface;
			rowData[INDEX_CLASSIFIER_NAME] = reportLine.claasifierName;
			rowData[INDEX_TOTAL_STEP_COUNT] = reportLine.totalStepCount;
			rowData[INDEX_REAL_STEP_COUNT] = reportLine.realStepCount;
			rowData[INDEX_JAVADOC_STEP_COUNT] = reportLine.javadocStepCount;
			rowData[INDEX_COMMENT_STEP_COUNT] = reportLine.commentStepCount;
			rowData[INDEX_ATTRIBUTE_COUNT] = reportLine.attributeCount;
			rowData[INDEX_METHOD_COUNT] = reportLine.methodCount;
			rowData[INDEX_CONSTRUCTOR_COUNT] = reportLine.constructorCount;
			rowData[INDEX_INNER_CLASS_COUNT] = reportLine.innerClassCount;
			rowData[INDEX_ANONYMOUS_CLASS_COUNT] = reportLine.anonymousClassCount;
			rowData[INDEX_INITIALIZER_COUNT] = reportLine.initializerCount;
			rowData[INDEX_INNER_ENUM_COUNT] = reportLine.innerEnumCount;
			rowData[INDEX_METHOD_MAX_STEP_COUNT] = reportLine.methodMaxStepCount;
			rowData[INDEX_METHOD_MAX_NEST_LEVEL] = reportLine.maxNestLevel;
			rowData[INDEX_METHOD_CONTROL_STEP_COUNT] = reportLine.controlStepCount;
			if (reportLine.isPackage){
				sumOfClassifierCount += this.toSummable(reportLine.classifierCount);
			} else {
				sumOfTotalStepCount += this.toSummable(reportLine.totalStepCount);
				sumOfRealStepCount += this.toSummable(reportLine.realStepCount);
				sumOfJavadocStepCount += this.toSummable(reportLine.javadocStepCount);
				sumOfCommentStepCount += this.toSummable(reportLine.commentStepCount);
				sumOfMethodCount += this.toSummable(reportLine.methodCount);
				sumOfAttributeCount += this.toSummable(reportLine.attributeCount);
				sumOfConstructorCount += this.toSummable(reportLine.constructorCount);
				sumOfInnerClassCount += this.toSummable(reportLine.innerClassCount);
				sumOfAnonymousClassCount += this.toSummable(reportLine.anonymousClassCount);
				sumOfInnerEnumCount += this.toSummable(reportLine.innerEnumCount);
				sumOfInitializerCount += this.toSummable(reportLine.initializerCount);
			}
			result[i] = rowData;
		}
		Object[] totalRowData = new Object[20];		
		totalRowData[INDEX_PACKAGE_NAME] = "集計";
		totalRowData[INDEX_SUB_PACKAGE_COUNT] = "";
		totalRowData[INDEX_CLASSIFIER_COUNT] = sumOfClassifierCount;
		totalRowData[INDEX_IS_PUBLIC] = "";
		totalRowData[INDEX_ABSTRACT_INTERFACE] = "";
		totalRowData[INDEX_TOTAL_STEP_COUNT] = "";
		totalRowData[INDEX_TOTAL_STEP_COUNT] = sumOfTotalStepCount;
		totalRowData[INDEX_REAL_STEP_COUNT] = sumOfRealStepCount;
		totalRowData[INDEX_JAVADOC_STEP_COUNT] = sumOfJavadocStepCount;
		totalRowData[INDEX_COMMENT_STEP_COUNT] = sumOfCommentStepCount;
		totalRowData[INDEX_ATTRIBUTE_COUNT] = sumOfAttributeCount;
		totalRowData[INDEX_METHOD_COUNT] = sumOfMethodCount;
		totalRowData[INDEX_CONSTRUCTOR_COUNT] = sumOfConstructorCount;
		totalRowData[INDEX_INNER_CLASS_COUNT] = sumOfInnerClassCount;
		totalRowData[INDEX_ANONYMOUS_CLASS_COUNT] = sumOfAnonymousClassCount;
		totalRowData[INDEX_INNER_ENUM_COUNT] = sumOfInnerEnumCount;
		totalRowData[INDEX_INITIALIZER_COUNT] = sumOfInitializerCount;
		totalRowData[INDEX_METHOD_MAX_STEP_COUNT] = "";
		totalRowData[INDEX_METHOD_MAX_NEST_LEVEL] = "";
		totalRowData[INDEX_METHOD_CONTROL_STEP_COUNT] = "";
		result[ROW_SIZE-1] = totalRowData;
		return result;
	}

	/**
	 * パッケージ行を設定する
	 * @param setRowIndex
	 * @param metricsLine
	 * @param sheet
	 * @throws NdDocumentationException 
	 */
	private void setPackageRow(int setRowIndex, NdMetricsLine metricsLine, NdSheet sheet) throws NdDocumentationException{
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_PACKAGE_NAME), metricsLine.packageName);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_SUB_PACKAGE_COUNT), metricsLine.subPackageCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_CLASSIFIER_COUNT), metricsLine.classifierCount);
	}
	
	/**
	 * 分類子行を設定する
	 * @param setRowIndex
	 * @param metricsLine
	 * @param sheet
	 * @throws NdDocumentationException 
	 */
	private void setClassifierRow(int setRowIndex, NdMetricsLine metricsLine, NdSheet sheet) throws NdDocumentationException{
		//列位置をマップから毎回取り出すことになるが、コードを小さくすることを優先した 2011.9.20
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_IS_PUBLIC), metricsLine.isPublic);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_ABSTRACT_INTERFACE), metricsLine.abstract_interface);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_CLASSIFIER_NAME), metricsLine.claasifierName);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_TOTAL_STEP_COUNT), metricsLine.totalStepCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_REAL_STEP_COUNT), metricsLine.realStepCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_JAVADOC_STEP_COUNT), metricsLine.javadocStepCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_COMMENT_STEP_COUNT), metricsLine.commentStepCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_METHOD_COUNT), metricsLine.methodCount);
		sheet.setValueAndWarning(setRowIndex, sheet.getColumnIndexByKey(KEY_METHOD_MAX_STEP_COUNT), metricsLine.methodMaxStepCount, this.methodSizeWarning);
		sheet.setValueAndWarning(setRowIndex, sheet.getColumnIndexByKey(KEY_METHOD_MAX_NEST_LEVEL), metricsLine.maxNestLevel, this.nestWarning);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_METHOD_CONTROL_STEP_COUNT), metricsLine.controlStepCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_ATTRIBUTE_COUNT), metricsLine.attributeCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_CONSTRUCTOR_COUNT), metricsLine.constructorCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_INNER_CLASS_COUNT), metricsLine.innerClassCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_ANONYMOUS_CLASS_COUNT), metricsLine.anonymousClassCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_INNER_ENUM_COUNT), metricsLine.innerEnumCount);
		sheet.setValue(setRowIndex, sheet.getColumnIndexByKey(KEY_INITIALIZER_COUNT), metricsLine.initializerCount);
		//ハイパーリンクを設定する
		sheet.setHyperlink(setRowIndex, sheet.getColumnIndexByKey(KEY_CLASSIFIER_NAME), metricsLine.hyperLinkFileName);
	}

	/**
	 * プロジェクトからレポート行リストを作成する
	 * @return
	 */
	public List<NdMetricsLine> createProjectReportLineList(){
		List<NdMetricsLine> resultList = new ArrayList<NdMetricsLine>();
		NdPackage rootPackage = this.project.getRootPackage();
		if (rootPackage != null){
			List<NdAbstractNamedElement> list = rootPackage.getNamedElementList();
			for(NdAbstractNamedElement element : list){
				if (element instanceof NdPackage){
					resultList.add(this.createPackageReportLine((NdPackage)element));
				} else if (element instanceof NdAbstractClassifier){
					resultList.add(this.createClassifierReportLine((NdAbstractClassifier)element));
				}
			}
		}
		return resultList;
	}

	/**
	 * パッケージのレポート行を作成する
	 * @param element
	 * @return
	 */
	private NdMetricsLine createPackageReportLine(NdPackage element){
		NdMetricsLine result = new NdMetricsLine();
		result.isPackage = true;
		result.packageName = element.getFullPackageName();
		result.subPackageCount = element.getChildPackageList().size();
		List<NdCompilationUnit> list = element.getCompilationUnitList();
		int classifierCount = 0;
		for(NdCompilationUnit unit : list){
			classifierCount += unit.getClassifierList().size();
		}
		result.classifierCount = classifierCount;
		return result;
	}

	/**
	 * 分類子のレポート行を作成する
	 * @param element
	 * @return
	 */
	private NdMetricsLine createClassifierReportLine(NdAbstractClassifier element){
		NdMetricsLine result = new NdMetricsLine();
		result.isPackage = false;
		result.claasifierName = NdMask.mask(element.getQualifiedNameWithoutPackage());
		if (element.isSubjectOfClassifierReport()){
			result.hyperLinkFileName = this.getClassifierReportFileName(element);
		}
		if (element.isPublic()){
			result.isPublic = "";
		} else {
			result.isPublic = "no";
		}
		result.abstract_interface = "";
		if (element.isInterface()){
			result.abstract_interface = "i";
		}
		if (element.isAbstract()){
			result.abstract_interface = "a";
		}
		if (element.isPrimaryClassifier()){
			result.totalStepCount = element.getCompilationUnitFileLineCount();
			result.realStepCount = element.getCompilationUnitStatementCount();
			result.javadocStepCount = element.getCompilationUnitJavadocLineCount();
			result.commentStepCount = element.getCompilationUnitBlockCommentLineCount() + element.getCompilationUnitLineCommentLineCount();
		}
		result.methodCount = element.getMethodListWithoutConstructor().size();
		result.methodMaxStepCount = element.getMaxMethodStepCount();
		result.maxNestLevel = element.getMaxMethodNestLevel();
		result.controlStepCount = element.getMaxMethodControlStepCount();
		result.attributeCount = element.getAttributeList().size();
		result.constructorCount = element.getConstructorList().size();
		result.innerClassCount = element.getInnerClassList().size();
		result.anonymousClassCount = element.getAnonymousClassList().size();
		result.innerEnumCount = element.getInnerEnumList().size();
		result.initializerCount = element.getInitializerList().size();
		return result;
	}

	/**
	 * コンストラクタ
	 * @param workbook
	 * @param project
	 * @throws NdApplicationPropertyException 
	 */
	public NdMetricsSheet(NdWorkbook workbook, NdProject project) throws NdApplicationPropertyException{
		super(workbook, project);
		this.nestWarning = NdApplicationProperty.getInstance().getMethodNestWarning();
		this.methodSizeWarning = NdApplicationProperty.getInstance().getMethodSizeWarning();
	}
}
