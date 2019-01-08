/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.documentation.classifier;

import java.io.File;
import java.io.IOException;

import jp.co.nextdesign.jcr.NdApplicationPropertyException;
import jp.co.nextdesign.jcr.documentation.NdBaseReport;
import jp.co.nextdesign.jcr.documentation.NdDocumentationException;
import jp.co.nextdesign.jcr.documentation.xls.NdSheet;
import jp.co.nextdesign.jcr.documentation.xls.NdWorkbook;
import jp.co.nextdesign.jcr.model.core.NdAbstractClassifier;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 * クラス/インタフェースレポート
 * @author murayama
 */
public class NdClassifierReport extends NdBaseReport {

	private NdAbstractClassifier classifier;
	private static final String TEMPLATE_FILE_NAME = "template/ClassifierReport.xlsx";
	private final static String HEADER_SHEET_CLASSIFIER_NAME_LINE_KEY = "ClassifierNameLine";
	private final static String HEADER_SHEET_CLASSIFIER_NAME_COL_KEY = "ClassifierName";

	/**
	 * クラスレポートを出力する
	 * @throws InvalidFormatException
	 * @throws NdDocumentationException
	 * @throws IOException
	 * @throws NdApplicationPropertyException 
	 */
	public void write() throws InvalidFormatException, NdDocumentationException, IOException, NdApplicationPropertyException{
		if (this.classifier != null){
			NdWorkbook workbook = this.createWorkbookByTemplate();
			if (workbook != null){
				this.writeHeaderSheet(workbook);
				this.writeDetailSheet(workbook);
				this.writeReferenceSheet(workbook);
				this.setUserNameInFooter(workbook);
				String outputFileName = this.getOutputDirectoryName() + "/" + this.getClassifierReportFileName(this.classifier);
				workbook.write(outputFileName);
			}
		}
	}

	/**
	 * 表示シートを作成する
	 * @throws NdApplicationPropertyException 
	 */
	protected void writeHeaderSheet(NdWorkbook workbook) throws NdDocumentationException, NdApplicationPropertyException{
		NdSheet sheet = workbook.getSheetAt(HEADER_SHEET_NO);
		if (sheet != null){
			this.setHeaderClassifierRow(sheet, this.classifier.getQualifiedName());
			this.setHeaderDateRow(sheet);
			this.setHeaderCompanyRow(sheet);
		}
	}
	
	/**
	 * 表紙の分類子名行を設定する
	 * @param sheet
	 * @throws NdDocumentationException
	 */
	protected void setHeaderClassifierRow(NdSheet sheet, String name) throws NdDocumentationException{
		if (sheet != null){
			int rowIndex = sheet.getRowIndexByKey(HEADER_SHEET_CLASSIFIER_NAME_LINE_KEY);
			int columnIndex = sheet.getColumnIndexByKey(HEADER_SHEET_CLASSIFIER_NAME_COL_KEY);
			if ((rowIndex >= 0) && (columnIndex >= 0)){
				sheet.setValue(rowIndex, columnIndex, name);
			}
		}
	}

	/**
	 * テンプレートファイル名を応答する
	 */
	@Override protected String getTemplateFileName(){
		return TEMPLATE_FILE_NAME;
	}

	/**
	 * 詳細シートを作成する
	 * @param workbook
	 * @throws NdDocumentationException
	 * @throws NdApplicationPropertyException
	 */
	private void writeDetailSheet(NdWorkbook workbook) throws NdDocumentationException, NdApplicationPropertyException{
		NdDetailSheet detailSheet = new NdDetailSheet(workbook, classifier);
		detailSheet.buildSheet();
	}
	
	/**
	 * クロスリファレンスシートを作成する
	 * @param workbook
	 * @throws NdApplicationPropertyException 
	 * @throws NdDocumentationException 
	 */
	private void writeReferenceSheet(NdWorkbook workbook) throws NdDocumentationException, NdApplicationPropertyException{
		NdCrossReferenceSheet referenceSheet = new NdCrossReferenceSheet(workbook, classifier);
		referenceSheet.buildSheet();
	}

	/**
	 * コンストラクタ
	 * 
	 * @param classifier
	 * @param outputDirectory
	 * @throws NdApplicationPropertyException
	 */
	public NdClassifierReport(NdAbstractClassifier classifier, File outputDirectory) throws NdApplicationPropertyException {
		super(outputDirectory);
		this.classifier = classifier;
	}
}
