package ua.in.quireg.foursquareapp.ui.views;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Arcturus Mengsk on 17.03.2019, 3:54.
 * foursquareapp
 */
public class SmallerDimensionSizeImageView extends android.support.v7.widget.AppCompatImageView {

    public SmallerDimensionSizeImageView(Context context) {
        super(context);
    }

    public SmallerDimensionSizeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmallerDimensionSizeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    @SuppressWarnings("SuspiciousNameCombination")
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSize > heightSize) {
            setMeasuredDimension(heightSize, heightSize);
        } else {
            setMeasuredDimension(widthSize, widthSize);
        }
    }
}
