/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.view.g;

//-- BLOCKSTART Import
//-- CASE ListPage
import jp.co.nextdesign.domain.Book;
import jp.co.nextdesign.service.g.DdBookService;
//-- CASE Ddb
import jp.co.nextdesign.view.ddb.DdBasePage;
import jp.co.nextdesign.view.ddb.DdConfirmationLink;
import jp.co.nextdesign.view.ddb.DdMessagePage;
import jp.co.nextdesign.view.ddb.DdOperationTypes;
import jp.co.nextdesign.service.ddb.DdBaseServiceException;

//-- CASE Date
import java.util.Date;
//-- CASE BigDecimal
import java.math.BigDecimal;
//-- BLOCKEND Import
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class BookInstanceListPage extends DdBasePage {
	private static final long serialVersionUID = 1L;

	//1ページの表示件数
	private static final int ITEMS_PER_PAGE = 10;
	//サービス
	private transient DdBookService bookService;

	/** 
	 * コンストラクタ
	 */
	public BookInstanceListPage() {
		super();
		this.solveService();
		this.buildView();
	}
	
	private Model<String> totalCountModel;

	/**
	 * ビューを作成する 
	 */
	private void buildView(){
		//メッセージ
		this.add(new FeedbackPanel("feedback"));
		//新規作成
		add(new Link<Void>("create"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(){
				Book book = new Book();
				BookEditPage nextPage = new BookEditPage(book, DdOperationTypes.New, BookInstanceListPage.this);
				setResponsePage(nextPage);
			}
		});
		//件数
		totalCountModel = new Model<String>(bookService.getSize().toString());
		Label totalCountLabel = new Label("totalCount", totalCountModel);
		totalCountLabel.setOutputMarkupId(true);
		add(totalCountLabel);
		//テーブル部
		this.setTableHeader();
		DataView<Book> dataView = this.createDataView();
		this.add(dataView);
		PagingNavigator pagingNavigator = new PagingNavigator("pagingNavigator", dataView);
		this.add(pagingNavigator);
	}
	
	/**
	 * テーブル見出しを作成する
	 */
	private void setTableHeader(){
		this.add(new Label("dDBEntityTitleTh", "%エンティティ見出し情報(see getDDBEntityTitle())%"));
	}

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
				final Book book = item.getModelObject();
				//操作（編集）
				item.add(new Link<Void>("edit"){
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick(){
						if (book != null){
							DdBookService bookService = new DdBookService();
							Book bookMerged = bookService.loadFull(book);
							BookEditPage nextPage = new BookEditPage(bookMerged, DdOperationTypes.Update, BookInstanceListPage.this);
							setResponsePage(nextPage);
						}
					}
				});
				//操作（削除）
				DdConfirmationLink<Void> cancelButton = new DdConfirmationLink<Void>("delete", "削除します。よろしいですか?"){
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick(AjaxRequestTarget target){
						DdBaseServiceException ddBaseServiceException = null;
						boolean deleted = false;
						try{
							deleted = deleteBook(book);
						} catch(DdBaseServiceException ex){
							ddBaseServiceException = ex;
						}
						if (ddBaseServiceException == null){
							if (deleted){
								BookInstanceListPage.this.info("削除しました。");
								DdBookService bookService = new DdBookService();
								totalCountModel.setObject(bookService.getSize().toString());
								setResponsePage(BookInstanceListPage.this.getPage());
							} else {
								BookInstanceListPage.this.error("削除できませんでした。");
								setResponsePage(BookInstanceListPage.this.getPage());
							}
						} else {
							DdMessagePage messagePage = new DdMessagePage((BookInstanceListPage)BookInstanceListPage.this.getPage(), ddBaseServiceException);
							setResponsePage(messagePage);
						}
					}
				};
				item.add(cancelButton);
				item.add(new Label("dDBEntityTitle", new Model<String>(book.getDDBEntityTitle())));
				
				//コメントアウト 2016.11.26 列数削減のため。また、ID、生成日はデバッグ情報に近い位置づけだったし、列幅も大きく取られるので一旦非表示にする。
				//item.add(new Label("id", new Model<String>(book.getId().toString())));
				//item.add(new Label("createdAt", new Model<String>(book.getCreatedAt().toString())));
				
			}
		};
		return result;
	}
	
	/** 削除する */
	private boolean deleteBook(Book book) throws DdBaseServiceException {
		boolean result = false;
		if (book != null){
			DdBookService bookService = new DdBookService();
			result = bookService.remove(book);
		}
		return result;
	}

	/**
	 * サービスを解決する
	 */
	protected void solveService(){
		if (this.bookService == null){
			this.bookService = new DdBookService();
		}
	}
	
	/**
	 * IDataProvider
	 */
	private class MyDataProvider implements IDataProvider<Book>{
		private static final long serialVersionUID = 1L;
		
		@Override
		public Iterator<? extends Book> iterator(long first, long count){
			solveService();
			totalCountModel.setObject(bookService.getSize().toString());
			List<Book> list = bookService.getList(first, count);
			return list.iterator();
		}
		
		@Override
		public long size(){
			solveService();
			return bookService.getSize();
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
