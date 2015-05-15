package com.xiaomi.mitv.shop.detail;

import com.xiaomi.mitv.shop.R;


public class ResConstants extends BaseResConstants{
//	public static final String CATEGORY_FRAGMENT_INSTALL = "install";
//	public static final String CATEGORY_FRAGMENT_CONTROLLER = "controller";
//	public static final String CATEGORY_FRAGMENT_TV = "tv";
	public static final String CATEGORY_FRAGMENT_HDPLAYER = "hdplayer";
//	public static final String CATEGORY_FRAGMENT_ONLINE_VIDEO = "onlinevideo";
//	public static final String CATEGORY_FRAGMENT_SETTINGS = "settings";
//	public static final String CATEGORY_FRAGMENT_APP_GAME = "appgame";
//	public static final String CATEGORY_FRAGMENT_CLOUD_GALLERY = "cloudgallery";
//	public static final String CATEGORY_FRAGMENT_MIRACAST = "miracast";
//	public static final String CATEGORY_FRAGMENT_STATUS_BAR = "statusbar";
//	public static final String CATEGORY_FRAGMENT_ACCESSORY = "accessory";
	
	public static final String[] CATEGORY_FRAGMENTS = new String[]{ CATEGORY_FRAGMENT_HDPLAYER};

	public static final int[] CATEGORY_TITLE_RES_ID = new int[]{R.string.hdplayer};

	public static final int[] ENTRANCE_IMAGEVIEW_DRAWABLE_RES_ID = new int[]{R.drawable.bg_1};

	public static final int[][] PAGEBOOK_RES_IDS = new int[][] {
		{R.drawable.pagebook_controller_1, R.drawable.pagebook_controller_2, R.drawable.pagebook_controller_3, R.drawable.pagebook_controller_4},
	};

	@Override
	public String[] getFragments() {
		return CATEGORY_FRAGMENTS;
	}

	@Override
	public int[] getTitleResIds() {
		return CATEGORY_TITLE_RES_ID;
	}

	@Override
	public int[] getEntranceResIds() {
		return ENTRANCE_IMAGEVIEW_DRAWABLE_RES_ID;
	}

	@Override
	public int[][] getPageBookResIds() {
		return PAGEBOOK_RES_IDS;
	}
}
