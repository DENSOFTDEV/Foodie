package com.iblinfotech.foodierecipe.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.FrameLayout;

import com.daprlabs.cardstack.SwipeListener;
import com.iblinfotech.foodierecipe.Activity.DiscoverSwipeActivity;
import com.iblinfotech.foodierecipe.utils.GlobalClass;

import java.util.ArrayList;


public class SwipeDeck extends FrameLayout {

    private static int NUMBER_OF_CARDS;
    private float ROTATION_DEGREES;
    private float CARD_SPACING;
    private boolean RENDER_ABOVE;
    private boolean RENDER_BELOW;
    private float OPACITY_END;
    private int CARD_GRAVITY;
    private int paddingLeft;
    private boolean hardwareAccelerationEnabled = true;

    private int paddingRight;
    private int paddingTop;
    private int paddingBottom;

    private com.daprlabs.cardstack.SwipeDeck.SwipeEventCallback eventCallback;
    private com.daprlabs.cardstack.SwipeDeck.CardPositionCallback cardPosCallback;

    private Adapter mAdapter;
    DataSetObserver observer;
    int nextAdapterCard = 0;
    private boolean restoreInstanceState = false;

    private SwipeListener swipeListener;
    private int leftImageResource;
    private int rightImageResource;
    private boolean cardInteraction;

    public SwipeDeck(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                com.daprlabs.cardstack.R.styleable.SwipeDeck,
                0, 0);
        try {
            NUMBER_OF_CARDS = a.getInt(com.daprlabs.cardstack.R.styleable.SwipeDeck_max_visible, 3);
            ROTATION_DEGREES = a.getFloat(com.daprlabs.cardstack.R.styleable.SwipeDeck_rotation_degrees, 15f);
            CARD_SPACING = a.getDimension(com.daprlabs.cardstack.R.styleable.SwipeDeck_card_spacing, 15f);
            RENDER_ABOVE = a.getBoolean(com.daprlabs.cardstack.R.styleable.SwipeDeck_render_above, true);
            RENDER_BELOW = a.getBoolean(com.daprlabs.cardstack.R.styleable.SwipeDeck_render_below, false);
            CARD_GRAVITY = a.getInt(com.daprlabs.cardstack.R.styleable.SwipeDeck_card_gravity, 0);
            OPACITY_END = a.getFloat(com.daprlabs.cardstack.R.styleable.SwipeDeck_opacity_end, 0.33f);
        } finally {
            a.recycle();
        }

        paddingBottom = getPaddingBottom();
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        paddingTop = getPaddingTop();

        //set clipping of view parent to false so cards render outside their view boundary
        //make sure not to clip to padding
        setClipToPadding(false);
        setClipChildren(false);

        this.setWillNotDraw(false);

