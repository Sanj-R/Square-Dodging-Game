package squareGame;

import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.*;

//DOCUMENTATION TO BE ADDED SOON!

public class RangedEnemy extends FrameComponent {

	private JLabel model = new JLabel();
	private int initialX = initialCoordGenX();
	private int initialY = initialCoordGenY();
	private JFrame frameUsed;
	private JLabel targetUsed;
	private JLabel targetUsed2;
	static int moveInterval = 50;
	static boolean moveActive = false;
	boolean still = false;
	static int shootInterval = 100;
    static boolean shootActive = false;
	static HashMap<Integer, HashSet<RangedEnemy>> enemiesMoveMap = new HashMap<Integer, HashSet<RangedEnemy>>();
	static HashMap<Integer, HashSet<RangedEnemy>> enemiesShootMap = new HashMap<Integer, HashSet<RangedEnemy>>();
	
	public RangedEnemy(JFrame frame, JLabel target, JLabel target2) {

		model.setBounds(initialX, initialY, 30, 30);
		model.setOpaque(true);
		model.setBackground(Color.GREEN);
		frame.add(model);
		frameUsed = frame;
		targetUsed = target;
		targetUsed2 = target2;
		moveActive = true;
		
		if (!enemiesMoveMap.containsKey(Game.constantTimerCount % moveInterval)) {
			enemiesMoveMap.put(Game.constantTimerCount % moveInterval, new HashSet<RangedEnemy>());
		}
		enemiesMoveMap.get(Game.constantTimerCount % moveInterval).add(this);
		
	}
	
	public void checkHitPlayer() {
		
		if(model.getX() + 30 >= targetUsed.getX() && model.getX() <= targetUsed.getX() + targetUsed.getWidth() && model.getY() + 30 >= targetUsed.getY() && model.getY() <= targetUsed.getY() + targetUsed.getHeight()) {
			
			gameOver = true;
			p1hit = true;
		}
		
		else if(model.getX() + 30 >= targetUsed2.getX() && model.getX() <= targetUsed2.getX() + targetUsed2.getWidth() && model.getY() + 30 >= targetUsed2.getY() && model.getY() <= targetUsed2.getY() + targetUsed2.getHeight()) {
			
			gameOver = true;
			p1hit = false;
		}
	}
	
	public void die() {
		model.setVisible(false);
		frameUsed.remove(model);
	}
	
	public void moveIfAble() {
		
		if (still) return;
		
		if(initialX == -31 && model.getX() != 9) model.setBounds(model.getX() + 10, model.getY(), model.getWidth(), model.getHeight());
		else if(initialX == 1401 && model.getX() != 1321) {model.setBounds(model.getX() - 10, model.getY(), model.getWidth(), model.getHeight());}
		else if(initialY == -31 && model.getY() != 9) model.setBounds(model.getX(), model.getY() + 10, model.getWidth(), model.getHeight());
		else if(initialY == 741 && model.getY() != 661) {model.setBounds(model.getX(), model.getY() - 10, model.getWidth(), model.getHeight());}
		
		if(model.getX() == 1321 || model.getX() == 9 || model.getY() == 9 || model.getY() == 661) {
			shootActive = true;
			still = true;

			if (!enemiesShootMap.containsKey(Game.constantTimerCount % shootInterval)) {
				enemiesShootMap.put(Game.constantTimerCount % shootInterval, new HashSet<RangedEnemy>());
			}

			enemiesShootMap.get(Game.constantTimerCount % shootInterval).add(this);
		}
	}
	
	public void shoot() {
		
		double distanceToPlayer1 = distanceBetween(model, targetUsed);
		double distanceToPlayer2 = distanceBetween(model, targetUsed2);
		
		JLabel btarget = null;
		JLabel btarget2 = null;
		
		if (distanceToPlayer1 >= distanceToPlayer2) {
			btarget = targetUsed2;
			btarget2 = targetUsed;
		}
		else {
			btarget = targetUsed;
			btarget2 = targetUsed2;
		}
		
		new Bullet(frameUsed, model.getX() + 10, model.getY() + 10, btarget, btarget2, 10);
	}
	
	public int initialCoordGenX() {
		
		//-31; 0-1371; 1401 //0-4166
		int coorProb = (int) (Math.random() * 4166);
		if(coorProb <= 711) return -31;
		else if(coorProb < 3455) return (int) (Math.random() * 1370);//0 - 1369
		else if(coorProb >= 3455) return 1401;
		return 0;
	}
	
	public double distanceBetween(JLabel a, JLabel b) {
		
		double xS = (a.getX() - b.getX()) * (a.getX() - b.getX());
		double yS = (a.getY() - b.getY()) * (a.getY() - b.getY());
		
		return Math.sqrt(xS + yS);
	}
	
	public int initialCoordGenY() {
		
		//0-710; -31 || 741; 0-710
		if(initialX == -31) return (int) (Math.random() * 711);
		else if(initialX < 1370) { if((int) (Math.random() * 2) == 0) return -31; else return 741;}
		else if(initialX == 1401) return (int) (Math.random() * 711);
		return 0;
	}

}