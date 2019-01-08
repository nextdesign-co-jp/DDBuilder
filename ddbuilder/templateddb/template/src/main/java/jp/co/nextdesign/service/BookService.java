package jp.co.nextdesign.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.nextdesign.domain.Author;
import jp.co.nextdesign.domain.Book;
import jp.co.nextdesign.domain.Edition;
import jp.co.nextdesign.domain.EnumLanguage;
import jp.co.nextdesign.service.ddb.DdBaseService;

/**
 * 書籍サービス
 * @author murayama
 *
 */
public class BookService extends DdBaseService {
	
	/**
	 * 書籍サービスメソッド1
	 * @param name 書名
	 * @param publishedAt 出版日
	 * @param campaign1 キャンペーン1 isなし
	 * @param isCampaign2 キャンペーン2 is付き
	 * @param attEnum 言語
	 * @param author 著者
	 * @param editionList 版
	 * @param integerAttribute Integer型属性名
	 * @param attByte Byte型属性名
	 * @param attShort Short型属性名
	 * @param attLong Long型属性名
	 * @param attFloat Float型属性名
	 * @param attDouble Double型属性名
	 * @param attCharacter Character型属性名
	 * @return 書籍リスト
	 */
	public List<Book> method1(
			String name,
			Date publishedAt,
			Boolean campaign1,
			Boolean isCampaign2,
			EnumLanguage attEnum,
			Author author,
			List<Edition> editionList,
			Integer integerAttribute,
			Byte attByte,
			Short attShort,
			Long attLong,
			Float attFloat,
			Double attDouble,
			Character attCharacter
			){

		//サービスメソッドのサンプル
		List<Book> resultList = new ArrayList<Book>();

		for(int i=1; i<=20; i++){
			Book book = new Book();
			book.setName("サンプル書名" + i);
			resultList.add(book);
		}

		return resultList;
	}
	
	/**
	 * 書籍サービスメソッド1
	 * @param name 書名
	 * @param publishedAt 出版日
	 * @param campaign1 キャンペーン1 isなし
	 * @param isCampaign2 キャンペーン2 is付き
	 * @param attEnum 言語
	 * @param author 著者
	 * @param editionList 版
	 * @param integerAttribute Integer型属性名
	 * @param attByte Byte型属性名
	 * @param attShort Short型属性名
	 * @param attLong Long型属性名
	 * @param attFloat Float型属性名
	 * @param attDouble Double型属性名
	 * @param attCharacter Character型属性名
	 * @return 書籍リスト
	 */
	public Book method2(
			String name,
			Date publishedAt,
			Boolean campaign1,
			Boolean isCampaign2,
			EnumLanguage attEnum,
			Author author,
			List<Edition> editionList,
			Integer integerAttribute,
			Byte attByte,
			Short attShort,
			Long attLong,
			Float attFloat,
			Double attDouble,
			Character attCharacter
			){

			Book book = new Book();
			book.setName("一件戻りのサンプル書名");

		return book;
	}

}
