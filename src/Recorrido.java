import java.util.ArrayList;
import java.util.Random;

public class Recorrido{
	
	private static ArrayList<Integer> A = new ArrayList<Integer>();
	private static ArrayList<Integer> B = new ArrayList<Integer>();
	private static ArrayList<Integer> C = new ArrayList<Integer>();
	private static ArrayList<Integer> D = new ArrayList<Integer>();
	private static ArrayList<Integer> E = new ArrayList<Integer>();
	private static ArrayList<Integer> F = new ArrayList<Integer>();
	private static ArrayList<Integer> G = new ArrayList<Integer>();
	private static ArrayList<Integer> H = new ArrayList<Integer>();
	private Auto car;
	private ArrayList<Integer> L;
	/*
	 * FABRICA => DEPOSITO: A y B (Auto Blanco)
	 * DEPOSITO => FABRICA: C y D (Auto Blanco)
	 * SUPERMERCADO => DEPOSITO: E y F (Auto Azul)
	 * DEPOSITO => SUPERMERCADO: G y H (Auto Azul)
	 */
	public Recorrido(Auto c){
		car = c;
		int color = c.getColor();
		int pos = c.getPosDivID();
		Random R = new Random();		
		int r = R.nextInt(2);				
		
		if(color == Auto.WHITE){ // Fabrica <-> Deposito
			if(pos == A.get(A.size()-1)){ // Si esta en el final de A o B...
				car.setSentido(Auto.ESTE);
				if(r == 0){
					L = C;
//					log(car.getName()+" ha obtenido un nuevo recorrido. Recorrido: C");
				}
				else{
					L = D;
//					log(car.getName()+" ha obtenido un nuevo recorrido. Recorrido: D");
				}
			}
			else if(pos == C.get(C.size()-1)){ // Si esta en el final de C o D...
				car.setSentido(Auto.SUR);
				if(r == 0){
					L = A;
//					log(car.getName()+" ha obtenido un nuevo recorrido. Recorrido: A");
				}
				else{
					L = B;
//					log(car.getName()+" ha obtenido un nuevo recorrido. Recorrido: B");
				}
			}
			else{
				error("@newRecorrido: posicion desconocida.    carID: "+c.getID()+"  pos: "+pos+"    color: WHITE    ID: "+c.getID());
			}
		}
		else if(color == Auto.BLUE){ // Deposito <-> Mercado			
			if(pos == E.get(E.size()-1)){ // Si esta en el final de E o F...
				car.setSentido(Auto.ESTE);
				if(r == 0){
					L = G;
//					log(car.getName()+" ha obtenido un nuevo recorrido. Recorrido: G");
				}
				else{
					L = H;
//					log(car.getName()+" ha obtenido un nuevo recorrido. Recorrido: H");
				}
			}
			else if(pos == G.get(G.size()-1)){ // Si esta en el final de G o H...
				car.setSentido(Auto.NORTE);
				if(r == 0){
					L = E;
//					log(car.getName()+" ha obtenido un nuevo recorrido. Recorrido: E");
				}
				else{
					L = F;
//					log(car.getName()+" ha obtenido un nuevo recorrido. Recorrido: F");
				}
			}
			else{
				error("@newRecorrido: posicion desconocida.    pos: "+pos+"    color: BLUE    ID: "+c.getID());
			}
		}
		else if(color == Auto.RED) return;
		else{
			error("@newRecorrido: COLOR desconocido.    color: "+color);
		}
	}
	
	public void getNextSentido(){
		int pos = car.getPosDivID();
		int nextPos;
		try{
			nextPos = L.get(L.indexOf(pos)+1);
		}
		catch(IndexOutOfBoundsException e){
//			log(car.getName()+" obteniendo nuevo recorrido.    color: "+car.getColor()+"    pos: "+pos);
			car.setRecorrido();
			return;
		}
//		log("@getNextSentido:  pos: "+pos+"    nextPos: "+nextPos);
		
		int[] R = Division.getTotalDivisions();
		
		if(nextPos == pos + 1) car.setSentido(Auto.ESTE);
		if(nextPos == pos - 1) car.setSentido(Auto.OESTE);
		if(nextPos == pos + R[0]) car.setSentido(Auto.SUR);
		if(nextPos == pos - R[0]) car.setSentido(Auto.NORTE);
	}
	
