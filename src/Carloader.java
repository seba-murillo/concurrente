public class Carloader extends Thread{
	public Carloader(){
		start();
	}
	
	@Override
	public void run(){
//		new Auto(3,12,Auto.WHITE);
//		new Auto(12,30,Auto.BLUE);
		
		for(int i = 0;i < 3;i++){
			new Auto(3,12,Auto.WHITE, 1);
			(new Auto(12,30,Auto.BLUE, 1)).cargar();
			hold(1);
		}
		hold(1);
//		new Auto(3,12,Auto.WHITE, 2);
//		new Auto(12,30,Auto.BLUE, 2);
	}
	
	private void hold(double s){
		try{
			sleep((int)(s*1000));
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
