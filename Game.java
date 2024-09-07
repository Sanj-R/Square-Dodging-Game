package squareGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.*;

// DOCUMENTATION TO BE ADDED SOON!

public class Game extends FrameComponent implements ActionListener, KeyListener{

	private static JFrame frame = new JFrame();
	private static JButton startButton = new JButton();
	private Timer constantTimer = new Timer(1, this);
	private static JLabel player = new JLabel();
	private static JLabel player2 = new JLabel();
	private Timer targetMoveW = new Timer(10, this);
	private Timer targetMoveA = new Timer(10, this);
	private Timer targetMoveS = new Timer(10, this);
	private Timer targetMoveD = new Timer(10, this);
	private Timer targetMoveWA = new Timer(10, this);
	private Timer targetMoveAA = new Timer(10, this);
	private Timer targetMoveSA = new Timer(10, this);
	private Timer targetMoveDA = new Timer(10, this);
	private JLabel bigScore = new JLabel();
	private JLabel smallScore = new JLabel();
	private JLabel constantHighscore = new JLabel("Highscore: 0");
	private JLabel newHighscore = new JLabel("New Highscore!");
	private int score = 0, highscore = 0;
	private boolean scoreActive = false;
	private boolean enemySpawnActive = false;
	private int scoreInterval = 10;
	private int enemySpawnInterval = 200;
	
	static int constantTimerCount = 0;
	
