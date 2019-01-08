/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.view.g;

import jp.co.nextdesign.service.ddb.DdBaseServiceException;
import jp.co.nextdesign.service.g.*;
//-- BLOCKSTART Import
//-- CASE EditPage
import jp.co.nextdesign.domain.Book;
import jp.co.nextdesign.domain.Edition;
import jp.co.nextdesign.domain.EnumLanguage;
import jp.co.nextdesign.domain.Isbn;
import jp.co.nextdesign.domain.store.Store;
//-- CASE Ddb
import jp.co.nextdesign.view.ddb.DdBaseEditPage;
import jp.co.nextdesign.view.ddb.DdBasePage;
import jp.co.nextdesign.view.ddb.DdConfirmationSubmitLink;
import jp.co.nextdesign.view.ddb.DdEnterDisabledTextField;
import jp.co.nextdesign.view.ddb.DdMessagePage;
import jp.co.nextdesign.view.ddb.DdModalWindow;
import jp.co.nextdesign.view.ddb.DdOperationTypes;

//-- CASE Date
import java.util.Date;
//-- CASE BigDecimal
import java.math.BigDecimal;
//-- BLOCKEND Import

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * ドメインオブジェクト編集画面
 * @author murayama
 *
 */
public class BookEditPage extends DdBaseEditPage {
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 */
	public BookEditPage(Book book, DdOperationTypes operationType, DdBasePage backPage) {
		super(operationType, backPage);
		this.buildPage(book);
	}

	/**
	 * コンストラクタ
	 */
	public BookEditPage(final PageParameters parameters) {
		super(parameters);
	}

	//-- BLOCKSTART BeforeAfterList
	//-- CASE ManyToMany
	private List<Store> ddbBeforeStoreList = new ArrayList<Store>();
	private List<Store> ddbAddStoreList = new ArrayList<Store>();
	private List<Book> ddbAddOtherSideBookList = new ArrayList<Book>();
	private List<Store> ddbRemoveStoreList = new ArrayList<Store>();
	private List<Book> ddbRemoveOtherSideBookList = new ArrayList<Book>();
	//-- BLOCKEND BeforeAfterList

	//buildPageとsubmitのonErrorで使用する
	private FeedbackPanel ddFeedbackPanel;

