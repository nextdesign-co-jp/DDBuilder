/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.view.g;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.co.nextdesign.view.ddb.DdBaseEditPage;
import jp.co.nextdesign.view.ddb.DdBasePage;
import jp.co.nextdesign.view.ddb.DdOperationTypes;
import jp.co.nextdesign.view.ddb.DdServiceMethodInfo;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * サービスメソッド一覧
 * @author murayama
 *
 */
public class ServiceMethodListPage extends DdBasePage {
	private static final long serialVersionUID = 1L;
	
	/** コンストラクタ */
	public ServiceMethodListPage(){
		super();
		buildList();
		buildView();
	}
	
	/** サービスメソッドリスト ListView表示のために必要 */
	private List<DdServiceMethodInfo> serviceMethodInfoList = new ArrayList<DdServiceMethodInfo>();
	
	/** サービスメソッドリストを作成する */
	private void buildList(){
		//-- BLOCKSTART Method
		//-- CASE All
		DdServiceMethodInfo _info = new DdServiceMethodInfo(
				"serviceClassName", 
				"serviceMethodTitle", 
				"serviceMethodName", 
				"returnValueTypeName", 
				"parameterNames");
		DdBaseEditPage _nextPage = 
		new BookServiceInvokePageMethod1(_info.getServiceMethodName(), DdOperationTypes.None, ServiceMethodListPage.this);
		_info.setNextPage(_nextPage);
		this.serviceMethodInfoList.add(_info);
		//-- BLOCKEND Method
	}

	/** ページを構成する */
	private void buildView(){
		DataView<DdServiceMethodInfo> dataView = this.createDataView();
		this.add(dataView);
		PagingNavigator pagingNavigator = new PagingNavigator("pagingNavigator", dataView);
		this.add(pagingNavigator);
	}

	private static final int ITEMS_PER_PAGE = 5;
	
	/**
	 * 一覧を作成する
	 */
	private DataView<DdServiceMethodInfo> createDataView() {
		DataView<DdServiceMethodInfo> result = new DataView<DdServiceMethodInfo>(
				"listView",
				new MyDataProvider(),
				ITEMS_PER_PAGE) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(Item<DdServiceMethodInfo> item){
				final DdServiceMethodInfo info = item.getModelObject();
				//実行ページリンク
				item.add(new Link<Void>("serviceMethod"){
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick(){
						if (info != null){
							setResponsePage(info.getNextPage());
						}
					}
				});
				item.add(new Label("serviceClassName", new Model<String>(info.getServiceClassName())));
				item.add(new Label("methodTitle", new Model<String>(info.getServiceMethodTitle())));
				item.add(new Label("serviceMethodName", new Model<String>(info.getServiceMethodName())));
				item.add(new Label("returnValue", new Model<String>(info.getReturnValueTypeName())));
				item.add(new Label("parameters", new Model<String>(info.getParameterNames())));
			}
		};
		return result;
	}
	
	/**
	 * IDataProvider
	 */
	private class MyDataProvider implements IDataProvider<DdServiceMethodInfo>{
		private static final long serialVersionUID = 1L;
		@Override
		public Iterator<? extends DdServiceMethodInfo> iterator(long first, long count){
			List<DdServiceMethodInfo> list = 
					ServiceMethodListPage.this.serviceMethodInfoList.subList((int)first, (int)(first + count));
			return list.iterator();
		}
		@Override
		public long size(){
			return ServiceMethodListPage.this.serviceMethodInfoList.size();
		}
		@Override
		public IModel<DdServiceMethodInfo> model(DdServiceMethodInfo obj){
			return new Model<DdServiceMethodInfo>(obj);
		}
		@Override
		public void detach() {
		}
	}
}