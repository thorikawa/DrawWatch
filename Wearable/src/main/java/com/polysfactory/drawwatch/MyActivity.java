package com.polysfactory.drawwatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MyActivity extends Activity {

    public static final int REQUEST_CODE_COLOR_SET = 101;
    private ImageView prefButton;
    private DrawingView drawingVeiw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        drawingVeiw = (DrawingView) findViewById(R.id.drawingView);
        prefButton = (ImageView) findViewById(R.id.prefButton);
        prefButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showColorPicker();
            }
        });
    }

    private void showColorPicker() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivityForResult(intent, REQUEST_CODE_COLOR_SET);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_COLOR_SET) {
            if (resultCode == SettingActivity.RESULT_CODE_COLOR_SET) {
                int color = data.getIntExtra(SettingActivity.KEY_COLOR, 0);
                drawingVeiw.setPaintColor(color);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
