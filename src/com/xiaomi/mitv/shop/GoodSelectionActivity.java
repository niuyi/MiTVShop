package com.xiaomi.mitv.shop;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.xiaomi.mitv.shop.widget.VerticalGridView;

/**
 * Created by niuyi on 2015/5/11.
 */
public class GoodSelectionActivity extends Activity {

    private static final String TAG = "MyListViewEx";
    private VerticalGridView mListView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_selection_activity);

        TextView title = (TextView)findViewById(R.id.title_text);
        title.setText(R.string.select_goods);

        mListView = (VerticalGridView)findViewById(R.id.list_view);

        Log.i(TAG, "para: " + mListView.getLayoutParams().getClass());
        mListView.setNumColumns(2);
        mListView.setVerticalMargin(45);
        mListView.setHorizontalMargin(118);
        mListView.setSelectedPosition(0);
        mListView.setHasFixedSize(true);
        mListView.setVerticalFadingEdgeEnabled(true);
        mListView.setFadingEdgeLength(100);
        mListView.setWindowAlignment(VerticalGridView.WINDOW_ALIGN_LOW_EDGE);
        GoodsListAdapter adapter = new GoodsListAdapter();
        mListView.setAdapter(adapter);

        FrameLayout.LayoutParams para = (FrameLayout.LayoutParams)mListView.getLayoutParams();
        if(adapter.getItemCount() <= 2){
            para.topMargin = 420;
        }else if(adapter.getItemCount() <= 4){
            para.topMargin = 276;
        }else{
            para.topMargin = 240;
        }

    }

    static class GoodsViewHolder extends RecyclerView.ViewHolder{

        @Override
        public GoodsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            TextView tv = new TextView(GoodSelectionActivity.this);
            tv.setFocusable(true);
            tv.setClickable(true);
            tv.setBackgroundResource(R.drawable.btn_normal);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 42);
//            android.support.v7.widget.GridLayoutManager.LayoutParams params = new android.support.v7.widget.GridLayoutManager.LayoutParams(242, 702);
            com.xiaomi.mitv.shop.widget.GridLayoutManager.LayoutParams params = new com.xiaomi.mitv.shop.widget.GridLayoutManager.LayoutParams(702, 242);
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(params);

        public GoodsViewHolder(TextView itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    class GoodsListAdapter extends RecyclerView.Adapter<GoodsViewHolder>{

        @Override
        public GoodsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            TextView tv = new TextView(GoodSelectionActivity.this);
            tv.setFocusable(true);
            tv.setClickable(true);
            tv.setBackgroundResource(R.drawable.btn_normal);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 42);
//            android.support.v7.widget.GridLayoutManager.LayoutParams params = new android.support.v7.widget.GridLayoutManager.LayoutParams(242, 702);
            com.xiaomi.mitv.shop.widget.GridLayoutManager.LayoutParams params = new com.xiaomi.mitv.shop.widget.GridLayoutManager.LayoutParams(702, 242);
            tv.setGravity(Gravity.CENTER);
            tv.setLayoutParams(params);

            return new GoodsViewHolder(tv);
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }



}