	/**
	 * ページを構成する
	 * @param book
	 */
	private void buildPage(final Book book){
		//コンテキスト
		this.add(new Label("ddPageContext", this.getDdPageContext()));
		//メッセージ
		ddFeedbackPanel = new FeedbackPanel("feedback");
		ddFeedbackPanel.setOutputMarkupId(true);
		this.add(ddFeedbackPanel);
		//フォーム
		Form<Void> form = new Form<Void>("form");
		this.add(form);

		//-- BLOCKSTART Field
		//-- CASE String
		//文字列
		DdEnterDisabledTextField<String> nameField = new DdEnterDisabledTextField<String>("name", new PropertyModel<String>(book, "name"));
		if (this.isStartPageOfEditing()){
			nameField.add(new OnChangeAjaxBehavior(){
				private static final long serialVersionUID = 1L;
				@Override
				protected void onUpdate(AjaxRequestTarget target){
					//Object value = getComponent().getDefaultModelObject();
					//book.setName((String)value);
					//何もしなくてもAjaxBehaviorを追加すると入力値がbookに（submitなしでも）反映される。AjaxBehaviorがないと入力値はsubmitされるまでbookに反映されない。
				}
			});
		}
		form.add(nameField);

		//-- CASE Date
		//日付
		final DateTextField publishedAtField = new DateTextField("publishedAt", new PropertyModel<Date>(book, "publishedAt"), "yyyy/MM/dd");
		if (this.isStartPageOfEditing()){
			publishedAtField.add(new OnChangeAjaxBehavior(){
				private static final long serialVersionUID = 1L;
				@Override
				protected void onUpdate(AjaxRequestTarget target){
				}
			});
		}
		DatePicker datePicker = new DatePicker();
		publishedAtField.add(datePicker);
		form.add(publishedAtField);
		
		//-- CASE BigDecimal
		//BigDecimal
		TextField<BigDecimal> priceField = new TextField<BigDecimal>("price", new PropertyModel<BigDecimal>(book, "price"));
		if (this.isStartPageOfEditing()){
			priceField.add(new OnChangeAjaxBehavior(){
				private static final long serialVersionUID = 1L;
				@Override
				protected void onUpdate(AjaxRequestTarget target){
				}
			});
		}
		form.add(priceField);

		//-- CASE Boolean
		//Boolean
		CheckBox campaign1 = new CheckBox("campaign1", new PropertyModel<Boolean>(book, "campaign1"));
		if (this.isStartPageOfEditing()){
			campaign1.add(new OnChangeAjaxBehavior(){
				private static final long serialVersionUID = 1L;
				@Override
				protected void onUpdate(AjaxRequestTarget target){
				}
			});
		}
		form.add(campaign1);

		//-- CASE nop
		//キャンペーン2
		CheckBox isCampaign2 = new CheckBox("isCampaign2", new PropertyModel<Boolean>(book, "isCampaign2"));
		if (this.isStartPageOfEditing()){
			isCampaign2.add(new OnChangeAjaxBehavior(){
				private static final long serialVersionUID = 1L;
				@Override
				protected void onUpdate(AjaxRequestTarget target){
				}
			});
		}
		form.add(isCampaign2);

		//-- CASE Enum
		//Enum
		IChoiceRenderer<EnumLanguage> attEnumChoiceRenderer = new ChoiceRenderer<EnumLanguage>();
		List<EnumLanguage> enumLanguageList = Arrays.asList(EnumLanguage.values());
		IModel<EnumLanguage> attEnumModel = new PropertyModel<EnumLanguage>(book, "attEnum");
		DropDownChoice<EnumLanguage> attEnumDropDownChoice =
				(DropDownChoice<EnumLanguage>)(new DropDownChoice<EnumLanguage>("attEnum", attEnumModel, enumLanguageList, attEnumChoiceRenderer).setNullValid(true));
		if (this.isStartPageOfEditing()){
			attEnumDropDownChoice.add(new OnChangeAjaxBehavior(){
				private static final long serialVersionUID = 1L;
				@Override
				protected void onUpdate(AjaxRequestTarget target){
				}
			});
		}
		form.add(attEnumDropDownChoice);

		//-- CASE Embedded
		//Embedded
		Link<Void> isbnLink = new Link<Void>("isbn"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(){
				IsbnEditPage nextPage = null;
				Isbn isbn = book.getIsbn();
				if (isbn == null){
					isbn = new Isbn();
					book.setIsbn(isbn);
					nextPage = new IsbnEditPage(isbn, DdOperationTypes.New, BookEditPage.this);
				} else {
					nextPage = new IsbnEditPage(isbn, DdOperationTypes.Update, BookEditPage.this);
				}
				BookEditPage.this.setResponsePage(nextPage);
			}
		};
		isbnLink.setEnabled(!this.isTransited("IsbnEditPage"));
		form.add(isbnLink);
		form.add(new Label("isbnTitle", new PropertyModel<String>(book, "isbn.dDBEntityTitle")));

		//-- CASE ManyToOne
		//ManyToOne
		final DdModalWindow authorModalWindow = new DdModalWindow("authorModalWindow", "Author %選択%");
		this.add(authorModalWindow);
		AjaxLink<Void> authorLink = new AjaxLink<Void>("author"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target){
				authorModalWindow.setContent(new BookAuthorSingleSelectPanel(authorModalWindow.getContentId(), book));
				authorModalWindow.setWindowClosedCallback(
						new ModalWindow.WindowClosedCallback() {
							private static final long serialVersionUID = 1L;
							@Override
							public void onClose(AjaxRequestTarget arg0) {
								updatePage(book);
							}
						});
				authorModalWindow.show(target);
			}
		};
		form.add(authorLink);
		form.add(new Label("authorTitle", new PropertyModel<String>(book, "author.dDBEntityTitle")));

