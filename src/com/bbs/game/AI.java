package com.bbs.game;

import java.util.Arrays;
import java.util.Vector;

public class AI
{
	private static final int INF = 0x7fffffff;
	
	private Othello othello;
	private int color;
	private final int MAX_DEPTH = 7;
	
	public AI(Othello game)
	{
		this.othello = game;
		this.color = Othello.NONE;
	}
	
	public void select()
	{
		int[][] vrtlBoard = new int[Othello.HEIGHT][Othello.WIDTH];

		for(int i = 0; i < Othello.HEIGHT; i++)
		{
			for(int j = 0; j < Othello.WIDTH; j++)
			{
				vrtlBoard[i][j] = othello.getBoard()[i][j];
			}
		}
		
		Pos pos = new Pos();
		int bestValue = -INF;
		Vector<Pos> posList = getPossibleMoves(vrtlBoard, color);
		for(Pos p : posList)
		{
			int[][] next = getCopy(vrtlBoard);
			boolean[] cant = new boolean[2];
			othello.flip(next, p.x, p.y, this.color);
			next[p.y][p.x] = this.color;
			
			int value = minimax(next, cant, 1, -INF, INF, othello.opp(this.color));
			if(value > bestValue)
			{
				bestValue = value;
				pos = p;
			}
		}
		if(bestValue == -INF)
		{
			System.out.println("AI resigned");
			othello.select(posList.firstElement().x, posList.firstElement().y);
		}
		else othello.select(pos.x, pos.y);
	}
	// depth should be even num
	/*
	 * 현재의 판 상태가 노드
	 * 현재 판에서 curColor색이 돌을 둘 차례 -> 그 노드의 자식들
	 * 
	 * 	*/
	public int minimax(int[][] board, boolean[] cant, int depth, int alpha, int beta, int curColor)
	{
		if(depth == MAX_DEPTH || (cant[Othello.BLACK] && cant[Othello.WHITE]))
		{
			int value = evaluate(board, cant, this.color);
			return value;
		}
		int bestValue;		
		if(curColor == this.color) bestValue = -INF;
		else bestValue = INF;
		
		Vector<Pos> posList = getPossibleMoves(board, curColor);
		if(posList.isEmpty())
		{
			boolean[] nCant = Arrays.copyOf(cant, cant.length);
			nCant[curColor] = true;
			return minimax(board, nCant, depth + 1, alpha, beta, othello.opp(curColor));
		}
		for(Pos p : posList)
		{
			int[][] next = getCopy(board);
			
			othello.flip(next, p.x, p.y, curColor);
			next[p.y][p.x] = curColor;
			
			int value = minimax(next, cant, depth + 1, alpha, beta, othello.opp(curColor));
			if(curColor == this.color)
			{
				bestValue = Math.max(bestValue, value);
				alpha = Math.max(alpha, value);
				
				if(alpha >= beta) break;
			}
			else
			{
				bestValue = Math.min(bestValue, value);
				beta = Math.min(beta, value);
				
				if(alpha >= beta) break;
			}
		}
		return bestValue;
	}
	
	public Vector<Pos> getPossibleMoves(int[][] board, int color)
	{
		Vector<Pos> posList = new Vector<Pos>();
		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board[i].length; j++)
			{
				if(othello.canFlip(board, j, i, color))
					posList.add(new Pos(j, i));
			}
		}
		
		return posList;
	}
	
	public Vector<Pos> flip(int[][] board, int x, int y, int color)
	{
		int d[][] = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}}; // 상하, 좌우, 우하대각, 우상대각		
		
		Vector<Pos> posList = new Vector<Pos>();
		Vector<Pos> temp = new Vector<Pos>();
		
		for(int i = 0; i < d.length; i++)
		{
			int nx = x + d[i][0], ny = y + d[i][1];
			while(othello.isSafe(nx, ny) && board[ny][nx] == othello.opp(color))
			{
				temp.add(new Pos(nx, ny));
				nx += d[i][0]; ny += d[i][1];
			}
			if(othello.isSafe(nx, ny) && board[ny][nx] == color && !temp.isEmpty())
			{
				for(Pos p : temp) posList.add(p);
			}
			temp.clear();
		}
		return posList;
	}
	
	public int evaluate(int[][] board, boolean[] cant, int myColor)
	{
		int value = 0, myPoint = 0, enemyPoint = 0;
		
		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board[i].length; j++)
			{
				if(board[i][j] == myColor) myPoint++;
				else if(board[i][j] == othello.opp(myColor)) enemyPoint++;
			}
		}

		if(cant[myColor] && cant[othello.opp(myColor)])
		{
			if(myPoint > enemyPoint) return INF;
			else if(enemyPoint > myPoint) return -INF;
		}
