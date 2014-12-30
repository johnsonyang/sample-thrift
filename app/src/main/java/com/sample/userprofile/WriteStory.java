package com.sample.userprofile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class WriteStory extends Activity implements View.OnClickListener  {

    private static final int REQ_PICK_PHOTO = 100;
    private static final int REQ_TAKE_PHOTO = 101;

    private static final int MAX_LENGTH = 200;

    private ImageView photoView;
    private EditText storyView;

    private Uri cameraUri;

    private boolean isEditChanging = false;
    private int highlightColor = 0xFFFDCCCB;
    private BackgroundColorSpan bgSpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_story);

        //findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.button_camera).setOnClickListener(this);
        findViewById(R.id.button_gallery).setOnClickListener(this);

        photoView = (ImageView) findViewById(R.id.story_image);
        storyView = (EditText) findViewById(R.id.story_text);

        bgSpan = new BackgroundColorSpan(highlightColor);
        storyView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isEditChanging) {
                    int length = storyView.getText().toString().length();
                    if (length > MAX_LENGTH) {
                        isEditChanging = true;
                        int selectStart = storyView.getSelectionStart();
                        int selectEnd = storyView.getSelectionEnd();
                        Spannable spannable = new SpannableString(storyView.getText());
                        spannable.setSpan(bgSpan, MAX_LENGTH, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        storyView.setText(spannable);
                        storyView.setSelection(selectStart, selectEnd);
                        isEditChanging = false;
                    }
                }
            }
        });

        loadImage();
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        switch (viewId) {
//            case R.id.btnBack:
//                onBackPressed();
//                break;
            case R.id.button_camera:
                takePhoto();
                break;
            case R.id.button_gallery:
                pickPhoto();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_PICK_PHOTO:
                    if (data != null) {
                        setImage(data.getData());
                    }
                    break;
                case REQ_TAKE_PHOTO:
                    setImage(cameraUri);
                    break;
            }
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        overridePendingTransition(android.R.anim.fade_in, R.anim.rotate_out_bottom);
//    }

    private void takePhoto() {
        cameraUri = CommonUtility.takePhoto(this, REQ_TAKE_PHOTO);
    }

    private void pickPhoto() {
        CommonUtility.pickPhoto(this, REQ_PICK_PHOTO);
    }

    private void setImage(final Uri uri) {
        BaseAsyncTask.run(this, new BaseAsyncTask.TaskAdapter() {
            @Override
            public Object onTaskRunning() {
                Bitmap bitmap = CommonUtility.getBitmapFromUri(WriteStory.this, uri);
                UserProfileManager.getInstance().setStoryImage(bitmap);
                return null;
            }

            @Override
            public void onTaskResult(Object result) {
                loadImage();
            }
        });
    }

    private void loadImage() {
        UserProfileManager userProfileManager = UserProfileManager.getInstance();
        Bitmap photo = userProfileManager.getStoryImage();

        if (photo != null) {
            photoView.setImageBitmap(photo);
        }
    }

}
