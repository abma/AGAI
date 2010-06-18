package agai;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Str {
	private static String str;
	private static int defaultlen=10;
	
	public Str(String str, int align){
		align(str,align);
	}
	
	public Str(final int val, final int align) {
		align(Integer.toString(val), align);
	}
	
	public Str(String str) {
		align(str, defaultlen);
	}

	public Str(int val) {
		align(Integer.toString(val),defaultlen);
	}

	public Str(float f) {
		NumberFormat numberFormat = new DecimalFormat("######.##");
		align(numberFormat.format(f),defaultlen);
	}

	public Str(float f, int align) {
		NumberFormat numberFormat = new DecimalFormat("######.##");
		align(numberFormat.format(f),align);
	}

	private static void align(String stri, int align) {
		String val=stri;
		str="";
		for(int i=0; i<align-val.length(); i++)
			str+=" ";
		for(int i=0; i<Math.min(align,stri.length()); i++)
			str=str+stri.charAt(i);
	}
	
	@Override
	public String toString(){
		return str;
	}
	
}
