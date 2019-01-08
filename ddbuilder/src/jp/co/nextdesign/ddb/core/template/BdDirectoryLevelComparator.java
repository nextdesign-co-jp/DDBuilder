/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.core.template;

import java.util.Comparator;

/**
 * テンプレートディレクトリを階層レベルで比較する比較子
 * @author murayama
 *
 */
public class BdDirectoryLevelComparator implements Comparator<BdTemplateDirectory> {

	@Override
	public int compare(BdTemplateDirectory o1, BdTemplateDirectory o2) {
		return o1.getDirectoryLevel() - o2.getDirectoryLevel();
	}
}
