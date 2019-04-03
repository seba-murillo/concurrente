public class ConditionVariable {
	
	private Semaforo mutex;
	private Semaforo condicion;	
	private int blocked;
	
	public ConditionVariable(Semaforo S){		
		mutex = S;
		condicion = new Semaforo(0);
		blocked = 0;
	}

	public void DELAY(){		
		blocked++;
		mutex.Signal();
		condicion.Wait();
		mutex.Wait();
	}

	public void RESUME(){		
		if(blocked > 0){
			blocked--;			
			condicion.Signal();
		}
	}
}