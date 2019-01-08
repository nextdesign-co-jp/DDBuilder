/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.view.ddb;

import org.apache.wicket.authroles.authentication.panel.SignInPanel;
import org.apache.wicket.markup.html.WebPage;

public class DdSignInPage extends WebPage {
	private static final long serialVersionUID = 1L;

	/** コンストラクタ */
	public DdSignInPage() {
		super();
		SignInPanel signInPanel = new SignInPanel("signInPanel", false);
//		DdSession session = (DdSession)DdSession.get();
//		if (session.isNewUserRegistering()){
//			signInPanel.setUsername(session.getNewUserEmail());
//		}
		this.add(signInPanel);
   }
}
