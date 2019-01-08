/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.view.ddb;

import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;

public abstract class DdConfirmationLink<T> extends AjaxLink<T> {
	private static final long serialVersionUID = 1L;
	private final String msg;

	public DdConfirmationLink(String id, String msg){
		super(id);
		this.msg = msg;
	}
	
	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes){
		super.updateAjaxAttributes(attributes);
		AjaxCallListener ajaxCallListener = new AjaxCallListener();
		ajaxCallListener.onPrecondition("return confirm('" + msg + "');");
		attributes.getAjaxCallListeners().add(ajaxCallListener);
	}
}
