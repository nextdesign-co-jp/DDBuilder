/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.documentation.project;

import java.io.File;
import java.io.IOException;

import jp.co.nextdesign.jcr.NdApplicationPropertyException;
import jp.co.nextdesign.jcr.documentation.NdBaseReport;
import jp.co.nextdesign.jcr.documentation.NdDocumentationException;
import jp.co.nextdesign.jcr.documentation.xls.NdSheet;
import jp.co.nextdesign.jcr.documentation.xls.NdWorkbook;
import jp.co.nextdesign.jcr.model.core.NdProject;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 * プロジェクトレポート
 * @author murayama
 */
public class NdProjectReport extends NdBaseReport {

	private NdProject project;
	private static final String TEMPLATE_FILE_NAME = "template/ProjectReport.xlsx";	
			
	/**
	 * レポートファイルを出力する
	 * @throws InvalidFormatException
	 * @throws NdDocumentationException
	 * @throws IOException
	 * @throws NdApplicationPropertyException 
	 */
	public void write() throws InvalidFormatException, NdDocumentationException, IOException, NdApplicationPropertyException{
		if (this.project != null){
			NdWorkbook workbook = this.createWorkbookByTemplate();
			if (workbook != null){
				this.writeHeaderSheet(workbook);
				this.writeMetricsSheet(workbook);
				this.writeIndexSheet(workbook);
				this.writePackageDependencySheet(workbook);
				this.setUserNameInFooter(workbook);
				workbook.write(this.getProjectReportFileName());
			}
		}
	}
	
	/**
	 * 表紙シートを作成する
	 * @throws NdDocumentationException 
	 * @throws NdApplicationPropertyException 
	 */
	protected void writeHeaderSheet(NdWorkbook workbook) throws NdDocumentationException, NdApplicationPropertyException{
		NdSheet sheet = workbook.getSheetAt(HEADER_SHEET_NO);
		if (sheet != null){
			this.setHeaderSystemRow(sheet);
			this.setHeaderDateRow(sheet);
			this.setHeaderCompanyRow(sheet);
		}
	}
	
	/**
	 * パッケージ依存シートを作成する
	 * @param workbook
	 * @throws NdDocumentationException
	 * @throws NdApplicationPropertyException
	 */
	private void writePackageDependencySheet(NdWorkbook workbook) throws NdDocumentationException, NdApplicationPropertyException{
		NdPackageDependencySheet packageDependencySheet = new NdPackageDependencySheet(workbook, project);
		packageDependencySheet.buildSheet();
	}
	
	/**
	 * 詳細シート（メトリクスシート）を作成する
	 * @param workbook
	 * @throws NdApplicationPropertyException
	 * @throws NdDocumentationException
	 */
	private void writeMetricsSheet(NdWorkbook workbook) throws NdApplicationPropertyException, NdDocumentationException{
		NdMetricsSheet metricsSheet = new NdMetricsSheet(workbook, project);
		metricsSheet.buildSheet();
	}
	
	/**
	 * 索引シートを作成する
	 * @param workbook
	 * @throws NdDocumentationException
	 * @throws NdApplicationPropertyException
	 */
	private void writeIndexSheet(NdWorkbook workbook) throws NdDocumentationException, NdApplicationPropertyException{
		NdIndexSheet indexSheet = new NdIndexSheet(workbook, project);
		indexSheet.buildSheet();
	}

	/**
	 * テンプレートファイル名を応答する
	 */
	@Override protected String getTemplateFileName(){
		return TEMPLATE_FILE_NAME;
	}
	
	/**
	 * 出力ファイル名を応答する
	 * @return
	 */
	private String getProjectReportFileName(){
		return this.getOutputDirectoryName() + "/" + "@プロジェクトレポート.xlsx";
	}

	/**
	 * コンストラクタ
	 * @param project
	 * @param outputDirectory
	 * @throws NdApplicationPropertyException
	 */
	public NdProjectReport(NdProject project, File outputDirectory) throws NdApplicationPropertyException {
		super(outputDirectory);
		this.project = project;
	}
}