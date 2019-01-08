/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import jp.co.nextdesign.domain.ddb.DdBaseEntity;

/**
 * 著者
 * @author murayama
 *
 */
@Entity
public class Author extends DdBaseEntity {
	private static final long serialVersionUID = 1L;

	/** 名前 */
	private String name;
	
	/** 書籍リスト owning/parent */
	@OneToMany(mappedBy="author", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Book> bookList;

	public Author(){
		super();
		this.name = "---";
		this.bookList = new ArrayList<Book>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Book> getBookList() {
		return bookList;
	}

	public void setBookList(List<Book> bookList) {
		this.bookList = bookList;
	}
	
	/*
	 * Hibernate ORM 5.2 User Guide2.7.2 Bidirectionaln@OneToMany 例を参考
	 * @param book
	 */
	public void addBook(Book book){
		//Hibernate ORM 5.2 User Guide2.7.2 Bidirectionaln@OneToMany 例を参考
		this.bookList.add(book);
		book.setAuthor(this);
	}
	/**
	 * Hibernate ORM 5.2 User Guide2.7.2 Bidirectionaln@OneToMany 例を参考
	 * @param book
	 */
	public void removeBook(Book book){
		this.bookList.remove(book);
		book.setAuthor(null);
	}
	
	@Override
	public String getDDBEntityTitle(){
		return this.name;
	}
	
	/** debug */
	public String getDebugInfo(){
		String info = "<" + this.getClass().getSimpleName() + ">";
		info += "\nname=" + this.getName();
		info += "\n</" + this.getClass().getSimpleName() + ">";
		return info;
	}
}
