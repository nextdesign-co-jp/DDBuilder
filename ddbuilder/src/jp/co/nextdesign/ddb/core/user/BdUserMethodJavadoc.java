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
