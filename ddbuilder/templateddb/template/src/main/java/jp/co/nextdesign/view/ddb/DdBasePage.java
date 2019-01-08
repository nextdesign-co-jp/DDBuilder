/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.view.ddb;

import jp.co.nextdesign.view.g.DomainClassListPage;
import jp.co.nextdesign.view.g.ServiceMethodListPage;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * すべてのページの基底クラス
 * @author murayama
 *
 */
public class DdBasePage extends WebPage {
	private static final long serialVersionUID = 1L;
	protected DdBasePage backPage = null;
	
	/** コンストラクタ */
	public DdBasePage(){
		super();
		this.buildPage();
	}

	/** コンストラクタ */
	public DdBasePage(final PageParameters parameters){
		super(parameters);
		this.buildPage();
	}

	/** ページ遷移情報を応答する */
	protected String getDdPageContext(){
		String result = this.getClass().getSimpleName();
		//		if (result.endsWith("Page")){
		//			result = result.substring(0, result.length() - "Page".length());
		//		}
		if (this.backPage != null){
			result = this.backPage.getDdPageContext() + " ＞" + result;
		}
		return result;
	}

	/** すでに遷移してきた画面か否か */
	protected boolean isTransited(String pageClassName){
		if (pageClassName != null){
			if (pageClassName.equals(this.getClass().getSimpleName())){
				return true;
			} else {
				if (this.backPage != null){
					return this.backPage.isTransited(pageClassName);
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}

	/** 部品を配置する */
	private void buildPage(){
		add(new Link<Void>("homePage"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(){
				
				
				//編集結果が破棄されることを警告し、OKであればホームページに遷移する
				
				this.setResponsePage(DdHomePage.class);
			}
		});
		add(new Link<Void>("serviceListPage"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(){
				this.setResponsePage(ServiceMethodListPage.class);
			}
		});
		add(new Link<Void>("domainClassListPage"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(){
				this.setResponsePage(DomainClassListPage.class);
			}
		});
		add(new Link<Void>("signout"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(){
				this.getSession().invalidate();
				this.setResponsePage(DdSignInPage.class);
			}
		});
		
		add(new Label("pageName", this.getClass().getName()));
		
		//もしヘッダーやフッターを動的に構成する場合は以下を参考にする
		//add(new Label("headerLabel", "HEADER").add(new AttributeModifier("class", "header")));
		//add(new Label("footerLabel", "FOOTER").add(new AttributeModifier("class", "footer")));
	}
}
