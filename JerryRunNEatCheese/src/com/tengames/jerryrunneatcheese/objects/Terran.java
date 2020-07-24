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
import woodyx.basicapi.physics.ObjectModel;
import woodyx.basicapi.sound.SoundManager;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.TweenPaths;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.tengames.jerryrunneatcheese.main.Assets;

public class Terran extends SimpleSprite {
	private World world;
	private SimpleModel model;
	private TweenManager tween;
	private String name;
	private int value;
	private byte type;
	private boolean isDie, isProcDie, canRemove;

	public Terran(World world, byte type, Vector2 position, Vector2 size, float angle, int index) {
		super(position.x, position.y, size.x, size.y);
		this.world = world;
		this.type = type;
		this.value = 0;
		this.isDie = false;
		this.isProcDie = false;
		this.canRemove = false;

		// set texture region for sprites
		switch (type) {
		case IconModel.BOX:
			name = "box";
			break;
		case IconModel.BRICK:
			name = "brick";
			break;
		case IconModel.CAKE_1:
			name = "cake-1";
			this.value = 10;
			createTween();
			break;
		case IconModel.CAKE_2:
			name = "cake-2";
			this.value = 20;
			createTween();
			break;
		case IconModel.CAKE_3:
			name = "cake-3";
			this.value = 30;
			createTween();
			break;
		case IconModel.CUP:
			name = "cup";
			break;
		case IconModel.ICE_BAR:
			name = "ice-bar";
			break;
		case IconModel.TRAP_1:
			name = "trap-1";
			break;
		case IconModel.TRAP_2:
			name = "trap-2";
			// create tween
			tween = new TweenManager();
			Tween.to(this, SpriteAccessor.ROTATION, 2f).target(360).ease(Linear.INOUT).repeat(-1, 0).start(tween);
			break;
		case IconModel.WOOD_BAR:
			name = "wood-bar";
			break;
		case IconModel.WOOD_MUD:
			name = "wood-mud";
			break;
		default:
			break;
		}

		// set region
		this.setRegion(Assets.taObjects.findRegion(name));
		this.setOriginCenter(this);

		// create models
		switch (type) {
		// polygon
		case IconModel.BRICK:
		case IconModel.WOOD_BAR:
		case IconModel.WOOD_MUD:
			model = new SimpleModel(world, ObjectModel.STATIC, ObjectModel.POLYGON, new Vector2(size.x, size.y),
					new Vector2(), 0, position, angle, 5, 0.5f, 0.1f, "terran");
			break;

		case IconModel.ICE_BAR:
			model = new SimpleModel(world, ObjectModel.STATIC, ObjectModel.POLYGON, new Vector2(size.x, size.y),
					new Vector2(), 0, position, angle, 5, 0.1f, 0.1f, "terran");
			break;

		case IconModel.BOX:
			model = new SimpleModel(world, ObjectModel.DYNAMIC, ObjectModel.POLYGON, new Vector2(size.x, size.y),
					new Vector2(), 0, position, angle, 0.5f, 0.2f, 0.1f, "terran");
			break;

		// polygon & sensor
		case IconModel.TRAP_1:
		case IconModel.TRAP_2:
		case IconModel.CUP:
			model = new SimpleModel(world, ObjectModel.STATIC, ObjectModel.POLYGON, new Vector2(size.x, size.y),
					new Vector2(), 0, position, angle, 5, 0.5f, 0.1f, ("name" + index));
			model.getBody().getFixtureList().get(0).setSensor(true);
			break;

		// circle & sensor
		case IconModel.CAKE_1:
		case IconModel.CAKE_2:
		case IconModel.CAKE_3:
			model = new SimpleModel(world, ObjectModel.STATIC, ObjectModel.CIRCLE, new Vector2(size.x, size.y),
					new Vector2(), size.x / 2, position, angle, 5, 0.5f, 0.1f, ("name" + index));
			model.getBody().getFixtureList().get(0).setSensor(true);
			break;
		default:
			break;
		}
	}

	private void createTween() {
		// create tween
		tween = new TweenManager();
		Tween.to(this, SpriteAccessor.SCALE_XY, 1 + MathUtils.random(3)).ease(Linear.INOUT).target(1.2f, 1.2f)
				.path(TweenPaths.catmullRom).repeatYoyo(-1, 0).start(tween);
	}

	public void update(float deltaTime) {
		// update tween
		if (tween != null)
			tween.update(deltaTime);

		// update follow
		if (!isDie) {
			if (type != IconModel.TRAP_2)
				this.updateFollowModel(model, true);
			else
				this.updateFollowModel(model, false);
		}

		// check die
		if (isDie && !isProcDie) {
			// create new tween
			switch (type) {
			case IconModel.CAKE_1:
			case IconModel.CAKE_2:
			case IconModel.CAKE_3:
				// play sound
				SoundManager.playSound(Assets.soEat);

				// create new tween
				Tween.to(this, SpriteAccessor.SCALE_XY, 0.5f).ease(Elastic.IN).target(0, 0)
						.setCallback(new TweenCallback() {
							@Override
							public void onEvent(int arg0, BaseTween<?> arg1) {
								// deactive body
								if (model.getBody() != null) {
									world.destroyBody(model.getBody());
									model = null;
								}
								// can remove
								canRemove = true;
							}
						}).start(tween);
				break;

			default:
				// deactive body
				if (model.getBody() != null) {
					world.destroyBody(model.getBody());
					model = null;
				}
				// can remove
				canRemove = true;
				break;
			}

			// turn off flag
			isProcDie = true;
		}
	}

	public SimpleModel getModel() {
		return this.model;
	}

	public int getType() {
		return this.type;
	}

	public int getValue() {
		return this.value;
	}

	public void setDie() {
		if (!isDie)
			isDie = true;
	}

	public boolean getCanRemove() {
		return this.canRemove;
	}

	public boolean getDie() {
		return this.isDie;
	}
}
