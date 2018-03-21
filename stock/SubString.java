package stock;

public class SubString {
	int AllLength = 0;
	
	public String getStockString(String allStr, String findStr, String endStr) throws Exception{
		int startLength = allStr.indexOf(findStr,AllLength);
		int startLength2 = findStr.length();
		int endLength = allStr.indexOf(endStr,startLength);
		String catchStr = allStr.substring(startLength+startLength2,endLength);
		catchStr = catchStr.replaceAll("<br>","");
		
		catchStr = catchStr.replaceAll("<FONTcolor=#e85454>","");
		catchStr = catchStr.replaceAll("<FONTcolor=#3f983f>","");
		catchStr = catchStr.replaceAll("</FONT>","");
		catchStr = catchStr.replaceAll("&nbsp;","");
		AllLength = startLength+startLength2;
		
		return catchStr;
	}
}
