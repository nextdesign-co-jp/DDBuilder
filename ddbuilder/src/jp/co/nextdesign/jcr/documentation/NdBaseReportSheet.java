/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.documentation;

import jp.co.nextdesign.jcr.documentation.xls.NdSheet;
import jp.co.nextdesign.jcr.documentation.xls.NdWorkbook;
import jp.co.nextdesign.jcr.model.core.NdAbstractClassifier;

/**
 * すべてのレポートシートの基底クラス
 * @author murayama
 */
public abstract class NdBaseReportSheet {

	protected NdWorkbook workbook;
	
	/**
	 * コンストラクタ
	 * @param project
	 */
	protected NdBaseReportSheet(NdWorkbook workbook){
		super();
		this.workbook = workbook;
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
