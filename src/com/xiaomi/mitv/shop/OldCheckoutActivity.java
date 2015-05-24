package com.xiaomi.mitv.shop;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niuyi on 2015/5/14.
 */
public class OldCheckoutActivity extends Activity {

    private static final String TAG = "CheckoutActivity";
    private View mInvoiceEdit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_checkout_activity);
        mInvoiceEdit = findViewById(R.id.invoice_title_container);
        ListView mCartList = (ListView)findViewById(R.id.cart_list);

        List<ItemNode> lists = new ArrayList<ItemNode>();

        for(int i = 0 ; i < 1 ; i++){
            ItemNode node = new ItemNode();
            node.mTitle = "小米手机4" + i;
            node.mCount = i + 1;
            node.mPrice = "1999";
            node.mThumbnailUrl = "http://static.home.mi.com/app/shop/img?id=shop_466161732d4d67bb15d3ae550da7c0f8.jpg&t=jpeg&z=1&q=80";

            lists.add(node);
        }

        CartListViewAdapter adapter = new CartListViewAdapter(this, lists);
        mCartList.setAdapter(adapter);

        View addressSelector = findViewById(R.id.address_selector);
        addressSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "select address");
            }
        });
    }

    public void showInvoiceEdit(View v){
        mInvoiceEdit.setVisibility(View.VISIBLE);
    }

    public void hideInvoiceEdit(View v){
        mInvoiceEdit.setVisibility(View.GONE);
    }

    class ItemNode{
        String mTitle;
        String mPrice;
        int mCount;
        String mThumbnailUrl;
    }

    class CartListViewAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<ItemNode> cartNodeList;

        class ViewHolder {
            ImageView image;
            TextView title;
            TextView price;
            TextView count;
        }

        public CartListViewAdapter(Context c, List<ItemNode> list) {
            context = c;
            inflater = LayoutInflater.from(context);
            cartNodeList = list;
        }

        @Override
        public int getCount() {
            return cartNodeList.size();
        }

        @Override
        public Object getItem(int index) {
            return cartNodeList.get(index);
        }

        @Override
        public long getItemId(int index) {
            return index;
        }

        @Override
        public View getView(int index, View view, ViewGroup group) {
            ViewHolder holder;

            final ItemNode node = cartNodeList.get(index);

            if (view == null) {
                view = inflater.inflate(R.layout.device_shop_orderlist_goods_item, null);
                holder = new ViewHolder();

                holder.image = (ImageView)view.findViewById(R.id.item_thumbnail);
                holder.title = (TextView)view.findViewById(R.id.item_desc);
                holder.price =  (TextView)view.findViewById(R.id.goods_price);
                holder.count =  (TextView)view.findViewById(R.id.goods_count);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.image.setImageResource(R.drawable.device_shop_image_default_logo);

            Picasso.with(getApplicationContext()).load(node.mThumbnailUrl).into(holder.image);

            holder.title.setText(node.mTitle);
            holder.price.setText(getString(
                    R.string.device_shop_cart_checkout_total_price, node.mPrice));
            holder.count.setText(getString(
                    R.string.device_shop_order_goods_item_count_fmt, node.mCount));

            return view;
        }

    }
}