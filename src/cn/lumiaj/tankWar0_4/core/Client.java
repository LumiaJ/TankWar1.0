package cn.lumiaj.tankWar0_4.core;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import cn.lumiaj.tankWar0_4.bean.Player;
import cn.lumiaj.tankWar0_4.bean.Robot;
import cn.lumiaj.tankWar0_4.bean.Tank;
import cn.lumiaj.utils.Constants;
import cn.lumiaj.utils.Utils;

@SuppressWarnings("all")
public class Client extends Frame {
	private Image offScreenImage;
	private Player player;
	private List<Robot> robots;
	private boolean isOver;
	private int time;
	
	public List<Robot> getRobots() {
		return robots;
	}
	
	public Player getPlayer() {
		return player;
	}

	/**
	 * 继承update方法，实现双缓冲解决闪现问题
	 */
	@Override
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(Constants.BOUND_WIDTH,Constants.BOUND_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GRAY);
		gOffScreen.fillRect(0, 0, Constants.BOUND_WIDTH, Constants.BOUND_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	@Override
	public void paint(Graphics g) {
		if(isOver) {
			switch(time) {
			case 1:
				g.drawImage(Utils.getImage("num/3.png"), 340, 240, null);
				break;
			case 2:
				g.drawImage(Utils.getImage("num/2.png"), 340, 240, null);
				break;
			case 3:
				g.drawImage(Utils.getImage("num/1.png"), 340, 240, null);
				break;
			}
		}else {
			player.paint(g);
			g.drawString("敌军数量:"+robots.size(), 60, 60);
			for(int i=0;i<robots.size();i++) {
				robots.get(i).paint(g);
			}
			if(robots.size()==0) {
				g.drawString("YOU WIN!!!", Constants.BOUND_WIDTH/2, Constants.BOUND_HEIGHT/2);
				gameOver();
			}
		}
	}
	
	/**
	 * 添加坦克
	 * @param count 坦克的数量
	 */
	private void addRobots(int count) {
		for(int i=0;i<count;i++) {
			boolean isOK = true;
			int x = (int)(Math.random()*(Constants.BOUND_WIDTH-Constants.TANK_SIZE));
			int y = (int)(Math.random()*(Constants.BOUND_HEIGHT-Constants.TANK_SIZE*2)+Constants.TANK_SIZE);
			Robot rob = new Robot(x, y, this);
			for(Robot r : robots) {
				if(r.getRect().intersects(rob.getRect())) {
					isOK = false;
					break;
				}
			}
			if(isOK) robots.add(rob);
		}
		
	}
	
	/**
	 * 初始化客户端
	 */
	public void launchFrame(int count) {
		this.setBounds(Constants.BOUND_X, Constants.BOUND_Y, Constants.BOUND_WIDTH, Constants.BOUND_HEIGHT);
		this.setVisible(true);
		this.setResizable(false);
		this.setTitle("Tank War");
		this.setBackground(Color.GRAY);
		//添加窗口的关闭
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		//添加键盘控制
		this.addKeyListener(new KeyMonitor());
		//添加坦克
		this.player = new Player(400,570,this);
		this.robots = new ArrayList<Robot>();
		addRobots(count);
		//线程开始
		this.time = 0;
		isOver = false;
		new Thread(new PaintThread(count)).start();
	}
	
	public void restart(int count) {
		this.player = new Player(400,570,this);
		this.robots = new ArrayList<Robot>();
		this.time = 0 ;
		addRobots(count);
		isOver = false;
	}
	
	public void gameOver() {
		isOver = true;
	}
	
	/**
	 * 键盘的监听类
	 * @author LumiaJ
	 *
	 */
	private class KeyMonitor extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			player.keyPressed(e);
		}
		@Override
		public void keyReleased(KeyEvent e) {
			player.keyReleased(e);
		}
	}

	private class PaintThread implements Runnable {
		private int count;
		public PaintThread(int count) {
			this.count = count;
		}
		@Override
		public void run() {
			long add1 = System.currentTimeMillis();
			long add2 = System.currentTimeMillis();
			long shut1 = System.currentTimeMillis();
			long shut2 = System.currentTimeMillis();
			while(true) {
				shut1 = System.currentTimeMillis();
				add2 = System.currentTimeMillis();
				while (!isOver) {
					add2 = System.currentTimeMillis();
					shut2 = System.currentTimeMillis();
					try {
						repaint();
						Thread.sleep(40);
						if(shut2 - shut1 >2000) {
							for(int i=0;i<robots.size();i++) {
								robots.get(i).shut();
							}
							shut1 = System.currentTimeMillis();
						}
						if(add2 - add1 >5000) {
							addRobots(2);
							add1 = System.currentTimeMillis();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if(add2-add1>1000) {
					time++;
					repaint();
					add1=System.currentTimeMillis();
					if(time==4) {
						restart(count);
//						launchFrame(10);
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		new Client().launchFrame(10);
	}
}
