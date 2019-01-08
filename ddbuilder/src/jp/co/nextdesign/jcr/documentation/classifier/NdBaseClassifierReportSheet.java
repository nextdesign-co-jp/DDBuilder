/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.documentation.classifier;

import jp.co.nextdesign.jcr.documentation.NdBaseReportSheet;
import jp.co.nextdesign.jcr.documentation.xls.NdWorkbook;
import jp.co.nextdesign.jcr.model.core.NdAbstractClassifier;

/**
 * クラスレポート内シートの基底クラス
 * @author murayama
 */
public abstract class NdBaseClassifierReportSheet extends NdBaseReportSheet {

	protected NdAbstractClassifier classifier;
	
	/**
	 * コンストラクタ
	 * @param workbook
	 * @param classifier
	 */
	protected NdBaseClassifierReportSheet(NdWorkbook workbook, NdAbstractClassifier classifier){
		super(workbook);
		this.classifier = classifier;
	}
}
