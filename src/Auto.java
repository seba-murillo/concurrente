import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;


public class Auto extends Thread{

	//Globales de la clase
	private static BufferedImage CAR_RED_IMG;
	private static BufferedImage CAR_BLUE_IMG;
	private static BufferedImage CAR_WHITE_IMG;
	private static BufferedImage CAR_RED_CARGADO_IMG;
	private static BufferedImage CAR_BLUE_CARGADO_IMG;
	private static BufferedImage CAR_WHITE_CARGADO_IMG;
	private static final int DISTANCIA_CHECKEO = 5;
	private static int IDcounter = 0;
	public static final int RED = 1;
	public static final int BLUE = 2;
	public static final int WHITE = 3;
	public static final int ESTE = 1;
	public static final int NORTE = 2;
	public static final int OESTE = 3;
	public static final int SUR = 4;
	public static int RED_CAR_WIDTH = 18;
	public static int RED_CAR_HEIGHT = 14;
	public static int BLUE_CAR_WIDTH = 18;
	public static int BLUE_CAR_HEIGHT = 14;
	public static int WHITE_CAR_WIDTH = 18;
	public static int WHITE_CAR_HEIGHT = 14;
	private static Auto player;
	public static boolean player_moving = false;
	private static ConcurrentHashMap<Integer, Auto> cars = new ConcurrentHashMap<Integer, Auto>();	
	
	// Variables de instacias
	private int ID;
	private int color;
	private BufferedImage image = null;
	private int sentido;
	/*
	 * 0 - invalido
	 * 1 - este
	 * 2 - norte
	 * 3 - oeste
	 * 4 - sur
	 */
	private int width;
	private int height;
	private float X;
	private float Y;
	private double VELOCIDAD = 1;
	private Division divisionActual;
	private Division divisionProxima;
	private Recorrido recorrido;
	private boolean cargado = false;
	private Rectangle rect_cuerpo;
	private Rectangle rect_precaucion;
	
	
	public Auto(int divX, int divY, int color, double V){		
		ID = IDcounter;
		IDcounter++;		
		this.setName("Auto " + ID);
		this.color = color;
		VELOCIDAD = V;		
		
		if(color == RED){
			player = this;
			image = CAR_RED_IMG;
			width = RED_CAR_WIDTH;
			height = RED_CAR_HEIGHT;
		}
		else if(color == BLUE){
			image = CAR_BLUE_IMG;
			width = BLUE_CAR_WIDTH;
			height = BLUE_CAR_HEIGHT;
		}
		else if(color == WHITE){
			image = CAR_WHITE_IMG;
			width = WHITE_CAR_WIDTH;
			height = WHITE_CAR_HEIGHT;
		}
		else{
			error("ERROR @AutoConstructor: color desconocido.");
		}		
		if(width == 0 || height == 0) error("ERROR @AutoConstructor: width o height invalido");
		
		int S[] = Division.getSize();
		int s = 0;
		int terreno = Division.getTerreno(divX*S[0] + S[0]/2, divY*S[1] + S[1]/2);
		if(terreno == Division.TERRAIN_ROAD_V_1_ID) s = NORTE;
		if(terreno == Division.TERRAIN_ROAD_V_2_ID) s = SUR;
		if(terreno == Division.TERRAIN_ROAD_H_1_ID) s = OESTE;
		if(terreno == Division.TERRAIN_ROAD_H_2_ID) s = ESTE;
		setSentido(s);
		
		X = S[0]*divX + (S[0]/2 - width/2);
		Y = S[1]*divY + (S[1]/2 - height/2);
//		log("Auto creado: ID: "+ID+"    X: "+X+"    Y: "+Y+"    S[0]: "+S[0]+"    S[1]: "+S[1]+"    : "+S[0]);
		rect_cuerpo = new Rectangle((int) X, (int) Y, width, height);
		divisionActual = Division.getDivision((int) X, (int) Y);
		
		if(color != RED){			
			setRecorrido();
			log("Auto " + ID + " creado.");
		}
		addCar(this);
		paint();
	}
	
	@Override
	public void run(){		
		while(true){
			mover();
			try{sleep(Main.DELAY);}catch(InterruptedException e){e.printStackTrace();}
		}
	}
	
	public int getColor(){return color;}
	
	public int getX(){
		return (int) X;
	}
	
	public int getY(){
		return (int) Y;
	}
	
	public int getDivisionID(boolean actual){
		if(actual){
			return divisionActual.getID();
		}
		else{
			return divisionProxima.getID();
		}
	}
	
