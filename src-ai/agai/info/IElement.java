package agai.info;

/**
 * The Enum ElementType.
 */
public class IElement {


	public static final int any=0;
	public static final int fly=1;
	public static final int land=2;
	public static final int sub=4;
	public static final int swim=8;
	public static final int hover = 16;

	/** The type. */
	private int type;

	/**
	 * Instantiates a new element type.
	 * 
	 * @param type
	 *            the type
	 */
	public IElement(int type) {
		this.type = type;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	public boolean isFly(){
		return (type & fly)!=0;
	}

	public boolean isLand(){
		return (type & land)!=0;
	}

	public boolean isSub(){
		return (type & sub)!=0;
	}

	public boolean isSwim(){
		return (type & swim)!=0;
	}
	public boolean isHover(){
		return (type & hover)!=0;
	}

	public boolean isAny() {
		return type==0;
	}
	

}