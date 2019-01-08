package jp.co.nextdesign.domain;

public enum EnumLanguage {
	JA("日本語"),
	EN("英語");

	private String fullName;
	private EnumLanguage(String fullName){
		this.fullName = fullName;
	}

	@Override
	public String toString(){
		return this.fullName;
	}
}
