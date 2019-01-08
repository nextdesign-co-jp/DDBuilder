/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.documentation.xls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.nextdesign.jcr.documentation.NdDocumentationException;

import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * POI Sheetを隠ぺいするクラス
 * 1.行の高さについて
 *   テンプレートの行を選択し→書式→行の高さの自動調整　にすればよい。
 *   POIで調整するのは面倒。Excelのバージョンによってはうまく調整されないかもしれない。
 * @author murayama
 */
public class NdSheet {

	private Sheet poiSheet;
	private Map<String, Integer> rowMap = new HashMap<String, Integer>();
	private Map<String, Integer> columnMap = new HashMap<String, Integer>();
	public static final String LINE_KEY_SUFFIX = "Line";

	/**
	 * 警告スタイルシートのキャッシュ（同じ列の場合同じスタイルなので同じスタイルインスタンスを使用するため）
	 */
	private Map<Integer, CellStyle> cellStyleCache = new HashMap<Integer, CellStyle>();
	private CellStyle hyperlinkStyleCache;

	/**
	 * コンストラクタ
	 * @param poiSheet
	 * @throws NdDocumentationException
	 */
	public NdSheet(Sheet poiSheet) throws NdDocumentationException{
		this.poiSheet = poiSheet;
		this.buildLayoutMap();
		this.clearAllCommentOnSheet();
	}
	
	/**
	 * フッター左側にテキストを設定する
	 * @param text
	 */
	public void setLeftOfFooter(String text, short fontSize){
		if (this.poiSheet != null){
			Footer poiFooter = this.poiSheet.getFooter();
			if (poiFooter != null){
				poiFooter.setLeft(HeaderFooter.fontSize(fontSize) + text);
			}
		}
	}

	/**
	 * ハイパーリンクを設定する
	 * @param rowIndex
	 * @param columnIndex
	 * @param hyperLinkFileName
	 */
	public void setHyperlink(int rowIndex, int columnIndex, String hyperLinkFileName){		
		if ((this.poiSheet != null) && (hyperLinkFileName != null) && (!"".equals(hyperLinkFileName))) {
			Row row = this.poiSheet.getRow(rowIndex);
			if (row != null) {
				Cell cell = row.getCell(columnIndex);
				if (cell != null){
					CreationHelper helper = this.poiSheet.getWorkbook().getCreationHelper();
					if (helper != null){
						Hyperlink link = helper.createHyperlink(Hyperlink.LINK_FILE);
						link.setAddress(hyperLinkFileName);
						cell.setHyperlink(link);
						CellStyle linkStyle = this.getHyperlnkCellStyle(cell);
						if (linkStyle != null){
							cell.setCellStyle(linkStyle);
						}
					}
				}
			}
		}
	}

	/**
	 * セルに値を設定する
	 * @param rowIndex
	 * @param columnIndex
	 * @param value
	 */
	public void setValue(int rowIndex, int columnIndex, Integer value){
		setValueAndWarning(rowIndex, columnIndex, value, -1);
	}

	/**
	 * セルに値を設定する
	 * @param rowIndex
	 * @param columnIndex
	 * @param value
	 * @param warningLevel
	 */
	public void setValueAndWarning(int rowIndex, int columnIndex, Integer value, Integer warningLevel) {
		if ((this.poiSheet != null) && (value != null)) {
			Row row = this.poiSheet.getRow(rowIndex);
			if (row != null) {
				Cell cell = row.getCell(columnIndex);
				if (cell == null){
					row.createCell(columnIndex);
					cell = row.getCell(columnIndex);
				}
				cell.setCellValue((double)value);
				if (!(warningLevel < 0) && (value > warningLevel)) {
					cell.setCellStyle(getWarningCellStyle(cell));
				}
			}
		}
	}

	/**
	 * 警告表示用のスタイルを応答する
	 * @param cell
	 * @return
	 */
	private CellStyle getWarningCellStyle(Cell cell){
		CellStyle result = null;
		if (this.cellStyleCache.containsKey(cell.getColumnIndex())){
			result = cellStyleCache.get(cell.getColumnIndex());
		} else {
			CellStyle original = cell.getCellStyle();
			result = poiSheet.getWorkbook().createCellStyle();
			result.cloneStyleFrom(original);
			result.setFillPattern(CellStyle.SOLID_FOREGROUND);
			result.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
			cell.setCellStyle(result);
			cellStyleCache.put(cell.getColumnIndex(), result);
		}
		return result;
	}


