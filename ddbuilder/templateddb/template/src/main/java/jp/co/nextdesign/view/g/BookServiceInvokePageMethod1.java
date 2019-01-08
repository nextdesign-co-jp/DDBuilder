/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.view.g;

import jp.co.nextdesign.service.g.*;
//-- BLOCKSTART Import
//-- CASE EditPage
import jp.co.nextdesign.domain.Author;
import jp.co.nextdesign.domain.Book;
import jp.co.nextdesign.domain.Edition;
import jp.co.nextdesign.domain.EnumLanguage;
import jp.co.nextdesign.domain.Isbn;
import jp.co.nextdesign.domain.store.Store;
import jp.co.nextdesign.service.BookService;
//-- CASE Ddb
import jp.co.nextdesign.view.ddb.DdBaseEditPage;
import jp.co.nextdesign.view.ddb.DdBasePage;
import jp.co.nextdesign.view.ddb.DdConfirmationLink;
import jp.co.nextdesign.view.ddb.DdConfirmationSubmitLink;
import jp.co.nextdesign.view.ddb.DdEnterDisabledTextField;
import jp.co.nextdesign.view.ddb.DdModalWindow;
import jp.co.nextdesign.view.ddb.DdOperationTypes;
import jp.co.nextdesign.view.ddb.DdVoidResultPage;

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
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * サービスメソッド実行画面
 * @author murayama
 *
 */
public class BookServiceInvokePageMethod1 extends DdBaseEditPage {
	private static final long serialVersionUID = 1L;
	
	//-- BLOCKSTART Declare
	//-- CASE String
	/** 書名 */
	private String paramName;

	//-- CASE Date
	/** 出版日 */
	private Date paramPublishedAt;

	//-- CASE BigDecimal
	/** 価格 */
	private BigDecimal paramPrice;

	//-- CASE Boolean
	/** キャンペーン1 isなし */
	private Boolean paramCampaign1;

	//-- CASE nop
	/** キャンペーン2 is付き */
	private Boolean paramIsCampaign2;

	//-- CASE Enum
	/** 言語 */
	private EnumLanguage paramAttEnum;

	//-- CASE Embedded
	/**
	 * ISBN one-to-one owning/parent これはHibernate ORM 5.2 User Guide2.7とは少し異なる
	 * サービスメソッド実行では使用しない、発生しないパターン
	 */
	private Isbn paramIsbn;

	//-- CASE ManyToOne
	/** 著者 */
	private Author paramAuthor;
	private AuthorSingleSelectPanel authorSingleSelectPanel;


	//-- CASE OneToMany
	/** 版 */
	private List<Edition> paramEditionList = new ArrayList<Edition>();

	//-- CASE ManyToMany
	/** 書店 サービスメソッド実行では使用しないパターン */
	private List<Store> paramStoreList;

	//-- CASE JavaPrimitiveClass
	/** Integer型属性名 */
	private Integer paramIntegerAttribute;

	//-- CASE nop
	/** Byte型属性名 */
	private Byte paramAttByte;
	/** Short型属性名 */
	private Short paramAttShort;
	/** Long型属性名 */
	private Long paramAttLong;
	/** Float型属性名 */
	private Float paramAttFloat;
	/** Double型属性名 */
	private Double paramAttDouble;
	/** Character型属性名 */
	private Character paramAttCharacter;
	//-- BLOCKEND Declare

	/**
	 * コンストラクタ
	 */
	public BookServiceInvokePageMethod1(String pageTitle, DdOperationTypes operationType, DdBasePage backPage) {
		super(operationType, backPage);
		this.buildPage(pageTitle);
	}

	/**
	 * コンストラクタ
	 */
	public BookServiceInvokePageMethod1(final PageParameters parameters) {
		super(parameters);
	}
	
	//buildPageとsubmitのonErrorで使用する
	private FeedbackPanel ddFeedbackPanel;

	/**
	 * ページを構成する
	 */
	private void buildPage(String pageTitle){
		//ページタイトル
		this.add(new Label("ddPageTitle", pageTitle + "実行（引数設定）画面"));
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
		DdEnterDisabledTextField<String> nameField = new DdEnterDisabledTextField<String>("name", new PropertyModel<String>(this, "paramName"));
		nameField.add(new OnChangeAjaxBehavior(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target){
				//Object value = getComponent().getDefaultModelObject();
				//book.setName((String)value);
				//何もしなくてもAjaxBehaviorを追加すると入力値がbookに（submitなしでも）反映される。AjaxBehaviorがないと入力値はsubmitされるまでbookに反映されない。
			}
		});
		form.add(nameField);