		//-- CASE OneToMany
		//OneToMany
		final DdModalWindow editionListModalWindow = new DdModalWindow("editionListModalWindow", "Edition %選択%");
		this.add(editionListModalWindow);
		AjaxLink<Void> editionListLink = new AjaxLink<Void>("editionList"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target){
				editionListModalWindow.setContent(new BookEditionListMultipleSelectPanel(editionListModalWindow.getContentId(), book));
				editionListModalWindow.setWindowClosedCallback(
						new ModalWindow.WindowClosedCallback() {
							private static final long serialVersionUID = 1L;
							@Override
							public void onClose(AjaxRequestTarget arg0) {
								//OneToManyの場合はこの方式で問題ないようだ。（ManyToManyはowinig側のそれぞれをmergeする必要がある）
								book.setEditionList(book.getEditionList());
								updatePage(book);
							}
						});
				editionListModalWindow.show(target);
			}
		};
		form.add(editionListLink);
		this.updateEditionListTitle(book);
		form.add(new Label("editionListTitle", new PropertyModel<String>(this, "editionListTitle")));

		//-- CASE ManyToMany
		//ManyToMany
		final DdModalWindow storeListModalWindow = new DdModalWindow("storeListModalWindow", "Store %選択%");
		this.add(storeListModalWindow);
		AjaxLink<Void> storeListListLink = new AjaxLink<Void>("storeList"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target){
				ddbBeforeStoreList.clear();
				ddbBeforeStoreList.addAll(book.getStoreList());
				storeListModalWindow.setContent(new BookStoreListMultipleSelectPanel(storeListModalWindow.getContentId(), book));
				storeListModalWindow.setWindowClosedCallback(
						new ModalWindow.WindowClosedCallback() {
							private static final long serialVersionUID = 1L;
							@Override
							public void onClose(AjaxRequestTarget arg0) {
								//ManyToManyではowning側でmergeしないとmappedBy側は永続化反映されない。
								//以下のコードはmappedBy側のEditPageで必要であり、owning側のEditPageでは必要ないかもしれない。
								ddbAddStoreList.clear();
								ddbAddOtherSideBookList.clear();
								for(Store newStore : book.getStoreList()){
									if (!ddbBeforeStoreList.contains(newStore)){
										ddbAddStoreList.add(newStore);
										ddbAddOtherSideBookList.add(book);
									}
								}
								ddbRemoveStoreList.clear();
								ddbRemoveOtherSideBookList.clear();
								for(Store oldStore : ddbBeforeStoreList){
									if (!book.getStoreList().contains(oldStore)){
										ddbRemoveStoreList.add(oldStore);
										ddbRemoveOtherSideBookList.add(book);
									}
								}
								for(int i=0; i<ddbAddStoreList.size(); i++){
									ddbAddStoreList.get(i).addBook(ddbAddOtherSideBookList.get(i));
								}
								for(int i=0; i<ddbRemoveStoreList.size(); i++){
									ddbRemoveStoreList.get(i).removeBook(ddbRemoveOtherSideBookList.get(i));
								}
								updatePage(book);
							}
						});
				storeListModalWindow.show(target);
			}
		};
		form.add(storeListListLink);
		this.updateStoreListTitle(book);
		form.add(new Label("storeListTitle", new PropertyModel<String>(this, "storeListTitle")));

		//-- CASE JavaPrimitiveClass
		//JavaPrimitiveClass
		DdEnterDisabledTextField<Integer> integerAttributeField = new DdEnterDisabledTextField<Integer>("integerAttribute", new PropertyModel<Integer>(book, "integerAttribute"));
		if (this.isStartPageOfEditing()){
			integerAttributeField.add(new OnChangeAjaxBehavior(){
				private static final long serialVersionUID = 1L;
				@Override
				protected void onUpdate(AjaxRequestTarget target){
				}
			});
		}
		form.add(integerAttributeField);

		//-- CASE nop
		//Byte
		DdEnterDisabledTextField<Byte> attByte = new DdEnterDisabledTextField<Byte>("attByte", new PropertyModel<Byte>(book, "attByte"));
		if (this.isStartPageOfEditing()){
			attByte.add(new OnChangeAjaxBehavior(){
				private static final long serialVersionUID = 1L;
				@Override
				protected void onUpdate(AjaxRequestTarget target){
				}
			});
		}
		form.add(attByte);
		//Short
		DdEnterDisabledTextField<Short> attShort = new DdEnterDisabledTextField<Short>("attShort", new PropertyModel<Short>(book, "attShort"));
		if (this.isStartPageOfEditing()){
			attShort.add(new OnChangeAjaxBehavior(){
				private static final long serialVersionUID = 1L;
				@Override
				protected void onUpdate(AjaxRequestTarget target){
				}
			});
		}
		form.add(attShort);
		//Long
		DdEnterDisabledTextField<Long> attLong = new DdEnterDisabledTextField<Long>("attLong", new PropertyModel<Long>(book, "attLong"));
		if (this.isStartPageOfEditing()){
			attLong.add(new OnChangeAjaxBehavior(){
				private static final long serialVersionUID = 1L;
				@Override
				protected void onUpdate(AjaxRequestTarget target){
				}
			});
		}
		form.add(attLong);
		//Float
		DdEnterDisabledTextField<Float> attFloat = new DdEnterDisabledTextField<Float>("attFloat", new PropertyModel<Float>(book, "attFloat"));
		if (this.isStartPageOfEditing()){
			attFloat.add(new OnChangeAjaxBehavior(){
				private static final long serialVersionUID = 1L;
				@Override
				protected void onUpdate(AjaxRequestTarget target){
				}
			});
		}
		form.add(attFloat);
		//Double
		DdEnterDisabledTextField<Double> attDouble = new DdEnterDisabledTextField<Double>("attDouble", new PropertyModel<Double>(book, "attDouble"));
		if (this.isStartPageOfEditing()){
			attDouble.add(new OnChangeAjaxBehavior(){
				private static final long serialVersionUID = 1L;
				@Override
				protected void onUpdate(AjaxRequestTarget target){
				}
			});
		}
		form.add(attDouble);
		//Character
		DdEnterDisabledTextField<Character> attCharacter = new DdEnterDisabledTextField<Character>("attCharacter", new PropertyModel<Character>(book, "attCharacter"));
		if (this.isStartPageOfEditing()){
			attCharacter.add(new OnChangeAjaxBehavior(){
				private static final long serialVersionUID = 1L;
				@Override
				protected void onUpdate(AjaxRequestTarget target){
				}
			});
		}
		form.add(attCharacter);
		//-- BLOCKEND Field

		//ok
		String buttonLabel = this.isStartPageOfEditing() ? "%保存%" : "%仮決定%";
		form.add(new AjaxButton("ok", new Model<String>(buttonLabel)){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form){
				DdBaseServiceException ddBaseServiceException = null;
				try{
					if (BookEditPage.this.isStartPageOfEditing()){
						DdBookService bookService = new DdBookService();
						if(operationType == DdOperationTypes.New){
							bookService.merge(book); //2015.7.11 persistをmergeに変更
						}else{
							bookService.merge(book);
						}
						//-- BLOCKSTART EditPageOk
						//-- CASE ManyToMany
						//ManyToManyではowning側でmergeしないとmappedBy側は永続化反映されない。
						//以下のコードはmappedBy側のEditPageで必要であり、owning側のEditPageでは必要ないかもしれない。
						DdStoreService storeService = new DdStoreService();
						for(Store store : book.getStoreList()){
							storeService.merge(store);
						}
						for(Store store : ddbRemoveStoreList){
							storeService.merge(store);
						}
						//-- BLOCKEND EditPageOk
					}
				} catch(DdBaseServiceException ex) {
					ddBaseServiceException = ex;
				}
				if (ddBaseServiceException == null){
					this.setResponsePage(BookEditPage.this.backPage);
				} else {
					this.setResponsePage(new DdMessagePage(BookEditPage.this.backPage, ddBaseServiceException));
				}
			}
			//AjaxButtonのsubmitではエラーを表示させるために必要
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form){
				target.add(BookEditPage.this.ddFeedbackPanel);
			}
		});
		//cancel
		DdConfirmationSubmitLink cancelButton = new DdConfirmationSubmitLink("cancel", "変更内容は破棄されます。よろしいですか?"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form){
				if (BookEditPage.this.isStartPageOfEditing()){
					DdBookService bookService = new DdBookService();
					bookService.clear();
				}
				this.setResponsePage(BookEditPage.this.backPage);
			}
		};
		cancelButton.setDefaultFormProcessing(false);
		form.add(cancelButton);		
	}

	//getStoreListTitle()メソッドはPageに定義する

	//-- BLOCKSTART Label
	//-- CASE EntityTitle
	private String editionListTitle;
	public void updateEditionListTitle(Book book){
		if (book.getEditionList() != null){
			editionListTitle = book.getEditionList().size() + "件";
			for(Edition edition : book.getEditionList()){
				editionListTitle += "|" + edition.getDDBEntityTitle();
			}
		} else {
			editionListTitle = "0件";
		}
	}
	public String getEditionListTitle(){
		return this.editionListTitle;
	}

	//-- CASE nop
	private String storeListTitle;
	public void updateStoreListTitle(Book book){
		storeListTitle = book.getStoreList().size() + "件";
		for(Store store : book.getStoreList()){
			storeListTitle += "|" + store.getDDBEntityTitle();
		}
	}
	public String getStoreListTitle(){
		return this.storeListTitle;
	}
	//-- BLOCKEND Label

	/**
	 * ModalWindowでの編集結果をStoreListTitle,EditionListTitleのDDBEntityTitleに反映させる
	 * なお、Model<String>(storeListTitle)では不可、PropertyModel<String>(this,"storeListTitle")であること
	 * @param book
	 */
	public void updatePage(Book book){

		//-- BLOCKSTART UpdatePage
		//-- CASE Update
		this.updateEditionListTitle(book);

		//-- CASE nop
		this.updateStoreListTitle(book);
		//-- BLOCKEND UpdatePage

		this.setResponsePage(this);
	}
}
