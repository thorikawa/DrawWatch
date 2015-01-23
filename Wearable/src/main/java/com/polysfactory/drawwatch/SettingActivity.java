package com.polysfactory.drawwatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.wearable.view.WatchViewStub;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingActivity extends Activity {

    public static final String KEY_COLOR = "color";
    public static final int RESULT_CODE_COLOR_SET = 100;
    public static final int RESULT_CODE_SHARE = 101;
    public static final int RESULT_CODE_RETRY = 102;
    public static final int RESULT_CODE_CLOSE = 103;
    private ImageView closeButton;
    private ColorPicker colorPicker;
    private ViewPager viewPager;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                viewPager = (ViewPager) findViewById(R.id.viewPager);
                viewPager.setAdapter(new SettingViewPagerAdapter(SettingActivity.this));
                closeButton = (ImageView) findViewById(R.id.closeButton);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
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
            } else if (position == 2) {
                view = buildRetryView();
            } else if (position == 3) {
                view = buildCloseView();
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
            return 4;
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
            final ImageView button = (ImageView) view.findViewById(R.id.action_image);
            button.setImageResource(R.drawable.ic_full_openonphone);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setResult(RESULT_CODE_SHARE, null);
                    finish();
                }
            });
            return view;
        }

        private View buildRetryView() {
            View view = inflater.inflate(R.layout.action_button, null);
            TextView title = (TextView) view.findViewById(R.id.action_title);
            title.setText(getString(R.string.startOver));
            final ImageView button = (ImageView) view.findViewById(R.id.action_image);
            button.setImageResource(R.drawable.ic_full_retry);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setResult(RESULT_CODE_RETRY, null);
                    finish();
                }
            });
            return view;
        }

        private View buildCloseView() {
            View view = inflater.inflate(R.layout.action_button, null);
            TextView title = (TextView) view.findViewById(R.id.action_title);
            title.setText(getString(R.string.closeApp));
            final ImageView button = (ImageView) view.findViewById(R.id.action_image);
            button.setImageResource(R.drawable.ic_full_cancel);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setResult(RESULT_CODE_CLOSE, null);
                    finish();
                }
            });
            return view;
        }
    }

}
