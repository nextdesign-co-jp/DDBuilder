/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.view.g;

import java.util.List;

//-- BLOCKSTART Import
//-- CASE MultipleSelectPanel
import jp.co.nextdesign.domain.Book;
import jp.co.nextdesign.domain.store.Store;
import jp.co.nextdesign.service.g.DdStoreService;
//-- BLOCKEND Import

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SimpleFormComponentLabel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * 複数選択画面 ManyToMany
 * @author murayama
 *
 */
public class BookStoreListMultipleSelectPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	public BookStoreListMultipleSelectPanel(String id, Book book){
		super(id);
		this.buildPanel(book);
	}
	
	private void buildPanel(final Book book){
		//フォーム
		Form<Void> form = new Form<Void>("form");
		this.add(form);
		DdStoreService storeService = new DdStoreService();
		List<Store> choiceList = storeService.getList();
		//複数選択式
		CheckGroup<Store> multipleChoiceCheckGroup = new CheckGroup<Store>("multipleChoiceCheckGroup", book.getStoreList());
		form.add(multipleChoiceCheckGroup);
		multipleChoiceCheckGroup.add(new ListView<Store>("multipleChoiceList", choiceList) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(ListItem<Store> itemChoice){
				Check<Store> multipleChoice = new Check<Store>("multipleChoice", itemChoice.getModel());
				multipleChoice.setLabel(new Model<String>(itemChoice.getModelObject().getDDBEntityTitle()));
				itemChoice.add(multipleChoice);
				itemChoice.add(new SimpleFormComponentLabel("multipleChoiceLabel", multipleChoice));
			}
		});
		//ok
		form.add(new AjaxButton("ok", form){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form){
				ModalWindow.closeCurrent(target);
			}
		});
		//cancel
		AjaxButton cancelButton = new AjaxButton("cancel", form){
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form){
				ModalWindow.closeCurrent(target);
			}
		};
		cancelButton.setDefaultFormProcessing(false);
		form.add(cancelButton);		
	}
}