        //render the cards and card deck above or below everything
        if (RENDER_ABOVE) {
            ViewCompat.setTranslationZ(this, Float.MAX_VALUE);
        }
        if (RENDER_BELOW) {
            ViewCompat.setTranslationZ(this, Float.MIN_VALUE);
        }
    }

    /**
     * Set Hardware Acceleration Enabled.
     *
     * @param accel
     */
    public void setHardwareAccelerationEnabled(Boolean accel) {
        this.hardwareAccelerationEnabled = accel;
    }

    public void setAdapter(Adapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(observer);
        }
        mAdapter = adapter;
        // if we're not restoring previous instance state
        if (!restoreInstanceState) nextAdapterCard = 0;

        observer = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                //handle data set changes
                //if we need to add any cards at this point (ie. the amount of cards on screen
                //is less than the max number of cards to display) add the cards.
                int childCount = getChildCount();
                //only perform action if there are less cards on screen than NUMBER_OF_CARDS
                Log.e("----DD----", "childCount:----" + childCount);
                if (childCount < NUMBER_OF_CARDS) {
                    Log.e("----EE----", "childCount:----" + childCount);

                    for (int i = childCount; i < NUMBER_OF_CARDS; ++i) {
                        addNextCard();
                    }
                    //position the items correctly on screen
                    for (int i = 0; i < getChildCount(); ++i) {
                        positionItem(i);
                    }
                }
            }

            @Override
            public void onInvalidated() {
                Log.e("---FF----", "onInvalidated:----");

                //reset state, remove views and request layout
                nextAdapterCard = 0;
                removeAllViews();
                requestLayout();
            }
        };

        adapter.registerDataSetObserver(observer);
        removeAllViewsInLayout();
        requestLayout();
    }


    public void setSelection(int position) {
        if (position < mAdapter.getCount()) {
            this.nextAdapterCard = position;
            removeAllViews();
            requestLayout();
        }
    }

    public View getSelectedView() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // if we don't have an adapter, we don't need to do anything
        if (mAdapter == null || mAdapter.getCount() == 0) {
            nextAdapterCard = 0;
            removeAllViewsInLayout();
            return;
        }

        //pull in views from the adapter at the position the top of the deck is set to
        //stop when you get to for cards or the end of the adapter
        int childCount = getChildCount();
        for (int i = childCount; i < NUMBER_OF_CARDS; ++i) {
            addNextCard();
        }
        for (int i = 0; i < getChildCount(); ++i) {
            positionItem(i);
        }
        //position the new children we just added and set up the top card with a listener etc
    }

    private void removeTopCard() {
        //top card is now the last in view children
//        ankita Last Card also removes...
        final View child = getChildAt(getChildCount() - 1);

//        ankita Last Card dont removes...
//        int childOffset = getChildCount() - NUMBER_OF_CARDS + 1;
//        final View child = getChildAt(getChildCount() - childOffset);
        Log.e("-------ANKITA---", "child" + child);

//        DiscoverSwipeActivity.discoverModeDatas.remove(DiscoverSwipeActivity.discoverModeDatas.size() - 1);
//        DiscoverSwipeActivity.adapter.notifyDataSetChanged();

        if (child != null) {
            Log.e("-------ANKITA-if_1--", "child" + child);
            child.setOnTouchListener(null);
            swipeListener = null;
            //this will also check to see if cards are depleted
            removeViewWaitForAnimation(child);
        }


    }

    private void removeViewWaitForAnimation(View child) {
        new RemoveViewOnAnimCompleted().execute(child);


    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
    }

    private void addNextCard() {
        if (nextAdapterCard < mAdapter.getCount()) {
            Log.e("---XXXXXXXXX---ifff----", "size::----" + DiscoverSwipeActivity.discoverModeDatas.size());

            // TODO: Make view recycling work
            // TODO: Instead of removing the view from here and adding it again when it's swiped
            // ... don't remove and add to this instance: don't call removeView & addView in sequence.
            View newBottomChild = mAdapter.getView(nextAdapterCard, null/*lastRemovedView*/, this);

            if (hardwareAccelerationEnabled) {
                //set backed by an off-screen buffer
                newBottomChild.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }

            //set the initial Y value so card appears from under the deck
            //newBottomChild.setY(paddingTop);
            addAndMeasureChild(newBottomChild);
            nextAdapterCard++;
        } else {
//            Log.e("---XXXXXXXXX-------", "size::----" + DiscoverSwipeActivity.discoverModeDatas.size());
            int size = DiscoverSwipeActivity.discoverModeDatas.size();
            for (int i = 0; i < size; i++) {
//                Log.e("---for loopppp-------", "size::----" + DiscoverSwipeActivity.discoverModeDatas.size());
                DiscoverSwipeActivity.discoverModeDatas.add(DiscoverSwipeActivity.discoverModeDatas.get(i));
                DiscoverSwipeActivity.adapter.notifyDataSetChanged();
            }


        }
        setupTopCard();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setZTranslations() {
        //this is only needed to add shadows to cardviews on > lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int count = getChildCount();
            for (int i = 0; i < count; ++i) {
                getChildAt(i).setTranslationZ(i * 10);
            }
        }
    }

    /**
     * Adds a view as a child view and takes care of measuring it
     *
     * @param child The view to add
     */
    private void addAndMeasureChild(View child) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        }

        //ensure new card is under the deck at the beginning
        child.setY(paddingTop);

        //every time we add and measure a child refresh the children on screen and order them
        ArrayList<View> children = new ArrayList<>();
        children.add(child);
        for (int i = 0; i < getChildCount(); ++i) {
            children.add(getChildAt(i));
        }

        removeAllViews();

        for (View c : children) {
            addViewInLayout(c, -1, params, true);
            int itemWidth = getWidth() - (paddingLeft + paddingRight);
            int itemHeight = getHeight() - (paddingTop + paddingBottom);
            c.measure(View.MeasureSpec.EXACTLY | itemWidth, View.MeasureSpec.EXACTLY | itemHeight); //MeasureSpec.UNSPECIFIED

            //ensure that if there's a left and right image set their alpha to 0 initially
            //alpha animation is handled in the swipe listener
            if (leftImageResource != 0) child.findViewById(leftImageResource).setAlpha(0);
            if (rightImageResource != 0) child.findViewById(rightImageResource).setAlpha(0);
        }
        setZTranslations();
    }

    /**
     * Positions the children at the "correct" positions
     */
    //ANKITA\\\\\old commented method
