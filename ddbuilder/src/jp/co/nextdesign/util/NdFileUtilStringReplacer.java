package jp.co.nextdesign.util;

import java.util.ArrayList;
import java.util.List;

public class NdFileUtilStringReplacer {
	
	private List<String[]> replacementPatternList = new ArrayList<String[]>();

	/**
	 * すべての置換を行った結果を応答する
	 * @param line
	 * @return
	 */
	public String replace(String line){
		String result = line;
		for(String[] rep : this.replacementPatternList){
			result = result.replace(rep[0], rep[1]);
		}
		return result;
	}
	
	/**
	 * 置換パターンを追加する
	 * @param oldString
	 * @param newString
	 */
	public void addReplacementPattern(String oldString, String newString){
		String[] replacement = {oldString, newString };
		this.replacementPatternList.add(replacement);
	}
	
	/**
	 * 自身のクローン(shallow copy)を応答する
	 * @return
	 */
	public NdFileUtilStringReplacer cloneNdFileUtilStringReplacer(){
		NdFileUtilStringReplacer result = new NdFileUtilStringReplacer();
		for(String[] pattern : this.replacementPatternList){
			result.replacementPatternList.add(pattern);
		}
		return result;
	}
}
