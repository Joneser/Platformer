package gamestate;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import audio.AudioPlayer;

import main.GamePanel;

import entity.Enemy;
import entity.Explosion;
import entity.HUD;
import entity.Player;
import entity.enemies.Slugger;

import tilemap.Background;
import tilemap.TileMap;

public class Level1State extends GameState {

	private TileMap tileMap;
	private Background bg;
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	
	private HUD hud;
	
	private AudioPlayer bgMusic;
	
	// events
	private boolean eventDead;
	private boolean bossFight;
	
	public Level1State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}
	
	public void init() {
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/grassbg1.gif", 0.1);
		
		player = new Player(tileMap);
		player.setPosition(100, 100);
		
		bossFight = false;
		
		populateEnemies();
		
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);
		
		bgMusic = new AudioPlayer("/Music/level1-1.mp3", true);
		bgMusic.play();
	}
	
	// TODO: Randomly place enemies each time level is loaded (so in different position every time)
	private void populateEnemies() {
		enemies = new ArrayList<Enemy>();
		
		Slugger s;
		Point[] points = new Point[] {
			new Point(200, 100),
			new Point(860, 200),
			new Point(1525, 200),
			new Point(1680, 200),
			new Point(1800, 200)
		};
		for(int i = 0; i < points.length; i++) {
			s = new Slugger(tileMap);
			s.setPosition(points[i].x, points[i].y);
			enemies.add(s);
		}
	}
	
	public void update() {
		
		// update player
		if(!bossFight || (bossFight && tileMap.getX() == -2890.0)) {
			player.update();
		}

		if(player.getHealth() == 0 || player.getY() > (tileMap.getHeight() - player.getCHeight())) {
			eventDead = true;
		}
		
		if(eventDead) eventDead();
		
		if(bossFight) {
			tileMap.setPosition(tileMap.getX() - 1.0f, tileMap.getY() - 1.0f);
		} else {
			tileMap.setPosition(GamePanel.WIDTH / 2 - player.getX(), GamePanel.HEIGHT / 2 - player.getY());
		}		
		
		// set background
		bg.setPosition(tileMap.getX(), tileMap.getY());
		
		// attack enemies
		player.checkAttack(enemies);
		
		// update enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()) {
				enemies.remove(i);
				i--;
				explosions.add(new Explosion(e.getX(), e.getY()));
			}
		}
		
		// update explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		
		if(player.getPosition().get("x") > 2965) {
			
			bossFight = true;			
			// change to boss fight music
			
			// TODO: Get boss fight music 
									
			// make sure to prevent player from moving out of boss area
			player.setPositionLimit(2902.0, 100.0, 3170.00, 200);
			
		}
	
	}
	
	public void draw(Graphics2D g) {
		// draw background
		bg.draw(g);
		
		// draw tilemap
		tileMap.draw(g);
		
		// draw player
		player.draw(g);
		
		// draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
		// draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition((int)tileMap.getX(), (int)tileMap.getY());
			explosions.get(i).draw(g);
		}
		
		// draw hud
		hud.draw(g);
	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_LEFT) {
			player.setLeft(true);
		}
		if(k == KeyEvent.VK_RIGHT) {
			player.setRight(true);
		}
		if(k == KeyEvent.VK_UP) {
			player.setUp(true);
		}
		if(k == KeyEvent.VK_DOWN) {
			player.setDown(true);
		}
		if(k == KeyEvent.VK_W) {
			player.setJumping(true);
		}
		if(k == KeyEvent.VK_E) {
			player.setGliding(true);
		}
		if(k == KeyEvent.VK_R) {
			player.setScratching();
		}
		if(k == KeyEvent.VK_F) {
			player.setFiring();
		}
	}
	
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_LEFT) {
			player.setLeft(false);
		}
		if(k == KeyEvent.VK_RIGHT) {
			player.setRight(false);
		}
		if(k == KeyEvent.VK_UP) {
			player.setUp(false);
		}
		if(k == KeyEvent.VK_DOWN) {
			player.setDown(false);
		}
		if(k == KeyEvent.VK_W) {
			player.setJumping(false);
		}
		if(k == KeyEvent.VK_E) {
			player.setGliding(false);
		}
	}
	
	// reset level
	private void reset() {
		player.reset();
		player.setPosition(100, 100);
		populateEnemies();
		eventStart();
	}
	
	// level started
	private void eventStart() {
		bgMusic.restart();
	}
	
	// player has died
	private void eventDead() {
		player.setDead();
		player.stop();

		if(player.getLives() == 0){
			bgMusic.close();
			gsm.setState(GameStateManager.MENUSTATE);
		} else {
			eventDead = false;
			reset();
		}
	}
}
