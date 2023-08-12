package com.bbs.game;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.bbs.engine.AbstractGame;
import com.bbs.engine.GameContainer;
import com.bbs.engine.Renderer;
import com.bbs.engine.gfx.Font;
import com.bbs.engine.gfx.Image;

public class GameManager extends AbstractGame
{
	public static final int GRID_SIZE = 64;
	public static final int FONT_SIZE = 25;
	public static final int BACKGROUND_COLOR = 0xffb8b8b8;
	public static final int TOP_BAR_HEIGHT = 20;

	private Othello othello;
	private AI ai;

	private Image blackImage;
	private Image whiteImage;
	private Image boardImage;
	private Image noticeImage;
	private Image dotImage;

	private Font Godic = new Font("/fonts/Godic.png");
	private Font HY = new Font("/fonts/HY.png");

	private boolean isInit;
	private boolean isNotice = false;
	private boolean isGameOver = false;
	private boolean isAImode;

	public GameManager()
	{
		blackImage = new Image("/64_black.png");
		whiteImage = new Image("/64_white.png");
		boardImage = new Image("/512_board.png");
		noticeImage = new Image("/notice.png");
		dotImage = new Image("/dot.png");

		othello = new Othello();
		ai = new AI(othello);

		isInit = true;
	}

	@Override
	public void update(GameContainer gc, float dt)
	{
		if(isInit)
		{
			if(gc.getInput().isKeyDown(KeyEvent.VK_A))
				{isAImode = true; isInit = false;}
			else if(gc.getInput().isKeyDown(KeyEvent.VK_B))
				{isAImode = false; isInit = false;}
			
			return;
		}
		else if(isAImode)
		{
			if(gc.getInput().isButtonDown(MouseEvent.BUTTON1))
			{
				int x = gc.getInput().getMouseX();
				int y = gc.getInput().getMouseY();

				if(x >= 0 && x <= GRID_SIZE && y >= 0 && y <= GRID_SIZE)
				{
					ai.setColor(Othello.WHITE);
					isAImode = false;
				}
				else if(x >= gc.getWidth() - GRID_SIZE && x <= gc.getWidth() && y >= 0 && y <= GRID_SIZE)
				{
					ai.setColor(Othello.BLACK);
					isAImode = false;
				}
			}
			return;
		}


		if(isNotice)
		{
			if(gc.getInput().isButtonDown(MouseEvent.BUTTON1))
			{
				int x = gc.getInput().getMouseX();
				int y = gc.getInput().getMouseY();

				int offX = (gc.getWidth() - noticeImage.getW()) / 2;
				int offY = (gc.getHeight() - noticeImage.getH()) / 2;

				if(x >= offX && x <= offX + noticeImage.getW() && y >= offY && y <= offY + noticeImage.getH())
				{
					isNotice = false;
				}
			}

		}
		else if(isGameOver)
		{
			if(gc.getInput().isButtonDown(MouseEvent.BUTTON1))
				System.exit(0);
		} 
		


		else if(gc.getInput().isButtonDown(MouseEvent.BUTTON1))
		{
			if(othello.hasNothingtoDo())
			{
				isNotice = true;
				othello.changeTurn();
				othello.update();
			}
			else
			{
				if(othello.turn == ai.getColor())
				{
					ai.select();
				}
				else
				{
					int x = (gc.getInput().getMouseX()) / GRID_SIZE;
					int y = (gc.getInput().getMouseY() - GRID_SIZE) / GRID_SIZE;
					othello.select(x, y);

				}
				
				if(othello.isChanged())
				{
					othello.countStone();
					othello.changeTurn();
					othello.update();
				}
			}

			if(othello.isGameSet()) isGameOver = true;
		}
		/*else if(gc.getInput().isButtonUp(MouseEvent.BUTTON1))
			{
				int x = (gc.getInput().getMouseX()) / GRID_SIZE;
				int y = (gc.getInput().getMouseY() - GRID_SIZE) / GRID_SIZE;
				if(othello.isSafe(x, y))
				{
					othello.select(x, y);
					if(othello.isChanged())
					{
						othello.update(othello.turn);
						othello.changeTurn();

						if(!othello.getCan()[othello.turn])
						{
							isNotice = true;
						}
					}
				}
			}*/
	}

