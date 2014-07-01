package com.polysfactory.drawwatch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Listens to DataItems and Messages from the local node.
 */
public class DataLayerListenerService extends WearableListenerService {

    public static final String MESSAGE_PATH = "/com.polysfactory.drawwatch/image";
    private static final String TAG = "DataLayerListenerServic";
    private static final String TEMP_IMAGE_PREFIX = "drawwatch";
    private static final String TEMP_IMAGE_EXT = ".png";
    GoogleApiClient mGoogleApiClient;

    public static void LOGD(final String tag, String message) {
        // if (Log.isLoggable(tag, Log.DEBUG)) {
        Log.d(tag, message);
        // }
    }

    private static File copyToTemporaryFile(Context context, Bitmap image) {
        try {
            File root = context.getExternalCacheDir();
            File tempFile = File.createTempFile(TEMP_IMAGE_PREFIX, TEMP_IMAGE_EXT, root);
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
            return tempFile;
        } catch (IOException e) {
            Log.w(TAG, "failed to copy the image", e);
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        LOGD(TAG, "onDataChanged: " + dataEvents);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String path = messageEvent.getPath();
        if (MESSAGE_PATH.equals(path)) {
            byte[] data = messageEvent.getData();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Intent intent = buildIntent(bitmap);
            startActivity(intent);
        }
    }

    @Override
    public void onPeerConnected(Node peer) {
        LOGD(TAG, "onPeerConnected: " + peer);
    }

    @Override
    public void onPeerDisconnected(Node peer) {
        LOGD(TAG, "onPeerDisconnected: " + peer);
    }

    private Intent buildIntent(Bitmap bitmap) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/png");
        // copy image to temp file to share with other apps
        File tempFile = copyToTemporaryFile(this, bitmap);
        if (tempFile != null) {
            i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
        }
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }
}
