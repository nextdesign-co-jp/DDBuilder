/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import jp.co.nextdesign.domain.ddb.DdBaseEntity;

/**
 * ISBN
 * @author murayama
 *
 */
@Entity
public class Isbn extends DdBaseEntity {
	private static final long serialVersionUID = 1L;
	
	/** 書籍 one-to-one non-owning/child これはHibernate ORM 5.2 User Guide2.7とは少し異なる */
	@OneToOne(mappedBy="isbn") //相手側(owning)の属性
	private Book book;
	
	/** グループ記号 */
	private String groupCode;
	
	/** 出版社記号 */
	private Integer publisherCode;
	
	/** 書名記号 */
	private String itemCode;
	
	/** チェックディジット */
	private String checkDigit;
	
	/** 決定日 */
	private Date determinatedAt;
	
	/** 旧ISBN */
	private Boolean isOldIsbn;
	
	/** コンストラクタ */
	public Isbn(){
		
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public Integer getPublisherCode() {
		return publisherCode;
	}

	public void setPublisherCode(Integer publisherCode) {
		this.publisherCode = publisherCode;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getCheckDigit() {
		return checkDigit;
	}

	public void setCheckDigit(String checkDigit) {
		this.checkDigit = checkDigit;
	}
	
	public Date getDeterminatedAt() {
		return determinatedAt;
	}

	public void setDeterminatedAt(Date determinatedAt) {
		this.determinatedAt = determinatedAt;
	}

	public Boolean getIsOldIsbn() {
		return isOldIsbn;
	}

	public void setIsOldIsbn(Boolean isOldIsbn) {
		this.isOldIsbn = isOldIsbn;
	}

	@Override
	public String getDDBEntityTitle(){
		return "ISBN" + this.getGroupCode() + "-" + this.getPublisherCode() + "-" + this.getItemCode();
	}
	
	/** debug */
	public String getDebugInfo(){
		String info = "<" + this.getClass().getSimpleName() + ">";
		info += "\ngroupCode=" + this.getGroupCode();
		info += "\npublisherCode=" + this.getPublisherCode();
		info += "\nitemNumber=" + this.getItemCode();
		info += "\n</" + this.getClass().getSimpleName() + ">";
		return info;
	}
}
