/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * Copyright(c) 2011 by NEXT DESIGN Ltd. All Rights Reserved.
 */
package jp.co.nextdesign.jcr.documentation.project;

import java.util.Comparator;

/**
 * 比較オブジェクト
 * @author murayama
 */
class NdPackageDependecyInfoComparator implements Comparator<NdPackageDependencyInfo>{

	@Override
	public int compare(NdPackageDependencyInfo info1, NdPackageDependencyInfo info2) {
		int result = 0;
		if ((info1 != null) && (info2 != null)){
			Boolean b1 = info1.isInverse();
			Boolean b2 = info2.isInverse();
			if (!b1.equals(b2)){
				if (b1){
					result = 1;
				} else {
					result = -1;
				}
			}
			if (result == 0){
				String from1 = info1.getFromName();
				String from2 = info2.getFromName();
				result = from1.compareTo(from2);
				if (result == 0){
					String to1 = info1.getToName();
					String to2 = info2.getToName();
					result = to1.compareTo(to2);
				}
			}
		}
		return result;
	}
}