//		if(myPoint + enemyPoint == Othello.WIDTH * Othello.HEIGHT)
//		{
//			if(myPoint > enemyPoint) return INF;
//			else if(enemyPoint > myPoint) return -INF;
//		}
//		if(myPoint == 0) return -INF;
//		if(enemyPoint == 0) return INF;
		if(myPoint == Othello.WIDTH * Othello.HEIGHT || enemyPoint == 0) return INF;
		else if(enemyPoint == Othello.WIDTH * Othello.HEIGHT || myPoint == 0) return -INF;
		
		int sideWeight = 5, conerWeight = 30;
		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board[i].length; j++)
			{
				if(isConer(j, i, Othello.WIDTH, Othello.HEIGHT))
				{
					if(board[i][j] == myColor) myPoint += conerWeight;
					else if(board[i][j] == othello.opp(myColor)) enemyPoint += conerWeight;
				}
				else if(isSide(j, i, Othello.WIDTH, Othello.HEIGHT))
				{
					if(board[i][j] == myColor) myPoint += sideWeight;
					else if(board[i][j] == othello.opp(myColor)) enemyPoint += sideWeight;
				}
			}
		}
		value = myPoint - enemyPoint;
		
		return value;
	}
	
	public int getFlipNum(int[][] board, int x, int y, int color)
	{
		int d[][] = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}}; // 상하, 좌우, 우하대각, 우상대각	
		int sum = 0;
		for(int i = 0; i < d.length; i++)
		{
			int nx = x + d[i][0], ny = y + d[i][1];
			int cnt = 0;
			while(othello.isSafe(nx, ny) && board[ny][nx] == othello.opp(color))
			{
				cnt++;
				nx += d[i][0]; ny += d[i][1];
			}
			if(othello.isSafe(nx, ny) && board[ny][nx] == color && cnt != 0)
			{
				sum += cnt;
			}
		}
		return sum;
	}
	
/*	public boolean isGameSet(int[][] board)
	{
		
	}
*/	
	public boolean isSide(int x, int y, int w, int h)
	{
		if(x == 0 || x == w - 1) return true;
		if(y == 0 || y == h - 1) return true;
		return false;
	}
	
	public boolean isConer(int x, int y, int w, int h)
	{
		if(x == 0 && y == 0) return true;
		if(x == 0 && y == h - 1) return true;
		if(x == w - 1 && y == 0) return true;
		if(x == w - 1 && y == h -1) return true;
		return false;
	}
	public int[][] getCopy(int[][] board)
	{
		int[][] b = new int[board.length][board[0].length];
		
		for (int i = 0; i < board.length; i++)
		{
			for (int j = 0; j < board[i].length; j++)
			{
				b[i][j] = board[i][j];
			}
		}
		return b;
	}
	
	public void print(boolean[][] arr)
	{
		for (int i = 0; i < arr.length; i++, System.out.println())
		{
			for (int j = 0; j < arr[i].length; j++)
			{
				System.out.print((arr[i][j] ? 1 : 0) + " ");
			}
		}
		System.out.println();
	}
	
	public int getColor()
	{
		return color;
	}

	public void setColor(int color)
	{
		this.color = color;
	}
	
	
}
