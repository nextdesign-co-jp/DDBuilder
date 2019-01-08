/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.view.ddb;

import jp.co.nextdesign.view.ddb.DdBaseEditPage;
import jp.co.nextdesign.view.ddb.DdBasePage;
import jp.co.nextdesign.view.ddb.DdOperationTypes;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * サービスメソッド結果表示画面（戻り値がVoidの場合）
 * @author murayama
 *
 */
public class DdVoidResultPage extends DdBaseEditPage {
	private static final long serialVersionUID = 1L;
	
	/**
	 * コンストラクタ
	 */
	public DdVoidResultPage(final PageParameters parameters) {
		super(parameters);
	}
	
	/**
	 * コンストラクタ
	 */
	public DdVoidResultPage(DdBasePage backPage, String methodName) {
		super(DdOperationTypes.None, backPage);
		this.buildPage(methodName);
	}

	/** ページを構築する */
	private void buildPage(String methodName){
		this.add(new Label("ddPageTitle", methodName + "サービス実行結果画面"));
		this.add(new Label("ddPageContext", this.getDdPageContext()));
		//テーブル部
		//なし
		//戻るボタン
		Link<Void> cancelButton = new Link<Void>("cancel"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(){
				this.setResponsePage(DdVoidResultPage.this.backPage);
			}
		};
		this.add(cancelButton);		
	}
	
}