//    private void positionItem(int index) {
//
//        View child = getChildAt(index);
//
//        int width = child.getMeasuredWidth();
//        int height = child.getMeasuredHeight();
//        int left = (getWidth() - width) / 2;
//        child.layout(left, paddingTop, left + width, paddingTop + height);
//        //layout each child slightly above the previous child (we start with the bottom)
//        int childCount = getChildCount();
//        float offset = (int) (((childCount - 1) * CARD_SPACING) - (index * CARD_SPACING));
//        //child.setY(paddingTop + offset);
//
//        child.animate().setDuration(restoreInstanceState ? 0 : 160).y(paddingTop - (offset / 3));
//        //ankita showing cards at top---
////        child.animate().setDuration(restoreInstanceState ? 0 : 160).y(paddingTop + offset);
//
//        restoreInstanceState = false;
//    }
    private void positionItem(int index) {
        View child = getChildAt(index);
        float offset = 1;
//        int width = child.getMeasuredWidth();
//        int height = child.getMeasuredHeight();
        int width = (int) (GlobalClass.DeviceWidth * 0.5);
        int height = child.getMeasuredHeight();
//        child.getLayoutParams().width = ((int) (GlobalClass.DeviceWidth));
        if (index == 0) {
            width = (int) (GlobalClass.DeviceWidth * 0.5);
        } else if (index == 1) {
            width = (int) (GlobalClass.DeviceWidth * 0.6);
        } else if (index == 2) {
            width = (int) (GlobalClass.DeviceWidth * 0.7);
        }

        int left = (getWidth() - width) / 2;
        child.layout(left, paddingTop, left + width, paddingTop + height);
        //layout each child slightly above the previous child (we start with the bottom)
        int childCount = getChildCount();
        offset = (int) (((childCount - 1) * CARD_SPACING) - (index * CARD_SPACING));
        child.animate().setDuration(restoreInstanceState ? 0 : 160).y(paddingTop - (offset / 3));
        restoreInstanceState = false;
//        float offset = (int) (((childCount - 1) * CARD_SPACING) - (index * CARD_SPACING));
        //child.setY(paddingTop + offset);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == View.MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == View.MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = widthSize;
        } else {
            //Be whatever you want
            width = widthSize;
        }

        //Measure Height
        if (heightMode == View.MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == View.MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = heightSize;
        } else {
            //Be whatever you want
            height = heightSize;
        }
        setMeasuredDimension(width, height);
    }


    private void setupTopCard() {

        int childCount = getChildCount();
        final View child;
        //TODO: maybe find a better solution this is kind of hacky
        //if there's an extra card on screen that means the top card is still being animated
        //in that case setup the next card along
        if (childCount == (NUMBER_OF_CARDS + 1)) {
            child = getChildAt(getChildCount() - 2);
        } else {
            child = getChildAt(getChildCount() - 1);
        }

        //this calculation is to get the correct position in the adapter of the current top card
        //the card position on setup top card is currently always the bottom card in the view
        //at any given time.
        int initialX = paddingLeft;
        int initialY = paddingTop;

        if (child != null) {
            Log.e("---AA---", "child:--" + child);
            //make sure we have a card
            swipeListener = new SwipeListener(child, new SwipeListener.SwipeCallback() {
                @Override
                public void cardSwipedLeft() {
                    int positionInAdapter = nextAdapterCard - getChildCount();
                    removeTopCard();
                    if (eventCallback != null) eventCallback.cardSwipedLeft(positionInAdapter);
                    addNextCard();
                }

                @Override
                public void cardSwipedRight() {
                    int positionInAdapter = nextAdapterCard - getChildCount();
                    removeTopCard();
                    if (eventCallback != null) eventCallback.cardSwipedRight(positionInAdapter);
                    addNextCard();
                }

                @Override
                public void cardOffScreen() {
                }

                @Override
                public void cardActionDown() {
                    if (eventCallback != null) eventCallback.cardActionDown();
                    cardInteraction = true;
                }

                @Override
                public void cardActionUp() {

                    if (eventCallback != null) eventCallback.cardActionUp();
                    cardInteraction = false;
                }

            }, initialX, initialY, ROTATION_DEGREES, OPACITY_END);


            //if we specified these image resources, get the views and pass them to the swipe listener
            //for the sake of animating them
            View rightView = null;
            View leftView = null;
            if (!(rightImageResource == 0)) rightView = child.findViewById(rightImageResource);
            if (!(leftImageResource == 0)) leftView = child.findViewById(leftImageResource);
            swipeListener.setLeftView(leftView);
            swipeListener.setRightView(rightView);
            child.setOnTouchListener(swipeListener);
        }
    }

    public void setEventCallback(com.daprlabs.cardstack.SwipeDeck.SwipeEventCallback eventCallback) {
        this.eventCallback = eventCallback;
    }


    public void swipeTopCardLeft(int duration) {

        int childCount = getChildCount();
        if (childCount > 0 && getChildCount() < (NUMBER_OF_CARDS + 1)) {
            swipeListener.animateOffScreenLeft(duration);

            int positionInAdapter = nextAdapterCard - getChildCount();
            removeTopCard();
            if (eventCallback != null) eventCallback.cardSwipedLeft(positionInAdapter);
            addNextCard();
        }

    }

    public void swipeTopCardRight(int duration) {
        int childCount = getChildCount();
        if (childCount > 0 && getChildCount() < (NUMBER_OF_CARDS + 1)) {
            swipeListener.animateOffScreenRight(duration);

            int positionInAdapter = nextAdapterCard - getChildCount();
            removeTopCard();
            if (eventCallback != null) eventCallback.cardSwipedRight(positionInAdapter);
            addNextCard();
        }
    }

    public void setPositionCallback(com.daprlabs.cardstack.SwipeDeck.CardPositionCallback callback) {
        cardPosCallback = callback;
    }

    public void setLeftImage(int imageResource) {
        leftImageResource = imageResource;
    }

    public void setRightImage(int imageResource) {
        rightImageResource = imageResource;
    }

    public interface SwipeEventCallback {
        //returning the object position in the adapter
        void cardSwipedLeft(int position);

        void cardSwipedRight(int position);

        void cardsDepleted();

        void cardActionDown();

        void cardActionUp();
    }

    public interface CardPositionCallback {
        void xPos(Float x);

        void yPos(Float y);
    }

    private int AnimationTime = 160;

    private class RemoveViewOnAnimCompleted extends AsyncTask<View, Void, View> {

        @Override
        protected View doInBackground(View... params) {
            android.os.SystemClock.sleep(AnimationTime);
            return params[0];
        }

        @Override
        protected void onPostExecute(View view) {
            super.onPostExecute(view);
            removeView(view);

            //if there are no more children left after top card removal let the callback know
            if (getChildCount() <= 0 && eventCallback != null) {
                eventCallback.cardsDepleted();
            }
        }
    }
}



