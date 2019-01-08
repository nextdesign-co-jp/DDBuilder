/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.documentation.project;

import jp.co.nextdesign.jcr.documentation.NdBaseReportSheet;
import jp.co.nextdesign.jcr.documentation.xls.NdWorkbook;
import jp.co.nextdesign.jcr.model.core.NdProject;

/**
 * プロジェクトレポート内シートの基底クラス
 * @author murayama
 */
public abstract class NdBaseProjectReportSheet extends NdBaseReportSheet {

	protected NdProject project;
	
	/**
	 * コンストラクタ
	 * @param workbook
	 * @param project
	 */
	protected NdBaseProjectReportSheet(NdWorkbook workbook, NdProject project){
		super(workbook);
		this.project = project;
	}
}
