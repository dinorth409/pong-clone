package newPong;

import java.awt.BorderLayout;
import sun.audio.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class Pong extends JPanel {
	
	JFrame f;
	
	int screenWidth = 1000, screenHeight = 1000;
	
	int ballX = screenWidth / 2, ballY = screenHeight / 2, ballSpeedX = 4, ballSpeedY = 2, ballSize = 30;
	
	static final int padHeight = 15, padWidth = 200;
	
	int topPadX = screenWidth / 2 - padWidth / 2, topPadY = padHeight, topPadSpeedX = 4;
	
	int botPadX = screenWidth / 2 - padWidth / 2, botPadY = screenHeight - (padHeight * 2);
	int botPadMidLeft = botPadX + (padWidth / 4), botPadMidRight = botPadMidLeft + (padWidth / 4);
	
	int speed = 0, maxSpeed = 5;
	int tempSpeedX = ballSpeedX;
	
	int aiScore = 0, playerScore = 0;
	String aiScoreDisplay, playerScoreDisplay;
	
	boolean lost = false, won = false, tracking = true;

	public Pong(){
		f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(this, BorderLayout.CENTER);
		f.setTitle("Pong");
		f.setContentPane(this);
		f.getContentPane().setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		f.pack();
		f.setVisible(true);
		
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
		this.getActionMap().put("left", new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				if (botPadX != 0){
					botPadX -= 20;
				}
				repaint();
			}
		});
		
		this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
		this.getActionMap().put("right", new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				if (botPadX + padWidth != screenWidth){
					botPadX += 20;
				}
				repaint();
			}
		});
	}
	
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		
		g2.setColor(Color.WHITE);
		g2.fillOval(ballX, ballY, ballSize, ballSize);
		g2.setColor(Color.RED);
		g2.fillRect(topPadX, topPadY, padWidth, padHeight);
		g2.setColor(Color.BLUE);
		g2.fillRect(botPadX, botPadY, padWidth, padHeight);
		g2.setColor(Color.WHITE);
		
		Font f = new Font("Ariel", Font.BOLD, 50);
		g2.setFont(f);
		
		if (aiScoreDisplay == null){
			g2.drawString("0", 50, 50);
		} else {
			g2.drawString(aiScoreDisplay, 50, 50);
		}
		
		if (playerScoreDisplay == null){
			g2.drawString("0", 50, 800);
		} else {
			g2.drawString(playerScoreDisplay, 50, 800);
		}
		
		if (lost){
			g2.drawString("You Lose", screenWidth / 2, screenHeight / 2);
		} else if (won){
			g2.drawString("You Win", screenWidth / 2, screenHeight / 2);
		}
	}	
	
	public void startPong(){
		while(true){
			ballX += ballSpeedX;
			ballY += ballSpeedY;
			
			
			// bottom pad
			if (ballX + (ballSize / 2) < botPadX + padWidth && ballX + (ballSize / 2) > botPadX && ballY + ballSize >= botPadY - padHeight / 2){
				//hits left of pad
				if (ballX >= botPadX && ballX <= botPadX + (padWidth / 4)){ 
					if (ballSpeedX > 0) {
						ballSpeedX = -ballSpeedX;
						tempSpeedX = ballSpeedX;
					} else if (ballSpeedX == 0) {
						ballSpeedY = 2;
						if (tempSpeedX > 0) {
							ballSpeedX += -tempSpeedX;
						} else if (tempSpeedX < 0){
							ballSpeedX += tempSpeedX;
						}
					}
				} else if (ballX > botPadX + (padWidth / 4) && ballX < botPadX + (padWidth / 4) + (padWidth / 4) + (padWidth / 4)){ 
					//hits middle of pad
					if (ballSpeedX > 0){
						ballSpeedX += -ballSpeedX;
					} else if (ballSpeedX < 0) {
						ballSpeedX += -ballSpeedX;
					}
					if (ballSpeedY == 2){
						ballSpeedY = ballSpeedY * 2;
					}
				} else if (ballX >= botPadX + (padWidth / 4) + (padWidth / 4) && ballX <= botPadX + padWidth){ 
					//hits right of pad
					if (ballSpeedX < 0){
						ballSpeedX = - ballSpeedX;
						tempSpeedX = ballSpeedX;
					} else if (ballSpeedX == 0) {
						ballSpeedY = 2;
						if (tempSpeedX > 0) {
							ballSpeedX += tempSpeedX;
						} else if (tempSpeedX < 0) {
							ballSpeedX += -tempSpeedX;
						}
					}
				}
				ballSpeedY = -ballSpeedY;
			}
			
			
			//top pad
			if (ballX >= topPadX && ballX <= topPadX + padWidth && ballY - ballSize / 2 <= topPadY){
				//hits left of pad
				if (ballX >= topPadX && ballX <= topPadX + (padWidth / 4)){ 
					if (ballSpeedX > 0) {
						ballSpeedX = -ballSpeedX;
						tempSpeedX = ballSpeedX;
					} else if (ballSpeedX == 0) {
						ballSpeedY = 2;
						if (tempSpeedX > 0) {
							ballSpeedX += -tempSpeedX;
						} else if (tempSpeedX < 0){
							ballSpeedX += tempSpeedX;
						}
					}
					//hits middle of pad
				} else if (ballX > topPadX + (padWidth / 4) && ballX < topPadX + (padWidth / 4) + (padWidth / 4) +(padWidth / 4)){ 
					if (ballSpeedX > 0){
						ballSpeedX += -ballSpeedX;
					} else if (ballSpeedX < 0) {
						ballSpeedX += -ballSpeedX;
					}
					if (ballSpeedY == -2){
						ballSpeedY = ballSpeedY * 2;
					}
					//hits right of pad
				} else if (ballX >= topPadX + (padWidth / 4) + (padWidth / 4) && ballX <= topPadX + padWidth){ 
					if (ballSpeedX < 0){
						ballSpeedX = - ballSpeedX;
						tempSpeedX = ballSpeedX;
					} else if (ballSpeedX == 0) {
						ballSpeedY = 2;
						if (tempSpeedX > 0) {
							ballSpeedX += tempSpeedX;
						} else if (tempSpeedX < 0) {
							ballSpeedX += -tempSpeedX;
						}
					}
				}
				ballSpeedY = -ballSpeedY;
			}
			
			
			
			//AI behavior
			if (tracking) {
				if (ballX + (ballSize / 2) != topPadX + (padWidth / 2) && ballX < topPadX + (padWidth / 2)) {
					if (topPadX != 0){
						topPadX -= topPadSpeedX;
					}
				} else if (ballX + (ballSize / 2) != topPadX + (padWidth / 2) && ballX > topPadX + (padWidth / 2)){
					if (topPadX + padWidth != screenWidth){
						topPadX += topPadSpeedX;
					}
				} 
			}
			
			
			//wall bouncing and score update
			if (ballX < 0) {
				ballSpeedX = -ballSpeedX;
			} else if (ballX + ballSize > screenWidth) {
				ballSpeedX = -ballSpeedX;
			} else if (ballY == 0) {
				ballSpeedY = -ballSpeedY;
				playerScore += 100;
				playerScoreDisplay = new String("" + playerScore);
			} else if (ballY + ballSize / 2 >= screenHeight) {
				ballSpeedY = -ballSpeedY;
				aiScore += 100;
				aiScoreDisplay = new String("" + aiScore);
			}
			
			
			//win/lose conditions
			if (aiScore == 500) {
				ballX = 50;
				ballY = 50;
				lost = true;
				tracking = false;
			} else if (playerScore == 500) {
				ballX = 50;
				ballY = 50;
				won = true;
				tracking = false;
			}
			
			repaint();
			
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("restriction")
	public static void main(String[] args) throws IOException {
		Pong pong = new Pong();
		pong.startPong();
		
	}

}
