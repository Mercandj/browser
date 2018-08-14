package com.mwm.android.publishing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class StoreActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, StoreActivity.class);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        ImageView image = findViewById(R.id.activity_store_image_background);
        setImageByDrawableName(image, "mwm_store_background_by_name");
    }

    private static void setImageByDrawableName(ImageView imageView, String resourceName) {
        Context context = imageView.getContext();
        Resources resources = context.getResources();
        String packageName = context.getPackageName();
        int id = resources.getIdentifier(resourceName, "drawable", packageName);
        imageView.setImageResource(id);
    }
}