		//-- CASE Date
		//日付
		final DateTextField publishedAtField = new DateTextField("publishedAt", new PropertyModel<Date>(this, "paramPublishedAt"), "yyyy/MM/dd");
		publishedAtField.add(new OnChangeAjaxBehavior(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target){
			}
		});
		DatePicker datePicker = new DatePicker();
		publishedAtField.add(datePicker);
		form.add(publishedAtField);

		//-- CASE BigDecimal
		//価格
		TextField<BigDecimal> priceField = new TextField<BigDecimal>("price", new PropertyModel<BigDecimal>(this, "paramPrice"));
		priceField.add(new OnChangeAjaxBehavior(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target){
			}
		});
		form.add(priceField);

		//-- CASE Boolean
		//Boolean
		CheckBox campaign1CheckBox = new CheckBox("campaign1", new PropertyModel<Boolean>(this, "paramCampaign1"));
		campaign1CheckBox.add(new OnChangeAjaxBehavior(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target){
			}
		});
		form.add(campaign1CheckBox);

		//-- CASE nop
		//キャンペーン2
		CheckBox isCampaign2 = new CheckBox("isCampaign2", new PropertyModel<Boolean>(this, "paramIsCampaign2"));
		isCampaign2.add(new OnChangeAjaxBehavior(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target){
			}
		});
		form.add(isCampaign2);

		//-- CASE Enum
		//Enum
		IChoiceRenderer<EnumLanguage> attEnumChoiceRenderer = new ChoiceRenderer<EnumLanguage>();
		List<EnumLanguage> enumLanguageList = Arrays.asList(EnumLanguage.values());
		IModel<EnumLanguage> attEnumModel = new PropertyModel<EnumLanguage>(this, "paramAttEnum");
		DropDownChoice<EnumLanguage> attEnumDropDownChoice =
				(DropDownChoice<EnumLanguage>)(new DropDownChoice<EnumLanguage>("attEnum", attEnumModel, enumLanguageList, attEnumChoiceRenderer).setNullValid(true));
		attEnumDropDownChoice.add(new OnChangeAjaxBehavior(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target){
			}
		});
		form.add(attEnumDropDownChoice);
		
		//-- CASE ManyToOne
		//ManyToOne
		final DdModalWindow authorModalWindow = new DdModalWindow("authorModalWindow", "Author %選択%");
		this.add(authorModalWindow);
		form.add(new AjaxLink<Void>("author"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target){
				authorSingleSelectPanel = new AuthorSingleSelectPanel(authorModalWindow.getContentId(), paramAuthor);
				authorModalWindow.setContent(authorSingleSelectPanel);
				authorModalWindow.setWindowClosedCallback(
						new ModalWindow.WindowClosedCallback() {
							private static final long serialVersionUID = 1L;
							@Override
							public void onClose(AjaxRequestTarget arg0) {
								updatePage();
							}
						});
				authorModalWindow.show(target);
			}
		});
		form.add(new Label("authorTitle", new PropertyModel<String>(this, "paramAuthor.dDBEntityTitle")));

		//-- CASE OneToMany
		//OneToMany
		final DdModalWindow editionListModalWindow = new DdModalWindow("editionListModalWindow", "Edition %選択%");
		this.add(editionListModalWindow);
		form.add(new AjaxLink<Void>("editionList"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target){
				editionListModalWindow.setContent(new EditionMultipleSelectPanel(editionListModalWindow.getContentId(), paramEditionList));
				editionListModalWindow.setWindowClosedCallback(
						new ModalWindow.WindowClosedCallback() {
							private static final long serialVersionUID = 1L;
							@Override
							public void onClose(AjaxRequestTarget arg0) {
								updatePage();
							}
						});
				editionListModalWindow.show(target);
			}
		});
		this.updateEditionListTitle();
		form.add(new Label("editionListTitle", new PropertyModel<String>(this, "editionListTitle")));
		
		//-- CASE JavaPrimitiveClass
		//JavaPrimitiveClass
		DdEnterDisabledTextField<Integer> integerAttributeField = new DdEnterDisabledTextField<Integer>("integerAttribute", new PropertyModel<Integer>(this, "paramIntegerAttribute"));
		integerAttributeField.add(new OnChangeAjaxBehavior(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target){
			}
		});
		form.add(integerAttributeField);
		
		//-- CASE nop
		//Byte
		DdEnterDisabledTextField<Byte> attByte = new DdEnterDisabledTextField<Byte>("attByte", new PropertyModel<Byte>(this, "paramAttByte"));
		attByte.add(new OnChangeAjaxBehavior(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target){
			}
		});
		form.add(attByte);
		//Short
		DdEnterDisabledTextField<Short> attShort = new DdEnterDisabledTextField<Short>("attShort", new PropertyModel<Short>(this, "paramAttShort"));
		attShort.add(new OnChangeAjaxBehavior(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target){
			}
		});
		form.add(attShort);
		//Long
		DdEnterDisabledTextField<Long> attLong = new DdEnterDisabledTextField<Long>("attLong", new PropertyModel<Long>(this, "paramAttLong"));
		attLong.add(new OnChangeAjaxBehavior(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target){
			}
		});
		form.add(attLong);
		//Float
		DdEnterDisabledTextField<Float> attFloat = new DdEnterDisabledTextField<Float>("attFloat", new PropertyModel<Float>(this, "paramAttFloat"));
		attFloat.add(new OnChangeAjaxBehavior(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target){
			}
		});
		form.add(attFloat);
		//Double
		DdEnterDisabledTextField<Double> attDouble = new DdEnterDisabledTextField<Double>("attDouble", new PropertyModel<Double>(this, "paramAttDouble"));
		attDouble.add(new OnChangeAjaxBehavior(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target){
			}
		});
		form.add(attDouble);
		//Character
		DdEnterDisabledTextField<Character> attCharacter = new DdEnterDisabledTextField<Character>("attCharacter", new PropertyModel<Character>(this, "paramAttCharacter"));
		attCharacter.add(new OnChangeAjaxBehavior(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target){
			}
		});
		form.add(attCharacter);
		//-- BLOCKEND Field
		
		//ok
		String buttonLabel = this.isStartPageOfEditing() ? "サービス実行" : "無効";
		form.add(new AjaxButton("ok", new Model<String>(buttonLabel)){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form){
				
				//-- BLOCKSTART InvokeHead
				//-- CASE Void
				//BookService bookService = new BookService();
				//bookService.method1(
				//-- CASE NotVoid
				BookService bookService = new BookService();
				List<Book> resultValue = bookService.method1(
				//-- BLOCKEND InvokeHead
						
				//-- BLOCKSTART InvokeParameters
				//-- CASE All
				paramName,
				paramPublishedAt,
				paramCampaign1,
				paramIsCampaign2,
				paramAttEnum,
				paramAuthor,
				paramEditionList,
				paramIntegerAttribute,
				paramAttByte,
				paramAttShort,
				paramAttLong,
				paramAttFloat,
				paramAttDouble,
				paramAttCharacter
				//-- BLOCKEND InvokeParameters
				
				);
				//結果表示ページへ
				//-- BLOCKSTART ToResultPage
				//-- CASE Void
				//this.setResponsePage(new DdVoidResultPage(BookServiceInvokePageMethod1.this, "メソッド名"));
				//-- CASE NotVoid
				this.setResponsePage(new BookServiceResultPageMethod1(resultValue , BookServiceInvokePageMethod1.this, "メソッド名"));
				//-- BLOCKEND ToResultPage
				
				//-- BLOCKSTART InvokeTail
				//-- CASE All
			}
			//AjaxButtonのsubmitではエラーを表示させるために必要
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form){
				target.add(BookServiceInvokePageMethod1.this.ddFeedbackPanel);
			}
		});
		//cancel
		DdConfirmationSubmitLink cancelButton = new DdConfirmationSubmitLink("cancel", "変更内容は破棄されます。よろしいですか?"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form){
				this.setResponsePage(BookServiceInvokePageMethod1.this.backPage);
			}
		};
		form.add(cancelButton);		
		//-- BLOCKEND InvokeTail
	}
	
	//getStoreListTitle()メソッドはPageに定義する
	
	//-- BLOCKSTART Label
	//-- CASE EntityTitle
	private String editionListTitle;
	public void updateEditionListTitle(){
		editionListTitle = this.paramEditionList != null ? this.paramEditionList.size() + "件" : "0件";
		for(Edition edition : this.paramEditionList){
			editionListTitle += "|" + edition.getDDBEntityTitle();
		}
	}
	public String getEditionListTitle(){
		return this.editionListTitle;
	}
	//-- BLOCKEND Label

	/**
	 * ModalWindowでの編集結果をStoreListTitle,EditionListTitleのDDBEntityTitleに反映させる
	 * なお、Model<String>(storeListTitle)では不可、PropertyModel<String>(this,"storeListTitle")であること
	 */
	public void updatePage(){
		
		//-- BLOCKSTART UpdatePage
		//-- CASE Update
		this.updateEditionListTitle();
		
		//-- CASE UpdateSelected
		if (this.authorSingleSelectPanel != null){
			this.paramAuthor = this.authorSingleSelectPanel.getSelected();
		}
		//-- BLOCKEND UpdatePage
		
		this.setResponsePage(this);
	}
}