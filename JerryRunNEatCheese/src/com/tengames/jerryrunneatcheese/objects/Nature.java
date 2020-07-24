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
import woodyx.basicapi.physics.BoxUtility;
import woodyx.basicapi.screen.Asset;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.TweenPaths;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.tengames.jerryrunneatcheese.main.Assets;

public class Nature {
	private SimpleSprite spCloud;
	private TweenManager tween;
	private Animation aniButterTemp, aniButterRed, aniButterBlue;
	private float stateTime;
	private int type;

	public Nature(int type) {
		this.type = type;
		this.stateTime = 0;

		switch (type) {
		case 1:
			// create cloud
			if (MathUtils.randomBoolean()) {
				spCloud = new SimpleSprite(Assets.taObjects.findRegion("cloud-1"),
						BoxUtility.ConvertToBox(-100 - MathUtils.random(100)),
						BoxUtility.ConvertToBox(200 + MathUtils.random(280)), 100, 60);
			} else {
				spCloud = new SimpleSprite(Assets.taObjects.findRegion("cloud-2"),
						BoxUtility.ConvertToBox(-100 - MathUtils.random(100)),
						BoxUtility.ConvertToBox(200 + MathUtils.random(280)), 140, 70);
			}
			break;

		case 2:
			// create butterfly
			spCloud = new SimpleSprite(new TextureRegion(Assets.txBlueButterfly, 0, 0, 70, 65),
					BoxUtility.ConvertToBox(-100 - MathUtils.random(100)),
					BoxUtility.ConvertToBox(200 + MathUtils.random(280)), 70, 65);

			// animations
			TextureRegion frameRegions[] = Asset.frameSplit(new TextureRegion(Assets.txBlueButterfly), 3, 12);
			TextureRegion frameRegionZ[] = new TextureRegion[frameRegions.length - 2];
			for (int i = 0; i < frameRegionZ.length; i++) {
				frameRegionZ[i] = frameRegions[i];
			}
			aniButterBlue = new Animation(0.1f, frameRegionZ);

			frameRegions = Asset.frameSplit(new TextureRegion(Assets.txRedButterfly), 3, 12);
			frameRegionZ = new TextureRegion[frameRegions.length - 2];
			for (int i = 0; i < frameRegionZ.length; i++) {
				frameRegionZ[i] = frameRegions[i];
			}
			aniButterRed = new Animation(0.1f, frameRegionZ);

			if (MathUtils.randomBoolean()) {
				aniButterTemp = aniButterBlue;
			} else
				aniButterTemp = aniButterRed;

			break;

		default:
			break;
		}

		// set scale
		spCloud.setScale(MathUtils.random(0.5f) + 0.5f);

		// create tween
		tween = new TweenManager();
		createTween(type);
	}

	private void createTween(final int type) {
		switch (type) {
		// tween cloud
		case 1:
			Tween.to(spCloud, SpriteAccessor.POS_XY, 8 + MathUtils.random(8))
					.target(BoxUtility.ConvertToBox(1000), spCloud.getY()).ease(Linear.INOUT)
					.setCallback(new TweenCallback() {
						@Override
						public void onEvent(int arg0, BaseTween<?> arg1) {
							// set new region
							if (MathUtils.randomBoolean()) {
								spCloud.setRegion(Assets.taObjects.findRegion("cloud-2"));
								spCloud.setSize(BoxUtility.ConvertToBox(140), BoxUtility.ConvertToBox(70));
							} else {
								spCloud.setRegion(Assets.taObjects.findRegion("cloud-1"));
								spCloud.setSize(BoxUtility.ConvertToBox(100), BoxUtility.ConvertToBox(60));
							}

							// set scale
							spCloud.setScale(MathUtils.random(0.5f) + 0.5f);
							// reset position
							spCloud.setPosition(BoxUtility.ConvertToBox(-100 - MathUtils.random(100)),
									BoxUtility.ConvertToBox(200 + MathUtils.random(280)));

							// recall
							createTween(type);
						}
					}).start(tween);
			break;

		// tween butterfly
		case 2:
			Tween.to(spCloud, SpriteAccessor.CPOS_XY, 8 + MathUtils.random(8))
					.waypoint(BoxUtility.ConvertToBox(100 + MathUtils.random(100)),
							BoxUtility.ConvertToBox(100 + MathUtils.random(300)))
					.waypoint(BoxUtility.ConvertToBox(200 + MathUtils.random(100)),
							BoxUtility.ConvertToBox(100 + MathUtils.random(200)))
					.waypoint(BoxUtility.ConvertToBox(300 + MathUtils.random(100)),
							BoxUtility.ConvertToBox(200 + MathUtils.random(200)))
					.waypoint(BoxUtility.ConvertToBox(400 + MathUtils.random(100)),
							BoxUtility.ConvertToBox(100 + MathUtils.random(100)))
					.waypoint(BoxUtility.ConvertToBox(600 + MathUtils.random(100)),
							BoxUtility.ConvertToBox(50 + MathUtils.random(150)))
					.target(BoxUtility.ConvertToBox(1000), spCloud.getY()).path(TweenPaths.catmullRom)
					.ease(Linear.INOUT).setCallback(new TweenCallback() {
						@Override
						public void onEvent(int arg0, BaseTween<?> arg1) {
							// reset size
							spCloud.setSize(BoxUtility.ConvertToBox(70), BoxUtility.ConvertToBox(65));

							// reset animation
							if (MathUtils.randomBoolean()) {
								aniButterTemp = aniButterBlue;
							} else
								aniButterTemp = aniButterRed;

							// set scale
							spCloud.setScale(MathUtils.random(0.5f) + 0.5f);

							// reset position
							spCloud.setPosition(BoxUtility.ConvertToBox(-100 - MathUtils.random(100)),
									BoxUtility.ConvertToBox(200 + MathUtils.random(280)));

							// recall
							createTween(type);
						}
					}).start(tween);

			break;

		default:
			break;
		}
	}

	public void update(float deltaTime) {
		if (type == 2) {
			stateTime += deltaTime;
		}
		// update tween
		tween.update(deltaTime);
	}

	TextureRegion keyFrame = null;

	public void render(SpriteBatch batch) {
		switch (type) {
		case 1:
			spCloud.draw(batch);
			break;
		case 2:
			keyFrame = aniButterTemp.getKeyFrame(stateTime, true);
			spCloud.renderSprite(keyFrame).draw(batch);
			break;
		default:
			break;
		}
	}
}
