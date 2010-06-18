package agai;

public class Str {
	private static String str;
	
	public Str(String str, int align){
		align(str,align);
	}
	
	public Str(final int val, final int align) {
		align(Integer.toString(val), align);
	}
	
	public Str(String str) {
		align(str, 15);
	}

	public Str(int val) {
		align(Integer.toString(val),10);
	}

	public Str(float f) {
		align(Float.toString(f),10);
	}

	private static void align(String stri, int align) {
		String val=stri;
		str="";
		for(int i=0; i<align-val.length(); i++)
			str+=" ";
		str=str+val;
	}
	
	@Override
	public String toString(){
		return str;
	}
	
}
