/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign;

import jp.co.nextdesign.persistence.DdEntityManagerException;
import jp.co.nextdesign.persistence.DdEntityManagerFactory;
import jp.co.nextdesign.view.ddb.DdHomePage;
import jp.co.nextdesign.view.ddb.DdSession;
import jp.co.nextdesign.view.ddb.DdSignInPage;
import jp.co.nextdesign.view.ddb.DdUnAuthorizedPage;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.authentication.strategy.DefaultAuthenticationStrategy;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.util.cookies.CookieDefaults;
import org.apache.wicket.util.cookies.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class. このクラス名はweb.xmlに登録されている。
 * test.StartでJettyを開始する場合もsetWar("src/main/webapp")でweb.xmlを参照する。
 * 
 * @see jp.co.nextdesign.Start#main(String[])
 */
public class DdApplication extends AuthenticatedWebApplication {
	private static Logger logger = LoggerFactory.getLogger(DdApplication.class);

	/** コンストラクタ */
	public DdApplication() {
		super();
	}

	/** 開始ページを応答する */
	@Override
	public Class<? extends WebPage> getHomePage() {
		return DdHomePage.class;
	}

	/** 初期化処理コールバック */
	@Override
	public void init() {
		super.init();
		
		// add your configuration here
		this.getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
		this.getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
		this.getDebugSettings().setDevelopmentUtilitiesEnabled(true);
		this.getMarkupSettings().setStripWicketTags(true);
		
		//----------------------------------------------
		//エラーページについて 2014.4.5
		//RuntimeExceptionのコンストラクタにメッセージを渡すことで
		//Wicketのデフォルトエラーページの見やすい場所に原因メッセージを表示することができたので
		//自前のエラーページをやめて、Wicketのエラーページを使うことにした。
		//自前のエラーページにメッセージを渡す方法が分からなかったこともある。
		//渡すことができれば自前のエラーページの方が良いのだが。
		//----------------------------------------------
		//RuntimeExceptionの場合は自前のエラーページを表示する
		//下2行を無効にするとWicket標準のUnExpectedRuntimeErrorページが表示される（エンドユーザーに見せるのは詳細過ぎるので自前のエラーページを表示する）
		//this.getApplicationSettings().setInternalErrorPage(DdErrorPage.class);
		//this.getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
		//----------------------------------------------
		
		// this.mountPage("/manager", NeManagerMenu.class);
		this.initPersistence();
		this.showSettings();

		//サインイン済みCookie（name=LoggedIn)が意図した時間で期限切れになるように以下のコードを追加したが解決しなかった。
		//サインアウトボタンを付ければ良いのだが、サインイン済みのCookieがあっても、authenticateが実行される前までは
		//isSignInがfalseのままなので、サインアウトボタンの活性化／非活性化がうまく制御できない。(Directのとき)
		//トップページも@AuthorizeInstantiationを付ければよいかもしれない。
		//ただし、StatelessページにできればOK。
		//Statelessページでないと、(Directのときのように)AWSのhealthチェックの問題が起きる。
		this.getSecuritySettings().setAuthenticationStrategy(
				new DefaultAuthenticationStrategy("myLoggedIn") {
					private CookieUtils cookieUtils;

					@Override
					protected CookieUtils getCookieUtils() {
						if (cookieUtils == null) {
							CookieDefaults cookie = new CookieDefaults();
							cookie.setMaxAge(24 * 60 * 60); // 60sec
							cookieUtils = new CookieUtils(cookie);
						}
						return cookieUtils;
					}
				});
	}

	/** 設定状態をログ出力する */
	private void showSettings() {
		logger.info("#init():ResponseRequestEncoding="
				+ this.getRequestCycleSettings().getResponseRequestEncoding());
		logger.info("#init():getDefaultMarkupEncoding="
				+ this.getMarkupSettings().getDefaultMarkupEncoding());
		logger.info("#init():isDevelopmentUtilitiesEnabled="
				+ this.getDebugSettings().isDevelopmentUtilitiesEnabled());
		logger.info("#init():getInternalErrorPage="
				+ this.getApplicationSettings().getInternalErrorPage());
	}

	/** 開発モードか本番配備モードか */
	@Override
	public RuntimeConfigurationType getConfigurationType() {
		// return RuntimeConfigurationType.DEPLOYMENT; //本番配備モード
		return RuntimeConfigurationType.DEVELOPMENT; // 開発モード
	}

	// /** セッションを生成する */
	// @Override
	// public Session newSession(Request request, Response response){
	// return new DdApplSession(request);
	// }

	/** 認証・認可のセッションクラスを応答する */
	@Override
	protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
		return DdSession.class;
	}

	/** サインイン（ログイン）ページを応答する */
	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return DdSignInPage.class;
	}

	@Override
	public void onUnauthorizedPage(Component page) {
		// ページの@AuthorizeInstantiation(Roles.ADMIN)で認可エラーの時の遷移先を指定する
		// デフォルトでは英語で権限エラーと表示されホームページへのリンクが付いたページが表示される
		throw new RestartResponseAtInterceptPageException(DdUnAuthorizedPage.class);
	}

	/** JPA EntityManagerFactoryを１つ生成し保持する */
	private void initPersistence() {
		try {
			DdEntityManagerFactory.getInstance().open();
		} catch (Exception e) {
			//DdEntityManagerExceptionの場合もここでcatchされる
			String msg = "\n[例外]DdApplication#initPersistence(): " + e.toString();
			logger.error(msg);
			throw new DdRuntimeException(msg);
		}
	}

	/** JPA EntityManagerFactoryクローズ */
	@Override
	public void onDestroy() {
		try {
			DdEntityManagerFactory.getInstance().close();
		} catch (DdEntityManagerException e) {
			String msg = "\n[例外] onDestroy: DdEntityManagerFactory.getInstance().close(): " + e.toString();
			logger.error(msg);
			throw new DdRuntimeException(msg);
		}
	}

	// @Override
	// public void onUnauthorizedPage(Page page){
	// //ページの@AuthorizeInstantiation(Roles.ADMIN)で認可エラーの時の繊維先を指定する
	// //デフォルトでは英語で権限エラーと表示されホームページへのリンクが付いたページが表示される
	// throw new RestartResponseAtInterceptPageException(NeSignIn.class);
	// }
}
