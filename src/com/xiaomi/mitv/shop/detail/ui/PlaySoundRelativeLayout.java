package com.xiaomi.mitv.shop.detail.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SoundEffectConstants;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class PlaySoundRelativeLayout extends FrameLayout {

	public PlaySoundRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void playErrorSound() {
		this.playSoundEffect(5);
	}

	protected void playKeyStoneSound() {
		this.playSoundEffect(SoundEffectConstants.NAVIGATION_DOWN);
	}
}
