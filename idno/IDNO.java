package idno;

import com.e104.rd.common.tool.IDNOConverter;;

public class IDNO {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		IDNOConverter idno = new IDNOConverter();
		
		System.err.println(idno.id2no_TW("N224428639"));
		System.err.println(idno.no2id_TW(1814853692246L));
	}

}
