
public class Main {
	public static final boolean debug = true;
	public static final int DELAY = 20;
	
	public static final String PATH_IMG = "img";
	
	public static void main(String[] args){		
		new Pantalla();
	}
	
	public static void log(String S){
		if(debug) System.out.println(S);
	}
	
	public static void error(String S){
		System.err.println(S);
		System.exit(1);
	}	
}