/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.documentation.xls;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import jp.co.nextdesign.jcr.documentation.NdDocumentationException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class NdWorkbook {

	private Workbook poiWorkbook;
	private ArrayList<NdSheet> sheetList = new ArrayList<NdSheet>();

	/**
	 * コンストラクタ
	 * @param fileName
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws NdDocumentationException
	 */
	public NdWorkbook(String fileName) throws IOException, InvalidFormatException, NdDocumentationException {
		InputStream inputStream = new FileInputStream(fileName);
		poiWorkbook = WorkbookFactory.create(inputStream); //最初の実行時に時間がかかる 2011.9.12
		this.buildSheetList();
	}

	/**
	 * シートリストを作成する
	 * @throws NdDocumentationException
	 */
	private void buildSheetList() throws NdDocumentationException{
		if (this.poiWorkbook != null){
			for(int i=0; i<poiWorkbook.getNumberOfSheets(); i++){
				Sheet poiSheet = poiWorkbook.getSheetAt(i);
				if (poiSheet != null){
					this.sheetList.add(new NdSheet(poiSheet));
				}
			}
		}
	}

	/**
	 * 指定されたシートを応答する
	 * @param index
	 * @return
	 */
	public NdSheet getSheetAt(int index) {
		NdSheet result = null;
		if ((index >= 0) && (index < this.getSheetCount())){
			result = this.sheetList.get(index);
		}
		return result;
	}

	/**
	 * シート数を応答する
	 * @return
	 */
	public int getSheetCount(){
		return this.sheetList.size();
	}

	/**
	 * 出力する
	 * @param fileName
	 * @throws IOException
	 */
	public void write(String fileName) throws IOException{
		if (this.poiWorkbook != null) {
			OutputStream outputStream = new FileOutputStream(fileName);
			this.poiWorkbook.write(outputStream);
		}
	}
}
