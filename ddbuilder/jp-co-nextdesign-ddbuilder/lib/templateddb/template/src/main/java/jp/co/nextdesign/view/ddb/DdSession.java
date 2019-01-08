/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.view.ddb;

import jp.co.nextdesign.util.DdString;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

public class DdSession extends AuthenticatedWebSession {
	private static final long serialVersionUID = 1L;

	// サインイン中のユーザの権限
	private Roles roles;

	/** コンストラクタ */
	public DdSession(Request request) {
		super(request);
	}

	/**
	 * 認証する
	 * ユーザ名、パスワードの判定方法は仮実装の状態なので、必要に応じて実装する必要があります。
	 */
	@Override
	public boolean authenticate(final String userName, final String password) {
		boolean result = false;
		if (!DdString.isNullOrEmpty(userName) && userName.equals(password)) {
			result = true;
			if (userName.length() > 2) {
				this.roles = new Roles(new String[] { Roles.ADMIN, Roles.USER });
			} else {
				this.roles = new Roles(new String[] { Roles.USER });
			}
		} else {
			this.roles = new Roles();
		}
		return result;
	}

	/** 権限を応答する */
	@Override
	public Roles getRoles() {
		return this.roles;
	}
}
