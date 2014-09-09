package im.proxy.umg.util;

public class NumberUtil {
	public static String normalize(String number) {
		if(number == null)
			return "";
		
		if(number.isEmpty())
			return "";
		
		if(number.startsWith("+"))
			return number.substring(1);
		
		if(number.startsWith("00"))
			return number.substring(2);
		
		if(number.startsWith("0"))
			return "92" + number.substring(1);
		
		return number;
	}
	
	public static void main(String[] args) {
		System.out.println(NumberUtil.normalize("03002790541"));
		System.out.println(NumberUtil.normalize("923002790541"));
		System.out.println(NumberUtil.normalize("00923002790541"));
		System.out.println(NumberUtil.normalize("+923002790541"));
	}
}
