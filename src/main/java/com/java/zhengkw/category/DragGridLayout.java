package com.java.zhengkw.category;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.java.zhengkw.R;

import java.util.List;

public class DragGridLayout extends GridLayout {
    private boolean isRemain;
    private int margin = 30;

    public void setItems(List<String> items) {
        for (int i = 0; i < items.size(); i++) {
            addGridItem(items.get(i));
        }
    }

    public DragGridLayout(Context context) {
        this(context, null);
    }

    public DragGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addGridItem(String content) {
        TextView tv = new TextView(getContext());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        tv.setPadding(0, 20, 0, 20);
        tv.setBackgroundColor(ContextCompat.getColor(tv.getContext(), R.color.catArrangeTextBgColor));
        tv.setGravity(Gravity.CENTER);
        if (!isRemain) {
            tv.setTextColor(0xFF9c9393);
            tv.setBackgroundResource(R.drawable.textview_unselect_border);
        }
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels / 4 - margin * 2;
        params.setMargins(margin, margin, margin, margin);
        tv.setLayoutParams(params);

        if (!isRemain)
            tv.setText("+" + content);
        else
            tv.setText(content.replace("+", ""));

        addView(tv);

        tv.setOnClickListener(onClickListener);
    }


    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onDragItemClickListener != null) {
                onDragItemClickListener.onDragItemClick((TextView) view);
            }
        }
    };

    //接口
    public interface OnDragItemClickListener {
        void onDragItemClick(TextView tv);
    }

    private OnDragItemClickListener onDragItemClickListener;

    public void setOnDragItemClickListener(OnDragItemClickListener onDragItemClickListener) {
        this.onDragItemClickListener = onDragItemClickListener;
    }

    public void setIsRemain(boolean isRemain) {
        this.isRemain = isRemain;
    }
}