/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import jp.co.nextdesign.domain.ddb.DdBaseEntity;

/**
 * 版
 * @author murayama
 *
 */
@Entity
public class Edition extends DdBaseEntity {
	private static final long serialVersionUID = 1L;

	/** 版番号 */
	private int editionNumber;
	
	/** 版名 */
	private String name;
	
	/** 書籍 */
	@ManyToOne
	//@JoinColumn(name="book_id") //省略可
	private Book book;
	
	public Edition(){
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getEditionNumber() {
		return editionNumber;
	}

	public void setEditionNumber(int editionNumber) {
		this.editionNumber = editionNumber;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}	

	@Override
	public String getDDBEntityTitle(){
		return this.name;
	}

	/** debug */
	public String getDebugInfo(){
		String info = "<" + this.getClass().getSimpleName() + ">";
		info += "\neditionNumber=" + this.getEditionNumber();
		info += "\n</" + this.getClass().getSimpleName() + ">";
		return info;
	}
}
