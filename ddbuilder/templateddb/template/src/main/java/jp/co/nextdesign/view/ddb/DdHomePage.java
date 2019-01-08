/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.view.ddb;

import org.apache.wicket.markup.html.WebPage;

/**
 * 先頭ページ
 * このページをお気に入りに登録する。
 * このページ以外はステートフルでURLの後ろに/?2などのバージョンが付く。
 * そのため、お気に入りに登録しても、次回アクセスしたときに/?2が付いたページインスタンスがあるとは限らない。
 * このページは、「お気に入りに登録できる」かつ「AWSのヘルスチェックでエラーにならない」ために以下の２条件を満たすように作成してある。
 * 条件１：Bookmarkable であること
 * （デフォルトコンストラクタ and/or 引数1つ(final PageParameters parameters)のコンストラクタを含む）
 * 条件２：ステートレスであること
 * （LinkとFormコンポーネントを含まない。そのために、DdBasePageではなくWebPageを継承する）
 * @author murayama
 *
 */
public class DdHomePage extends WebPage {
	private static final long serialVersionUID = 1L;
	
	public DdHomePage(){
		super();
	}
}
