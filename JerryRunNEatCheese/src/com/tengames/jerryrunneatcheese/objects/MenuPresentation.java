/*
The MIT License

Copyright (c) 2014 kong <tengames.inc@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.tengames.jerryrunneatcheese.objects;

import woodyx.basicapi.accessor.SpriteAccessor;
import woodyx.basicapi.sound.SoundManager;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.tengames.jerryrunneatcheese.main.Assets;

public class MenuPresentation {
	private final static byte STATE_STAND = 0;
	private final static byte STATE_RUN = 1;
	private final static byte STATE_JUMP = 2;

	private TweenManager tweenJerry, tweenLogo, tweenCake;
	private SimpleSprite spJerry, spLogo, spCake;
	private float stateTime;
	private byte state;
	private boolean isLeft, isComplete;

	public MenuPresentation(Vector2 position, final Vector2 target, float delay, int type) {
		this.isComplete = false;
		this.stateTime = 0;
		this.state = STATE_RUN;
		this.isLeft = true;

		switch (type) {
		// jerry
		case 1:
			// create jerry
			spJerry = new SimpleSprite(Assets.taObjects.findRegion("jerry-run-1"), position.x, position.y);

			// create tween
			tweenJerry = new TweenManager();

			Tween.to(spJerry, SpriteAccessor.POS_XY, 3f).target(target.x, target.y).ease(Linear.INOUT)
					.setCallback(new TweenCallback() {
						@Override
						public void onEvent(int arg0, BaseTween<?> arg1) {
							// change state
							state = STATE_STAND;
							// flip
							isLeft = false;
							// creat new tween pause
							Timeline.createSequence().pushPause(5f).setCallback(new TweenCallback() {
								@Override
								public void onEvent(int arg0, BaseTween<?> arg1) {
									// change state
									state = STATE_RUN;
									// create new tween
									Tween.to(spJerry, SpriteAccessor.POS_XY, 1f).target(400, target.y)
											.ease(Linear.INOUT).setCallback(new TweenCallback() {
												@Override
												public void onEvent(int arg0, BaseTween<?> arg1) {
													// play sound
													SoundManager.playSound(Assets.soJump);
													// change state
													state = STATE_JUMP;
													// create final tween
													Tween.to(spJerry, SpriteAccessor.POS_XY, 0.5f)
															.target(spJerry.getX() - 20, target.y + 250).ease(Quad.OUT)
															.setCallback(new TweenCallback() {
																@Override
																public void onEvent(int arg0, BaseTween<?> arg1) {
																	// complete
																	isComplete = true;
																}
															}).start(tweenJerry);
												}
											}).start(tweenJerry);
								}
							}).start(tweenJerry);
						}
					}).start(tweenJerry);
			break;

		// logo
		case 2:
			// sp logo
			spLogo = new SimpleSprite(Assets.taObjects.findRegion("text-logo"), position.x, position.y);

			// create tween
			tweenLogo = new TweenManager();

			Timeline.createSequence()
					.push(Tween.to(spLogo, SpriteAccessor.POS_XY, 4).target((800 - spLogo.getWidth()) / 2, target.y)
							.ease(Linear.INOUT))
					.pushPause(1f).push(Tween.to(spLogo, SpriteAccessor.POS_XY, 2)
							.target((800 - spLogo.getWidth()) / 2, 320).ease(Back.INOUT))
					.start(tweenLogo);
			break;

		// cake
		case 3:
			spCake = new SimpleSprite(Assets.taObjects.findRegion("cake-1"), position.x, position.y);
			spCake.setOriginCenter(spCake);
			// create tween
			tweenCake = new TweenManager();

			Timeline.createSequence().push(Tween.set(spCake, SpriteAccessor.SCALE_XY).target(0, 0)).pushPause(7f)
					.push(Tween.to(spCake, SpriteAccessor.SCALE_XY, 1f).target(1, 1).ease(Elastic.OUT))
					.push(Tween.to(spCake, SpriteAccessor.SCALE_XY, 2f).target(1.2f, 1.2f).ease(Linear.INOUT)
							.repeatYoyo(1000, 0))
					.start(tweenCake);
			break;

		default:
			break;
		}
	}

	// update
	public void update(float deltaTime) {
		if (tweenJerry != null) {
			// update stateTime
			stateTime += deltaTime;
			// update tween
			tweenJerry.update(deltaTime);
		}
		if (tweenLogo != null)
			tweenLogo.update(deltaTime);
		if (tweenCake != null)
			tweenCake.update(deltaTime);
	}

	// render
	TextureRegion keyFrame = null;

	public void render(SpriteBatch batch) {
		// jerry
		if (spJerry != null) {
			switch (state) {
			case STATE_STAND:
				if (isLeft)
					keyFrame = Assets.aniStand.getKeyFrame(stateTime, true);
				else
					keyFrame = Assets.aniStandF.getKeyFrame(stateTime, true);
				break;
			case STATE_RUN:
				if (isLeft)
					keyFrame = Assets.aniRun.getKeyFrame(stateTime, true);
				else
					keyFrame = Assets.aniRunF.getKeyFrame(stateTime, true);
				break;
			case STATE_JUMP:
				if (isLeft)
					keyFrame = Assets.aniJump.getKeyFrame(stateTime, false);
				else
					keyFrame = Assets.aniJumpF.getKeyFrame(stateTime, false);
				break;
			default:
				break;
			}
			// render jerry
			spJerry.renderSprite(keyFrame).draw(batch);
		}
		// logo
		if (spLogo != null)
			spLogo.draw(batch);
		// cake
		if (spCake != null)
			spCake.draw(batch);
	}

	public boolean getComplete() {
		return isComplete;
	}

	public void lockComplete() {
		if (isComplete)
			this.isComplete = false;
	}
}
