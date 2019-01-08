/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.template;

import java.io.File;

import jp.co.nextdesign.ddb.BdApplicationPropertyException;
import jp.co.nextdesign.ddb.core.BdBuilderException;
import jp.co.nextdesign.ddb.core.template.html.BdCodeFileDomainClassListPageHtml;
import jp.co.nextdesign.ddb.core.template.html.BdCodeFileEditPageHtml;
import jp.co.nextdesign.ddb.core.template.html.BdCodeFileInstanceListPageHtml;
import jp.co.nextdesign.ddb.core.template.html.BdCodeFileManyToManySelectPanelHtml;
import jp.co.nextdesign.ddb.core.template.html.BdCodeFileManyToOneSelectPanelHtml;
import jp.co.nextdesign.ddb.core.template.html.BdCodeFileMultipleSelectPanelHtml;
import jp.co.nextdesign.ddb.core.template.html.BdCodeFileOneToManySelectPanelHtml;
import jp.co.nextdesign.ddb.core.template.html.BdCodeFileServiceMethodInvokePageHtml;
import jp.co.nextdesign.ddb.core.template.html.BdCodeFileServiceMethodListPageHtml;
import jp.co.nextdesign.ddb.core.template.html.BdCodeFileServiceMethodResultPageHtml;
import jp.co.nextdesign.ddb.core.template.html.BdCodeFileSingleSelectPanelHtml;
import jp.co.nextdesign.ddb.core.template.html.BdCodeFileTestDataLoadPageHtml;
import jp.co.nextdesign.ddb.core.template.java.BdCodeFileDomainClassListPageJava;
import jp.co.nextdesign.ddb.core.template.java.BdCodeFileEditPageJava;
import jp.co.nextdesign.ddb.core.template.java.BdCodeFileInstanceListPageJava;
import jp.co.nextdesign.ddb.core.template.java.BdCodeFileManagerJava;
import jp.co.nextdesign.ddb.core.template.java.BdCodeFileManyToManySelectPanelJava;
import jp.co.nextdesign.ddb.core.template.java.BdCodeFileManyToOneSelectPanelJava;
import jp.co.nextdesign.ddb.core.template.java.BdCodeFileOneToManySelectPanelJava;
import jp.co.nextdesign.ddb.core.template.java.BdCodeFileServiceJava;
import jp.co.nextdesign.ddb.core.template.java.BdCodeFileServiceMethodInvokePageJava;
import jp.co.nextdesign.ddb.core.template.java.BdCodeFileServiceMethodListPageJava;
import jp.co.nextdesign.ddb.core.template.java.BdCodeFileServiceMethodResultPageJava;
import jp.co.nextdesign.ddb.core.template.java.BdCodeFileSingleSelectPanelJava;
import jp.co.nextdesign.ddb.core.template.java.BdCodeFileTestDataLoadPageJava;
import jp.co.nextdesign.ddb.core.template.java.BdCodeFileTestDataServiceJava;
import jp.co.nextdesign.ddb.core.template.java.BdCodeFileMultipleSelectPanelJava;

public class BdTemplateFileFactory {

