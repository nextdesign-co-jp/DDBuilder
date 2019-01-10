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
package jp.co.nextdesign.ddb;

/**
 * DDBuilder　すべての例外の基底クラス
 * @author murayama
 *
 */
public abstract class BdBaseException extends Exception {
	private static final long serialVersionUID = 1L;
	private Exception origin;
	private String applMessage;
	
	/** コンストラクタ */
	public BdBaseException(String applMessage, Exception origin){
		super(origin);
		this.applMessage = applMessage;
		this.origin = origin;
	}
	
	/** コンストラクタ */
	public BdBaseException(String applMessage){
		super();
		this.applMessage = applMessage;
	}

	@Override
	public String toString(){
		String result = BdMessageResource.get("dialog.title.message") + applMessage; //"メッセージ：" + applMessage;
		if (origin != null){
			result += BdConstants.CR + origin.toString();
		}
		result += BdConstants.CR + super.toString();
		return result;
	}
	
	/** アプリケーションメッセージを応答する */
	public String getApplMessage(){
		return this.applMessage;
	}
}
