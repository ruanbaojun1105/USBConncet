package com.hwx.usbconnect.usbconncet.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.hwx.usbconnect.usbconncet.R;
import com.hwx.usbconnect.usbconncet.utils.IClickListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HorizontalPicker extends LinearLayout implements View.OnTouchListener {
    private final float DENSITY;
    @DrawableRes
    private int backgroundSelector;
    @ColorRes
    private int colorSelector;
    private int textSize;
    private int selectedIndex;
    private int itemHeight;
    private int itemWidth;
    private int itemMargin;
    private List<PickerItem> items;
    HorizontalPicker.OnSelectionChangeListener changeListener;
    private boolean canTouch=true;
    private boolean isGravity=false;
    IClickListener clickListener;

    public IClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(IClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setGravity(boolean gravity) {
        isGravity = gravity;
        setGravity(Gravity.CENTER);
        requestLayout();
    }

    public void setCanTouch(boolean canTouch) {
        this.canTouch = canTouch;
    }

    public HorizontalPicker.OnSelectionChangeListener getChangeListener() {
        return this.changeListener;
    }

    public void setChangeListener(HorizontalPicker.OnSelectionChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public HorizontalPicker(Context context) {
        this(context, (AttributeSet)null);
    }

    public HorizontalPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.DENSITY = this.getContext().getResources().getDisplayMetrics().density;
        this.textSize = 12;
        this.selectedIndex = -1;
        this.itemHeight = -2;
        this.itemWidth = -2;
        this.itemMargin = 20;
        this.items = new ArrayList();
        this.initAttributes(context, attrs, defStyleAttr);
        if (isGravity)
            this.setGravity(17);
    }

    private void initAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        this.textSize = (int)((float)this.textSize * this.DENSITY);
        this.itemMargin = (int)((float)this.itemMargin * this.DENSITY);
        this.selectedIndex = -1;
        if(attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HorizontalPicker, defStyleAttr, 0);
            this.backgroundSelector = array.getResourceId(R.styleable.HorizontalPicker_backgroundSelector, this.backgroundSelector);
            this.colorSelector = array.getResourceId(R.styleable.HorizontalPicker_textColorSelector, this.colorSelector);
            this.textSize = array.getDimensionPixelSize(R.styleable.HorizontalPicker_textSize, this.textSize);
            this.itemHeight = array.getDimensionPixelSize(R.styleable.HorizontalPicker_itemHeight, this.itemHeight);
            this.itemWidth = array.getDimensionPixelSize(R.styleable.HorizontalPicker_itemWidth, this.itemWidth);
            this.itemMargin = array.getDimensionPixelSize(R.styleable.HorizontalPicker_itemMargin, this.itemMargin);
            array.recycle();
        }

    }

    private void initViews() {
        this.removeAllViews();
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setOnTouchListener(this);
        //new LayoutParams(this.itemWidth, this.itemHeight);
        Iterator var4 = this.items.iterator();

        while(var4.hasNext()) {
            HorizontalPicker.PickerItem pickerItem = (HorizontalPicker.PickerItem)var4.next();
            if(pickerItem.hasDrawable()) {
                ImageView imageView = new ImageView(this.getContext());
                imageView.setImageResource(pickerItem.getDrawable());
                imageView.setScaleType(ScaleType.CENTER_INSIDE);
                this.initStyle(imageView);
                this.addView(imageView);
            } else if(pickerItem.getText() != null) {
                TextView textView = new TextView(this.getContext());
                textView.setGravity(Gravity.CENTER);
                textView.setText(pickerItem.getText());
                this.initStyle(textView);
                this.addView(textView);
            }
        }

    }

    private void initStyle(View view) {
        LayoutParams params = new LayoutParams(this.itemWidth, this.itemHeight);
        view.setLayoutParams(params);
        params.rightMargin=itemMargin;
        view.setBackgroundResource(this.backgroundSelector);
        if(view instanceof TextView) {
            ((TextView)view).setTextSize(0, (float)this.textSize);
            ((TextView)view).setTextColor(this.getResources().getColorStateList(this.colorSelector));
        }
        if (clickListener!=null)
            view.setOnClickListener(clickListener);
    }

    private void initStyles() {
        for(int i = 0; i < this.getChildCount(); ++i) {
            this.initStyle(this.getChildAt(i));
        }

    }

    public void setItems(List<HorizontalPicker.PickerItem> items) {
        this.items = items;
        this.initViews();
        this.selectChild(-1);
    }

    public void setItems(List<HorizontalPicker.PickerItem> items, int selectedIndex) {
        this.setItems(items);
        this.selectChild(selectedIndex);
    }

    public List<HorizontalPicker.PickerItem> getItems() {
        return this.items;
    }

    private void selectChild(int index) {
        if(this.selectedIndex != index) {
            this.selectedIndex = -1;

            for(int i = 0; i < this.getChildCount(); ++i) {
                this.getChildAt(i).setSelected(i == index);
                if(i == index) {
                    this.selectedIndex = index;
                }
            }

            if(this.changeListener != null) {
                this.changeListener.onItemSelect(this, this.selectedIndex);
            }
        }

    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!canTouch)
            return false;
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                Rect hitRect = new Rect();

                for (int i = 0; i < this.getChildCount(); ++i) {
                    View v = this.getChildAt(i);
                    v.getHitRect(hitRect);
                    if (hitRect.contains(x, y)) {
                        this.selectChild(i);
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        getParent().requestDisallowInterceptTouchEvent(true);
        return true;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectChild(selectedIndex);
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    public HorizontalPicker.PickerItem getSelectedItem() {
        return (HorizontalPicker.PickerItem)this.items.get(this.selectedIndex);
    }

    @DrawableRes
    public int getBackgroundSelector() {
        return this.backgroundSelector;
    }

    public void setBackgroundSelector(@DrawableRes int backgroundSelector) {
        this.backgroundSelector = backgroundSelector;
        this.initStyles();
    }

    @ColorRes
    public int getColorSelector() {
        return this.colorSelector;
    }

    public void setColorSelector(@ColorRes int colorSelector) {
        this.colorSelector = colorSelector;
        this.initStyles();
    }

    public int getTextSize() {
        return this.textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        this.initStyles();
    }

    public int getItemWidth() {
        return this.itemWidth;
    }

    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
        this.initStyles();
    }

    public int getItemMargin() {
        return this.itemMargin;
    }

    public void setItemMargin(int itemMargin) {
        this.itemMargin = itemMargin;
        this.initStyles();
    }

    public int getItemHeight() {
        return this.itemHeight;
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
        this.initStyles();
    }

    public interface OnSelectionChangeListener {
        void onItemSelect(HorizontalPicker var1, int var2);
    }

    public static class DrawableItem implements HorizontalPicker.PickerItem {
        @DrawableRes
        private int drawable;

        public DrawableItem(@DrawableRes int drawable) {
            this.drawable = drawable;
        }

        public String getText() {
            return null;
        }

        @DrawableRes
        public int getDrawable() {
            return this.drawable;
        }

        public boolean hasDrawable() {
            return true;
        }
    }

    public static class TextItem implements HorizontalPicker.PickerItem {
        private String text;

        public TextItem(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public int getDrawable() {
            return 0;
        }

        public boolean hasDrawable() {
            return false;
        }
    }

    public interface PickerItem {
        String getText();

        @DrawableRes
        int getDrawable();

        boolean hasDrawable();
    }
}