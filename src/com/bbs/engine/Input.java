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
	private boolean[] keysLast = new boolean[NUM_KEYS]; // ������ �����ӿ� ���� Ű ����

	private final int NUM_BUTTONS = 5; // ���콺(��,��,��,��,��)
	private boolean[] buttons = new boolean[NUM_BUTTONS];
	private boolean[] buttonsLast = new boolean[NUM_BUTTONS];

	private int mouseX, mouseY;
	private int scroll; // + : �Ʒ��� ��ũ��, - : ���� ��ũ��
	
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
			keysLast[i] = keys[i]; // �������� ���� Ű ����
		}
		for(int i = 0; i < NUM_BUTTONS; i++)
		{
			buttonsLast[i] = buttons[i]; // �������� ���� Ű ����
		}
	}
	
	public boolean isKey(int keyCode)
	{
		return keys[keyCode];
	}
	
	public boolean isKeyUp(int keyCode) // ���� �����ӿ��� ���Ȱ�, ���� �����ӿ��� �� ���� ���� -> Ű�� �� ������ �ִ� ����
	{
		return !keys[keyCode] && keysLast[keyCode];
	}
	
	public boolean isKeyDown(int keyCode)
	{
		return keys[keyCode] && !keysLast[keyCode]; // ���� �����ӿ��� ������ �ʾҰ�, ���� �����ӿ��� ���� ����
	}
	
	public boolean isButton(int button)
	{
		return buttons[button];
	}
	// ��ư�� ���� ������ true
	public boolean isButtonUp(int button) // ���� �����ӿ��� ���Ȱ�, ���� �����ӿ��� �� ���� ���� -> Ű�� �� ������ �ִ� ����
	{
		return !buttons[button] && buttonsLast[button];
	}
	// ��ư�� ������ �� ������ true
	public boolean isButtonDown(int button)
	{
		return buttons[button] && !buttonsLast[button]; // ���� �����ӿ��� ������ �ʾҰ�, ���� �����ӿ��� ���� ����
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		scroll = e.getWheelRotation();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		mouseX = (int)(e.getX() / gc.getScale()); // ȭ���� �ػ�?�����Ͽ� ���� �ùٸ��� �����ϱ� ����
		mouseY = (int)(e.getY() / gc.getScale()); // ȭ���� �ػ�?�����Ͽ� ���� �ùٸ��� �����ϱ� ����
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseX = (int)(e.getX() / gc.getScale()); // ȭ���� �ػ�?�����Ͽ� ���� �ùٸ��� �����ϱ� ����
		mouseY = (int)(e.getY() / gc.getScale()); // ȭ���� �ػ�?�����Ͽ� ���� �ùٸ��� �����ϱ� ����
		// ���� �������� �׵θ� �κб��� ����ؼ� �ؾ�������, ���� �ʿ������ ���Ƽ� ��ŵ��.
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