	/**
	 * CodeTemplateファクトリメソッド
	 * @param file
	 * @param project
	 * @return
	 * @throws BdBuilderException
	 * @throws BdApplicationPropertyException
	 */
	public static BdTemplateFile create(File file, BdTemplateProject project) throws BdBuilderException, BdApplicationPropertyException{
		BdTemplateFile result = null;
		if (file.getName().equals("DdBookManager.java")) {
			result = new BdCodeFileManagerJava(file, project);
		} else if (file.getName().equals("DdBookService.java")) {
			result = new BdCodeFileServiceJava(file, project);	
		} else if (file.getName().equals("DomainClassListPage.java")) {
			result = new BdCodeFileDomainClassListPageJava(file, project);
		} else if (file.getName().equals("DomainClassListPage.html")) {
			result = new BdCodeFileDomainClassListPageHtml(file, project);
		} else if (file.getName().equals("BookInstanceListPage.java")) {
			result = new BdCodeFileInstanceListPageJava(file, project);
		} else if (file.getName().equals("BookInstanceListPage.html")) {
			result = new BdCodeFileInstanceListPageHtml(file, project);
		} else if (file.getName().equals("BookEditPage.java")) {
			result = new BdCodeFileEditPageJava(file, project);
		} else if (file.getName().equals("BookEditPage.html")) {
			result = new BdCodeFileEditPageHtml(file, project);
//Embedded,OneToOne型の属性の編集画面はDialog編集画面ではなくBookEditPageと同等のXxxEditPageに遷移する 2015.7.8			
//		} else if (file.getName().equals("BookIsbnEditPanel.java")) {
//			result = new BdCodeFileEmbeddedEditPanelJava(file, project);
//		} else if (file.getName().equals("BookIsbnEditPanel.html")) {
//			result = new BdCodeFileEmbeddedEditPanelHtml(file, project);
		} else if (file.getName().equals("BookAuthorSingleSelectPanel.java")) {
			result = new BdCodeFileManyToOneSelectPanelJava(file, project);
		} else if (file.getName().equals("BookAuthorSingleSelectPanel.html")) {
			result = new BdCodeFileManyToOneSelectPanelHtml(file, project);
		} else if (file.getName().equals("BookEditionListMultipleSelectPanel.java")) {
			result = new BdCodeFileOneToManySelectPanelJava(file, project);
		} else if (file.getName().equals("BookEditionListMultipleSelectPanel.html")) {
			result = new BdCodeFileOneToManySelectPanelHtml(file, project);
		} else if (file.getName().equals("BookStoreListMultipleSelectPanel.java")) {
			result = new BdCodeFileManyToManySelectPanelJava(file, project);
		} else if (file.getName().equals("BookStoreListMultipleSelectPanel.html")) {
			result = new BdCodeFileManyToManySelectPanelHtml(file, project);
		} else if (file.getName().equals("TestDataService.java")) {
			result = new BdCodeFileTestDataServiceJava(file, project);
		} else if (file.getName().equals("TestDataLoadPage.java")) {
			result = new BdCodeFileTestDataLoadPageJava(file, project);
		} else if (file.getName().equals("TestDataLoadPage.html")) {
			result = new BdCodeFileTestDataLoadPageHtml(file, project);
		} else if (file.getName().equals("ServiceMethodListPage.java")) {
			result = new BdCodeFileServiceMethodListPageJava(file, project);
		} else if (file.getName().equals("ServiceMethodListPage.html")) {
			result = new BdCodeFileServiceMethodListPageHtml(file, project);
		} else if (file.getName().equals("BookServiceInvokePageMethod1.java")){
			result = new BdCodeFileServiceMethodInvokePageJava(file, project);
		} else if (file.getName().equals("BookServiceInvokePageMethod1.html")){
			result = new BdCodeFileServiceMethodInvokePageHtml(file, project);
		} else if (file.getName().equals("BookServiceResultPageMethod1.java")){
			result = new BdCodeFileServiceMethodResultPageJava(file, project);
		} else if (file.getName().equals("BookServiceResultPageMethod1.html")){
			result = new BdCodeFileServiceMethodResultPageHtml(file, project);
		} else if (file.getName().equals("AuthorSingleSelectPanel.java")){
			result = new BdCodeFileSingleSelectPanelJava(file, project);
		} else if (file.getName().equals("AuthorSingleSelectPanel.html")){
			result = new BdCodeFileSingleSelectPanelHtml(file, project);
		} else if (file.getName().equals("EditionMultipleSelectPanel.java")){
			result = new BdCodeFileMultipleSelectPanelJava(file, project);
		} else if (file.getName().equals("EditionMultipleSelectPanel.html")){
			result = new BdCodeFileMultipleSelectPanelHtml(file, project);
		} else {
			result = new BdTemplateFile(file, project);
		}
		return result;
	}	
}
