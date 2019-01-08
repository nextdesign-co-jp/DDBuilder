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


/**
 * メソッドのJavadocの各行を現す
 * @author murayama
 *
 */
public class BdUserMethodJavadocInfo {

	private String keyName = "";
	private String paramName = "";
	private String description = "";

	public BdUserMethodJavadocInfo(String keyName, String line){
		super();
		this.keyName = keyName;
		if (line != null){
			String line2 = line.replaceAll("　", "");
			String[] splitted = line2.split(" ");
			List<String> elementList = new ArrayList<String>();
			for(String element : splitted){
				if (!element.isEmpty()){
					elementList.add(element);
				}
			}
			if (elementList.size() > 1){
				this.paramName = elementList.get(1);
				if (elementList.size() > 2){
					for(int i=2; i<elementList.size(); i++){
						this.description +=elementList.get(i) + " ";
					}
					if (this.description.endsWith(" ")){
						this.description = this.description.substring(0, this.description.length() - 1);
					}
				}
			}
		}
	}

	public String getKeyName() {
		return keyName;
	}

	public String getParamName() {
		return paramName;
	}

	public String getDescription() {
		return description;
	}
}
