/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.user;

import java.util.ArrayList;
import java.util.List;

import jp.co.nextdesign.jcr.model.core.NdJavadoc;

/**
 * メソッドのJavadoc
 * @author murayama
 *
 */
public class BdUserMethodJavadoc {

	public static final String KEY_PARAM = "@param";
	public static final String KEY_RETURN = "@return";

	private NdJavadoc ndJavadoc;
	private List<BdUserMethodJavadocInfo> paramInfoList;
	private BdUserMethodJavadocInfo returnInfo;

	public BdUserMethodJavadoc(NdJavadoc ndJavadoc){
		super();
		this.ndJavadoc = ndJavadoc;
		this.buildInfo();
	}

	private void buildInfo(){
		this.paramInfoList = new ArrayList<BdUserMethodJavadocInfo>();
		if (this.ndJavadoc != null){
			String block = ndJavadoc.getBlockString();
			String[] lines = block.split("\n");
			for(String line : lines){
				String lineCleaned = line.replaceAll("\\*", "").replaceAll("/", "").replaceAll("　", " ").trim();
				String lowerCase = lineCleaned.toLowerCase();
				if (lowerCase.startsWith(KEY_PARAM)){
					paramInfoList.add(new BdUserMethodJavadocInfo(KEY_PARAM, lineCleaned));
				} else if (lowerCase.startsWith(KEY_RETURN)){
					this.returnInfo = new BdUserMethodJavadocInfo(KEY_RETURN, lineCleaned);
				}
			}
		}
	}

	public BdUserMethodJavadocInfo getParameterInfoByName(String name){
		BdUserMethodJavadocInfo result = null;
		if (name != null && !name.isEmpty()){
			for(BdUserMethodJavadocInfo info : this.paramInfoList){
				if (name.equals(info.getParamName())){
					result = info;
					break;
				}
			}
		}
		return result;
	}

	public BdUserMethodJavadocInfo getParameterInfoByIndex(int index){
		return index < this.paramInfoList.size() ? this.paramInfoList.get(index) : null;
	}
	
	public BdUserMethodJavadocInfo getReturnInfo(){
		return this.returnInfo;
	}
}
