package com.bbs.engine;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameContainer implements Runnable {
	
	private Thread thread;
	private Window window;
	private Renderer renderer;
	private Input input;
	private AbstractGame game;
	
	private boolean running = false;
	private final double UPDATE_CAP = 1.0/60.0; // ������Ʈ ����. 1�ʿ� 60�� ������Ʈ�ϰڴٴ� �Ҹ�
	private int width = 320, height = 320; // â�� �ʺ�� ����
	private float scale = 3f; // ����
	private String title = "Othello";
	
	public GameContainer(AbstractGame game)
	{
		this.game = game;
	}
	
	public void start()
	{
		window = new Window(this);
		renderer = new Renderer(this);
		input = new Input(this);
		
		thread = new Thread(this);
		thread.run(); // �� �����带 ���� ������� �����Ŵ.
	}
	
	public void stop(){}
	
	public void run()
	{
		running = true;
		
		boolean render = false;
		double firstTime = 0; // ó�� �ð�
		double lastTime = System.nanoTime() / (double)1e9; // ������ �ð�
		double passedTime = 0; // �帥 �ð�
		double unprocessedTime = 0;
		
		double frameTime = 0;
		int frames = 0;
		int fps = 0;
		
		while(running)
		{
			render = false;
			firstTime = System.nanoTime() / (double)1e9;
			passedTime = firstTime - lastTime;
			lastTime = firstTime;
			
			unprocessedTime += passedTime;
			frameTime += passedTime;
			
			while(unprocessedTime >= UPDATE_CAP) // ������Ʈ ������ŭ�� �ð��� �귶�� ��� ������Ʈ��
			{
				unprocessedTime -= UPDATE_CAP;
				render = true; // update�� ���� true�� �ٲپ� �������ϰ� ��
				
				//TODO: Update game
				game.update(this, (float)UPDATE_CAP);
				
				input.update();
				
				if(frameTime >= 1.0) // nanoTime�� 1e9�� ������ ������ 1�ʸ��� �� if���� ���� ��.
				{
					frameTime = 0;
					fps = frames; // 1�ʵ��� �� ������ ���� -> FPS��
					frames = 0;
				}
			}
			
			if(render)
			{
				//TODO: Render game
				renderer.clear();
				game.render(this, renderer);
				//renderer.drawText("FPS : " + fps, 0, 0, 0xff00ffff);
				window.update(); // �������� �� update ����
				frames++; // �������� ������ �������� ������ ������Ŵ
			}
			else // Render ���� ���� �� -> ȭ�鿡 ��ȭ�� ���� ����̹Ƿ� Thread.sleep�� �����Ͽ� CPU �������� ����.
			{
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		dispose(); // �������� dispose()�� ������. (�� ���ӿ����� ����Ʈ���� �������� ���� ������ ũ�� �߿������� ����) 
	}
	
	private void dispose(){}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public float getScale() 
	{
		return scale;
	}

	public void setScale(float scale) 
	{
		this.scale = scale;
	}

	public String getTitle() 
	{
		return title;
	}

	public void setTitle(String title) 
	{
		this.title = title;
	}

	public Window getWindow()
	{
		return window;
	}

	public Input getInput()
	{
		return input;
	}
}
