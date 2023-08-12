package com.bbs.game;

import java.util.Vector;

public class Othello
{
	public static final int WIDTH = 8;
	public static final int HEIGHT = 8;

	public static final int NONE = -1;
	public static final int BLACK = 0;
	public static final int WHITE = 1;	

	public int turn = BLACK; // 0 : BLACK
	public int Nturn = WHITE;
	
	private int whiteCount = 0;
	private int blackCount = 0;
	private int[][] board;
	
	private boolean[][][] canPlace;
	private boolean[] can;
	private boolean isChanged;
	
	private Pos recentMove = new Pos(-1,-1);
		
	public Othello()
	{
		board = new int[HEIGHT][WIDTH];
		canPlace = new boolean[2][HEIGHT][WIDTH];
		can = new boolean[2];
		
		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board[i].length; j++)
			{
				board[i][j] = NONE;
			}
		}
		
//		board[0][0] = WHITE;
//		board[3][3] = WHITE;
//		board[3][4] = BLACK;
		board[3][3] = board[4][4] = WHITE;
		board[3][4] = board[4][3] = BLACK;
		blackCount = 2;
		whiteCount = 2;
		
		update(BLACK);
	}

	public void update(int color)
	{
		can[color] = false;
		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board[i].length; j++)
			{
				canPlace[color][i][j] = canFlip(board, j, i, color);
				
				can[color] |= canPlace[color][i][j];
			}
		}
	}
	
	public void update()
	{
		can[turn] = false;
		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board[i].length; j++)
			{
				canPlace[turn][i][j] = canFlip(board, j, i, turn);
				
				can[turn] |= canPlace[turn][i][j];
			}
		}
	}
	
	public void select(int x, int y)
	{
		isChanged = false;
		
		if(isSafe(x, y) && canPlace[turn][y][x])
		{
			flip(board, x, y, turn);
			board[y][x] = turn;
			recentMove = new Pos(x, y);
			isChanged = true;
		}
		else
		{
			System.out.println("그 곳에는 둘 수 없습니다.");
		}
		
		
		/*if(!can[turn])
		{
			System.out.println("둘 수 있는 곳이 없습니다. 다음 턴으로 넘어갑니다.");
			turn = opp(turn);
		}
		else if(!canPlace[turn][y][x])
		{
			System.out.println("그곳에는 둘 수 없습니다.");
		}
		else if(canPlace[turn][y][x])
		{
			flip(x, y, turn);
			board[y][x] = turn;

			turn = opp(turn);
			countStone();
		}

		update(turn);*/
	}
	
	public boolean flip(int[][] board, int x, int y, int color)
	{
		if(!(isSafe(x, y) && board[y][x] == NONE))
			return false;
		
		int d[][] = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}}; // 상하, 좌우, 우하대각, 우상대각		
		
		Vector<Pos> posList = new Vector<Pos>();
		boolean isChanged = false;
		
		for(int i = 0; i < d.length; i++)
		{
			int nx = x + d[i][0], ny = y + d[i][1];
			
			while(isSafe(nx, ny) && board[ny][nx] == opp(color))
			{
				posList.add(new Pos(nx, ny));
				nx += d[i][0]; ny += d[i][1];
			}
			if(isSafe(nx, ny) && board[ny][nx] == color && !posList.isEmpty())
			{
				isChanged = true;
				setColor(board, posList, color);
			}
			else posList.clear();
		}
		
		return isChanged;
	}
	
	public boolean canFlip(int[][] board, int x, int y, int color)
	{
		if(board[y][x] != NONE) return false;
		
		int d[][] = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}}; // 상하, 좌우, 우하대각, 우상대각	
	
		for(int i = 0; i < d.length; i++)
		{
			int nx = x + d[i][0], ny = y + d[i][1];
			int cnt = 0;
			while(isSafe(nx, ny) && board[ny][nx] == opp(color))
			{
				cnt++;
				nx += d[i][0]; ny += d[i][1];
			}
			if(isSafe(nx, ny) && board[ny][nx] == color && cnt != 0)
			{
				return true;
			}
		}
		return false;
	}
	
	public void changeTurn()
	{
		turn = opp(turn);
	}
	
	public boolean isGameSet()
	{
		if(!can[BLACK] && !can[WHITE]) return true;
		else if(blackCount + whiteCount == WIDTH * HEIGHT) return true;
		else if(blackCount == 0 || whiteCount == 0) return true;
		
		return false;
	}
	
	public void setColor(int[][] board, Vector<Pos> list, int color)
	{
		for(Pos pos : list)
		{
			board[pos.y][pos.x] = color;
		}
	}
	
	public void countStone()
	{
		int b = 0, w = 0;
		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board[i].length; j++)
			{
				if(board[i][j] == BLACK) b++;
				else if(board[i][j] == WHITE) w++;
			}
		}
		blackCount = b;
		whiteCount = w;
	}

	public boolean hasNothingtoDo()
	{
		return !can[turn];
	}
	
	public int opp(int color)
	{
		if(color == BLACK) return WHITE;
		else return BLACK;
	}
	
	public boolean isSafe(int x, int y)
	{
		return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
	}
	
	public int[][] getBoard()
	{
		return board;
	}

	public void setBoard(int[][] board)
	{
		this.board = board;
	}

	public int getWhiteCount()
	{
		return whiteCount;
	}

	public void setWhiteCount(int whiteCount)
	{
		this.whiteCount = whiteCount;
	}

	public int getBlackCount()
	{
		return blackCount;
	}

	public void setBlackCount(int blackCount)
	{
		this.blackCount = blackCount;
	}

	public boolean[] getCan()
	{
		return can;
	}

	public boolean[][][] getCanPlace()
	{
		return canPlace;
	}

	public boolean isChanged()
	{
		return isChanged;
	}

	public Pos getRecentMove()
	{
		return recentMove;
	}
}