	public Game() {
		
		frame.setBounds(0, 0, 1400, 740);
		frame.setLayout(null);
		frame.setFocusable(true);
		frame.addKeyListener(this);
		
		player.setBounds(1400/3, 370, 30, 30);
		player.setBackground(Color.BLUE);
		player.setOpaque(true);
		frame.add(player);
		
		player2.setBounds((2*1400)/3, 370, 30, 30);
		player2.setBackground(Color.RED);
		player2.setOpaque(true);
		frame.add(player2);
		
		smallScore.setBounds(15, 10, 100, 30);
		smallScore.setText("" + score);
		smallScore.setFont(new Font("Arial", Font.BOLD, 20));
		
		bigScore.setBounds(450, 100, 600, 300);
		bigScore.setText("Score: " + score);
		bigScore.setFont(new Font("Arial", Font.BOLD, 50));
		
		newHighscore.setBounds(612, 300, 300, 30);
		newHighscore.setFont(new Font("Arial", Font.BOLD, 20));
		frame.add(newHighscore);
		newHighscore.setVisible(false);
		
		constantHighscore.setBounds(1200, 10, 300, 30);
		constantHighscore.setFont(new Font("Arial", Font.BOLD, 20));
		frame.add(constantHighscore);

		startButton.setBounds(0, 600, 1360, 100);
		startButton.setFont(new Font("Arial", Font.BOLD, 20));
		startButton.setText("Start");
		startButton.addActionListener(this);
		frame.add(startButton);
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		constantTimer.start();
	}
	//startButton, constant timer, key press/release is always the trigger
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == constantTimer) {
			
			if (scoreActive && constantTimerCount % scoreInterval == 0) {
				score++;
				smallScore.setText("" + score);
			}
			
			if (enemySpawnActive && constantTimerCount % enemySpawnInterval == 0) {
				new RangedEnemy(frame, player, player2);
			}
			
			if (RangedEnemy.moveActive && RangedEnemy.enemiesMoveMap.containsKey(constantTimerCount % RangedEnemy.moveInterval)) {
				for (RangedEnemy enemy: RangedEnemy.enemiesMoveMap.get(constantTimerCount % RangedEnemy.moveInterval)) {
					enemy.moveIfAble();
				}
			}
			
			if (RangedEnemy.shootActive && RangedEnemy.enemiesShootMap.containsKey(constantTimerCount % RangedEnemy.shootInterval)) {
				for (RangedEnemy enemy: RangedEnemy.enemiesShootMap.get(constantTimerCount % RangedEnemy.shootInterval)) {
					enemy.shoot();
				}
			}
			
			if (Bullet.moveActive && Bullet.bullets.containsKey(constantTimerCount % Bullet.moveInterval)) {
				for (Bullet bullet: Bullet.bullets.get(constantTimerCount % Bullet.moveInterval)) {
					bullet.tryMove();
				}
			}
			
			if (gameOver) {
				constantTimer.stop();
				
				for (HashSet<Bullet> bulletSet: Bullet.bullets.values()) {
					for (Bullet bullet: bulletSet) {
						bullet.die();
					}
				}
				Bullet.bullets.clear();
				
				for (HashSet<RangedEnemy> enemySet: RangedEnemy.enemiesMoveMap.values()) {
					for (RangedEnemy enemy: enemySet) {
						enemy.die();
					}
				}
				RangedEnemy.enemiesMoveMap.clear();
				RangedEnemy.enemiesShootMap.clear();
				
				targetMoveW.stop(); 
				targetMoveA.stop(); 
				targetMoveS.stop(); 
				targetMoveD.stop(); 
				
				player.setVisible(false); 
				player2.setVisible(false);
				scoreActive = false;
				
				
				String winner = p1hit ? "Red" : "Blue";
				
				bigScore.setText("Score:  " + score + " - " + winner + " wins!"); 
				smallScore.setVisible(false); 
				constantHighscore.setVisible(false); 
				frame.add(bigScore); 
				bigScore.setVisible(true); 
				startButton.setVisible(true);
				
				if(score > highscore) {highscore = score; newHighscore.setVisible(true); constantHighscore.setText("Highscore: " + score);}
			}
			
			if (playerInEnemyZone(player) || playerInEnemyZone(player2)) {
				for (HashSet<RangedEnemy> enemySet: RangedEnemy.enemiesMoveMap.values()) {
					for (RangedEnemy enemy: enemySet) {
						enemy.checkHitPlayer();
					}
				}
			}
			
			for (HashSet<Bullet> bulletSet: Bullet.bullets.values()) {
				for (Bullet bullet: bulletSet) {
					bullet.checkHitPlayer();
				}
			}
			
			constantTimerCount++;
		}
		
		else if(e.getSource() == targetMoveW && player.getY() > -15 && !gameOver) {player.setBounds(player.getX(), player.getY() - 3, player.getWidth(), player.getHeight());}
		else if(e.getSource() == targetMoveA && player.getX() > -15 && !gameOver) {player.setBounds(player.getX() - 3, player.getY(), player.getWidth(), player.getHeight());}
		else if(e.getSource() == targetMoveS && player.getY() < 680 && !gameOver) {player.setBounds(player.getX(), player.getY() + 3, player.getWidth(), player.getHeight());}
		else if(e.getSource() == targetMoveD && player.getX() < 1350 && !gameOver) {player.setBounds(player.getX() + 3, player.getY(), player.getWidth(), player.getHeight());}

		else if(e.getSource() == targetMoveWA && player2.getY() > -15 && !gameOver) {player2.setBounds(player2.getX(), player2.getY() - 3, player2.getWidth(), player2.getHeight());}
		else if(e.getSource() == targetMoveAA && player2.getX() > -15 && !gameOver) {player2.setBounds(player2.getX() - 3, player2.getY(), player2.getWidth(), player2.getHeight());}
		else if(e.getSource() == targetMoveSA && player2.getY() < 680 && !gameOver) {player2.setBounds(player2.getX(), player2.getY() + 3, player2.getWidth(), player2.getHeight());}
		else if(e.getSource() == targetMoveDA && player2.getX() < 1350 && !gameOver) {player2.setBounds(player2.getX() + 3, player2.getY(), player2.getWidth(), player2.getHeight());}
		
		else if(e.getSource() == startButton && gameOver) {
			
			p1hit = false;
			
			gameOver = false;
			startButton.setVisible(false);
			newHighscore.setVisible(false);
			smallScore.setVisible(true);
			constantHighscore.setVisible(true);
			smallScore.setText("0");
			bigScore.setVisible(false);
			player.setBounds(1400/3, 370, 30, 30);
			player.setVisible(true);
			player.setFocusable(true);
			player.addKeyListener(this);
			player2.setBounds(2*1400/3, 370, 30, 30);
			player2.setVisible(true);
			player2.setFocusable(true);
			player2.addKeyListener(this);
			constantTimer.start();
			score = 0;
			scoreActive = true;
		}
		
		else if(e.getSource() == startButton) {
			startButton.setVisible(false); 
			frame.add(smallScore); 
			enemySpawnActive = true;
			scoreActive = true;
		}
	}
	
	public static void main(String[] args) {

		new Game();
	}

	public void keyPressed(KeyEvent e) {

		if((e.getKeyChar() == 'w' || e.getKeyChar() == 'W') && !startButton.isVisible()) {targetMoveW.start();}
		else if((e.getKeyChar() == 'a' || e.getKeyChar() == 'A') && !startButton.isVisible()) {targetMoveA.start();}
		else if((e.getKeyChar() == 's' || e.getKeyChar() == 'S') && !startButton.isVisible()) {targetMoveS.start();}
		else if((e.getKeyChar() == 'd' || e.getKeyChar() == 'D') && !startButton.isVisible()) {targetMoveD.start();}
		
		else if((e.getKeyCode() == KeyEvent.VK_UP) && !startButton.isVisible()) {targetMoveWA.start();}
		else if((e.getKeyCode() == KeyEvent.VK_LEFT) && !startButton.isVisible()) {targetMoveAA.start();}
		else if((e.getKeyCode() == KeyEvent.VK_DOWN) && !startButton.isVisible()) {targetMoveSA.start();}
		else if((e.getKeyCode() == KeyEvent.VK_RIGHT) && !startButton.isVisible()) {targetMoveDA.start();}
		
		else if((e.getKeyCode() == KeyEvent.VK_ENTER) && gameOver) {
			
			p1hit = false;
			gameOver = false;
			startButton.setVisible(false);
			newHighscore.setVisible(false);
			smallScore.setVisible(true);
			constantHighscore.setVisible(true);
			smallScore.setText("0");
			bigScore.setVisible(false);
			player.setBounds(1400/3, 370, 30, 30);
			player.setVisible(true);
			player.setFocusable(true);
			player.addKeyListener(this);
			player2.setBounds(2*1400/3, 370, 30, 30);
			player2.setVisible(true);
			player2.setFocusable(true);
			player2.addKeyListener(this);
			constantTimer.start();
			score = 0;
			scoreActive = true;	
		}
	}

	public void keyReleased(KeyEvent e) {
		
		
		if(e.getKeyChar() == 'w' || e.getKeyChar() == 'W') {targetMoveW.stop();}
		else if(e.getKeyChar() == 'a' || e.getKeyChar() == 'A') {targetMoveA.stop();}
		else if(e.getKeyChar() == 's' || e.getKeyChar() == 'S') {targetMoveS.stop();}
		else if( e.getKeyChar() == 'd' || e.getKeyChar() == 'D') {targetMoveD.stop();}
		
		else if(e.getKeyCode() == KeyEvent.VK_UP) {targetMoveWA.stop();}
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) {targetMoveAA.stop();}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN) {targetMoveSA.stop();}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {targetMoveDA.stop();}
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	
	public boolean playerInEnemyZone(JLabel p) {
		return p.getX() >= 1321 - 30 || p.getX() <= 9 + 30 || p.getY() <= 9 + 30 || p.getY() >= 661 - 30;
	}

}
