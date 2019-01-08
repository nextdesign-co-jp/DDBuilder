/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.model.core;

/**
 * Annotationの属性（例：@OneToMany(cascade=CascadeType.ALL)のカッコ内）
 * 2015.7.20 DDBuilderで属性が必要になったので新規追加した
 * @author murayama
 *
 */
public class NdAnnotationAttribute {
	private String name;
	private String value;
	public NdAnnotationAttribute(){
		super();
		this.name = "";
		this.value = "";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
