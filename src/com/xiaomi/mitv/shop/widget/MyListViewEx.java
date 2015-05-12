package com.xiaomi.mitv.shop.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.*;
import com.xiaomi.mitv.api.util.KeyPressHelper;
import com.xiaomi.mitv.shop.R;

import java.lang.reflect.Field;

/**
 * Created by niuyi on 2015/5/11.
 */
public class MyListViewEx extends FrameLayout implements View.OnFocusChangeListener {

    private static final String TAG = "MyListViewEx";
    private MyListView mListView;
    private BaseAdapter mListAdapter = null;
    private ImageView mSelectorView;
    private View mStubView;
    private Scroller mScroller;

    private long mLastKeyTime = 0;
    private int mScrollDuration = 180;
    private int mSelectorDuration = 150;
    private int mSelection = 0;
    private ObjectAnimator mSelectorAnimator;
    private float mSelectorTop = 0;
    private int mTopPosition = 0;
    private float maskTransY = Float.MIN_VALUE;

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

        //list view
        mListView = new MyListView(context);
        mListView.setPadding(40, 0, 40, 0);
        mListView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mListView.setSelector(android.R.color.transparent);
        mListView.setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY);
        mListView.setFocusable(true);

        FrameLayout.LayoutParams para = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        para.topMargin = 40;
        addView(mListView, para);


        //selectorview
        mSelectorView = new ImageView(getContext());

        para = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 320);
        para.gravity = Gravity.CENTER_HORIZONTAL;

        mSelectorView.setScaleType(ImageView.ScaleType.FIT_XY);
        mSelectorView.setBackgroundResource(R.drawable.selector);

        this.setOnFocusChangeListener(this);
        mListView.setOnFocusChangeListener(this);

        addView(mSelectorView, para);

        //stub view
        mStubView = new View(context);
        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        mStubView.setLayoutParams(new ListView.LayoutParams(1, screenHeight));
        mListView.addFooterView(mStubView);

        setWillNotDraw(false);
        mScroller = new Scroller();
    }

    public void setAdapter(BaseAdapter adapter){
        mListAdapter = adapter;
        mListView.setAdapter(adapter);
    }

    public void setSelection(int selection){
        Log.d(TAG, "set selection to " + selection);
        mSelection = selection;
        invalidateSelection();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener){
        mListView.setOnItemClickListener(listener);
    }

    public int getSelection(){
        return mSelection;
    }

    private void invalidateSelection(){
        maskTransY = Float.MIN_VALUE;
        invalidate();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.i(TAG, "dispatchKeyEvent: " + event + " ,focus: " + this.getFocusedChild().getClass().getCanonicalName());

        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                && event.getAction() == KeyEvent.ACTION_DOWN){
            View selectionView = getSelectionView(mSelection);

            Log.i(TAG, "ON CLICK: " + selectionView);
            Log.i(TAG, "ON CLICK: " + mSelection);
        }

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

    private void handleUpDownKey(KeyEvent event) {
        Log.i(TAG, "handleUpDownKey : keyCode = " + event.getKeyCode() +
                ", mSelection = " + mSelection);

        if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP){
            if(mSelection > 0){
                mSelection--;
                postSelection();
                View selectionView = getSelectionView(mSelection);
                if(selectionView != null){
                    float targetY = calcSelector(selectionView);
                    moveSelector(targetY);
                    return;
                }

                mTopPosition = mSelection;
                mScroller.scrollTo(mSelection, mScrollDuration);
            }
        }else if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN){
            Log.i(TAG, "mListView.getCount(): " + mListView.getCount());
            if(mSelection >= 0 && mSelection < mListView.getCount() - 2){
                mSelection++;
                postSelection();
                Log.d(TAG, "move selection to = " + mSelection);
                View selectionView = getSelectionView(mSelection);
                if(selectionView != null){
                    float targetY = calcSelector(selectionView);
                    Log.d(TAG, "selectionView top = " + selectionView.getTop());
                    moveSelector(targetY);
                    return;
                }
                mTopPosition++;
                Log.d(TAG, "smoothScrollToPositionFromTop to = " + mTopPosition);
                mListView.smoothScrollToPositionFromTop(mTopPosition, 0, mScrollDuration);
                setLinearInterpolator();
            }
        }


    }

    private float calcSelector(View selectionView){
        Log.i(TAG, "calcSelector: " + mSelection);
        return selectionView.getTop();
//        if(mSelection == 0){
//            return selectionView.getTop() - 38;
//        }else{
//            int top = selectionView.getTop() - 38;
//            return top - 1;//һ�����صĽ���
//        }
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

    @Override
    public void draw(Canvas canvas) {
        Log.i(TAG, "draw");

        super.draw(canvas);
        if(maskTransY == Float.MIN_VALUE){
            View selectionView = getSelectionView(mSelection);
            if(selectionView != null){
                Log.i(TAG, "draw, selectionView not null");
                mSelectorView.setVisibility(View.VISIBLE);
                maskTransY = calcSelector(selectionView);
                mSelectorView.setTranslationY(maskTransY);
            }else{
                Log.i(TAG, "draw, selectionView is null");
                if(mSelection >= 0 && mSelection < mListView.getCount() - 1){
                    Log.i(TAG, "mSelection: " + mSelection);
                    mListView.setSelectionFromTop(mSelection, 0);
                    mTopPosition = mSelection;
                }
            }
        }
        if(mSelection < 0 || mSelection >= mListView.getCount() - 1 || mListView.getCount() <= 0){
            if(mSelectorView.getVisibility() == View.VISIBLE){
                mSelectorView.setVisibility(View.INVISIBLE);            }
        }
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

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        Log.i(TAG, "onFocusChange: " + view.getClass().getCanonicalName() + " ,hasFocus: " + hasFocus);
        if(view == this && hasFocus){
            mListView.requestFocus();
        }
        if(view == mListView){
//            mIsFocused = hasFocus;
            invalidateSelection();
        }
    }



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
            Log.i(TAG, "MyListView:onLayout");
            mScroller.stop();
            setSelection(mTopPosition);
//            MILog.d(TAG, "onLayout: set top position = " + mTopPosition);
            super.onLayout(changed, l, t, r, b);
        }
    }

    private class Scroller implements Runnable{

        private final static int INVALID_POS = -1;
        private int mTargetPos = INVALID_POS;
        private int mLastFirstPos = INVALID_POS;
        private int mDuration = 200;

        public void scrollTo(int position, int duration){
            if(mListView.getChildCount() == 1 || position < 0 || position >= mListView.getCount()){
                return;
            }
            mHandler.removeCallbacks(this);
            mDuration = duration;
            mTargetPos = position;
            boolean isHandled = handleScroll();
            mLastFirstPos = mListView.getFirstVisiblePosition();
            if(!isHandled){
                mHandler.post(this);
            }
        }

        public void stop(){
            mListView.setScrollY(0);
            mListView.smoothScrollBy(0, 0);
            mHandler.removeCallbacks(this);
        }

        @Override
        public void run() {
            if(mLastFirstPos == INVALID_POS){
                mHandler.removeCallbacks(this);
                return;
            }
            int firstPos = mListView.getFirstVisiblePosition();
            if(mLastFirstPos == firstPos){
                mHandler.post(this);
                return;
            }
            mLastFirstPos = firstPos;
            boolean isHandled = handleScroll();
            if(!isHandled){
                mHandler.post(this);
            }else{
                mHandler.removeCallbacks(this);
                mLastFirstPos = INVALID_POS;
            }
        }
        private boolean handleScroll(){
            int firstPos = mListView.getFirstVisiblePosition();
            int lastPos = firstPos + mListView.getChildCount() - 1;
            if(mTargetPos >= firstPos && mTargetPos <= lastPos){
                int targetTop = (int) mListView.getChildAt(mTargetPos - firstPos).getTop();
                if(targetTop != 0){
                    mListView.smoothScrollBy(targetTop, mDuration);
                    setLinearInterpolator();
                    return true;
                }
            }else if(mTargetPos < firstPos){
                mListView.smoothScrollBy(-mListView.getChildAt(0).getHeight(), mDuration);
                setLinearInterpolator();
            }else if(mTargetPos > lastPos){
                mListView.smoothScrollBy(mListView.getChildAt(0).getHeight(), mDuration);
                setLinearInterpolator();
            }
            return false;
        }
    }

    private void setLinearInterpolator(){
        try {
            Field flingField = AbsListView.class.getDeclaredField("mFlingRunnable");
            flingField.setAccessible(true);
            Object flingObject = flingField.get(mListView);
            Class<?> flingRunnableClass = Class.forName("android.widget.AbsListView$FlingRunnable");
            Field scrollerField = flingRunnableClass.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            Object scroller = scrollerField.get(flingObject);
            Field interpolatorField = OverScroller.class.getDeclaredField("mInterpolator");
            interpolatorField.setAccessible(true);
            interpolatorField.set(scroller, new LinearInterpolator());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
