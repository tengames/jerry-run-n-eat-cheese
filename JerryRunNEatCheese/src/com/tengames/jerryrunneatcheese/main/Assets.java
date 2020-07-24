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
package com.tengames.jerryrunneatcheese.main;

import woodyx.basicapi.screen.Asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class Assets extends Asset {
	public static Texture txBgMenu, txBgStage, txBgGame, txBlueButterfly, txRedButterfly;
	public static TextureAtlas taSkin, taObjects;
	public static Animation aniRun, aniStand, aniJump, aniRunF, aniStandF, aniJumpF;
	public static BitmapFont fNumber, fStage;
	public static LabelStyle lbSNumber, lbSStage;
	public static Music muBgMeu, muBgStage, muBgGame;
	public static Sound soClick, soEat, soJump, soWin, soFail, soHurt;

	public static void loadResLoading() {
		loading("drawable/", "atlas", "loading");
		assetManager.finishLoading();
	}

	public static void load() {
//		// loading background
		loading("drawable/backgrounds/", "jpg", "bggame", "bgmenu", "bgstage");

//		// loading objects
		loading("drawable/objects/", "atlas", "objects", "uiskin");
		loading("drawable/objects/", "png", "blue-butterfly", "red-butterfly");

//		// loading fonts
		loading("fonts/", "png", "numfont", "numfontrc");

//		// loading sound
		loading("raw/", "ogg", "sobutton", "soeat", "sofail", "sowin", "sojump", "soyow");

//		// loading music
		loading("raw/", "mp3", "bmmenu", "bmstage", "bmgame");
	}

	public static void loadDone() {
//		// loaded backgrounds
		txBgGame = assetManager.get("drawable/backgrounds/bggame.jpg");
		txBgGame.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		txBgStage = assetManager.get("drawable/backgrounds/bgstage.jpg");
		txBgStage.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		txBgMenu = assetManager.get("drawable/backgrounds/bgmenu.jpg");
		txBgMenu.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// loaded objects
		txBlueButterfly = assetManager.get("drawable/objects/blue-butterfly.png");
		txBlueButterfly.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		txRedButterfly = assetManager.get("drawable/objects/red-butterfly.png");
		txRedButterfly.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		taSkin = assetManager.get("drawable/objects/uiskin.atlas");
		taObjects = assetManager.get("drawable/objects/objects.atlas");

//		// loaded animation
		aniStand = new Animation(0.2f, taObjects.findRegion("jerry-stand-1"), taObjects.findRegion("jerry-stand-2"),
				taObjects.findRegion("jerry-stand-3"));

		TextureRegion jerryStand1 = new TextureRegion(taObjects.findRegion("jerry-stand-1"));
		TextureRegion jerryStand2 = new TextureRegion(taObjects.findRegion("jerry-stand-2"));
		TextureRegion jerryStand3 = new TextureRegion(taObjects.findRegion("jerry-stand-3"));
		jerryStand1.flip(true, false);
		jerryStand2.flip(true, false);
		jerryStand3.flip(true, false);

		aniStandF = new Animation(0.2f, jerryStand1, jerryStand2, jerryStand3);

		aniRun = new Animation(0.05f, taObjects.findRegion("jerry-run-1"), taObjects.findRegion("jerry-run-2"),
				taObjects.findRegion("jerry-run-3"), taObjects.findRegion("jerry-run-4"),
				taObjects.findRegion("jerry-run-5"), taObjects.findRegion("jerry-run-6"),
				taObjects.findRegion("jerry-run-7"), taObjects.findRegion("jerry-run-8"),
				taObjects.findRegion("jerry-run-9"), taObjects.findRegion("jerry-run-10"),
				taObjects.findRegion("jerry-run-11"));

		TextureRegion jerryRun1 = new TextureRegion(taObjects.findRegion("jerry-run-1"));
		TextureRegion jerryRun2 = new TextureRegion(taObjects.findRegion("jerry-run-2"));
		TextureRegion jerryRun3 = new TextureRegion(taObjects.findRegion("jerry-run-3"));
		TextureRegion jerryRun4 = new TextureRegion(taObjects.findRegion("jerry-run-4"));
		TextureRegion jerryRun5 = new TextureRegion(taObjects.findRegion("jerry-run-5"));
		TextureRegion jerryRun6 = new TextureRegion(taObjects.findRegion("jerry-run-6"));
		TextureRegion jerryRun7 = new TextureRegion(taObjects.findRegion("jerry-run-7"));
		TextureRegion jerryRun8 = new TextureRegion(taObjects.findRegion("jerry-run-8"));
		TextureRegion jerryRun9 = new TextureRegion(taObjects.findRegion("jerry-run-9"));
		TextureRegion jerryRun10 = new TextureRegion(taObjects.findRegion("jerry-run-10"));
		TextureRegion jerryRun11 = new TextureRegion(taObjects.findRegion("jerry-run-11"));
		jerryRun1.flip(true, false);
		jerryRun2.flip(true, false);
		jerryRun3.flip(true, false);
		jerryRun4.flip(true, false);
		jerryRun5.flip(true, false);
		jerryRun6.flip(true, false);
		jerryRun7.flip(true, false);
		jerryRun8.flip(true, false);
		jerryRun9.flip(true, false);
		jerryRun10.flip(true, false);
		jerryRun11.flip(true, false);

		aniRunF = new Animation(0.05f, jerryRun1, jerryRun2, jerryRun3, jerryRun4, jerryRun5, jerryRun6, jerryRun7,
				jerryRun8, jerryRun9, jerryRun10, jerryRun11);

		aniJump = new Animation(0.2f, taObjects.findRegion("jerry-jump-1"), taObjects.findRegion("jerry-jump-2"));

		TextureRegion jerryJump1 = new TextureRegion(taObjects.findRegion("jerry-jump-1"));
		TextureRegion jerryJump2 = new TextureRegion(taObjects.findRegion("jerry-jump-2"));
		jerryJump1.flip(true, false);
		jerryJump2.flip(true, false);

		aniJumpF = new Animation(0.2f, jerryJump1, jerryJump2);

//		// loaded fonts
		Texture txNumFont = assetManager.get("fonts/numfont.png");
		txNumFont.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		fNumber = new BitmapFont(Gdx.files.internal("fonts/numfont.fnt"), new TextureRegion(txNumFont), false);
		fNumber.setScale(0.6f);

		Texture txFont = assetManager.get("fonts/numfontrc.png");
		txFont.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		fStage = new BitmapFont(Gdx.files.internal("fonts/numfontrc.fnt"), new TextureRegion(txFont), false);

		lbSNumber = new LabelStyle();
		lbSNumber.font = fNumber;
		lbSStage = new LabelStyle();
		lbSStage.font = fStage;

//		// loaded sound
		soClick = assetManager.get("raw/sobutton.ogg");
		soEat = assetManager.get("raw/soeat.ogg");
		soFail = assetManager.get("raw/sofail.ogg");
		soJump = assetManager.get("raw/sojump.ogg");
		soWin = assetManager.get("raw/sowin.ogg");
		soHurt = assetManager.get("raw/soyow.ogg");

//		// loaded music
		muBgGame = assetManager.get("raw/bmgame.mp3");
		muBgMeu = assetManager.get("raw/bmmenu.mp3");
		muBgStage = assetManager.get("raw/bmstage.mp3");
	}
}
