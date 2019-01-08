/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import jp.co.nextdesign.domain.ddb.DdBaseEntity;
import jp.co.nextdesign.domain.store.Store;

/**
 * 書籍
 * @author murayama
 *
 */
@Entity
public class Book extends DdBaseEntity {
	private static final long serialVersionUID = 1L;
	
	/** 書名 */
	private String name;
	
	/** 出版日 */
	private Date publishedAt;
	
	/** 価格 */
	private BigDecimal price;
	
	/** キャンペーン1 isなし */
	private Boolean campaign1;
	
	/** キャンペーン2 is付き */
	private Boolean isCampaign2;
	
	/** 言語 */
	@Enumerated(EnumType.STRING)
	private EnumLanguage attEnum;
	
	/** ISBN one-to-one owning/parent これはHibernate ORM 5.2 User Guide2.7とは少し異なる */
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="isbn_id") //自分の列
	private Isbn isbn;
	
	/** 著者 */
	@ManyToOne
	private Author author;
	
	/** 版 */
	@OneToMany(mappedBy="book", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Edition> editionList;
	
	/** 書店 */
	@ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER)
	private List<Store> storeList;
	
	/** Integer型属性名 */
	private Integer integerAttribute;
	
	/** Byte型属性名 */
	private Byte attByte;
	
	/** Short型属性名 */
	private Short attShort;
	
	/** Long型属性名 */
	private Long attLong;
	
	/** Float型属性名 */
	private Float attFloat;
	
	/** Double型属性名 */
	private Double attDouble;
	
	/** Character型属性名 */
	private Character attCharacter;
	
	/** コンストラクタ */
	public Book(){
		super();
		this.name = "";
		this.storeList = new ArrayList<Store>();
		this.editionList = new ArrayList<Edition>();
	}
	
	/*
	 * getter/setterに加えて、このメソッドを追加する理由
	 * OneToManyの関連に関連先を追加するためには、
	 * book.getEditionList().add(newEdition)として、persist(book)としても追加されない。
	 * newEdition.setBook(book)としてから、persist(book)しなければならない。
	 * ただ、シーケンスとしてbook側を変更するだけにしたい場合もあるので、以下のようなaddEdition(newEdition)を実装した。
	 * ただし、双方向維持のための常套コードのようにedition.setBook(book)からbook.addEdition(edition)とすると、
	 * 復元時に"復元中にコレクションが変更された"という例外が発生するので、edition.setBook(book)からbook.addEdition(edition)は使用しないようにすること。
	 */
	public void addEdition(Edition edition){
//		if(edition!=null && !this.editionList.contains(edition)){
//			edition.setBook(this);
//			this.editionList.add(edition);
//		}
		//Hibernate ORM 5.2 User Guide2.7.2 Bidirectionaln@OneToMany 例を参考
		this.editionList.add(edition);
		edition.setBook(this);
	}
	
	/**
	 * Hibernate ORM 5.2 User Guide2.7.2 Bidirectionaln@OneToMany 例を参考
	 * @param edition
	 */
	public void removeEdition(Edition edition){
		this.editionList.remove(edition);
		edition.setBook(null);
	}

	/**
	 * ManyToManyで双方向関連を維持するためのaddStore,removeStoreを含む。owning側ではなくmappedBy側から使用するが、両側に実装する。
	 */
	public List<Store> getStoreList() {
		return storeList;
	}
	public void setStoreList(List<Store> storeList) {
		this.storeList = storeList;
	}
	public void addStore(Store store){
		if (store != null && !this.storeList.contains(store)){
			this.storeList.add(store);
			store.addBook(this);
		}
	}
	public void removeStore(Store store){
		if (store != null && this.storeList.contains(store)){
			this.storeList.remove(store);
			store.removeBook(this);
		}
	}

	public Isbn getIsbn() {
		return isbn;
	}
	public void setIsbn(Isbn isbn) {
		this.isbn = isbn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Author getAuthor() {
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	public List<Edition> getEditionList() {
		return editionList;
	}
	public void setEditionList(List<Edition> editionList) {
		this.editionList = editionList;
	}
	public Date getPublishedAt() {
		return publishedAt;
	}
	public void setPublishedAt(Date publishedAt) {
		this.publishedAt = publishedAt;
	}	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	//Eclipseのgetter/setter自動生成では(booleanではなくBooleanの場合)is名前形式のgetterは生成されない。
	//Wicketはis名前形式のgetterが必要と思われたが、それは勘違いでget名前形式でよいようだ。
	public Boolean getCampaign1() {
		return campaign1;
	}
	public void setCampaign1(Boolean campaign1) {
		this.campaign1 = campaign1;
	}
	public Boolean getIsCampaign2() {
		return isCampaign2;
	}
	public void setIsCampaign2(Boolean isCampaign2) {
		this.isCampaign2 = isCampaign2;
	}
	
	public Byte getAttByte() {
		return attByte;
	}
	public void setAttByte(Byte attByte) {
		this.attByte = attByte;
	}
	public Short getAttShort() {
		return attShort;
	}
	public void setAttShort(Short attShort) {
		this.attShort = attShort;
	}
	public Integer getIntegerAttribute() {
		return integerAttribute;
	}
	public void setIntegerAttribute(Integer integerAttribute) {
		this.integerAttribute = integerAttribute;
	}
	public Long getAttLong() {
		return attLong;
	}
	public void setAttLong(Long attLong) {
		this.attLong = attLong;
	}
	public Float getAttFloat() {
		return attFloat;
	}
	public void setAttFloat(Float attFloat) {
		this.attFloat = attFloat;
	}
	public Double getAttDouble() {
		return attDouble;
	}
	public void setAttDouble(Double attDouble) {
		this.attDouble = attDouble;
	}
	public Character getAttCharacter() {
		return attCharacter;
	}
	public void setAttCharacter(Character attCharacter) {
		this.attCharacter = attCharacter;
	}
	public EnumLanguage getAttEnum() {
		return attEnum;
	}
	public void setAttEnum(EnumLanguage attEnum) {
		this.attEnum = attEnum;
	}

	@Override
	public String getDDBEntityTitle(){
		String result = this.getName();
		result += this.getAuthor() != null ? this.getAuthor().getName() : "";
		return result;
	}
	
	/** debug */
	public void debugPrint(){
		String info = "<" + this.getClass().getSimpleName() + ">";
		info += "\nname=" + this.getName();
		info += "\npublishedAt=" + this.getPublishedAt();
		if(this.author != null) info += "\n" + this.getAuthor().getDebugInfo();
		if(this.isbn != null) info += "\n" + this.getIsbn().getDebugInfo();
		for(Edition edition : this.getEditionList()){
			info += "\n" + edition.getDebugInfo();
		}
		for(Store bookStore : this.getStoreList()){
			info += "\n" + bookStore.getDebugInfo();
		}
		info += "\n</" + this.getClass().getSimpleName() + ">";
		System.out.println("--------------------------------------");
		System.out.println(info);
		System.out.println("--------------------------------------");
	}
}
