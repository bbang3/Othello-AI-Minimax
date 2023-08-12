package com.bbs.engine.gfx;

public class Font
{
	public static final Font STANDARD = new Font("/fonts/Arial.png");
	
	private Image fontImage;
	private int[] offsets;
	private int[] widths; // 글자마다 각각 너비가 다름(같게 하면 안이쁨)
	public Font(String path) // 폰드 경로
	{
		fontImage = new Image(path);
		
		offsets = new int[256]; // 58 유니코드를 쓸 거임
		widths = new int[256];
		
		int unicode = 0;
		
		for (int i = 0; i < fontImage.getW(); i++)
		{
			if(fontImage.getP()[i] == 0xff0000ff) // 파랑
			{
				offsets[unicode] = i;
			}
			if(fontImage.getP()[i] == 0xffffff00)
			{
				widths[unicode] = i - offsets[unicode];
				unicode++;
			}
		}
	}
	public Image getFontImage()
	{
		return fontImage;
	}
	public void setFontImage(Image fontImage)
	{
		this.fontImage = fontImage;
	}
	public int[] getOffsets()
	{
		return offsets;
	}
	public void setOffsets(int[] offsets)
	{
		this.offsets = offsets;
	}
	public int[] getWidths()
	{
		return widths;
	}
	public void setWidths(int[] widths)
	{
		this.widths = widths;
	}
}
