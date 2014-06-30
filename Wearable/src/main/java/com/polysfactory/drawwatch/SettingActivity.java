package com.polysfactory.drawwatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SettingActivity extends Activity {

    public static final String KEY_COLOR = "color";
    public static final int RESULT_CODE_COLOR_SET = 100;
    private ImageView closeButton;
    private ColorPicker colorPicker;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new SettingViewPagerAdapter(this));
        closeButton = (ImageView) findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    class SettingViewPagerAdapter extends PagerAdapter {

        private Context context;

        public SettingViewPagerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            if (position == 0) {
                view = buildColorPicker();
                container.addView(view);
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        View buildColorPicker() {
            ColorPicker colorPicker = new ColorPicker(context);
            colorPicker.setOnColorPickedListener(new ColorPicker.OnColorPickedListener() {
                @Override
                public void onColorPicked(int color) {
                    Intent data = new Intent();
                    data.putExtra(KEY_COLOR, color);
                    setResult(RESULT_CODE_COLOR_SET, data);
                    finish();
                }
            });
            return colorPicker;
        }
    }

}
