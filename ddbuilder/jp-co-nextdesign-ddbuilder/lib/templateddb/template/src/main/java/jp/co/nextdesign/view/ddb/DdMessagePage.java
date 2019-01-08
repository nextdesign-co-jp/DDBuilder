/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.view.ddb;

import jp.co.nextdesign.service.ddb.DdBaseServiceException;
import jp.co.nextdesign.service.ddb.DdConstraintViolationException;
import jp.co.nextdesign.service.ddb.DdOptimisticLockException;
import jp.co.nextdesign.view.ddb.DdBaseEditPage;
import jp.co.nextdesign.view.ddb.DdBasePage;
import jp.co.nextdesign.view.ddb.DdOperationTypes;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * サービス例外メッセージ画面
 * @author murayama
 *
 */
public class DdMessagePage extends DdBaseEditPage {
	private static final long serialVersionUID = 1L;
	
	/**
	 * コンストラクタ
	 */
	public DdMessagePage(final PageParameters parameters) {
		super(parameters);
	}
	
	/**
	 * コンストラクタ
	 */
	public DdMessagePage(DdBasePage backPage, DdBaseServiceException ddBaseServiceException) {
		super(DdOperationTypes.None, backPage);
		this.buildPage(ddBaseServiceException);
	}

	/** ページを構築する */
	private void buildPage(DdBaseServiceException ddBaseServiceException){
		this.add(new Label("ddPageTitle", "サービス例外メッセージ画面"));
		this.add(new Label("ddPageContext", this.getDdPageContext()));
		
		//メッセージ部
		String msg = ddBaseServiceException.getClass().getSimpleName();
		if (ddBaseServiceException instanceof DdOptimisticLockException){
			msg += "\n" + "同時操作による競合が発生しました。";
		} else if (ddBaseServiceException instanceof DdConstraintViolationException){
			msg += "\n" + "データの制約違反がありました。";
		} else {
			msg += "\n" + "例外が発生しました。";
		}
		msg += "\n"+ ddBaseServiceException.getApplMessage();
		this.add(new MultiLineLabel("message", msg));
		
		//戻るボタン
		Link<Void> cancelButton = new Link<Void>("cancel"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(){
				this.setResponsePage(DdMessagePage.this.backPage);
				
			}
		};
		this.add(cancelButton);		
	}
	
}
