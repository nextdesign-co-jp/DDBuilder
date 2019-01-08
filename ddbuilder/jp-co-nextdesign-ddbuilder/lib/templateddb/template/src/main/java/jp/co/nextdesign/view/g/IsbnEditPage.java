package jp.co.nextdesign.view.g;

import java.util.Date;

import jp.co.nextdesign.domain.Isbn;
import jp.co.nextdesign.service.g.DdIsbnService;
import jp.co.nextdesign.view.ddb.DdBaseEditPage;
import jp.co.nextdesign.view.ddb.DdConfirmationLink;
import jp.co.nextdesign.view.ddb.DdEnterDisabledTextField;
import jp.co.nextdesign.view.ddb.DdOperationTypes;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class IsbnEditPage extends DdBaseEditPage {
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 */
	public IsbnEditPage(Isbn isbn, DdOperationTypes operationType, DdBaseEditPage fromPage) {
		super(operationType, fromPage);
		this.buildPage(isbn);
	}

	/**
	 * コンストラクタ
	 */
	public IsbnEditPage(final PageParameters parameters) {
		super(parameters);
	}

	//buildPageとsubmitのonErrorで使用する
	private FeedbackPanel ddFeedbackPanel;

	private void buildPage(final Isbn isbn){
		//ページタイトル
		this.add(new Label("ddPageTitle", isbn.getClass().getSimpleName() + "編集画面"));
		this.add(new Label("ddPageContext", this.getDdPageContext()));
		//メッセージ
		ddFeedbackPanel = new FeedbackPanel("feedback");
		ddFeedbackPanel.setOutputMarkupId(true);
		this.add(ddFeedbackPanel);
		//フォーム
		Form<Void> form = new Form<Void>("form");
		this.add(form);
		
		//グループ記号
		DdEnterDisabledTextField<String> groupCodeField = new DdEnterDisabledTextField<String>("groupCode", new PropertyModel<String>(isbn, "groupCode"));
		groupCodeField.add(new OnChangeAjaxBehavior(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target){
			}
		});
		form.add(groupCodeField);
				
		/** 出版社記号 */
		DdEnterDisabledTextField<Integer> publisherCodeField = new DdEnterDisabledTextField<Integer>("publisherCode", new PropertyModel<Integer>(isbn, "publisherCode"));
		publisherCodeField.add(new OnChangeAjaxBehavior(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target){
			}
		});
		form.add(publisherCodeField);
		
		/** 書名記号 */
		DdEnterDisabledTextField<String> itemCodeField = new DdEnterDisabledTextField<String>("itemCode", new PropertyModel<String>(isbn, "itemCode"));
		itemCodeField.add(new OnChangeAjaxBehavior(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target){
			}
		});
		form.add(itemCodeField);
		
		//決定日
		final DateTextField determinatedAtField = new DateTextField("determinatedAt", new PropertyModel<Date>(isbn, "determinatedAt"), "yyyy/MM/dd");
		determinatedAtField.add(new OnChangeAjaxBehavior(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target){
			}
		});
		DatePicker datePicker = new DatePicker();
		determinatedAtField.add(datePicker);
		form.add(determinatedAtField);
		
		//Boolean
		CheckBox isOldIsbnField = new CheckBox("isOldIsbn", new PropertyModel<Boolean>(isbn, "isOldIsbn"));
		isOldIsbnField.add(new OnChangeAjaxBehavior(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onUpdate(AjaxRequestTarget target){
			}
		});
		isOldIsbnField.setLabel(new ResourceModel("isOldIsbn"));
		form.add(isOldIsbnField);

		//ok
		String buttonLabel = this.isStartPageOfEditing() ? "保存" : "仮決定";
		form.add(new AjaxButton("ok", new Model<String>(buttonLabel)){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form){
				if (IsbnEditPage.this.isStartPageOfEditing()){
					DdIsbnService isbnService = new DdIsbnService();
					if(operationType == DdOperationTypes.New){
						isbnService.persist(isbn);
					}else{
						isbnService.merge(isbn);
					}
				}
				this.setResponsePage(IsbnEditPage.this.backPage);
			}
			//AjaxButtonのsubmitではエラーを表示させるために必要
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form){
				target.add(IsbnEditPage.this.ddFeedbackPanel);
			}
		});
		//cancel
		DdConfirmationLink<Void> cancelButton = new DdConfirmationLink<Void>("cancel", "変更内容は破棄されます。よろしいですか?"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target){
				if (IsbnEditPage.this.isStartPageOfEditing()){
					DdIsbnService isbnService = new DdIsbnService();
					isbnService.clear();
				}
				this.setResponsePage(IsbnEditPage.this.backPage);
			}
		};
		form.add(cancelButton);		
	}
}