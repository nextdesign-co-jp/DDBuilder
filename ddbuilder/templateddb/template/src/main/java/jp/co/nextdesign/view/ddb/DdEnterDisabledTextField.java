/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.view.ddb;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

/**
 * このテキストフィールドでEnterを入力しても、サブミットボタンが押下されたようにはならないコンポーネント。
 * このコンポーネントを使わない場合、テキスト入力後にEnterを押すと、デフォルトのサブミットボタンが押されたことになる。
 * @author murayama
 * @param <T>
 */
public class DdEnterDisabledTextField<T> extends TextField<T> {
		private static final long serialVersionUID = 1L;
		private static final String JS_SUPPRESS_ENTER = "if(event.keyCode==13 || window.event.keyCode==13){return false;}else{return true;}";

		public DdEnterDisabledTextField(String id, IModel<T> model){
			super(id, model);
		}

	// Enter抑止すると期待したタイミングでは呼び出されない
//		@Override
//		protected void onModelChanging(){
//			super.onModelChanging();
//			//updateButton.setEnabled(true);
//		}
		
		@Override
		protected void onComponentTag(ComponentTag tag){
			super.onComponentTag(tag);
			tag.put("onkeydown", JS_SUPPRESS_ENTER);
			tag.put("onkeypress", JS_SUPPRESS_ENTER);
		}
	}