	public void mover(){
		if(this == player){
			if(!player_moving) return;
			VELOCIDAD = 2;
			int terreno = divisionActual.getTerreno();
			if(terreno == Division.TERRAIN_GRASS_ID){
				VELOCIDAD = 1;
			}				
		}
		if(recorrido == null) setRecorrido();
		if(checkCollision()){
			if(!(player.isCargado()) && this.isCargado()){
				player.cargar();
				descargar();
			}
			return;
		}
		switch (sentido){
			case ESTE:{
				X += VELOCIDAD;
				break;
			}
			case NORTE:{
				Y -= VELOCIDAD;
				break;
			}
			case OESTE:{
				X -= VELOCIDAD;
				break;
			}
			case SUR:{
				Y += VELOCIDAD;
				break;
			}
			default:{
				log("@mover - SENTIDO INVALIDO - ID: "+ID+"    sentido: "+sentido+"    isPlayer: "+player);
				break;
			}
		}
		if(this != player) recorrido.getNextSentido();
	}
	
	private boolean checkCollision(){
		if(this == player){
			if(Division.checkCollision()) return true;
		}
		for(int C = 0; C < cars.size();C++){
			if(C == ID) continue;
			if(rect_precaucion.intersects(cars.get(C).getCuerpo())){
//				log("Collision: ID: "+ID+" embistio a ID "+C);
				return true;
			}
		}
		return false;
	}

	private Rectangle getCuerpo(){		
		return rect_cuerpo;
	}
	
	public Rectangle getPrecaucion(){		
		return rect_precaucion;
	}

	public void dejarDivisionActual(){
		divisionActual.salir();
		divisionActual = divisionProxima;
	}
	
	//***** HACER ESTAS FUNCIONES *****//	
	public void escapado(){
		System.err.println("WARNNING: Auto "+ID+" ha escapado de la ciudad.");
	}
	
	public void chocar() {
	}

	public void destruir() {
	}
	
	public void depositarMercancia() {
	}
	//*********************************//
	public void paint(){		
//		log("@paint Auto - ID: "+ID+"    X: "+(int) X+"    Y: "+(int)Y+"    DivID: "+Division.getID((int) X, (int) Y)+"    width: "+width+"    height: "+height);		
		divisionActual = Division.getDivision((int) X, (int) Y);
		if(sentido == 0){
			//int terrain = Division.getTerreno((int) X, (int) Y);
			int terrain = divisionActual.getTerreno();
			if(terrain == Division.TERRAIN_CORNER_1_ID){
				sentido = OESTE;
			}
			if(terrain == Division.TERRAIN_CORNER_2_ID){
				sentido = SUR;
				rotarIzquierda();
			}
			if(terrain == Division.TERRAIN_CORNER_3_ID){
				sentido = ESTE;
				rotarDerecha();
				rotarDerecha();
			}
			if(terrain == Division.TERRAIN_CORNER_4_ID){
				sentido = NORTE;
				rotarDerecha();
			}
			if(terrain == Division.TERRAIN_ROAD_V_1_ID){
				sentido = NORTE;
				rotarDerecha();
			}
			if(terrain == Division.TERRAIN_ROAD_V_2_ID){
				sentido = SUR;
				rotarIzquierda();
			}
			if(terrain == Division.TERRAIN_ROAD_H_1_ID){
				sentido = ESTE;
				rotarDerecha();
				rotarDerecha();
			}
			if(terrain == Division.TERRAIN_ROAD_H_2_ID){
				sentido = OESTE;
			}
			if(sentido == 0) error("@paint Auto - terreno invalido. Terreno: "+terrain);
		}
		if(image == null) error("@paint Auto - image NULL.");

		if(this != player) center(); // Correccion para centrar en DIV
		updateRectangle();
        Pantalla.getG().drawImage(image, (int) X, (int) Y, width, height, null, null);
//        Pantalla.getG().fillRect((int) rect_precaucion.getX(), (int)  rect_precaucion.getY(), (int) rect_precaucion.getWidth(), (int) rect_precaucion.getHeight());
    }
	
	private void center(){
		divisionActual = Division.getDivision((int) X, (int) Y);
		int RC[] = divisionActual.getRC();
		int XY[] = Division.getSize();
		float cx = 0;
		float cy = 0;
		if(XY[0]%2 != 0) cx = XY[0]+2;
		if(XY[1]%2 != 0) cy = XY[1]+2;
		if(sentido == NORTE || sentido == SUR) X = XY[0] * RC[0] + (cx - width)/2;
		if(sentido == ESTE || sentido == OESTE) Y = XY[1] * RC[1] + (cy - height)/2;
	}
	
