package com.bbs.engine;

import java.awt.image.DataBufferInt;

import com.bbs.engine.gfx.Font;
import com.bbs.engine.gfx.Image;

public class Renderer
{
	private int pW, pH; // (픽셀) 높이, 너비 자주 쓸 거라 변수로 사용
	private int[] pixels; // 픽셀 저장

	public Renderer(GameContainer gc)
	{
		pW = gc.getWidth();
		pH = gc.getHeight();
		// Window 객체의 Image의 픽셀값들을 모두 받아와 저장. 이것을 수정 시 실제로 바뀜
		pixels = ((DataBufferInt) gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
	}

	// 스크린을 모두 지우는 함수
	public void clear()
	{
		for (int i = 0; i < pixels.length; i++)
		{
			pixels[i] = 0;
		}
	}
	// (x, y) 위치의 픽셀을 value로 설정하는 함수
	public void setPixel(int x, int y, int value)
	{
		int alpha = ((value >> 24) & 0xff);
		// 0xffff00ff : 핑크색인데 이 색을 alpha(안 보이는 색)로 설정할 것임.(가장 안 쓸 만한 색이라)
		if((x < 0 || x >= pW || y < 0 || y >= pH) || alpha == 0)
			return;
		// 1차원 배열에서 픽셀들은 아래와 같이 저장되어 있음.
		pixels[x + y * pW] = value;
	}
	// text를 (offX, offY)에 color색으로 지정된 폰트로 출력하는 함수
	public void drawText(String text, int offX, int offY, int color, Font font)
	{
		//text = text.toUpperCase(); // 대문자밖에 안만들어서 전부 대문자로 출력해야함
		int offset = 0;
		
		for (int i = 0; i < text.length(); i++)
		{
			int unicode = text.codePointAt(i); // 공백이 유니코드에서 32부터 시작함
		
			for(int y = 0; y < font.getFontImage().getH(); y++)
			{
				for(int x = 0; x < font.getWidths()[unicode]; x++)
				{
					if(font.getFontImage().getP()[(x + font.getOffsets()[unicode]) + y * font.getFontImage().getW()] == 0xffffffff) // 하양(폰트가 하얀색임)
					{
						setPixel(x + offX + offset, y + offY, color);
					}
				}
			}
			
			offset += font.getWidths()[unicode]; // 한 글자 그리고 난 뒤 offset값 변경
		}
	}
	
	// 원하는 이미지를 (offX, offY) 위치에 그리는 함수
	public void drawImage(Image image, int offX, int offY)
	{
		// 이미지가 화면에 아얘 그려지지 않는 경우에는 for문조차 돌지 않음.
		if(offX < -image.getW()) return;
		if(offY < -image.getH()) return;
		if(offX >= pW) return;
		if(offY >= pH) return;

		int newX = 0, newY = 0;
		int newWidth = image.getW(), newHeight = image.getH();

		// 이미지가 화면 바깥으로 나갈 경우 너비와 높이를 그만큼 잘라서 연산 줄임.
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