	@Override
	public void render(GameContainer gc, Renderer renderer)
	{
		renderer.drawFillRect(0, 0, gc.getWidth(), gc.getHeight(), BACKGROUND_COLOR);
		renderer.drawImage(boardImage, (gc.getWidth() - boardImage.getW()) / 2, gc.getHeight() - boardImage.getH());

		renderer.drawImage(blackImage, 0, 0);
		renderer.drawImage(whiteImage, gc.getWidth() - whiteImage.getW(), 0);
		
		if(isInit)
		{
			renderer.drawText("A : 1P / B : 2P", (gc.getWidth() - FONT_SIZE * 12) / 2, 20, 0xff000000, HY);
			return;
		}
		if(isAImode)
		{
			if(ai.getColor() == -1)
				renderer.drawText("Choose Color", (gc.getWidth() - FONT_SIZE * 12) / 2 , 20, 0xff7e7e7e, HY);
			else if(ai.getColor() == Othello.BLACK)
				renderer.drawText("BLACK", (gc.getWidth() - FONT_SIZE * 6) / 2, 20, 0xff000000, HY);
			else if(ai.getColor() == Othello.WHITE)
				renderer.drawText("WHITE", (gc.getWidth() - FONT_SIZE * 6) / 2, 20, 0xffffffff, HY);

			return;
		}
		if(isGameOver)
		{
			String str = "";
			int color = 0xff000000;
			if(othello.getBlackCount() == othello.getWhiteCount())
			{
				str = "Draw";
				color = 0xff7e7e7e;
			}
			else if(othello.getBlackCount() > othello.getWhiteCount())
				str = "Black Win";	
			else 
			{
				str = "White Win";
				color = 0xffffffff;
			}
			renderer.drawText(str, (gc.getWidth() - str.length() * FONT_SIZE) / 2, TOP_BAR_HEIGHT, color, HY);
		}
		else 
		{
			renderer.drawText("Othello", gc.getWidth() / 2 - 80, TOP_BAR_HEIGHT, 0xff000000, HY);
			if(othello.turn == Othello.BLACK)
				renderer.drawText("<", GRID_SIZE * 2, TOP_BAR_HEIGHT, 0xff000000, HY);
			else
				renderer.drawText(">", gc.getWidth() - GRID_SIZE * 2 - 23, TOP_BAR_HEIGHT, 0xff000000, HY);	
		}

		renderer.drawText(othello.getBlackCount() / 10 + "" + othello.getBlackCount() % 10, GRID_SIZE + (GRID_SIZE - FONT_SIZE * 2) / 2, TOP_BAR_HEIGHT, 0xff000000, HY);
		renderer.drawText(othello.getWhiteCount() / 10 + "" + othello.getWhiteCount() % 10, gc.getWidth() - GRID_SIZE * 2 + (GRID_SIZE - FONT_SIZE * 2) / 2, TOP_BAR_HEIGHT, 0xff000000, HY);


		for (int i = 0; i < othello.getBoard().length; i++)
		{
			for (int j = 0; j < othello.getBoard()[i].length; j++)
			{
				int x = j * GRID_SIZE;
				int y = i * GRID_SIZE + GRID_SIZE;
				if(othello.getBoard()[i][j] == Othello.BLACK)
					renderer.drawImage(blackImage, x, y);
				else if(othello.getBoard()[i][j] == Othello.WHITE)
					renderer.drawImage(whiteImage, x, y);
				else if(othello.getCanPlace()[othello.turn][i][j])
					renderer.drawImage(dotImage, x + (GRID_SIZE - dotImage.getW()) / 2, y + (GRID_SIZE - dotImage.getH()) / 2);
			
				if(othello.getRecentMove().x == j && othello.getRecentMove().y == i)
					renderer.drawFillRect(x + (GRID_SIZE - 10) / 2, y + (GRID_SIZE - 10) / 2, 10, 10, 0xffff0000);
			}
		}
		if(isNotice)
		{
			renderer.drawImage(noticeImage, (gc.getWidth() - noticeImage.getW()) / 2, (gc.getHeight() - noticeImage.getH()) / 2);	
		}
	}

	public static void main(String[] args)
	{
		GameContainer gc = new GameContainer(new GameManager());
		gc.setWidth(512);
		gc.setHeight(576);
		gc.setScale(1f);
		gc.start();
	}
}
