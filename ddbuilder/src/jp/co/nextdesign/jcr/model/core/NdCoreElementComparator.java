/**
 * プロジェクト
 * @author murayama
 */
package jp.co.nextdesign.jcr.model.core;

import java.util.Comparator;

/**
 * NdCoreelement用Comparator
 * @author murayama
 */
public class NdCoreElementComparator implements Comparator<NdCoreElement> {

	@Override
	public int compare(NdCoreElement o1, NdCoreElement o2) {
		int result = 0;
		if ((o1 == null) || (o2 == null)){
			return result;
		}
		String s1 = o1.getName().toUpperCase();
		String s2 = o2.getName().toUpperCase();
		result = s1.compareTo(s2);
		return result;
	}


}
