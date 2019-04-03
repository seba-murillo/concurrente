import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.io.IOException;
import javax.swing.JFrame;

public class Pantalla extends JFrame{

	private static Pantalla screen;
	private static final long serialVersionUID = 1L;

	public static final int screenWidth = 714;
	public static final int screenHeight = 714;
	private static Graphics G;
	private Play game;
	
	public Pantalla(){
		super("Juego");
		screen = this;		
		setBackground(Color.GRAY);
		setLayout(new GridLayout());
		setSize(screenWidth, screenHeight);
		setLocationRelativeTo(null);
		setUndecorated(true);
		setResizable(false);
		setVisible(true);
		G = this.getGraphics();
		Division.loadDivisions();
		crearEsquinas();
		Recorrido.crearRecorridos();
		loadCars();
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new Listener());		
		game = new Play();
		game.start();
	}
	
	public void loadCars(){
		try{
			Auto.loadImages();
		}catch (IOException e){			
			e.printStackTrace();
			error("");
		}
		log(">>> Agregando autos...");
		new Auto(5, 15, Auto.RED, 2);
		log("Jugador creado.");
		new Carloader();		
	}
	
	public void crearEsquinas()
	{
	}
	
	public static Pantalla getScreen(){
		return screen;
	}
	
	public static Graphics getG(){
		return G;
	}

	private static void log(String S){
		Main.log(S);
	}
	
	private static void error(String S){
		Main.error(S);
	}
	
    public static void update(){
    	Division.paintBackground();
    	Auto.pintarAutos();
    }
}