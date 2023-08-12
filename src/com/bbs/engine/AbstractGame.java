package com.bbs.engine;
// 게임의 추상 클래스
public abstract class AbstractGame
{
	// dt는 UPDATE_CAP과 같은 의미(리소스 때문에 getter 안쓰고 이거 씀)
	public abstract void update(GameContainer gc, float dt);
	public abstract void render(GameContainer gc, Renderer render);
}