	/**
	 * ハイパーリンク用のスタイルを応答する
	 * @param cell
	 * @return
	 */
	private CellStyle getHyperlnkCellStyle(Cell cell){
		CellStyle result = null;
		if(hyperlinkStyleCache != null){
			result = hyperlinkStyleCache;
		} else {
			CellStyle original = cell.getCellStyle();
			if (original != null){
				result = poiSheet.getWorkbook().createCellStyle();
				result.cloneStyleFrom(original);
				short fontIndex = original.getFontIndex();
				Font originalFont = this.poiSheet.getWorkbook().getFontAt(fontIndex);
				if (originalFont != null){
					Font font = this.poiSheet.getWorkbook().createFont();
					font.setFontName(originalFont.getFontName());
					font.setItalic(originalFont.getItalic());
					font.setUnderline(Font.U_SINGLE);
					font.setColor(IndexedColors.BLUE.getIndex());
					result.setFont(font);
					cell.setCellStyle(result);
					cellStyleCache.put(cell.getColumnIndex(), result);
					hyperlinkStyleCache = result;
				}
			}
		}
		return result;
	}

	/**
	 * セルに値を設定する
	 * @param rowIndex
	 * @param columnIndex
	 * @param value
	 */
	public void setValue(int rowIndex, int columnIndex, String value){
		if ((this.poiSheet != null) && (value != null)) {
			Row row = this.poiSheet.getRow(rowIndex);
			if (row != null) {
				Cell cell = row.getCell(columnIndex);
				if (cell == null){
					row.createCell(columnIndex);
					cell = row.getCell(columnIndex);
				}
				cell.setCellValue(value);
			}
		}
	}

	/**
	 * キーから列インデックスを応答する
	 * @param key
	 * @return
	 * @throws NdDocumentationException 
	 */
	public Integer getColumnIndexByKey(String key) throws NdDocumentationException{
		Integer result = -1;
		if (this.columnMap.containsKey(key)){
			result = this.columnMap.get(key);
		}
		if ((result == null) || ( result < 0)){
			throw new NdDocumentationException("キーエラー key=" + key);
		}
		return result;
	}	

	/**
	 * キーから行インデックスを応答する
	 * @param key
	 * @return
	 * @throws NdDocumentationException 
	 */
	public Integer getRowIndexByKey(String key) throws NdDocumentationException{
		Integer result = null;
		if (this.rowMap.containsKey(key)){
			result = this.rowMap.get(key);
		}
		if ((result == null) || ( result < 0)){
			throw new NdDocumentationException("キーエラー key=" + key);
		}
		return result;
	}

	/**
	 * rowMapとcolumnMapを作成する
	 * @throws NdDocumentationException
	 */
	private void buildLayoutMap() throws NdDocumentationException{
		if (this.poiSheet != null){
			for(int rowIndex=0; rowIndex<=this.poiSheet.getLastRowNum(); rowIndex++){
				Row poiRow = this.poiSheet.getRow(rowIndex);
				if (poiRow != null) {
					for(int columnIndex=0; columnIndex<poiRow.getLastCellNum(); columnIndex++){
						String[] comments = this.getCommentListInCell(poiRow.getCell(columnIndex));
						for(String comment : comments){
							this.putMap(comment, rowIndex, columnIndex);
						}
					}
				}
			}
		}
	}

	/**
	 * rowMapまたはcolumnMapに情報を追加する（どちらのmapかはキーの接尾語で決める）
	 * @param key
	 * @param rowIndex
	 * @param columnIndex
	 * @throws NdDocumentationException
	 */
	private void putMap(String key, int rowIndex, int columnIndex) throws NdDocumentationException{
		if (this.isLineKey(key)){
			if (!this.rowMap.containsKey(key)) {
				this.rowMap.put(key, rowIndex);
			} else {
				throw new NdDocumentationException(this.getClass().getSimpleName() + " 重複キー=" + key);
			}
		} else {
			if (!this.columnMap.containsKey(key)) {
				this.columnMap.put(key, columnIndex);
			} else {
				throw new NdDocumentationException(this.getClass().getSimpleName() + " 重複キー=" + key);
			}
		}
	}

	/**
	 * 行位置のためのキーか否かを応答する
	 * @param key
	 * @return
	 */
	private boolean isLineKey(String key){
		return (key != null) && (key.endsWith(LINE_KEY_SUFFIX));
	}

	/**
	 * セル内のコメントリストを応答する（複数コメントの場合、カンマ区切りされている）
	 * @param poiCell
	 * @return
	 */
	private String[] getCommentListInCell(Cell poiCell){
		String[] resultList = new String[0];
		if ((poiCell != null) && (poiCell.getCellComment() != null)) {
			String comment = poiCell.getCellComment().getString().toString();
			resultList = comment.split(",");
			for(int i=0; i<resultList.length; i++) {
				resultList[i] = resultList[i].replaceAll("\n", "").trim();
			}
		}
		return resultList;
	}

