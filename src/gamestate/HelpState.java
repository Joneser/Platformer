package gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import tilemap.Background;

public class HelpState extends GameState {
	
	private Background bg;
	
	private int currentChoice = 0;
	private String[] options = {
			"Return"
	};
	
	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	
	public HelpState(GameStateManager gsm) {
		this.gsm = gsm;

		try {
			
			bg = new Background("/Backgrounds/menubg.gif", 1);
			bg.setVector(-0.1, 0);
			
			titleColor = new Color(128, 0, 0);
			titleFont = new Font("Century Gothic", Font.PLAIN, 28);
			
			font = new Font("Arial", Font.PLAIN, 12);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		bg.update();
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		bg.draw(g);
		
		g.setColor(titleColor);
		g.setFont(titleFont);
		
		g.setFont(font);
		g.drawString("Press the left and right arrow keys to run", 50, 80);
		g.drawString("Press ' W ' to jump", 100, 100);
		g.drawString("Press ' E ' to glide", 100, 120);
		g.drawString("Press ' R ' to scratch", 100, 140);
		g.drawString("Press ' F ' to shoot", 100, 160);
		
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.BLACK);
			} else {
				g.setColor(Color.RED);
			}
			g.drawString(options[i], 125, 200);
		}
	}
	
	private void select() {
		if(currentChoice == 0) {
			gsm.setState(GameStateManager.MENUSTATE);
		}
	}

	@Override
	public void keyPressed(int k) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if(k == KeyEvent.VK_ENTER) {
			select();
		}
		if(k == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}
		
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}

}
