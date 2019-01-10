/*
 * DDBuilder
 * http://www.nextdesign.co.jp/ddd/index.html
 * Copyright 2015 NEXT DESIGN Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