	/**
	 * 行を挿入する
	 * copyRowIndexの下にinsertRowCount分のコピーを挿入する
	 * @param sourceRowIndex
	 * @param addRowCount
	 */
	public void addRow(int sourceRowIndex, int addRowCount){
		if ((this.poiSheet != null) && (sourceRowIndex >= 0) && (addRowCount > 0))  {
			int lastRowIndex = poiSheet.getLastRowNum();
			if (sourceRowIndex <= lastRowIndex){
				int insertRowIndex = sourceRowIndex + 1;
				this.insertRows(insertRowIndex, addRowCount);
				this.updateRowMap(insertRowIndex, addRowCount);
				Row poiCopyRow = this.poiSheet.getRow(sourceRowIndex);
				for (int rowCount=1;  rowCount<=addRowCount; rowCount++){
					int newRowIndex = sourceRowIndex + rowCount;
					Row poiNewRow = poiSheet.getRow(newRowIndex);
					this.copyRow(poiCopyRow, poiNewRow);
				}
			}
		}
	}

	/**
	 * rowMapを更新する
	 * 行追加による行位置を補正する
	 * @param insertRowIndex
	 * @param addRowCount
	 */
	private void updateRowMap(int insertRowIndex, int addRowCount){
		List<String> updateList = new ArrayList<String>();
		Set<String> keyMap = this.rowMap.keySet();
		Iterator<String> keyIterator = keyMap.iterator();
		while (keyIterator.hasNext()){
			String key = keyIterator.next();
			int rowIndex = this.rowMap.get(key);
			if (rowIndex >= insertRowIndex){
				updateList.add(key);
			}
		}
		for(String key : updateList){
			int rowIndex = this.rowMap.get(key);
			rowIndex += addRowCount;
			this.rowMap.put(key, rowIndex);
		}
	}

	/**
	 * 行をコピーする
	 * @param poiCopyRow
	 * @param poiNewRow
	 */
	private void copyRow(Row poiCopyRow, Row poiNewRow){
		for (int columnIndex=0; columnIndex<poiCopyRow.getLastCellNum(); columnIndex++){
			Cell poiCopyCell = poiCopyRow.getCell(columnIndex);
			String copyCellValue = "";
			CellStyle copyCellStyle = null;
			if (poiCopyCell != null){
				try{
					copyCellValue = poiCopyCell.getStringCellValue();
				} catch(Exception ex) {
					copyCellValue = "";
				}
				copyCellStyle = poiCopyCell.getCellStyle();
			}
			Cell poiNewCell = poiNewRow.createCell(columnIndex);
			if (copyCellStyle != null){
				copyCellStyle.setWrapText(true);
				poiNewCell.setCellStyle(copyCellStyle);
			} else {
				System.out.println("style is null.");
			}
			poiNewCell.setCellValue(copyCellValue);
		}
		this.copyMergedRegion(poiCopyRow, poiNewRow);
	}

	/**
	 * セル結合情報をコピーする
	 * @param poiCopyRow
	 * @param poiNewRow
	 */
	private void copyMergedRegion(Row poiCopyRow, Row poiNewRow){
		int num = this.poiSheet.getNumMergedRegions();
		for (int i=0; i<num; i++){
			CellRangeAddress range = this.poiSheet.getMergedRegion(i);
			if ((range.getFirstRow() == poiCopyRow.getRowNum()) && (range.getFirstRow() == range.getLastRow())) {
				int row1 = poiNewRow.getRowNum();
				int row2 = row1;
				int col1 = range.getFirstColumn();
				int col2 = range.getLastColumn();
				CellRangeAddress newRange = new CellRangeAddress(row1, row2, col1, col2);
				this.poiSheet.addMergedRegion(newRange);
			}
		}
	}

	/**
	 * 行インデックスinsertRowIndexの位置にinsertRowCount分の行を挿入する
	 * @param insertRowIndex
	 * @param insertRowCount
	 */
	private void insertRows(int insertRowIndex, int insertRowCount){
		int lastRowIndex = this.poiSheet.getLastRowNum();
		if (insertRowIndex <= lastRowIndex){
			this.poiSheet.shiftRows(insertRowIndex, lastRowIndex, insertRowCount);
		}
		for(int newRowCount=0; newRowCount<insertRowCount; newRowCount++){
			poiSheet.createRow(insertRowIndex + newRowCount);
		}
	}

	/**
	 * 最終行インデックスを応答する
	 * Sheet#getLastRowNum() は最終行のインデックスを応答する（0-based）
	 * @return
	 */
	public int getLastRowIndex(){
		int result = 0;
		if (this.poiSheet != null){
			result = this.poiSheet.getLastRowNum();
		}
		return result;
	}

	/**
	 * シート内の全セルのコメントを削除する
	 * コメントを含むセルをshiftすると出力したExcelが壊れるようだ
	 */
	public void clearAllCommentOnSheet(){
		if (this.poiSheet != null) {
			Iterator<Row> rowIterator = this.poiSheet.rowIterator();
			while (rowIterator.hasNext()){
				Row poiRow = rowIterator.next();
				Iterator<Cell> cellIterator = poiRow.cellIterator();
				while(cellIterator.hasNext()){
					cellIterator.next().removeCellComment();
				}
			}
		}
	}
}
