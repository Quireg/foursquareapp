package ua.in.quireg.foursquareapp.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.in.quireg.foursquareapp.R;

public class LeftPaddedDivider extends RecyclerView.ItemDecoration {

    private Drawable mDivider;

    public LeftPaddedDivider(Context context) {
        mDivider = context.getResources().getDrawable(R.drawable.shadowline);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {

            View view = parent.getChildAt(i);

            if (parent.getChildAt(i + 1) instanceof TextView || view instanceof TextView) {
                //do not draw if it is a header or next item is a header
                continue;
            }

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

            int left = ((ViewGroup) view).getChildAt(0).getWidth() + ((ViewGroup) view).getChildAt(1).getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int top = view.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
