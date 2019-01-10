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

import jp.co.nextdesign.jcr.model.core.NdJavadoc;

public abstract class BdUserAbstractElement {

	protected BdUserAbstractElement(){
		super();
	}

	protected String formatJavadoc(NdJavadoc javadoc){
		String result = "---";
		if (javadoc != null){
			result = javadoc.getFirstLine().replace("*", "").trim();
			String[] splitted = result.split(" ");
			if (splitted != null && splitted.length > 0){
				result = splitted[0];
			}
		}
		return result;
	}
}
