package com.bbs.engine;

import java.awt.image.DataBufferInt;

import com.bbs.engine.gfx.Font;
import com.bbs.engine.gfx.Image;

public class Renderer
{
	private int pW, pH; // (�ȼ�) ����, �ʺ� ���� �� �Ŷ� ������ ���
	private int[] pixels; // �ȼ� ����

	public Renderer(GameContainer gc)
	{
		pW = gc.getWidth();
		pH = gc.getHeight();
		// Window ��ü�� Image�� �ȼ������� ��� �޾ƿ� ����. �̰��� ���� �� ������ �ٲ�
		pixels = ((DataBufferInt) gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
	}

	// ��ũ���� ��� ����� �Լ�
	public void clear()
	{
		for (int i = 0; i < pixels.length; i++)
		{
			pixels[i] = 0;
		}
	}
	// (x, y) ��ġ�� �ȼ��� value�� �����ϴ� �Լ�
	public void setPixel(int x, int y, int value)
	{
		int alpha = ((value >> 24) & 0xff);
		// 0xffff00ff : ��ũ���ε� �� ���� alpha(�� ���̴� ��)�� ������ ����.(���� �� �� ���� ���̶�)
		if((x < 0 || x >= pW || y < 0 || y >= pH) || alpha == 0)
			return;
		// 1���� �迭���� �ȼ����� �Ʒ��� ���� ����Ǿ� ����.
		pixels[x + y * pW] = value;
	}
	// text�� (offX, offY)�� color������ ������ ��Ʈ�� ����ϴ� �Լ�
	public void drawText(String text, int offX, int offY, int color, Font font)
	{
		//text = text.toUpperCase(); // �빮�ڹۿ� �ȸ��� ���� �빮�ڷ� ����ؾ���
		int offset = 0;
		
		for (int i = 0; i < text.length(); i++)
		{
			int unicode = text.codePointAt(i); // ������ �����ڵ忡�� 32���� ������
		
			for(int y = 0; y < font.getFontImage().getH(); y++)
			{
				for(int x = 0; x < font.getWidths()[unicode]; x++)
				{
					if(font.getFontImage().getP()[(x + font.getOffsets()[unicode]) + y * font.getFontImage().getW()] == 0xffffffff) // �Ͼ�(��Ʈ�� �Ͼ����)
					{
						setPixel(x + offX + offset, y + offY, color);
					}
				}
			}
			
			offset += font.getWidths()[unicode]; // �� ���� �׸��� �� �� offset�� ����
		}
	}
	
	// ���ϴ� �̹����� (offX, offY) ��ġ�� �׸��� �Լ�
	public void drawImage(Image image, int offX, int offY)
	{
		// �̹����� ȭ�鿡 �ƾ� �׷����� �ʴ� ��쿡�� for������ ���� ����.
		if(offX < -image.getW()) return;
		if(offY < -image.getH()) return;
		if(offX >= pW) return;
		if(offY >= pH) return;

		int newX = 0, newY = 0;
		int newWidth = image.getW(), newHeight = image.getH();

		// �̹����� ȭ�� �ٱ����� ���� ��� �ʺ�� ���̸� �׸�ŭ �߶� ���� ����.
		if(offX < 0) { newX = -offX; }
		if(offY < 0) { newY = -offY; }
		if(newWidth + offX >= pW) { newWidth = pW - offX; }
		if(newHeight + offY >= pH) { newHeight = pH - offY; }

		for (int y = newY; y < newHeight; y++)
		{
			for (int x = newX; x < newWidth; x++)
			{
				setPixel(x + offX, y + offY, image.getP()[x + y * image.getW()]);
			}
		}
	}
	
	public void drawRect(int offX, int offY, int width, int height, int color)
	{
		for(int y = 0; y <= height; y++)
		{
			setPixel(offX			,y + offY,color);
			setPixel(offX + width	,y + offY,color);
		}
		for(int x = 0; x <= width; x++)
		{
			setPixel(x + offX,offY			,color);
			setPixel(x + offX,offY + height	,color);
		}
	}
	public void drawFillRect(int offX, int offY, int width, int height, int color)
	{
		//Don't Render code
		if(offX < -width) return;
		if(offY < -height) return;
		if(offX >= pW) return;
		if(offY >= pH) return;
		
		for(int y = 0; y < height;y++)
		{
			for(int x = 0; x < width;x++)
			{
				setPixel(x + offX,y +offY,color);
			}
		}
	}
}