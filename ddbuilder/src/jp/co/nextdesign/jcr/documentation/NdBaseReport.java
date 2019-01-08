/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.documentation;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import jp.co.nextdesign.jcr.NdApplicationProperty;
import jp.co.nextdesign.jcr.NdApplicationPropertyException;
import jp.co.nextdesign.jcr.NdConstants;
import jp.co.nextdesign.jcr.documentation.xls.NdSheet;
import jp.co.nextdesign.jcr.documentation.xls.NdWorkbook;
import jp.co.nextdesign.jcr.key.NdAbstractKey;
import jp.co.nextdesign.jcr.key.NdKeyManager;
import jp.co.nextdesign.jcr.model.core.NdAbstractClassifier;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 * レポート基底クラス
 * @author murayama
 */
public abstract class NdBaseReport {
	
	private File outputDirectory;
	protected static final String CR = "\n";
	
	protected static final int HEADER_SHEET_NO = 0;
	private static final String HEADER_SHEET_DATE_LINE_KEY = "ReportDateLine";
	private static final String HEADER_SHEET_DATE_COL_KEY = "ReportDate";
	private static final String HEADER_SHEET_SYSTEM_LINE_KEY = "SystemLine";
	private static final String HEADER_SHEET_SYSTEM_NAME_COL_KEY = "SystemName";
	private static final String HEADER_SHEET_COMPANY_LINE_KEY = "CompanyLine";
	private static final String HEADER_SHEET_COMPANY_NAME_COL_KEY = "CompanyName";
	private static final SimpleDateFormat HEADER_REPORT_DATE_FORMAT = new SimpleDateFormat("yyyy年M月d日 作成 ");

	/**
	 * Excel出力ディレクトリ名を応答する
	 * @return
	 */
	protected String getOutputDirectoryName(){
		String result = "";
		if (this.outputDirectory != null){
			result = this.outputDirectory.getPath();
		}
		return result;
	}
	
	/**
	 * Excel出力ファイル名を応答する
	 * ProjectReportのHyperLink作成時にも使用するので、NdClassifierReportではなく、ここに定義する。
	 * @param classifier
	 * @return
	 */
	protected String getClassifierReportFileName(NdAbstractClassifier classifier){
		String result = classifier.getQualifiedName();
		result = result.replaceAll("\\.", "_");
		result = "クラスレポート_" + result + ".xlsx";;
		return result;
	}

	/**
	 * Excelテンプレートを読み込む
	 */
	protected NdWorkbook createWorkbookByTemplate() throws InvalidFormatException, NdDocumentationException, IOException{
		NdWorkbook result = new NdWorkbook(this.getTemplateFileName());
		return result;
	}
	
	/**
	 * Excelテンプレートファイル名を応答する
	 * @return
	 */
	protected abstract String getTemplateFileName();

	/**
	 * 表紙用レポート作成日を応答する
	 * @return
	 */
	protected String getReportDate(){
		Calendar now = Calendar.getInstance();
		return HEADER_REPORT_DATE_FORMAT.format(now.getTime());
	}
	
	/**
	 * 表紙のシステム名を設定する
	 * @param sheet
	 * @throws NdDocumentationException
	 * @throws NdApplicationPropertyException
	 */
	protected void setHeaderSystemRow(NdSheet sheet) throws NdDocumentationException, NdApplicationPropertyException{
		if (sheet != null){
			String systemName = NdApplicationProperty.getInstance().getSystemName();
			int rowIndex = sheet.getRowIndexByKey(HEADER_SHEET_SYSTEM_LINE_KEY);
			int columnIndex = sheet.getColumnIndexByKey(HEADER_SHEET_SYSTEM_NAME_COL_KEY);
			if ((rowIndex >= 0) && (columnIndex >= 0)){
				sheet.setValue(rowIndex, columnIndex, systemName);
			}
		}
	}	
	
	/**
	 * 表紙に会社名を設定する
	 * @param sheet
	 * @throws NdDocumentationException 
	 * @throws NdApplicationPropertyException 
	 */
	protected void setHeaderCompanyRow(NdSheet sheet) throws NdDocumentationException, NdApplicationPropertyException{
		if (sheet != null){
			String companyName = NdApplicationProperty.getInstance().getCompanyName();
			int rowIndex = sheet.getRowIndexByKey(HEADER_SHEET_COMPANY_LINE_KEY);
			int columnIndex = sheet.getColumnIndexByKey(HEADER_SHEET_COMPANY_NAME_COL_KEY);
			if ((rowIndex >= 0) && (columnIndex >= 0)){
				sheet.setValue(rowIndex, columnIndex, companyName);
			}
		}
	}
	
	/**
	 * 全シートのフッターにユーザ名を設定する
	 * @param workbook
	 */
	protected void setUserNameInFooter(NdWorkbook workbook){
		if (workbook != null){
			NdAbstractKey key = NdKeyManager.getInstance().getKey();
			if ((key != null) && (!key.getUserName4Footer().equals(""))){
				int sheetSize = workbook.getSheetCount();
				for (int i=0; i<sheetSize; i++){
					NdSheet sheet = workbook.getSheetAt(i);
					sheet.setLeftOfFooter(key.getUserName4Footer(), NdConstants.FOOTER_FONT_SIZE);
				}
			}
		}
	}
	
	/**
	 * 表紙に日付を設定する
	 * @param sheet
	 * @throws NdDocumentationException
	 */
	protected void setHeaderDateRow(NdSheet sheet) throws NdDocumentationException{
		if (sheet != null){
			int rowIndex = sheet.getRowIndexByKey(HEADER_SHEET_DATE_LINE_KEY);
			int columnIndex = sheet.getColumnIndexByKey(HEADER_SHEET_DATE_COL_KEY);
			if ((rowIndex >= 0) && (columnIndex >= 0)){
				sheet.setValue(rowIndex, columnIndex, this.getReportDate());
			}
		}
	}
		
	/**
	 * コンストラクタ
	 * @param outputDirectory
	 */
	protected NdBaseReport(File outputDirectory){
		this.outputDirectory = outputDirectory;
	}
	
	/**
	 * シートに空行を追加する
	 * @param projectReportRowList
	 * @param sheet
	 * @throws NdDocumentationException 
	 */
	protected void addEmptyRows(int size, NdSheet sheet, String lineKey) throws NdDocumentationException{
		int addRowPosition = sheet.getRowIndexByKey(lineKey);
		sheet.addRow(addRowPosition, size);
	}

	/**
	 * Integerを集計可能な値に補正する
	 * @param val
	 * @return
	 */
	protected Integer toSummable(Integer val){
		if (val != null){
			return val;
		} else {
			return 0;
		}
	}
}
