/*
 * DDBuilder%DDB_VERSION%が生成したファイルです。%FILE_GENERATED_AT%
 */
package jp.co.nextdesign.domain.ddb;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * ドメインオブジェクトの基底クラス
 * Serializableではないオブジェクトが Wicketページ内に存在すると、
 * 例えば、ブラウザのバックボタンで戻った時にPage Expiredになる。
 * ドメインモデルにSerializableが必須となると、MVVMのViewModelを作った方が良いという考えも出てくる。
 * @author murayama
 *
 */
@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class DdBaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// 注意:JPAアノテーションをgetterに付けたい場合は@Idをgetterに付け、他のアノテーションもすべてgetterに付けること。

	// JPA識別子 uuidを使用する場合の例
	// @Id
	// @GeneratedValue(generator="uuid")
	// @GenericGenerator(name="uuid", strategy="uuid2") //org.hibernate.annotations.GenericGenerator 
	// protected String id;

	// JPA識別子
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	protected Long id;
	
	// 生成日
	@Temporal(TemporalType.TIMESTAMP)
	protected Date createdAt;

	 //楽観的ロック制御 java.sql.Timestampよりも数値型がよい
	 @Version
	 protected Long version;

	/** コンストラクタ */
	public DdBaseEntity(){
		super();
		//this.createdAt = DdCalendar.getNow(); //createdAtをString型とした場合の例
		this.createdAt = Calendar.getInstance().getTime();
	}

	@Override
	public boolean equals(Object obj){
		boolean result = false;
		if (obj != null && obj instanceof DdBaseEntity){
			Long targetId = ((DdBaseEntity)obj).getId();
			if (targetId > NOT_PERSISTED_ID && targetId.equals(this.getId())){
				result = true;
			}
		}
		return result;
	}

	@Override
	public int hashCode(){
		return 31 * this.getId().hashCode();
	}

	private static final Long NOT_PERSISTED_ID = 0L;
	public Long getId() {
		if (this.id != null){
			return id;
		}else{
			return NOT_PERSISTED_ID;
		}
	}

	public void setId(Long id) {
		this.id = id;
	}

	// 2015.5.30 簡素化のため一旦コメントアウト
	// public String getLastUpdated() {
	// 	return lastUpdated;
	// }
	//
	// public void setLastUpdated(String lastUpdated) {
	// 	this.lastUpdated = lastUpdated;
	// }
	//
	//
	// public String getCreatedByUserId() {
	// 	return createdByUserId;
	// }
	//
	// public void setCreatedByUserId(String createdByUserId) {
	// 	this.createdByUserId = createdByUserId;
	// }
	//
	// public Long getVersion() {
	// 	return version;
	// }
	//
	// public void setVersion(Long version) {
	// 	this.version = version;
	// }

	/** 各サブクラスで実装する */
	public String getDDBEntityTitle(){
		return "DDBEntityTitleは未設定";
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	/** デバッグ情報を表示する場合は各サブクラスで実装する */
	public void debugPrint(){
	}
}
