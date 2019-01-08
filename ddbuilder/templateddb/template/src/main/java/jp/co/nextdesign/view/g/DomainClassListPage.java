/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.view.g;

import jp.co.nextdesign.view.ddb.DdBasePage;

import org.apache.wicket.markup.html.link.Link;

/**
 * ドメインクラス一覧
 * @author murayama
 *
 */
public class DomainClassListPage extends DdBasePage {
	private static final long serialVersionUID = 1L;

	public DomainClassListPage(){
		super();
		buildPage();
	}

	private void buildPage(){
		//-- BLOCKSTART Entity
		//-- CASE All
		this.add(new Link<Void>("bookListPage"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(){
				BookInstanceListPage nextPage = new BookInstanceListPage();
				setResponsePage(nextPage);
			}
		});
		//-- BLOCKEND Entity
	}
}
