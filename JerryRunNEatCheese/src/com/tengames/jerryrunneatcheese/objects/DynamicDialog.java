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

import woodyx.basicapi.gui.XDialog;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tengames.jerryrunneatcheese.main.Assets;
import com.tengames.jerryrunneatcheese.screens.ScreenGame;

public class DynamicDialog extends XDialog {
	private Button btMenu, btReplay, btNext;
	private int value;

	public DynamicDialog(WindowStyle windowStyle, TextureRegion trBackground, Vector2 size, Vector2 position,
			Vector2 target, int realScore, int levelScore, byte type) {
		super(windowStyle, trBackground, size, position, target, TYPE_0);
		dialog.setTouchable(Touchable.disabled);
		switch (type) {
		case ScreenGame.STATE_WIN:
			Image imgJerry = new Image(Assets.taObjects.findRegion("unlock-stage"));
			imgJerry.setPosition(-10, 240);
			this.addActor(imgJerry);
			Image imgButter = new Image(Assets.taObjects.findRegion("cup"));
			imgButter.setPosition(150, 260);
			this.addActor(imgButter);
			Image imgText = new Image(Assets.taObjects.findRegion("text-win"));
			imgText.setSize(310 * 0.8f, 91 * 0.8f);
			imgText.setPosition((374 - imgText.getWidth()) / 2, 160);
			this.addActor(imgText);
			Image imgCup = null;

			if ((float) realScore / levelScore >= 0.7f) {
				imgCup = new Image(Assets.taObjects.findRegion("star-3"));
				setValue(3);
			} else if ((float) realScore / levelScore >= 0.4f) {
				imgCup = new Image(Assets.taObjects.findRegion("star-2"));
				setValue(2);
			} else {
				imgCup = new Image(Assets.taObjects.findRegion("star-1"));
				setValue(1);
			}

			imgCup.setPosition((374 - imgCup.getWidth()) / 2, 110);
			imgCup.setRotation(-15);
			this.addActor(imgCup);

			// button
			btMenu = new Button(new TextureRegionDrawable(Assets.taObjects.findRegion("bt-menu")));
			btMenu.setSize(83 * 0.6f, 83 * 0.6f);
			btMenu.setPosition(50, -5);
			this.addActor(btMenu);

			btReplay = new Button(new TextureRegionDrawable(Assets.taObjects.findRegion("bt-replay")));
			btReplay.setPosition(162, -5);
			this.addActor(btReplay);

			btNext = new Button(new TextureRegionDrawable(Assets.taObjects.findRegion("bt-next")));
			btNext.setPosition(270, -5);
			this.addActor(btNext);
			break;

		case ScreenGame.STATE_LOOSE:
			Image imgTom = new Image(Assets.taObjects.findRegion("jerry-fail"));
			imgTom.setPosition(200, 160);
			this.addActor(imgTom);
			Image imgButters = new Image(Assets.taObjects.findRegion("cake-1"));
			imgButters.setPosition(160, 200);
			Image imgCake2 = new Image(Assets.taObjects.findRegion("cake-2"));
			imgCake2.setPosition(140, 180);
			Image imgCake3 = new Image(Assets.taObjects.findRegion("cake-3"));
			imgCake3.setPosition(180, 180);
			this.addActor(imgButters);
			this.addActor(imgCake2);
			this.addActor(imgCake3);
			Image imgTextf = new Image(Assets.taObjects.findRegion("text-fail"));
			imgTextf.setSize(310 * 0.8f, 91 * 0.8f);
			imgTextf.setPosition((374 - imgTextf.getWidth()) / 2, 90);
			this.addActor(imgTextf);

			// button
			btMenu = new Button(new TextureRegionDrawable(Assets.taObjects.findRegion("bt-menu")));
			btMenu.setSize(83 * 0.6f, 83 * 0.6f);
			btMenu.setPosition(50, -5);
			this.addActor(btMenu);

			btReplay = new Button(new TextureRegionDrawable(Assets.taObjects.findRegion("bt-replay")));
			btReplay.setPosition(270, -5);
			this.addActor(btReplay);
			break;

		default:
			break;
		}
	}

	public Button getBtMenu() {
		return btMenu;
	}

	public Button getBtReplay() {
		return btReplay;
	}

	public Button getBtNext() {
		return btNext;
	}

	public int getValue() {
		return value;
	}

	private void setValue(int value) {
		this.value = value;
	}

}