	private void updateRectangle(){
//		log("@updateRectangle. sentido: "+sentido);
		rect_cuerpo.setLocation((int) X, (int) Y);
		rect_cuerpo.setSize(width, height);
		if(rect_precaucion == null) rect_precaucion = new Rectangle();
		switch(sentido){
			case ESTE:{
//				log("@updateRectangle. sentido: ESTE");
				rect_precaucion.setLocation((int) X + width, (int) Y + 0);
				rect_precaucion.setSize(DISTANCIA_CHECKEO, height);
				break;
			}
			case OESTE:{
//				log("@updateRectangle. sentido: OESTE");
				rect_precaucion.setLocation((int) X - DISTANCIA_CHECKEO, (int) Y + 0);
				rect_precaucion.setSize(DISTANCIA_CHECKEO, height);
				break;
			}
			case NORTE:{
//				log("@updateRectangle. sentido: NORTE");
				rect_precaucion.setLocation((int) X + 0, (int) Y - DISTANCIA_CHECKEO);
				rect_precaucion.setSize(width, DISTANCIA_CHECKEO);
				break;
			}			
			case SUR:{
//				log("@updateRectangle. sentido: SUR");
				rect_precaucion.setLocation((int) X + 0, (int) Y + height);
				rect_precaucion.setSize(width, DISTANCIA_CHECKEO);
				break;
			}
		}
	}
	
	public void setSentido(int S){
		if(sentido == 0) sentido = OESTE;
		switch(sentido){
			case ESTE:{
				switch(S){
					case ESTE:{
						break;
					}
					case NORTE:{
						rotarIzquierda();
						break;
					}
					case OESTE:{
						rotarDerecha();
						rotarDerecha();
						break;
					}
					case SUR:{
						rotarDerecha();
						break;
					}
				}
				break;
			}
			case NORTE:{
				switch(S){
					case ESTE:{
						rotarDerecha();
						break;
					}
					case NORTE:{
						break;
					}
					case OESTE:{
						rotarIzquierda();
						break;
					}
					case SUR:{
						rotarDerecha();
						rotarDerecha();
						break;
					}
				}
				break;
			}
			case OESTE:{
				switch(S){
					case ESTE:{
						rotarDerecha();
						rotarDerecha();
						break;
					}
					case NORTE:{
						rotarDerecha();
						break;
					}
					case OESTE:{
						break;
					}
					case SUR:{
						rotarIzquierda();
						break;
					}
				}
				break;
			}
			case SUR:{
				switch(S){
					case ESTE:{
						rotarIzquierda();
						break;
					}
					case NORTE:{
						rotarDerecha();
						rotarDerecha();
						break;
					}
					case OESTE:{
						rotarDerecha();
						break;
					}
					case SUR:{
						break;
					}
				}
				break;				
			}
		}
		sentido = S;
	}
           
    private void rotarDerecha(){
//		log("@rotarDerecha - ID: "+ID+"    width: "+width+"    height: "+height);
    	int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage rotado = new BufferedImage(h, w, image.getType());
        for(int i = 0;i < w;i++){
            for(int j = 0;j < h;j++){
//            	log("i: "+i+"    j: "+j);
            	rotado.setRGB(h-1-j, i, image.getRGB(i,j) );
            }
        }
        int tmp = width;
        width = height;
        height = tmp;
        image = rotado;
    }
    
    private void rotarIzquierda(){
//    	log("@rotarIzquierda - ID: "+ID+"    width: "+width+"    height: "+height);
    	int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage rotado = new BufferedImage(h, w, image.getType());
        for(int i = 0;i < w;i++){
            for(int j = 0;j < h;j++){
//            	log("i: "+i+"    j: "+j);
            	rotado.setRGB(j, w-1-i, image.getRGB(i,j) );
            }
        }
        int tmp = width;
        width = height;
        height = tmp;
        image = rotado;
    }
    
    public boolean isOutOfBounds(){
    	if(X < 0 || X > Pantalla.screenWidth || Y < 0 || Y > Pantalla.screenHeight) return true;
    	return false;
	}
    
    public int getPosDivID(){
		return (Division.getDivision((int) X, (int) Y)).getID();
    }
    
    public static void loadImages() throws IOException{
    	CAR_RED_IMG = ImageIO.read(new File(Main.PATH_IMG + "/" + "car_player_red.png"));
    	CAR_BLUE_IMG = ImageIO.read(new File(Main.PATH_IMG + "/" + "car_ia_blue.png"));
    	CAR_WHITE_IMG = ImageIO.read(new File(Main.PATH_IMG + "/" + "car_ia_white.png"));
    	CAR_RED_CARGADO_IMG = ImageIO.read(new File(Main.PATH_IMG + "/" + "car_player_red_cargado.png"));
    	CAR_BLUE_CARGADO_IMG = ImageIO.read(new File(Main.PATH_IMG + "/" + "car_ia_blue_cargado.png"));
    	File F = new File(Main.PATH_IMG + "/" + "car_ia_blue_cargado.png");
    	CAR_WHITE_CARGADO_IMG = ImageIO.read(F);
	}
    
