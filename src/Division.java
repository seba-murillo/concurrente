import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Division{

//	Globales de la clase
	public static final int TERRAIN_BUILDING_ID = 1;
	public static final int TERRAIN_WATER_ID = 2;
	public static final int TERRAIN_CORNER_1_ID = 20;
	public static final int TERRAIN_CORNER_2_ID = 21;
	public static final int TERRAIN_CORNER_3_ID = 22;
	public static final int TERRAIN_CORNER_4_ID = 23;	
	public static final int TERRAIN_ROAD_V_1_ID = 30;
	public static final int TERRAIN_ROAD_V_2_ID = 31;
	public static final int TERRAIN_ROAD_H_1_ID = 32;
	public static final int TERRAIN_ROAD_H_2_ID = 33;	
	public static final int TERRAIN_START_ID = 6;
	public static final int TERRAIN_OBJECTIVE_ID = 7;
	public static final int TERRAIN_GRASS_ID = 8;
	
	private static BufferedImage TERRAIN_BUILDING_IMG;
//	private static BufferedImage TERRAIN_WATER_IMG;
	private static BufferedImage TERRAIN_CORNER_1_IMG;
	private static BufferedImage TERRAIN_CORNER_2_IMG;
	private static BufferedImage TERRAIN_CORNER_3_IMG;
	private static BufferedImage TERRAIN_CORNER_4_IMG;
	private static BufferedImage TERRAIN_ROAD_V_1_IMG;
	private static BufferedImage TERRAIN_ROAD_V_2_IMG;
	private static BufferedImage TERRAIN_ROAD_H_1_IMG;
	private static BufferedImage TERRAIN_ROAD_H_2_IMG;
	private static BufferedImage TERRAIN_START_IMG;
	private static BufferedImage TERRAIN_OBJECTIVE_IMG;
	private static BufferedImage TERRAIN_GRASS_IMG;
	private static BufferedImage background;
	
	private static int xDivisions = 0;
	private static int yDivisions = 0;
	private static int width = 0;
	private static int height = 0;
	private static int rowCounter = 0;
	private static int columnCounter = 0;
	
	private static ArrayList<Division> divisiones = new ArrayList<Division>();
	private static int IDcounter = 0;
	
//	 Variables de instacias
	private int row;
	private int column;
	private int ID;
	private int terrainID = 0;
	private BufferedImage image;
	
	private Semaforo ExMutua = new Semaforo(1);
	private ConditionVariable CondVar = new ConditionVariable(ExMutua);
	private boolean ocupado = false;
	private int carID = 0;
	private Rectangle rectangulo;
		
	public Division(int Pixel){
		ID = IDcounter;
		IDcounter++;
		row = rowCounter;
		column = columnCounter;
		rowCounter++;
		if(rowCounter >= xDivisions){
			rowCounter = 0;
			columnCounter++;
		}
		else if(columnCounter >= yDivisions){
			columnCounter = 0;
			error("ERROR @Division(int P) - Fuera de limites - RC: (" + row +","+ column +")    MAX:("+ rowCounter +","+ columnCounter+")");
		}
		setTerrain(Pixel);
		ocupado = false;
		rectangulo = new Rectangle(row*width, column*height, width, height);
		divisiones.add(this);
	}
	
	public static void loadImages() throws IOException{
		TERRAIN_BUILDING_IMG = ImageIO.read(new File(Main.PATH_IMG + "/" + "Build.png"));
//		TERRAIN_WATER_IMG = ImageIO.read(new File(Main.PATH_IMG + "/" + "Water.png"));
		TERRAIN_CORNER_1_IMG = ImageIO.read(new File(Main.PATH_IMG + "/" + "Center1.png"));
		TERRAIN_CORNER_2_IMG = ImageIO.read(new File(Main.PATH_IMG + "/" + "Center2.png"));
		TERRAIN_CORNER_3_IMG = ImageIO.read(new File(Main.PATH_IMG + "/" + "Center3.png"));
		TERRAIN_CORNER_4_IMG = ImageIO.read(new File(Main.PATH_IMG + "/" + "Center4.png"));
		TERRAIN_ROAD_V_1_IMG = ImageIO.read(new File(Main.PATH_IMG + "/" + "RoadV1.png"));
		TERRAIN_ROAD_V_2_IMG = ImageIO.read(new File(Main.PATH_IMG + "/" + "RoadV2.png"));
		TERRAIN_ROAD_H_1_IMG = ImageIO.read(new File(Main.PATH_IMG + "/" + "RoadH1.png"));
		TERRAIN_ROAD_H_2_IMG = ImageIO.read(new File(Main.PATH_IMG + "/" + "RoadH2.png"));
		TERRAIN_START_IMG = ImageIO.read(new File(Main.PATH_IMG + "/" + "null.png"));
		TERRAIN_OBJECTIVE_IMG = ImageIO.read(new File(Main.PATH_IMG + "/" + "null.png"));
		TERRAIN_GRASS_IMG = ImageIO.read(new File(Main.PATH_IMG + "/" + "Grass.png"));
	}
	
	private void setTerrain(int P)
	{
		int RGB[] = pixelToRGB(P);
		if(RGB[0] == 136 && RGB[1] == 0 && RGB[2] == 21){
			terrainID = TERRAIN_ROAD_H_1_ID;
			image = TERRAIN_ROAD_H_1_IMG;
		}
		if(RGB[0] == 153 && RGB[1] == 217 && RGB[2] == 234){
			terrainID = TERRAIN_ROAD_H_2_ID;
			image = TERRAIN_ROAD_H_2_IMG;
		}
		if(RGB[0] == 255 && RGB[1] == 174 && RGB[2] == 201){
			terrainID = TERRAIN_ROAD_V_2_ID;
			image = TERRAIN_ROAD_V_2_IMG;
		}
		if(RGB[0] == 185 && RGB[1] == 122 && RGB[2] == 87){
			terrainID = TERRAIN_ROAD_V_1_ID;
			image = TERRAIN_ROAD_V_1_IMG;
		}
		if(RGB[0] == 204 && RGB[1] == 194 && RGB[2] == 83){
			terrainID = TERRAIN_CORNER_1_ID;
			image = TERRAIN_CORNER_1_IMG;
		}
		if(RGB[0] == 90 && RGB[1] == 130 && RGB[2] == 90){
			terrainID = TERRAIN_CORNER_2_ID;
			image = TERRAIN_CORNER_2_IMG;
		}
		if(RGB[0] == 222 && RGB[1] == 222 && RGB[2] == 222){
			terrainID = TERRAIN_CORNER_3_ID;
			image = TERRAIN_CORNER_3_IMG;
		}
		if(RGB[0] == 63 && RGB[1] == 72 && RGB[2] == 204){
			terrainID = TERRAIN_CORNER_4_ID;
			image = TERRAIN_CORNER_4_IMG;
		}
		if(RGB[0] == 255 && RGB[1] == 0 && RGB[2] == 0){
			terrainID = TERRAIN_START_ID;
			image = TERRAIN_START_IMG;
		}
		if(RGB[0] == 255 && RGB[1] == 255 && RGB[2] == 0){
			terrainID = TERRAIN_OBJECTIVE_ID;
			image = TERRAIN_OBJECTIVE_IMG;
		}
		if(RGB[0] == 0 && RGB[1] == 0 && RGB[2] == 0){
			terrainID = TERRAIN_BUILDING_ID;
			image = TERRAIN_BUILDING_IMG;
		}
		if(RGB[0] == 0 && RGB[1] == 255 && RGB[2] == 0){
			terrainID = TERRAIN_GRASS_ID;
			image = TERRAIN_GRASS_IMG;
		}
		//----------------------------------------------------------------------------
		if(RGB[0] == 50 && RGB[1] == 200 && RGB[2] == 200){ // Fabrica_start
			terrainID = TERRAIN_CORNER_1_ID;
			image = TERRAIN_ROAD_V_2_IMG;
		}
		if(RGB[0] == 200 && RGB[1] == 50 && RGB[2] == 200){ // Deposito_start
			terrainID = TERRAIN_CORNER_1_ID;
			image = TERRAIN_ROAD_H_2_IMG;
		}
		if(RGB[0] == 200 && RGB[1] == 200 && RGB[2] == 50){ // Mercado_start
			terrainID = TERRAIN_CORNER_1_ID;
			image = TERRAIN_ROAD_V_1_IMG;
		}
		//----------------------------------------------------------------------------
		if(terrainID == 0 || image == null){
			error("@setTerrain("+P+") - terrainID OR image NULL. ("+terrainID+","+image+")");
			return;
		}
	}
	
	private static int[] pixelToRGB(int P){
    	int RGB[] = new int[3];
    	RGB[0] = (P >> 16) & 0xFF;
    	RGB[1] = (P >> 8) & 0xFF;
    	RGB[2] = P & 0xFF;
    	return RGB;
    }

	public static int[] getSize(){
		if(width == 0 || height == 0) error("@getSize - width OR height null.");
		int R[] = {width,height};
		return R;
	}
	
	public static int[] getTotalDivisions(){
		int R[] = {xDivisions, yDivisions};
		return R;
	}
	
	public static int getTerreno(int x, int y){		
		if(divisiones.isEmpty()){
			error("@getTerreno: Empty list.");
			return 0;
		}
		Division R = null;
		int xx = x/width;
		int yy = y/height;
		if(xx < 0 ){xx = 0;System.err.println("WARNING: @Division - getTerreno: xx < 0");}
		if(yy < 0 ){yy = 0;System.err.println("WARNING: @Division - getTerreno: yy < 0");}
		if(xx > 33 ){xx = 33;System.err.println("WARNING: @Division - getTerreno: xx > 33");}
		if(yy > 33 ){yy = 33;System.err.println("WARNING: @Division - getTerreno: yy > 33");}		
		R = divisiones.get((yy*33)+xx+yy);
		if(R == null){
			error("@getTerreno: invalid division.");
			return 0;
		}
		return R.terrainID;
	}
	
	public static Division getDivision(int x, int y){
		if(divisiones.isEmpty()){
			error("@getTileFromXY: Empty list.");
			return null;
		}
		Division R = null;
		int xx = x/width;
		int yy = y/height;
		if(xx < 0 ){xx = 0;System.err.println("WARNING: @Division - getDiv: xx < 0");}
		if(yy < 0 ){yy = 0;System.err.println("WARNING: @Division - getDiv: yy < 0");}
		if(xx > 33 ){xx = 33;System.err.println("WARNING: @Division - getDiv: xx > 33");}
		if(yy > 33 ){yy = 33;System.err.println("WARNING: @Division - getDiv: yy > 33");}
		R = divisiones.get((yy*33)+xx+yy);
//		log("@getDiv:    xx: "+xx+"    yy: "+yy+"    (yy*33)+xx+yy: "+((yy*33)+xx+yy));
		if(R == null){
			error("@getDiv: invalid division.");
			return null;
		}
		return R;
	}
	
	public static Division getDivision(int id){
		if(divisiones.isEmpty()){
			error("@getTileFromXY: Empty list.");
			return null;
		}
		Division R = divisiones.get(id);
		if(R == null){
			error("@getDiv: invalid division.");
			return null;
		}
		return R;
	}
	
	public static int getID(int x, int y){
		return getDivision(x,y).getID();
	}
	
	public static void CreateBackground(int screenX, int screenY) {
		background = new BufferedImage(screenX,screenY, BufferedImage.TYPE_INT_RGB);
		Graphics G = background.createGraphics();
		for(Division div : divisiones)
		{
			G.drawImage(div.image, div.row*width, div.column*height, width, height, null, null);
		}
	    File outputfile = new File("background.png");
	    try{ImageIO.write(background, "png", outputfile);} catch (IOException e){e.printStackTrace();}
	    paintBackground();
	}
	
	public static void paintBackground(){
		Pantalla.getG().drawImage(background, 0, 0, background.getWidth(), background.getHeight(), null, null);
	}

	public static void log(String S){
		Main.log(S);
	}
	
	public static void error(String S){
		Main.error(S);
	}
	
	public int getID(){
		return ID;
	}
	
	public int getCar(){
		return carID;
	}
	
	public int getTerreno(){
		return terrainID;
	}
	
	public int[] getRC(){
		int R[] = {row,column};
		return R;
	}
	
	public synchronized void setOcupado(boolean ocupado){
		this.ocupado = ocupado;
	}
	
	public boolean isOcupado(){
		return ocupado;
	}
	
	public void entrar(){
		ExMutua.Wait();
		Auto A = (Auto) Thread.currentThread();
		int autoID = A.getID();
		if(autoID == carID){
			ExMutua.Signal();
			return;
		}
		while(isOcupado() && carID != autoID){			
			if(Auto.checkCarPosition(carID) != ID){// doble chequeo antes de DELAY
				ExMutua.Signal();
				((Auto) Thread.currentThread()).report("DELAY");
				return;
			}
			CondVar.DELAY();
		}
//		log(A.getName() + " entrando en Division " + ID);
		ocupado = true;
		carID = autoID;
		A.dejarDivisionActual();
		ExMutua.Signal();
    }
	
	public void salir(){		
		ExMutua.Wait();
//		Auto A = (Auto) Thread.currentThread();
//		if(A.getID() != carID) System.err.println("WARNING: dejando divion de la  que no pertenece");
//		log(A.getName() + " dejando Division " + ID);
		ocupado = false;
		carID = -1;
		CondVar.RESUME();
		ExMutua.Signal();
    }
	
	public boolean isDrivable(){
		if(terrainID == TERRAIN_CORNER_1_ID || terrainID == TERRAIN_CORNER_2_ID || terrainID == TERRAIN_CORNER_3_ID || terrainID == TERRAIN_CORNER_4_ID){
			return true;
		}
		if(terrainID == TERRAIN_ROAD_V_1_ID || terrainID == TERRAIN_ROAD_V_2_ID || terrainID == TERRAIN_ROAD_H_1_ID || terrainID == TERRAIN_ROAD_H_2_ID){
			return true;
		}
		if(terrainID == TERRAIN_START_ID || terrainID == TERRAIN_OBJECTIVE_ID || terrainID == TERRAIN_GRASS_ID){
			return true;
		}
		return false;
	}

	public static void loadDivisions(){
		File img = new File("GRID.bmp");
		BufferedImage image = null;
		try{
			image = ImageIO.read(img);
		}catch (IOException e){
			error("ERROR: @loadDivisions - imagen invalida");
			e.printStackTrace();			
		}
		if(image == null) System.err.println("ERROR: @loadDivisions - imagen invalida");
		
		xDivisions = image.getWidth();
		yDivisions = image.getHeight();
		width = Pantalla.screenWidth/xDivisions;
		height = Pantalla.screenHeight/yDivisions;
		try{
			Division.loadImages();
		}catch (IOException e) {
			error("ERROR: @loadDivisions - @loadImages");
			e.printStackTrace();
		}
		divisiones.clear();
		for(int y = 0;y < yDivisions;y++){
			for(int x = 0;x < xDivisions;x++){
				new Division(image.getRGB(x, y));
			}
		}
		Division.CreateBackground(Pantalla.screenWidth, Pantalla.screenHeight);
		log("@setAmount - pantalla: "+ Pantalla.screenWidth +"x"+ Pantalla.screenHeight +"    divisiones: "+ xDivisions +"x"+ yDivisions +"    cada division: "+ width +"x"+ height);
		log("@loadDivisions - GRID cargada - width: "+xDivisions+"    height: "+yDivisions+"    divisiones.size(): "+divisiones.size());
	}

	public static boolean checkCollision(){
		Auto player = (Auto) Thread.currentThread();
		
		int pos = player.getDivisionID(true);
		int[] R = Division.getTotalDivisions();
		Division ESTE = divisiones.get(pos + 1);
		Division OESTE = divisiones.get(pos - 1);
		Division SUR = divisiones.get(pos + R[0]);
		Division NORTE = divisiones.get(pos - R[0]);
		
		if(player.getPrecaucion().intersects(ESTE.getRectangle()) && !ESTE.isDrivable()) return true;
		if(player.getPrecaucion().intersects(OESTE.getRectangle()) && !OESTE.isDrivable()) return true;
		if(player.getPrecaucion().intersects(SUR.getRectangle()) && !SUR.isDrivable()) return true;
		if(player.getPrecaucion().intersects(NORTE.getRectangle()) && !NORTE.isDrivable()) return true;
		
//		for(Division D : divisiones){
//			if(player.getPrecaucion().intersects(D.getRectangle()) && !D.isDrivable()) return true;
//		}
		
		/* NOTA: Se eligen las 4 divisiones cardenales para combropar porque haciendo el loop completo sobre el arraylist 'divisiones' laguea mucho */
		return false;
	}

	private Rectangle getRectangle(){
		return rectangulo;
	}
}