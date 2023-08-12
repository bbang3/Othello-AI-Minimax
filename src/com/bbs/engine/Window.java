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
	{ // gc�κ��� ����, �ʺ� ���� ���� �޾ƿ�.
		// �ʺ�, ����, �̹��� Ÿ��
		image = new BufferedImage(gc.getWidth(), gc.getHeight(), BufferedImage.TYPE_INT_RGB);
		canvas = new Canvas();
		// �ʺ�, ���̿� ���� scale�� ���� ������ Dimension�� ������.
		Dimension s = new Dimension((int) (gc.getWidth() * gc.getScale()), (int) (gc.getHeight() * gc.getScale()));
		// canvas�� ũ�⸦ s�� �ϰ�, �� �̻� ����� �� ���� ��.
		canvas.setPreferredSize(s);
		canvas.setMaximumSize(s);
		canvas.setMinimumSize(s);
		// setting for frame
		frame = new JFrame(gc.getTitle());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(canvas, BorderLayout.CENTER); // canvas�� �����ӿ� �ø�
		frame.pack(); // frame�� ũ�⸦ ���� ������Ʈ�� ũ��� ������.
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
		// ĵ���� ũ�⸸ŭ�� �׸��� �׸� bufferstrategy��
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
