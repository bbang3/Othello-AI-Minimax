package com.bbs.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener
{
	private GameContainer gc;
	
	private final int NUM_KEYS = 256;
	private boolean[] keys = new boolean[NUM_KEYS];
	private boolean[] keysLast = new boolean[NUM_KEYS]; // 마지막 프레임에 눌린 키 저장

	private final int NUM_BUTTONS = 5; // 마우스(왼,중,오,앞,뒤)
	private boolean[] buttons = new boolean[NUM_BUTTONS];
	private boolean[] buttonsLast = new boolean[NUM_BUTTONS];

	private int mouseX, mouseY;
	private int scroll; // + : 아래로 스크롤, - : 위로 스크롤
	
	public Input(GameContainer gc)
	{
		this.gc = gc;
		mouseX = 0;
		mouseY = 0;
		scroll = 0;
		
		gc.getWindow().getCanvas().addKeyListener(this);
		gc.getWindow().getCanvas().addMouseListener(this);
		gc.getWindow().getCanvas().addMouseMotionListener(this);
		gc.getWindow().getCanvas().addMouseWheelListener(this);
	}

	public void update()
	{
		scroll = 0;
		for(int i = 0; i < NUM_KEYS; i++)
		{
			keysLast[i] = keys[i]; // 마지막에 눌린 키 저장
		}
		for(int i = 0; i < NUM_BUTTONS; i++)
		{
			buttonsLast[i] = buttons[i]; // 마지막에 눌린 키 저장
		}
	}
	
	public boolean isKey(int keyCode)
	{
		return keys[keyCode];
	}
	
	public boolean isKeyUp(int keyCode) // 이전 프레임에서 눌렸고, 지금 프레임에서 안 눌린 상태 -> 키를 꾹 누르고 있는 상태
	{
		return !keys[keyCode] && keysLast[keyCode];
	}
	
	public boolean isKeyDown(int keyCode)
	{
		return keys[keyCode] && !keysLast[keyCode]; // 이전 프레임에서 눌리지 않았고, 현재 프레임에서 눌린 상태
	}
	
	public boolean isButton(int button)
	{
		return buttons[button];
	}
	// 버튼을 누른 순간에 true
	public boolean isButtonUp(int button) // 이전 프레임에서 눌렸고, 지금 프레임에서 안 눌린 상태 -> 키를 꾹 누르고 있는 상태
	{
		return !buttons[button] && buttonsLast[button];
	}
	// 버튼을 누르고 뗀 순간에 true
	public boolean isButtonDown(int button)
	{
		return buttons[button] && !buttonsLast[button]; // 이전 프레임에서 눌리지 않았고, 현재 프레임에서 눌린 상태
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		scroll = e.getWheelRotation();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		mouseX = (int)(e.getX() / gc.getScale()); // 화면의 해상도?스케일에 따라 올바르게 저장하기 위해
		mouseY = (int)(e.getY() / gc.getScale()); // 화면의 해상도?스케일에 따라 올바르게 저장하기 위해
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseX = (int)(e.getX() / gc.getScale()); // 화면의 해상도?스케일에 따라 올바르게 저장하기 위해
		mouseY = (int)(e.getY() / gc.getScale()); // 화면의 해상도?스케일에 따라 올바르게 저장하기 위해
		// 원래 프레임의 테두리 부분까지 고려해서 해야하지만, 별로 필요없을거 같아서 스킵함.
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		buttons[e.getButton()] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		buttons[e.getButton()] = false;
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	}

	public int getMouseX()
	{
		return mouseX;
	}

	public int getMouseY()
	{
		return mouseY;
	}

	public int getScroll()
	{
		return scroll;
	}
}
