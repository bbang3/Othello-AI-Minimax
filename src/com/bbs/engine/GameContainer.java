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
	private final double UPDATE_CAP = 1.0/60.0; // 업데이트 단위. 1초에 60번 업데이트하겠다는 소리
	private int width = 320, height = 320; // 창의 너비와 높이
	private float scale = 3f; // 비율
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
		thread.run(); // 이 스레드를 메인 스레드로 실행시킴.
	}
	
	public void stop(){}
	
	public void run()
	{
		running = true;
		
		boolean render = false;
		double firstTime = 0; // 처음 시간
		double lastTime = System.nanoTime() / (double)1e9; // 마지막 시간
		double passedTime = 0; // 흐른 시간
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
			
			while(unprocessedTime >= UPDATE_CAP) // 업데이트 단위만큼의 시간이 흘렀을 경우 업데이트함
			{
				unprocessedTime -= UPDATE_CAP;
				render = true; // update할 때만 true로 바꾸어 렌더링하게 함
				
				//TODO: Update game
				game.update(this, (float)UPDATE_CAP);
				
				input.update();
				
				if(frameTime >= 1.0) // nanoTime을 1e9로 나눴기 때문에 1초마다 이 if문에 들어가게 됨.
				{
					frameTime = 0;
					fps = frames; // 1초동안 센 프레임 개수 -> FPS임
					frames = 0;
				}
			}
			
			if(render)
			{
				//TODO: Render game
				renderer.clear();
				game.render(this, renderer);
				//renderer.drawText("FPS : " + fps, 0, 0, 0xff00ffff);
				window.update(); // 렌더링할 때 update 실행
				frames++; // 렌더링할 때마다 프레임의 개수를 증가시킴
			}
			else // Render 하지 않을 때 -> 화면에 변화가 없는 경우이므로 Thread.sleep을 실행하여 CPU 점유율을 줄임.
			{
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		dispose(); // 마지막엔 dispose()를 실행함. (이 게임에서는 소프트웨어 렌더러를 쓰기 때문에 크게 중요하지는 않음) 
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
