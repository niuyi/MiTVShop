package com.xiaomi.mitv.shop;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.squareup.picasso.Picasso;
import com.xiaomi.mitv.shop.widget.MyListViewEx;

/**
 * Created by niuyi on 2015/5/11.
 */
public class GoodSelectionActivity extends Activity {

    private MyListViewEx mListView;
    private LayoutInflater mLayoutInflater;
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_selection_activity);

        mListView = (MyListViewEx)findViewById(R.id.list_view);
//        mListView.setAdapter(new L);
        mListView.setAdapter(new MyListAdapter());
        mListView.setSelection(0);
        mLayoutInflater = getLayoutInflater();
    }

    public class ViewHolder {
        ImageView icon;
        TextView title;
        TextView price;
        View backIcon;
    }

    class MyListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return 9;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if(convertView == null){
                convertView = mLayoutInflater.inflate(R.layout.selection_list_item, parent, false);

                holder = new ViewHolder();

                holder.icon = (ImageView)convertView.findViewById(R.id.poster_view);
                holder.title = (TextView)convertView.findViewById(R.id.tv_title);
                holder.price = (TextView)convertView.findViewById(R.id.tv_price);
                holder.backIcon = convertView.findViewById(R.id.arrow);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            Picasso.with(GoodSelectionActivity.this).load("http://static.home.mi.com/app/shop/img?id=shop_466161732d4d67bb15d3ae550da7c0f8.jpg&t=jpeg&z=1&q=80").into(holder.icon);
            holder.title.setText("小米手机4: "+ position);
            holder.price.setText("1999元");

            return convertView;
        }
    }
}