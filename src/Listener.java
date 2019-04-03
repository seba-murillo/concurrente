import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

/*
KeyEvent.VK_LEFT  37
KeyEvent.VK_UP    38
KeyEvent.VK_RIGHT 39
KeyEvent.VK_DOWN  40
*/

public class Listener implements KeyEventDispatcher{
	@Override
	public boolean dispatchKeyEvent(KeyEvent e){
		int K = e.getKeyCode();
		Auto player = Auto.getPlayer();
		if(e.getID() == KeyEvent.KEY_PRESSED){
			switch(K){
				case KeyEvent.VK_LEFT:{
					player.setSentido(Auto.OESTE);
					Auto.player_moving = true;
					break;
				}
				case KeyEvent.VK_UP:{
					player.setSentido(Auto.NORTE);
					Auto.player_moving = true;
					break;
				}
				case KeyEvent.VK_RIGHT:{
					player.setSentido(Auto.ESTE);
					Auto.player_moving = true;
					break;
				}
				case KeyEvent.VK_DOWN:{
					player.setSentido(Auto.SUR);
					Auto.player_moving = true;
					break;
				}
			}
        }
		else if (e.getID() == KeyEvent.KEY_RELEASED){
			if(K == KeyEvent.VK_LEFT || K == KeyEvent.VK_UP || K == KeyEvent.VK_RIGHT || K == KeyEvent.VK_DOWN) Auto.player_moving = false;
        }
		return false;
	}
}