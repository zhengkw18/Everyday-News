package com.java.zhengkw.category;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.java.zhengkw.R;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    private DragGridLayout gridLayout1;
    private DragGridLayout gridLayout2;
    private ArrayList<String> mCategory = new ArrayList<>();
    private ArrayList<String> mDelCategory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);

        mCategory = new ArrayList<>();
        SharedPreferences sp = getSharedPreferences("category", MODE_PRIVATE);
        if (sp.getBoolean("news", true)) mCategory.add("news");
        else mDelCategory.add("news");
        if (sp.getBoolean("paper", true)) mCategory.add("paper");
        else mDelCategory.add("paper");
        if (sp.getBoolean("event", true)) mCategory.add("event");
        else mDelCategory.add("event");
        gridLayout1 = findViewById(R.id.arr_grid1);
        gridLayout1.setIsRemain(true);
        gridLayout2 = findViewById(R.id.arr_grid2);

        gridLayout1.setItems(mCategory);
        gridLayout2.setItems(mDelCategory);

        gridLayout1.setOnDragItemClickListener(new DragGridLayout.OnDragItemClickListener() {
            @Override
            public void onDragItemClick(TextView tv) {
                mCategory.remove(tv.getText().toString().replace("+", ""));
                mDelCategory.add(tv.getText().toString().replace("+", ""));
                gridLayout1.removeView(tv);
                gridLayout2.addGridItem(tv.getText().toString());
            }
        });
        gridLayout2.setOnDragItemClickListener(new DragGridLayout.OnDragItemClickListener() {
            @Override
            public void onDragItemClick(TextView tv) {
                mDelCategory.remove(tv.getText().toString().replace("+", ""));
                mCategory.add(tv.getText().toString().replace("+", ""));
                gridLayout2.removeView(tv);
                gridLayout1.addGridItem(tv.getText().toString());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sp = getSharedPreferences("category", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("news", mCategory.contains("news"));
        editor.putBoolean("paper", mCategory.contains("paper"));
        editor.putBoolean("event", mCategory.contains("event"));
        editor.apply();
    }

}
