package cn.lumiaj.tankWar0_4.bean;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import cn.lumiaj.tankWar0_4.core.Client;
import cn.lumiaj.utils.Constants;
import cn.lumiaj.utils.Direction;
import cn.lumiaj.utils.Utils;

public class Player extends Tank {
	private boolean leftMove, rightMove, upMove, downMove;
	private int HP;
//	private int bigbangCount;

	public void paint(Graphics g) {
		if(HP<=30) {
			Color c = g.getColor();
			g.setColor(Color.red);
			g.drawString("HP:"+HP, x, y-3);
			g.setColor(c);
		}else
			g.drawString("HP:"+HP, x, y-3);
		Color c = g.getColor();
		g.setColor(Constants.PLAYER_COLOR);
		// g.drawImage(Utils.getImage("p/1.png"),x,y,null);
		move();
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).paint(g);
		}
		paintPT(g, ptDirection);
		g.setColor(c);
	}

	@Override
	public void paintPT(Graphics g, Direction dir) {
		switch (dir) {
		case U:
			g.drawImage(Utils.getImage("p/1.png"), x, y, null);
			break;
		case D:
			g.drawImage(Utils.getImage("p/5.png"), x, y, null);
			break;
		case L:
			g.drawImage(Utils.getImage("p/7.png"), x, y, null);
			break;
		case R:
			g.drawImage(Utils.getImage("p/3.png"), x, y, null);
			break;
		case UR:
			g.drawImage(Utils.getImage("p/2.png"), x, y, null);
			break;
		case UL:
			g.drawImage(Utils.getImage("p/8.png"), x, y, null);
			break;
		case DR:
			g.drawImage(Utils.getImage("p/4.png"), x, y, null);
			break;
		case DL:
			g.drawImage(Utils.getImage("p/6.png"), x, y, null);
			break;
		case S:
			break;
		}
	}

	public void bigbang() {
		for (Direction d : Direction.values()) {
			if (d != Direction.S) {
				ptDirection = d;
				shut();
			}
		}
		// bigbangCount--;
	}

	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		// stop
		switch (code) {
		case KeyEvent.VK_LEFT:
			leftMove = false;
			break;
		case KeyEvent.VK_RIGHT:
			rightMove = false;
			break;
		case KeyEvent.VK_UP:
			upMove = false;
			break;
		case KeyEvent.VK_DOWN:
			downMove = false;
			break;
		case KeyEvent.VK_A:
			shut();
			break;
		case KeyEvent.VK_S:
			speed = Constants.PLAYER_SPEED;
			break;
		case KeyEvent.VK_Q:
//			if (bigbangCount > 0) {
//				bigbang();
//			}
			if(HP>10) {
				HP-=10;
				bigbang();
			}
			break;
		}
		redirection();
	}

	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		// move
		switch (code) {
		case KeyEvent.VK_LEFT:
			leftMove = true;
			redirection();
			break;
		case KeyEvent.VK_RIGHT:
			rightMove = true;
			redirection();
			break;
		case KeyEvent.VK_UP:
			upMove = true;
			redirection();
			break;
		case KeyEvent.VK_DOWN:
			downMove = true;
			redirection();
			break;
		case KeyEvent.VK_S:
			speed *= 2;
			break;
		}
	}

	public void redirection() {
		if (leftMove && !rightMove && !upMove && !downMove)
			direction = Direction.L;
		else if (!leftMove && rightMove && !upMove && !downMove)
			direction = Direction.R;
		else if (!leftMove && !rightMove && upMove && !downMove)
			direction = Direction.U;
		else if (!leftMove && !rightMove && !upMove && downMove)
			direction = Direction.D;
		else if (leftMove && !rightMove && upMove && !downMove)
			direction = Direction.UL;
		else if (leftMove && !rightMove && !upMove && downMove)
			direction = Direction.DL;
		else if (!leftMove && rightMove && upMove && !downMove)
			direction = Direction.UR;
		else if (!leftMove && rightMove && !upMove && downMove)
			direction = Direction.DR;
		else if (!leftMove && !rightMove && !upMove && !downMove)
			direction = Direction.S;
		if (direction != Direction.S) {
			ptDirection = direction;
		}
	}

	public Player(int x, int y, Client client) {
		this.x = x;
		this.y = y;
		this.client = client;
		this.speed = Constants.PLAYER_SPEED;
		this.bulletColor = Constants.PLAYER_BULLET_COLOR;
		this.ptDirection = Direction.U;
		this.HP = 100;
//		this.bigbangCount = 3;
	}

	@Override
	public void boom() {
		HP -= 10;
		if (HP <= 0) {
			isAlive = false;
			client.gameOver();
		}
	}

}
