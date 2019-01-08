/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.view.g;

import java.util.List;

//-- BLOCKSTART Import
//-- CASE SingleSelectPanel
import jp.co.nextdesign.domain.Author;
import jp.co.nextdesign.service.g.DdAuthorService;
//-- BLOCKEND Import

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.SimpleFormComponentLabel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * シングル選択画面 ManyToOne
 * @author murayama
 *
 */
public class AuthorSingleSelectPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	private Author selected;
		
	public AuthorSingleSelectPanel(String id, Author param){
		super(id);
		this.selected = param;
		this.buildPanel();
	}
	
	private void buildPanel(){
		//フォーム
		Form<Void> form = new Form<Void>("form");
		this.add(form);
		DdAuthorService authorService = new DdAuthorService();
		List<Author> choiceList = authorService.getList();
		//１つ選択式質問
		RadioGroup<Author> singleChoiceRadioGroup = new RadioGroup<Author>("singleChoiceRadioGroup", new PropertyModel<Author>(this, "selected"));
		form.add(singleChoiceRadioGroup);
		singleChoiceRadioGroup.add(new ListView<Author>("singleChoiceList", choiceList) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(ListItem<Author> itemChoice){
				Radio<Author> singleChoice = new Radio<Author>("singleChoice", itemChoice.getModel());
				//final Author choice = itemChoice.getModelObject();
				singleChoice.setLabel(new Model<String>(itemChoice.getModelObject().getDDBEntityTitle()));
				itemChoice.add(singleChoice);
				itemChoice.add(new SimpleFormComponentLabel("singleChoiceLabel", singleChoice));
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

	public Author getSelected() {
		return selected;
	}
}
