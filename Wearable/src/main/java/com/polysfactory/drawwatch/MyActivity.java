package com.polysfactory.drawwatch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.HashSet;

public class MyActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int REQUEST_CODE_SETTING = 101;
    private ImageView prefButton;
    private DrawingView drawingVeiw;
    private GoogleApiClient mGoogleApiClient;

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

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    private void showColorPicker() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SETTING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SETTING) {
            if (resultCode == SettingActivity.RESULT_CODE_COLOR_SET) {
                int color = data.getIntExtra(SettingActivity.KEY_COLOR, 0);
                drawingVeiw.setPaintColor(color);
            } else if (resultCode == SettingActivity.RESULT_CODE_SHARE) {
                shareBitmapAsync();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void shareBitmapAsync() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                shareBitmap();
                return null;
            }
        }.execute();
    }

    private void shareBitmap() {
        Bitmap bitmap = drawingVeiw.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Collection<String> nodes = getNodes();
        for (String node : nodes) {
            Log.d(Constants.TAG, "send message to " + node);
            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node, Constants.MESSAGE_PATH, byteArray).await();
            if (!result.getStatus().isSuccess()) {
                Log.e(Constants.TAG, "ERROR: failed to send Message: " + result.getStatus());
            }
        }
    }

    private Collection<String> getNodes() {
        HashSet<String> results = new HashSet<String>();
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();

        for (Node node : nodes.getNodes()) {
            results.add(node.getId());
        }

        return results;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(Constants.TAG, "Google API Client was connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(Constants.TAG, "Connection to Google API client was suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(Constants.TAG, "Connection to Google API client has failed");
    }
}
