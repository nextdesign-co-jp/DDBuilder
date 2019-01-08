/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.view.g;

import jp.co.nextdesign.service.g.TestDataService;
import jp.co.nextdesign.view.ddb.DdBasePage;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

//ログインユーザ権限
@AuthorizeInstantiation("USER")
public class TestDataLoadPage extends DdBasePage {
	private static final long serialVersionUID = 1L;
	
	private static final Integer COUNT = 1;

	/** コンストラクタ */
	public TestDataLoadPage(final PageParameters parameters) {
		super(parameters);
		addTemplateData();
		Label label = new Label("count", COUNT.toString());
		this.add(label);
	}
	
	private void addTemplateData(){
		TestDataService testService = new TestDataService();
		testService.addTestData(COUNT);
	}
}
