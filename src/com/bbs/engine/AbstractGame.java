package com.bbs.engine;
// ������ �߻� Ŭ����
public abstract class AbstractGame
{
	// dt�� UPDATE_CAP�� ���� �ǹ�(���ҽ� ������ getter �Ⱦ��� �̰� ��)
	public abstract void update(GameContainer gc, float dt);
	public abstract void render(GameContainer gc, Renderer render);
}
