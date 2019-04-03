public class Play extends Thread{
	
	public Play(){}

	@Override
	public void run() {
		while(true){
			try {Pantalla.update();}
			catch(Exception p){p.printStackTrace();System.err.println("WARNING: Exception @Play - Pantalla.update()");}
			try{sleep(Main.DELAY);}catch(InterruptedException e){e.printStackTrace();}
		}
	}	
}