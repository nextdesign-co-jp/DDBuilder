/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.view.g;

//-- BLOCKSTART Import
//-- CASE EditPage
import jp.co.nextdesign.domain.Book;
import jp.co.nextdesign.domain.EnumLanguage;
//-- CASE Ddb
import jp.co.nextdesign.view.ddb.DdBaseEditPage;
import jp.co.nextdesign.view.ddb.DdBasePage;
import jp.co.nextdesign.view.ddb.DdOperationTypes;
//-- CASE Date
import java.util.Date;
import java.util.Calendar;
//--CASE BigDecimal
import java.math.BigDecimal;
//-- BLOCKEND Import

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * サービスメソッド結果表示画面
 * @author murayama
 *
 */
public class BookServiceResultPageMethod1 extends DdBaseEditPage {
	private static final long serialVersionUID = 1L;
	
	//-- BLOCKSTART DeclareList
	//-- CASE Date
	private static final SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss S");
	//-- CASE All
	//List<T>の場合だけではなく、Tの場合もコレクションにしてから扱う
	private List<Book> bookList;
	//-- CASE nop
	//テスト用 ここから
	//private List<String> stringList = new ArrayList<String>(Arrays.asList("テストString"));
	//private List<Date> dateList = new ArrayList<Date>(Arrays.asList(Calendar.getInstance().getTime()));
	//private List<Boolean> booleanList = new ArrayList<Boolean>(Arrays.asList(new Boolean(false)));
	//private List<EnumLanguage> enumList = new ArrayList<EnumLanguage>(Arrays.asList(EnumLanguage.EN));
	//private List<Integer> integerList = new ArrayList<Integer>(Arrays.asList(9));
	//テスト用 ここまで
	//-- BLOCKEND DeclareList

	/**
	 * コンストラクタ
	 */
	public BookServiceResultPageMethod1(final PageParameters parameters) {
		super(parameters);
	}
	
	/**
	 * コンストラクタ
	 */
	public BookServiceResultPageMethod1(List<Book> resultValue, DdBasePage backPage, String methodName) {
		super(DdOperationTypes.None, backPage);
		this.bookList = resultValue;
		this.buildPage(methodName);
	}

	/**
	 * コンストラクタ
	 */
	public BookServiceResultPageMethod1(Book resultValue, DdBasePage backPage, String methodName) {
		super(DdOperationTypes.Update, backPage);
		this.bookList = new ArrayList<Book>(Arrays.asList(resultValue));
		this.buildPage(methodName);
	}

	/** ページを構築する */
	private void buildPage(String methodName){
		this.add(new Label("ddPageTitle", methodName + "サービス実行結果画面"));
		this.add(new Label("ddPageContext", this.getDdPageContext()));
		//テーブル部
		DataView<Book> dataView = this.createDataView();
		this.add(dataView);
		//戻るボタン
		Link<Void> cancelButton = new Link<Void>("cancel"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(){
				this.setResponsePage(BookServiceResultPageMethod1.this.backPage);
			}
		};
		this.add(cancelButton);
		
		//-- BLOCKSTART Navi
		//-- CASE OneToMany
		//ナビゲータ
		PagingNavigator pagingNavigator = new PagingNavigator("pagingNavigator", dataView);
		this.add(pagingNavigator);
		//-- BLOCKEND Navi
	}
	
	private int ITEMS_PER_PAGE = 5;
	
	/**
	 * 一覧を作成する
	 */
	private DataView<Book> createDataView() {
		DataView<Book> result = new DataView<Book>(
				"listView",
				new MyDataProvider(),
				ITEMS_PER_PAGE) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(Item<Book> item){
								
				//-- BLOCKSTART Field
				//-- CASE String
				//文字列
				//item.add(new Label("dDBEntityTitle", new Model<String>(stringList.get(0).toString())));
				
				//-- CASE Date
				//日付
				//item.add(new Label("dDBEntityTitle", new Model<String>(simpleFormat.format(dateList.get(0)))));

				//-- CASE BigDecimal
				//価格
				//item.add(new Label("dDBEntityTitle", new Model<String>(bigDecimalList.get(0).toString())));
				
				//-- CASE Boolean
				//Boolean
				//item.add(new Label("dDBEntityTitle", new Model<String>(booleanList.get(0).toString())));
				
				//-- CASE Enum
				//Enum
				//item.add(new Label("dDBEntityTitle", new Model<String>(enumList.get(0).toString())));
				
				//-- CASE ManyToOne
				//final Book bookObj = item.getModelObject();
				//ManyToOne
				//item.add(new Label("dDBEntityTitle", new Model<String>(bookObj.getDDBEntityTitle())));
				
				//-- CASE OneToMany
				final Book bookObj = item.getModelObject();
				//OneToMany
				item.add(new Label("dDBEntityTitle", new Model<String>(bookObj.getDDBEntityTitle())));
				
				//-- CASE JavaPrimitiveClass
				//JavaPrimitiveClass
				//item.add(new Label("dDBEntityTitle", new Model<String>(integerList.get(0).toString())));
				
				//-- CASE nop
				//Byte
				//item.add(new Label("id", new Model<String>(book.getId().toString())));
				//Short
				//item.add(new Label("id", new Model<String>(book.getId().toString())));
				//Long
				//item.add(new Label("id", new Model<String>(book.getId().toString())));
				//Float
				//item.add(new Label("id", new Model<String>(book.getId().toString())));
				//Double
				//item.add(new Label("id", new Model<String>(book.getId().toString())));
				//Character
				//item.add(new Label("id", new Model<String>(book.getId().toString())));
				//-- BLOCKEND Field
			}
		};
		return result;
	}
	
	/**
	 * IDataProvider
	 */
	private class MyDataProvider implements IDataProvider<Book>{
		private static final long serialVersionUID = 1L;
		@Override
		public Iterator<? extends Book> iterator(long first, long count){
			int fromIndex = (int)(first >= 0 ? first : 0);
			int toIndex = (int)(count >= 1 ? first + count : first);
			toIndex = toIndex > bookList.size() ? bookList.size() : toIndex;
			return bookList.subList(fromIndex, toIndex).iterator();
		}
		@Override
		public long size(){
			return bookList.size();
		}
		@Override
		public IModel<Book> model(Book obj){
			return new Model<Book>(obj);
		}
		@Override
		public void detach() {
		}
	}
}
