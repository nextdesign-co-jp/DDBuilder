/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.documentation.project;

/**
 * プロジェクトレポート詳細行（メトリクス行）
 * @author murayama
 */
public class NdMetricsLine {
	protected boolean isPackage = false;
	protected String packageName = "";
	protected Integer subPackageCount;
	protected Integer classifierCount;
	protected String isPublic = "";
	protected String abstract_interface = "";
	protected String claasifierName = "";
	protected String hyperLinkFileName = "";
	protected Integer totalStepCount;
	protected Integer realStepCount;
	protected Integer javadocStepCount;
	protected Integer commentStepCount;
	protected Integer methodCount;
	protected Integer constructorCount;
	protected Integer innerClassCount;
	protected Integer anonymousClassCount;
	protected Integer innerEnumCount;
	protected Integer initializerCount;
	protected Integer methodMaxStepCount;
	protected Integer maxNestLevel;
	protected Integer controlStepCount;
	protected Integer attributeCount;
}
