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
package jp.co.nextdesign.ddb.core.template;

import java.util.Comparator;

/**
 * テンプレートディレクトリを階層レベルで比較する比較子
 * @author murayama
 *
 */
public class BdDirectoryLevelComparator implements Comparator<BdTemplateDirectory> {

	@Override
	public int compare(BdTemplateDirectory o1, BdTemplateDirectory o2) {
		return o1.getDirectoryLevel() - o2.getDirectoryLevel();
	}
}
