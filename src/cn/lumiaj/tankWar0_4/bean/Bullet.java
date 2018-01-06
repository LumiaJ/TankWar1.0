package cn.lumiaj.tankWar0_4.bean;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import cn.lumiaj.utils.Constants;
import cn.lumiaj.utils.Direction;

public class Bullet {
	private int x, y;
	private Color color;
	private Direction direction;
	private Tank tank;

	public void paint(Graphics g) {
		fly();
		checkDisappear();
		isHitted(g);
		Color c = g.getColor();
		g.setColor(color);
		g.fillOval(x, y, Constants.BULLET_SIZE, Constants.BULLET_SIZE);
		g.setColor(c);
	}
	
	public void isHitted(Graphics g) {
		//player的子弹检测
		if(tank instanceof Player) {
			for(int i=0;i<tank.client.getRobots().size();i++) {
				if(getRect().intersects(tank.client.getRobots().get(i).getRect())) {
					tank.client.getRobots().get(i).boom();
					tank.getBullets().remove(this);
				}
			}
		}
		//robot的子弹检测
		if(tank instanceof Robot) {
			if(getRect().intersects(tank.client.getPlayer().getRect())) {
				tank.client.getPlayer().boom();
				tank.getBullets().remove(this);
			}
		}
	}

	public void fly() {
		switch (direction) {
		case U:
			y -= Constants.BULLET_SPEED;
			break;
		case D:
			y += Constants.BULLET_SPEED;
			break;
		case L:
			x -= Constants.BULLET_SPEED;
			break;
		case R:
			x += Constants.BULLET_SPEED;
			break;
		case UR:
			y -= 0.7 * Constants.BULLET_SPEED;
			x += 0.7 * Constants.BULLET_SPEED;
			break;
		case UL:
			y -= 0.7 * Constants.BULLET_SPEED;
			x -= 0.7 * Constants.BULLET_SPEED;
			break;
		case DR:
			y += 0.7 * Constants.BULLET_SPEED;
			x += 0.7 * Constants.BULLET_SPEED;
			break;
		case DL:
			y += 0.7 * Constants.BULLET_SPEED;
			x -= 0.7 * Constants.BULLET_SPEED;
			break;
		case S: break;
		}
	}
	
	public void checkDisappear() {
		if(x<0 || x > Constants.BOUND_WIDTH || y<30 || y>Constants.BOUND_HEIGHT) {
			tank.getBullets().remove(this);
		}
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, Constants.BULLET_SIZE, Constants.BULLET_SIZE);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Bullet(int x, int y, Tank tank) {
		super();
		this.x = x;
		this.y = y;
		this.tank = tank;
		this.direction = tank.getPtDirection();
		this.color = tank.bulletColor;
	}

}
