package com.polysfactory.drawwatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingActivity extends Activity {

    public static final String KEY_COLOR = "color";
    public static final int RESULT_CODE_COLOR_SET = 100;
    public static final int RESULT_CODE_SHARE = 101;
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
        private LayoutInflater inflater;

        public SettingViewPagerAdapter(Context context) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            if (position == 0) {
                view = buildColorPicker();
            } else if (position == 1) {
                view = buildShareView();
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        private View buildColorPicker() {
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

        private View buildShareView() {
            View view = inflater.inflate(R.layout.action_button, null);
            TextView title = (TextView) view.findViewById(R.id.action_title);
            title.setText(getString(R.string.share));
            ImageView button = (ImageView) view.findViewById(R.id.action_image);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setResult(RESULT_CODE_SHARE, null);
                    finish();
                }
            });
            return view;
        }
    }

}
