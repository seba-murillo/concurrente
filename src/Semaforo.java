public class Semaforo {
	
private int V;
	
	public Semaforo(int i){
		V = i;
	}

	public synchronized void Wait(){
		
		while(V < 1){
			try{
				wait();
			} catch (InterruptedException e){
				e.printStackTrace();
			}
		}		
		V--;
	}
	
	public synchronized void Signal(){
		V++;
		notify();
	}
}