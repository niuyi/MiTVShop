package com.xiaomi.mitv.shop.widget;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import com.xiaomi.mitv.api.util.MILog;
import com.xiaomi.mitv.shop.R;

/**
 * Created by niuyi on 2015/5/11.
 */
public class MyListViewEx extends FrameLayout {

    private static final String TAG = "MyListViewEx";
    private MyListView mListView;
    private BaseAdapter mListAdapter = null;
    private ImageView mSelectorView;
    private long mLastKeyTime = 0;
    private int mScrollDuration = 180;
    private int mSelectorDuration = 150;
    private int mSelection = 0;
    private ObjectAnimator mSelectorAnimator;
    private float mSelectorTop = 0;
    private int mTopPosition = 0;

    protected int mHeightDelta = 0;

    private Handler mHandler = new Handler();

    public MyListViewEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyListViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyListViewEx(Context context) {
        super(context);
        init();
    }

    private void init(){
        final Context context = getContext();

        mListView = new MyListView(context);
        mListView.setPadding(0, 0, 0, 0);
        mListView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        mListView.setSelector(android.R.color.transparent);

        addView(mListView);

        mSelectorView = new ImageView(getContext());

        FrameLayout.LayoutParams para = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 242);
        para.gravity = Gravity.CENTER_HORIZONTAL;

//        mSelectorView.setScaleType(ImageView.ScaleType.FIT_XY);
        mSelectorView.setBackgroundResource(R.drawable.selector);

        addView(mSelectorView, para);

//        mListView.setFocusable(true);
//        this.setOnFocusChangeListener(this);
//        mListView.setOnFocusChangeListener(this);
//        mStubView = new View(context);
//        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
//        mStubView.setLayoutParams(new ListView.LayoutParams(1, screenHeight));
//        mListView.addFooterView(mStubView);
//        setWillNotDraw(false);
//
//        mScroller = new Scroller();
//        mKeyPressHelper.setOnKeyPressListener(this);
    }

    public void setAdapter(BaseAdapter adapter){
        mListAdapter = adapter;
        mListView.setAdapter(adapter);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP ||
                    event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN){
                if(System.currentTimeMillis() - mLastKeyTime > mScrollDuration - 5){
                    mLastKeyTime = System.currentTimeMillis();
                    handleUpDownKey(event);
                }
                return true;
            }
        }else if(event.getAction() == KeyEvent.ACTION_DOWN &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            mHandler.removeCallbacks(mUpdateSelection);
        }
        return super.dispatchKeyEvent(event);
    }

    private void handleUpDownKey(KeyEvent event){
        Log.d(TAG, "handleUpDownKey : keyCode = " + event.getKeyCode() +
                ", mSelection = " + mSelection);
        if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP){
            if(mSelection > 0){
                mSelection--;
                postSelection();
                View selectionView = getSelectionView(mSelection);
                handleUpKey(selectionView);
            }
        }else if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN){
            if(mSelection >= 0 && mSelection < mListView.getCount() - 2){
                int oldSeletion = mSelection;
                mSelection++;
                postSelection();
                handleDownKey();
            }
        }
    }

    private void handleUpKey(View selectionView){
        if(selectionView != null){
            float targetY = calcSelector(selectionView);
            Log.d(TAG, "selectionView top = " + selectionView.getTop());
            moveSelector(targetY);
        }else{
            mTopPosition = mSelection;
//            mScroller.scrollTo(mSelection, mScrollDuration);
        }
    }

    private void handleDownKey(){

        Log.d(TAG, "move selection to = " + mSelection);
        View selectionView = getSelectionView(mSelection);
        if(selectionView != null){
            float targetY = calcSelector(selectionView);
            Log.d(TAG, "selectionView top = " + selectionView.getTop());
            moveSelector(targetY);
        }else{
            mTopPosition++;
            mListView.smoothScrollToPositionFromTop(mTopPosition, 0, mScrollDuration);
//            setLinearInterpolator();
            if(mTopPosition == 1){//这是为了避免header跟别的child尺寸不一样
                if(mListView.getChildAt(0) != null && mListView.getChildAt(1) != null){
                    int deltaHeight = mListView.getChildAt(0).getHeight() - mListView.getChildAt(1).getHeight();
                    if(deltaHeight > 10){
                        float adjustY = mSelectorView.getTranslationY() - deltaHeight;
                        if((mSelectorAnimator != null) && mSelectorAnimator.isRunning()){
                            adjustY = mSelectorTop - deltaHeight;
                        }

                        moveSelector(adjustY);
                    }
                }
            }
        }
    }

    private float calcSelector(View selectionView){
        Log.i(TAG, "calcSelector: " + mSelection);

        if(mSelection == 0){
            return selectionView.getTop();
        }else{
            int top = selectionView.getTop();
            return top - 1;//一个像素的矫正
        }
    }


    public void postSelection(){
//        onSelectionChanged();
        mHandler.removeCallbacks(mUpdateSelection);
        mHandler.postDelayed(mUpdateSelection, mScrollDuration + 300);
    }

    private void moveSelector(float targetY){
        if(mSelectorAnimator != null){
            mSelectorAnimator.cancel();
        }
        mSelectorTop = targetY;
        mSelectorAnimator = ObjectAnimator.ofFloat(mSelectorView, "translationY",
                mSelectorView.getTranslationY(), mSelectorTop);
        mSelectorAnimator.setInterpolator(new LinearInterpolator());
        mSelectorAnimator.setDuration(mSelectorDuration);
        mSelectorAnimator.start();
    }

    public View getSelectionView(int selection){
        if(selection >= 0 && selection < mListView.getCount() - 1){
            int firstPosition = mListView.getFirstVisiblePosition();
            int index = selection - firstPosition;
            if(index >= 0 && index < mListView.getChildCount()){
                View view = mListView.getChildAt(index);
                if(view.getBottom() < mListView.getHeight() - mHeightDelta - mListView.getPaddingTop() -
                        mListView.getPaddingBottom() && view.getTop() >= mListView.getPaddingTop()){
                    return view;
                }
            }
        }
        return null;
    }

    private Runnable mUpdateSelection = new Runnable() {
        @Override
        public void run() {
//            if(mOnItemSelectedListener != null){
//                mOnItemSelectedListener.onItemSelected(MyListViewEx.this, mSelection);
//            }
            mListView.setScrollY(0);
            if(mListAdapter != null){
                mListAdapter.notifyDataSetChanged();
            }
        }
    };

    private class MyListView extends ListView{

        public MyListView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public MyListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public MyListView(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
//            mScroller.stop();
//            setSelection(mTopPosition);
//            MILog.d(TAG, "onLayout: set top position = " + mTopPosition);
            super.onLayout(changed, l, t, r, b);
        }
    }
}
