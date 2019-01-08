/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.service.g;

import java.text.SimpleDateFormat;
import java.util.Calendar;

//-- BLOCKSTART NoCode
import jp.co.nextdesign.domain.Author;
import jp.co.nextdesign.domain.Book;
import jp.co.nextdesign.domain.Edition;
import jp.co.nextdesign.domain.Isbn;
import jp.co.nextdesign.domain.g.DdAuthorManager;
import jp.co.nextdesign.domain.g.DdBookManager;
import jp.co.nextdesign.domain.g.DdEditionManager;
import jp.co.nextdesign.domain.g.DdStoreManager;
import jp.co.nextdesign.domain.store.Store;
//-- BLOCKEND NoCode
import jp.co.nextdesign.service.ddb.DdBaseService;

/**
 * テスト用データを作成するサービス
 * このクラスはDDBuilderによって上書きされません。存在しない場合のみ新規作成します。
 * Service for creating test data.
 * This class is not overwritten by DDBuilder. It will only be created if it does not exist.
 *
 */
public class TestDataService extends DdBaseService{

	public void addTestData(int count){
		try {
			startService();
			begin();
			
			/*
			必要な場合は、ここにテスト用データを作成するためのコードを書きます。
			ホームページの「テストデータ追加」ボタンを押下するとこのメソッドが実行されます。
			以下はテストデータとして、書籍クラスのインスタンスを1つ作成するためのサンプルコードです。
			If necessary, write code to create test data here.
			This method is executed when you click "add test data" button on the homepage.
			The following is a sample code for creating one instance of the book class as test data.
			 */
			
//			DdBookManager bookManager = new DdBookManager();
//			Book book1 = new Book();
//			book1.setName("book title");
//			bookManager.persist(book1);


			//-- BLOCKSTART NoCode
			SimpleDateFormat format = new SimpleDateFormat("MMdd_HHmmss_SSS");
			for(int i=1; i<=count; i++){
				String timeStamp = format.format(Calendar.getInstance().getTime());
				
				//作者
				DdAuthorManager authorManager = new DdAuthorManager();
				Author author1 = new Author();
				author1.setName("夏目漱石" + timeStamp);
				authorManager.persist(author1); //persist author
				Author author2 = new Author();
				author2.setName("森鴎外" + timeStamp);
				authorManager.persist(author2); //persist author
				Author author3 = new Author();
				author3.setName("太宰治" + timeStamp);
				authorManager.persist(author3); //persist author

				//書店1
				DdStoreManager bookStoreManager = new DdStoreManager();
				Store bookStore1 = new Store();
				bookStore1.setName("書泉グランデ" + timeStamp);
				bookStoreManager.persist(bookStore1);
				
				//書店2
				Store bookStore2 = new Store();
				bookStore2.setName("紀伊国屋" + timeStamp);
				bookStoreManager.persist(bookStore2);

				//書店3
				Store bookStore3 = new Store();
				bookStore3.setName("有隣堂" + timeStamp);
				bookStoreManager.persist(bookStore3); //non-owning側(mappedByのない方)のみ更新される

				//版1
				DdEditionManager editionManager = new DdEditionManager();
				Edition edition1 = new Edition();
				edition1.setEditionNumber(i);
				edition1.setName("第" + i + "版");
				editionManager.persist(edition1);
				
				//版2
				Edition edition2 = new Edition();
				edition2.setEditionNumber(i + 1);
				edition2.setName("第" + (i + 1) + "版");
				editionManager.persist(edition2);

				//書籍
				DdBookManager bookManager = new DdBookManager();
				Book book = new Book();
				book.setPublishedAt(Calendar.getInstance().getTime());
				book.setName("三四郎" + timeStamp);
				book.setAuthor(author1); //作者
				
				//ISBN
				Isbn isbn = new Isbn();
				isbn.setGroupCode("J" + timeStamp);
				isbn.setPublisherCode(1234);
				isbn.setItemCode("5678");

				book.setIsbn(isbn); //ISBN
//				book.getBookStoreList().add(bookStore1); //書店1
//				book.getBookStoreList().add(bookStore2); //書店2
//				bookStore3.getBookList().add(book); //書店3
				book.addStore(bookStore1); //書店1
				//book.addStore(bookStore2); //書店2
				//bookStore3.addBook(book); //書店3
				
				//bookStoreManager.persist(bookStore3); //デフォルトはCascadeのような感じだ 2015.6.4
				book.addEdition(edition1); //版1
				book.addEdition(edition2); //版1
				
				//2015.6.25追加
				book.setAttByte((byte)0);
				book.setAttShort((short)1);
				book.setIntegerAttribute(11);
				book.setAttLong(20L);
				book.setAttFloat(30F);
				book.setAttDouble(40D);
				book.setAttCharacter('B');
				
				bookManager.persist(book); //persist book
			}
			//-- BLOCKEND NoCode
			commit();
		} catch (Exception e) {
			rollback();
		} finally {
			endService();
		}
	}
}
