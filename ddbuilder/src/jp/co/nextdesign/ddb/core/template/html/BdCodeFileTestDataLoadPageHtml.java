/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.template.html;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.co.nextdesign.ddb.BdApplicationPropertyException;
import jp.co.nextdesign.ddb.core.BdBuilderException;
import jp.co.nextdesign.ddb.core.template.BdBaseCodeFile;
import jp.co.nextdesign.ddb.core.template.BdCodeLine;
import jp.co.nextdesign.ddb.core.template.BdGeneratedFile;
import jp.co.nextdesign.ddb.core.template.BdTemplateProject;
import jp.co.nextdesign.ddb.core.template.block.BdBaseCodeBlock;

	public class BdCodeFileTestDataLoadPageHtml extends BdBaseCodeFile {

		/**
		 * コンストラクタ
		 * @param file
		 * @param project
		 * @throws BdBuilderException
		 * @throws BdApplicationPropertyException
		 */
		public BdCodeFileTestDataLoadPageHtml(File file, BdTemplateProject project) throws BdBuilderException, BdApplicationPropertyException{
			super(file, project);
		}
		
		/**
		 * 出力ファイルを生成する
		 * ドメインクラスが1つも定義されていない場合もDomainClassListPageは生成するため
		 */
		@Override
		public List<BdGeneratedFile> generate(String groupId){
			BdGeneratedFile result = new BdGeneratedFile();
			result.setFullPathName(this.getPlainFile().getAbsolutePath()); //パス名に置換文字なし
			result.addLines(this.generateLines(groupId));
			return new ArrayList<BdGeneratedFile>(Arrays.asList(result));
		}
		
		/**
		 * 出力ファイルのコード部分を作成する
		 * ドメインクラスが1つも定義されていない場合もDomainClassListPageは生成するため
		 * @param c
		 * @return
		 */
		private List<String> generateLines(String groupId) {
			List<String> resultList = new ArrayList<String>();
			int lineIndex = 0;
			while (lineIndex < this.getCodeLineList().size()){
				BdCodeLine bdCodeLine = this.getCodeLineList().get(lineIndex);
				if (bdCodeLine.hasCodeBlock()){
					//CodeBlock内は空
					lineIndex = bdCodeLine.getCodeBlock().getEndIndex() + 1;
				}else{
					resultList.add(this.customize(bdCodeLine.getLine()));
					lineIndex++;
				}
			}
			return resultList;
		}

		/** 
		 * 文字列を置き換える
		 */
		private String customize(String line){
			return line;
		}

		//このテンプレートファイルに含まれるブロックキーはない

		/** 
		 * 各サブクラスでOverrideする
		 */
		@Override
		protected BdBaseCodeBlock createCodeBlock(String key, List<BdCodeLine> lineListOfBlock){
			return null;
		}
	}