    public void setRecorrido(){
    	if(this == player) return;
    	recorrido = new Recorrido(this);
    	recorrido.getNextSentido();
    }
    
    public int getID(){
    	return ID;
    }
    
    private void reloadImage(){
    	if(color == RED){			
			width = RED_CAR_WIDTH;
			height = RED_CAR_HEIGHT;
		}
		else if(color == BLUE){
			width = BLUE_CAR_WIDTH;
			height = BLUE_CAR_HEIGHT;
		}
		else if(color == WHITE){
			width = WHITE_CAR_WIDTH;
			height = WHITE_CAR_HEIGHT;
		}
    	int tmp = sentido;
		sentido = 0;
		setSentido(tmp);
    }
    
    public boolean cargar(){
    	if(isCargado()) return false;
		cargado = true;
		if(color == RED) image = CAR_RED_CARGADO_IMG;
		if(color == BLUE) image = CAR_BLUE_CARGADO_IMG;
		if(color == WHITE) image = CAR_WHITE_CARGADO_IMG;
		reloadImage();
		return true;		
    }
    
    public boolean descargar(){
    	if(!isCargado()) return false;
		cargado = false;
		if(color == RED) image = CAR_RED_IMG;
		if(color == BLUE) image = CAR_BLUE_IMG;
		if(color == WHITE) image = CAR_WHITE_IMG;
		reloadImage();
		return true;
    }
    
    public boolean isCargado(){
    	return cargado;
    }

	public static void log(String S){
		Main.log(S);
	}
	
	public static void error(String S){
		Main.error(S);
	}
	
	public static void addCar(Auto C){
    	cars.put(C.getID(), C);
    	C.start();
    }

	public synchronized void report(String S) {
		log("\n\n");
		if(S == "DELAY"){
			try{
			log("Auto " + ID + " DELAYED.\ndivisionActual: "+divisionActual.getID()+"    divisionProxima: "+divisionProxima.getID());
			log("En divisionActual "+divisionActual.getID()+"    carID: "+divisionActual.getCar());
			log("En divisionProxima "+divisionProxima.getID()+"    carID: "+divisionActual.getCar());
			log("divisionActual en el autoID que esta en divisionActual de ID: "+divisionActual.getCar()+":  "+cars.get((divisionActual.getCar())).divisionActual.getID());
			log("divisionProxima en el autoID que esta en divisionActual de ID: "+divisionActual.getCar()+":  "+cars.get((divisionActual.getCar())).divisionProxima.getID());
			log("divisionActual en el autoID que esta en divisionProxima de ID: "+divisionProxima.getCar()+":  "+cars.get((divisionProxima.getCar())).divisionActual.getID());
			log("divisionProxima en el autoID que esta en divisionProxima de ID: "+divisionProxima.getCar()+":  "+cars.get((divisionProxima.getCar())).divisionProxima.getID());
			}catch(Exception E){}
		}
		if(S == "RESUMED"){
			try{
			log("Auto " + ID + " RESUMED.\ndivisionActual: "+divisionActual.getID()+"    divisionProxima: "+divisionProxima.getID());
			}catch(Exception E){}
		}
	}

	public static void pintarAutos() {
		for(int C = 0; C < cars.size();C++){
    		Auto A = cars.get(C);
    		if(A != null) A.paint();
    		else{
    			log("\n\nLista de Autos:\n " + cars.toString() + "\n");
    			error("@Auto.pintarAutos() - Auto null:  C: " + C);
    		}
//    		(cars.get(C)).paint();
    		/*// try/catch laguea demasiado
    		try{
    			Auto A = cars.get(C);
//    			log("@update() - C: " + C + "    cars.get(C).getName(): "+A.getName()+"    ID: "+A.getID()+"    color: "+A.getColor());
    			A.paint();
    		}catch(Exception E){
    			E.printStackTrace();
    			error("");
    		}
    		*/
		}		
	}

	public static int checkCarPosition(int C){
		Auto A = null;
		try{
			A = cars.get(C);
		}catch(Exception e){
			if(Main.debug){
				e.printStackTrace();
			}
		}
		return A.getDivisionID(true);
	}

	public static Auto getPlayer() {
		return player;
	}
}