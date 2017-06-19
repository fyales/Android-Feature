package com.fyales.android.demo.rx;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;


import com.fyales.android.demo.R;
import com.fyales.android.demo.imageloader.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageDetailActivity extends AppCompatActivity {

    public static final String URL = "url";
    @BindView(R.id.imageview)
    protected ImageView mImageView;
    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        ButterKnife.bind(this);

//       用 mToolbar.setTitle("")设置无效
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String url = getIntent().getStringExtra(URL);
        ImageUtils.getInstance().display(mImageView, url);
    }


}
