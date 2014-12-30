package com.sample.userprofile;

import android.graphics.Bitmap;

public class UserProfileManager {

	private static UserProfileManager manager = null;

    private static final String TAG = "UserProfileManager";

	private Bitmap bgBitmap = null;
	private Bitmap profileBitmap = null;
	private Bitmap storyBitmap = null;

	public static UserProfileManager getInstance() {
		if (manager == null) {
			manager = new UserProfileManager();
		}
		return manager;
	}

	UserProfileManager() {
	}

	public Bitmap getProfileImage() {
		return profileBitmap;
	}

	public void setProfileImage(Bitmap bitmap) {
		if (profileBitmap != null && !profileBitmap.isRecycled()) {
			profileBitmap.recycle();
		}

		profileBitmap = bitmap;
	}

	public Bitmap getBgImage() {
		return bgBitmap;
	}

	public void setBgImage(Bitmap bitmap) {
		if (bgBitmap != null && !bgBitmap.isRecycled()) {
			bgBitmap.recycle();
		}

		bgBitmap = bitmap;
	}

	public Bitmap getStoryImage() {
		return storyBitmap;
	}

	public void setStoryImage(Bitmap bitmap) {
		if (storyBitmap != null && !storyBitmap.isRecycled()) {
			storyBitmap.recycle();
		}
		storyBitmap = bitmap;
	}
}
