/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.view.ddb;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

public class DdModalWindow extends ModalWindow {
	private static final long serialVersionUID = 1L;

	public DdModalWindow(String wicketId, String title) {
		super(wicketId);
		this.setTitle(title);
		this.setCssClassName(ModalWindow.CSS_CLASS_GRAY);
		this.setAutoSize(true);
		this.setWidthUnit("px");
		this.setMinimalWidth(600);
		this.setHeightUnit("px");
		this.setMinimalHeight(500);
		this.setResizable(true);
	}
}
