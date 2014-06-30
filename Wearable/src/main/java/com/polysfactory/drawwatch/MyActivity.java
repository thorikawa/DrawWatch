package com.polysfactory.drawwatch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MyActivity extends Activity {

    private ImageView closeButton;
    private ImageView prefButton;
    private ColorPicker colorPicker;
    private DrawingView drawingVeiw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        drawingVeiw = (DrawingView) findViewById(R.id.drawingView);
        colorPicker = (ColorPicker) findViewById(R.id.colorPicker);
        colorPicker.setOnColorPickedListener(new ColorPicker.OnColorPickedListener() {
            @Override
            public void onColorPicked(int color) {
                drawingVeiw.setPaintColor(color);
                hideColorPicker();
            }
        });
        closeButton = (ImageView) findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideColorPicker();
            }
        });
        prefButton = (ImageView) findViewById(R.id.prefButton);
        prefButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showColorPicker();
            }
        });
    }

    private void hideColorPicker() {
        colorPicker.setVisibility(View.GONE);
        closeButton.setVisibility(View.GONE);
        prefButton.setVisibility(View.VISIBLE);
    }

    private void showColorPicker() {
        colorPicker.setVisibility(View.VISIBLE);
        closeButton.setVisibility(View.VISIBLE);
        prefButton.setVisibility(View.GONE);
    }
}
