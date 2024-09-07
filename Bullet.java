package squareGame;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.util.HashMap;
import java.util.HashSet;


//DOCUMENTATION TO BE ADDED SOON!

public class Bullet extends FrameComponent {

	private JLabel model = new JLabel();
	private JLabel targetUsed;
	private JLabel targetUsed2;
	private JFrame frameUsed;
	static int moveInterval = 3;
	static double speed = 15;
	static boolean moveActive = false;
	boolean tooFar = false;
	static HashMap<Integer, HashSet<Bullet>> bullets = new HashMap<Integer, HashSet<Bullet>>();
	int start_x;
	int start_y;
	int numStepsMoved = 0;
	double[] changeXY;
	
	public Bullet(JFrame frame, int x, int y, JLabel target, JLabel target2, int side) {
		
		start_x = x;
		start_y = y;
		
		model.setBounds(x, y, side, side);
		model.setOpaque(true);
		model.setBackground(Color.GREEN);
		targetUsed = target;
		targetUsed2 = target2;
		frameUsed = frame;
		changeXY = xyChange(x+side, y+side, target.getX()+target.getWidth(), target.getY()+target.getHeight(), target2.getX()+target2.getWidth(), target2.getY()+target2.getWidth());
		
		frame.add(model);
		moveActive = true;
		if (!bullets.containsKey(Game.constantTimerCount % moveInterval)) {
			bullets.put(Game.constantTimerCount % moveInterval, new HashSet<Bullet>());
		}
		bullets.get(Game.constantTimerCount % moveInterval).add(this);
	}
	
	
	public void checkHitPlayer() {
		
		if(model.isVisible() && distanceBetween(model, targetUsed)<100 && model.getX() + 10 >= targetUsed.getX() && model.getX() <= targetUsed.getX() + targetUsed.getWidth() && model.getY() + 10 >= targetUsed.getY() && model.getY() <= targetUsed.getY() + targetUsed.getHeight()) {
			
			gameOver = true;
			p1hit = true;
		}
		
		else if(model.isVisible() && distanceBetween(model, targetUsed2)<100 && model.getX() + 10 >= targetUsed2.getX() && model.getX() <= targetUsed2.getX() + targetUsed2.getWidth() && model.getY() + 10 >= targetUsed2.getY() && model.getY() <= targetUsed2.getY() + targetUsed2.getHeight()) {
			
			gameOver = true;
			p1hit = false;
		}
	}
	
	public void tryMove() {
		
		if (tooFar) return;
		
		model.setBounds(start_x + (int) Math.round((numStepsMoved+1)*changeXY[0]), start_y + (int) Math.round((numStepsMoved+1)*changeXY[1]), model.getWidth(), model.getHeight());
		
		if(model.getX() < -15 || model.getX() > 1401 || model.getY() < -15 || model.getY() > 705) {
			
			tooFar = true;
		}
		
		numStepsMoved++;
	}
	
	public void die() {
		model.setVisible(false);
		frameUsed.remove(model);
	}
	
	public double distanceBetween(JLabel a, JLabel b) {
		
		double xS = (a.getX() - b.getX()) * (a.getX() - b.getX());
		double yS = (a.getY() - b.getY()) * (a.getY() - b.getY());
		
		return Math.sqrt(xS + yS);
	}
	
	public double distanceBetween(int a_x, int a_y, int b_x, int b_y) {
		
		double xS = (a_x - b_x) * (a_x - b_x);
		double yS = (a_y - b_y) * (a_y - b_y);
		
		return Math.sqrt(xS + yS);
	}
	
	public double[] xyChange(int model_x, int model_y, int target1_x, int target1_y, int target2_x, int target2_y) {
		
		if (distanceBetween(model_x, model_y, target2_x, target2_y) < distanceBetween(model_x, model_y, target1_x, target1_y)) {
			return xyChange(model_x, model_y, target2_x, target2_y, target1_x, target1_y);
		}
		
		double dist = distanceBetween(model_x, model_y, target1_x, target1_y);
		double unitXChange = (target1_x - model_x) / dist;
		double unitYChange = (target1_y - model_y) / dist;
		
		return new double[] {unitXChange * speed, unitYChange * speed};
		
	}

}