	public int getNextDivisionID(){
		int pos = car.getPosDivID();
		int index = L.indexOf(pos);		
		return L.get(index + 1);
	}
	
//	public int[] getPath(){
//		int R[] = {0,0,0};
//		int pos = car.getPosDivID();
//		if(!L.contains(pos)) error("@getPath: posicion desconocida.");
//		int index = L.indexOf(pos);
//		try{R[0] = L.get(index+1);}catch(IndexOutOfBoundsException e){R[0] = 0;}
//		try{R[1] = L.get(index+2);}catch(IndexOutOfBoundsException e){R[1] = 0;}
//		try{R[2] = L.get(index+3);}catch(IndexOutOfBoundsException e){R[2] = 0;}
//		return R;
//	}
	
	public static void crearRecorridos(){
		A.add(445);
		A.add(446);
		A.add(480);
		A.add(514);
		A.add(548);
		A.add(582);
		A.add(616);
		A.add(650);
		A.add(684);
		A.add(718);
		A.add(752);
		A.add(786);
		A.add(820);
		A.add(854);
		A.add(888);
		A.add(922);
		A.add(956);
		A.add(990);
		A.add(991);
		A.add(992);
		A.add(993);
		A.add(994);
		A.add(995);
		A.add(996);
		A.add(997);
		A.add(998);
		A.add(1032);
		B.add(445);
		B.add(446);
		B.add(447);
		B.add(448);
		B.add(449);
		B.add(450);
		B.add(451);
		B.add(452);
		B.add(453);
		B.add(454);
		B.add(488);
		B.add(522);
		B.add(556);
		B.add(590);
		B.add(624);
		B.add(658);
		B.add(692);
		B.add(726);
		B.add(727);
		B.add(728);
		B.add(729);
		B.add(730);
		B.add(731);
		B.add(732);
		B.add(733);
		B.add(734);
		B.add(768);
		B.add(802);
		B.add(836);
		B.add(870);
		B.add(904);
		B.add(938);
		B.add(972);
		B.add(971);
		B.add(970);
		B.add(969);
		B.add(968);
		B.add(967);
		B.add(966);
		B.add(965);
		B.add(964);
		B.add(998);
		B.add(1032);
		C.add(1033);
		C.add(999);
		C.add(965);
		C.add(964);
		C.add(963);
		C.add(962);
		C.add(961);
		C.add(960);
		C.add(959);
		C.add(958);
		C.add(957);
		C.add(923);
		C.add(889);
		C.add(855);
		C.add(821);
		C.add(787);
		C.add(753);
		C.add(719);
		C.add(685);
		C.add(651);
		C.add(617);
		C.add(583);
		C.add(549);
		C.add(515);
		C.add(481);
		C.add(447);
		C.add(413);
		C.add(412);
		C.add(411);
		D.add(1033);
		D.add(999);
		D.add(1000);
		D.add(1001);
		D.add(1002);
		D.add(1003);
		D.add(1004);
		D.add(1005);
		D.add(1006);
		D.add(1007);
		D.add(973);
		D.add(939);
		D.add(905);
		D.add(871);
		D.add(837);
		D.add(803);
		D.add(769);
		D.add(735);
		D.add(701);
		D.add(700);
		D.add(699);
		D.add(698);
		D.add(697);
		D.add(696);
		D.add(695);
		D.add(694);
		D.add(693);
		D.add(659);
		D.add(625);
		D.add(591);
		D.add(557);
		D.add(523);
		D.add(489);
		D.add(455);
		D.add(421);
		D.add(420);
		D.add(419);
		D.add(418);
		D.add(417);
		D.add(416);
		D.add(415);
		D.add(414);
		D.add(413);
		D.add(412);
		D.add(411);		
		E.add(166);
		E.add(165);
		E.add(164);
		E.add(198);
		E.add(232);
		E.add(266);
		E.add(300);
		E.add(334);
		E.add(368);
		E.add(402);
		E.add(436);
		E.add(435);
		E.add(434);
		E.add(433);
		E.add(432);
		E.add(431);
		E.add(430);
		E.add(429);
		E.add(428);
		E.add(462);
		E.add(496);
		E.add(530);
		E.add(564);
		E.add(598);
		E.add(632);
		E.add(666);
		E.add(700);
		E.add(734);
		E.add(768);
		E.add(802);
		E.add(836);
		E.add(870);
		E.add(904);
		E.add(938);
		E.add(972);
		E.add(971);
		E.add(970);
		E.add(969);
		E.add(968);
		E.add(967);
		E.add(966);
		E.add(965);
		E.add(964);
		E.add(998);
		E.add(1032);
		F.add(166);
		F.add(165);
		F.add(164);
		F.add(198);
		F.add(232);
		F.add(266);
		F.add(300);
		F.add(334);
		F.add(368);
		F.add(402);
		F.add(436);
		F.add(470);
		F.add(504);
		F.add(538);
		F.add(572);
		F.add(606);
		F.add(640);
		F.add(674);
		F.add(708);
		F.add(742);
		F.add(776);
		F.add(810);
		F.add(844);
		F.add(878);
		F.add(912);
		F.add(946);
		F.add(980);
		F.add(979);
		F.add(978);
		F.add(977);
		F.add(976);
		F.add(975);
		F.add(974);
		F.add(973);
		F.add(972);
		F.add(971);
		F.add(970);
		F.add(969);
		F.add(968);
		F.add(967);
		F.add(966);
		F.add(965);
		F.add(964);
		F.add(998);
		F.add(1032);
		G.add(1033);
		G.add(999);
		G.add(1000);
		G.add(1001);
		G.add(1002);
		G.add(1003);
		G.add(1004);
		G.add(1005);
		G.add(1006);
		G.add(1007);
		G.add(1008);
		G.add(1009);
		G.add(1010);
		G.add(1011);
		G.add(1012);
		G.add(1013);
		G.add(1014);
		G.add(1015);
		G.add(981);
		G.add(947);
		G.add(913);
		G.add(879);
		G.add(845);
		G.add(811);
		G.add(777);
		G.add(743);
		G.add(709);
		G.add(675);
		G.add(641);
		G.add(607);
		G.add(573);
		G.add(539);
		G.add(505);
		G.add(471);
		G.add(437);
		G.add(403);
		G.add(369);
		G.add(335);
		G.add(301);
		G.add(267);
		G.add(233);
		G.add(199);
		G.add(200);
		H.add(1033);
		H.add(999);
		H.add(1000);
		H.add(1001);
		H.add(1002);
		H.add(1003);
		H.add(1004);
		H.add(1005);
		H.add(1006);
		H.add(1007);
		H.add(973);
		H.add(939);
		H.add(905);
		H.add(871);
		H.add(837);
		H.add(803);
		H.add(769);
		H.add(735);
		H.add(701);
		H.add(667);
		H.add(633);
		H.add(599);
		H.add(565);
		H.add(531);
		H.add(497);
		H.add(463);
		H.add(464);
		H.add(465);
		H.add(466);
		H.add(467);
		H.add(468);
		H.add(469);
		H.add(470);
		H.add(471);
		H.add(437);
		H.add(403);
		H.add(369);
		H.add(335);
		H.add(301);
		H.add(267);
		H.add(233);
		H.add(199);
		H.add(200);
		log(">>> Recorridos creados:");
		log("Size:    A:"+A.size()+"    B:"+B.size()+"    C:"+C.size()+"    D:"+D.size()+"    E:"+E.size()+"    F:"+F.size()+"    G:"+G.size()+"    H:"+H.size());
		log("Ends:    A:"+A.get(A.size()-1)+"    B:"+B.get(B.size()-1)+"    C:"+C.get(C.size()-1)+"    D:"+D.get(D.size()-1)+"    E:"+E.get(E.size()-1)+"    F:"+F.get(F.size()-1)+"    G:"+G.get(G.size()-1)+"    H:"+H.get(H.size()-1));
	}
	
	public static void log(String S){
		Main.log(S);
	}
	
	public static void error(String S){
		Main.error(S);
	}
}