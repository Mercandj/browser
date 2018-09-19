package com.mercandalli.android.browser.on_boarding;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mercandalli.android.browser.R;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * Defined width, this {@link ImageView} compute its height to conserve YouTube thumbnail ratio.
 */
public class OnBoardingThumbnail extends AppCompatImageView {

    private Attributes attributes;

    public OnBoardingThumbnail(final Context context) {
        super(context);
        init(context, null, 0);
    }

    public OnBoardingThumbnail(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public OnBoardingThumbnail(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * The {@link FrameLayout#onMeasure(int, int)}. Keep the YouTube ratio.
     */
    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        if (attributes.ratioWidthReference) {
            int originalWidth = MeasureSpec.getSize(widthMeasureSpec);
            float ratio = attributes.ratioHeight / attributes.ratioWidth;
            super.onMeasure(
                    MeasureSpec.makeMeasureSpec(
                            originalWidth,
                            MeasureSpec.EXACTLY
                    ),
                    MeasureSpec.makeMeasureSpec(
                            (int) (originalWidth * ratio),
                            MeasureSpec.EXACTLY
                    )
            );
        } else {
            int originalHeight = MeasureSpec.getSize(heightMeasureSpec);
            float ratio = attributes.ratioWidth / attributes.ratioHeight;
            super.onMeasure(
                    MeasureSpec.makeMeasureSpec(
                            (int) (originalHeight * ratio),
                            MeasureSpec.EXACTLY
                    ),
                    MeasureSpec.makeMeasureSpec(
                            originalHeight,
                            MeasureSpec.EXACTLY
                    )
            );
        }
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        attributes = extractAttributes(context, attrs, defStyleAttr);
    }

    private Attributes extractAttributes(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.OnBoardingThumbnail, defStyleAttr, 0);
        float ratioWidth = typedArray.getFloat(R.styleable.OnBoardingThumbnail_ratio_width, 1f);
        float ratioHeight = typedArray.getFloat(R.styleable.OnBoardingThumbnail_ratio_height, 1f);
        boolean ratioWidthReference = typedArray.getBoolean(R.styleable.OnBoardingThumbnail_ratio_width_reference, true);
        typedArray.recycle();
        return new Attributes(
                ratioWidth,
                ratioHeight,
                ratioWidthReference
        );
    }

    private class Attributes {

        final float ratioWidth;

        final float ratioHeight;

        final boolean ratioWidthReference;

        Attributes(
                float ratioWidth,
                float ratioHeight,
                boolean ratioWidthReference
        ) {
            this.ratioWidth = ratioWidth;
            this.ratioHeight = ratioHeight;
            this.ratioWidthReference = ratioWidthReference;
        }
    }
}
