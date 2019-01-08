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
import jp.co.nextdesign.jcr.model.NdName;
import jp.co.nextdesign.jcr.model.core.NdAbstractClassifier;
import jp.co.nextdesign.jcr.model.reference.NdAbstractReference;

/**
 * クラスレポート　クロスリファレンスシート
 * @author murayama
 */
public class NdCrossReferenceSheet extends NdBaseClassifierReportSheet {

	private static final int SHEET_NO = 2;
	private static final String LINE_KEY_TITLE = "TitleLine";
	private static final String LINE_KEY_REFERENCE = "ReferenceLine";
	private static final String COL_KEY_TITLE = "Title";
	private static final String COL_KEY_NO = "No";
	private static final String COL_KEY_DIRECTION = "Direction";
	private static final String COL_KEY_CLASSIFIER = "Classifier";
	private static final String COL_KEY_POINT = "Point";
	private static final String COL_KEY_REFERENCE_TYPE_NAME = "ReferenceTypeName";
		
	/**
	 * 参照/被参照シートを作成する
	 * @throws NdDocumentationException
	 * @throws NdApplicationPropertyException
	 */
	public void buildSheet()throws NdDocumentationException, NdApplicationPropertyException{
		NdSheet sheet = workbook.getSheetAt(SHEET_NO);
		if (sheet != null){
			this.setTitleRow(sheet);
			List<NdCrossReferenceLine> crossReferenceList = new ArrayList<NdCrossReferenceLine>();
			List<NdAbstractReference> list = this.classifier.getReferenceList();
			for(int i=0; i<list.size(); i++){
				if (list.get(i).isAvailableReference()){
					crossReferenceList.add(new NdCrossReferenceLine(list.get(i), false));
				}
			}
			list = this.classifier.getInverseReferenceList();
			for(int i=0; i<list.size(); i++){
				if (list.get(i).isAvailableReference()){
					crossReferenceList.add(new NdCrossReferenceLine(list.get(i), true));
				}
			}
			int addRowCount = crossReferenceList.size() - 1;
			this.addEmptyRows(addRowCount, sheet, LINE_KEY_REFERENCE);
			this.setReferenceRow(crossReferenceList, sheet);
		}
	}
	
	/**
	 * タイトル行を設定する
	 * @param sheet
	 * @throws NdDocumentationException
	 */
	private void setTitleRow(NdSheet sheet) throws NdDocumentationException{
		int rowIndex = sheet.getRowIndexByKey(LINE_KEY_TITLE);
		int colTitleIndex = sheet.getColumnIndexByKey(COL_KEY_TITLE);
		sheet.setValue(rowIndex, colTitleIndex, this.classifier.getQualifiedName());
	}

	/**
	 * 参照行を作成する
	 * 
	 * @param list
	 * @param sheet
	 * @param isInverse
	 * @throws NdDocumentationException
	 */
	private void setReferenceRow(List<NdCrossReferenceLine> list, NdSheet sheet) throws NdDocumentationException{
		int rowIndex = sheet.getRowIndexByKey(LINE_KEY_REFERENCE);
		int colNoIndex = sheet.getColumnIndexByKey(COL_KEY_NO);
		int colDirectionIndex = sheet.getColumnIndexByKey(COL_KEY_DIRECTION);
		int colClassifierIndex = sheet.getColumnIndexByKey(COL_KEY_CLASSIFIER);
		int colPointIndex = sheet.getColumnIndexByKey(COL_KEY_POINT);
		int colReferenceTypeNameIndex = sheet.getColumnIndexByKey(COL_KEY_REFERENCE_TYPE_NAME);
		for(int i=0; i<list.size(); i++){
			NdCrossReferenceLine line = list.get(i);
			String linkFileName = "";
			if (line.isInverse){
				sheet.setValue(rowIndex, colDirectionIndex, "被参照");
				sheet.setValue(rowIndex, colClassifierIndex, NdMask.mask(line.reference.getFrom().getQualifiedName()));
				linkFileName = this.getClassifierReportFileName(line.reference.getFrom());
			} else {
				sheet.setValue(rowIndex, colDirectionIndex, "参照");
				sheet.setValue(rowIndex, colClassifierIndex, NdMask.mask(line.reference.getTo().getQualifiedName()));
				linkFileName = this.getClassifierReportFileName(line.reference.getTo());
			}
			sheet.setValue(rowIndex, colNoIndex, (i+1));
			
			NdName name = line.reference.getReferenceName();
			if (name.isQualifiedName()){
				sheet.setValue(rowIndex, colReferenceTypeNameIndex, name.getQualifiedName());
			} else {
				sheet.setValue(rowIndex, colReferenceTypeNameIndex, name.getSimpleName());
			}
			sheet.setValue(rowIndex, colPointIndex, line.reference.getReferencePointName());
			//ハイパーリンクを設定する
			if (!"".equals(linkFileName)){
				sheet.setHyperlink(rowIndex, colClassifierIndex, linkFileName);
			}
			rowIndex++;
		}
	}
	
	/**
	 * コンストラクタ
	 * @param workbook
	 * @param classifier
	 */
	protected NdCrossReferenceSheet(NdWorkbook workbook, NdAbstractClassifier classifier){
		super(workbook, classifier);
	}
	
	/**
	 * クロスリファレンス行の１行
	 * @author murayama
	 */
	private class NdCrossReferenceLine{
		NdAbstractReference reference;
		boolean isInverse;
		NdCrossReferenceLine(NdAbstractReference ref, boolean isInverse){
			this.reference = ref;
			this.isInverse = isInverse;
		}
	}
}
