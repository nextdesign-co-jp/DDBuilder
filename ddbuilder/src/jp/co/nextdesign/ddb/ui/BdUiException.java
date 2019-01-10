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
package jp.co.nextdesign.ddb.ui;

import jp.co.nextdesign.ddb.BdBaseException;

/**
 * ユーザインタフェース内での例外
 * @author murayama
 */
public class BdUiException extends BdBaseException {
	
	private static final long serialVersionUID = 1L;

	/** コンストラクタ */
	public BdUiException(String applMessage, Exception origin){
		super(applMessage, origin);
	}
	
	/** コンストラクタ */
	public BdUiException(String applMessage){
		super(applMessage);
	}
}
