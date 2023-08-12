package com.bbs.engine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Window
{

	private JFrame frame; // JFrame
	private BufferedImage image; // BufferedImage
	private Canvas canvas;
	private BufferStrategy bs;
	private Graphics g;

	public Window(GameContainer gc)
	{ // gc로부터 높이, 너비 등의 정보 받아옴.
		// 너비, 높이, 이미지 타입
		image = new BufferedImage(gc.getWidth(), gc.getHeight(), BufferedImage.TYPE_INT_RGB);
		canvas = new Canvas();
		// 너비, 높이에 각각 scale을 곱한 값으로 Dimension을 생성함.
		Dimension s = new Dimension((int) (gc.getWidth() * gc.getScale()), (int) (gc.getHeight() * gc.getScale()));
		// canvas의 크기를 s로 하고, 더 이상 변경될 수 없게 함.
		canvas.setPreferredSize(s);
		canvas.setMaximumSize(s);
		canvas.setMinimumSize(s);
		// setting for frame
		frame = new JFrame(gc.getTitle());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(canvas, BorderLayout.CENTER); // canvas를 프레임에 올림
		frame.pack(); // frame의 크기를 하위 컴포넌트의 크기로 조정함.
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		// setting for bufferstrategy
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		g = bs.getDrawGraphics();
	}

	public void update()
	{
		// 캔버스 크기만큼의 그림을 그림 bufferstrategy에
		g.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
		bs.show();
	}

	public BufferedImage getImage()
	{
		return image;
	}

	public Canvas getCanvas()
	{
		return canvas;
	}

	public JFrame getFrame()
	{
		return frame;
	}
}
