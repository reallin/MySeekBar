package com.lxj.seektest;





import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

public class SeekBarHint extends SeekBar implements SeekBar.OnSeekBarChangeListener {

    private int mPopupWidth;
    private int mPopupStyle;
    public static final int POPUP_FIXED = 1;
    public static final int POPUP_FOLLOW = 0;

    private PopupWindow mPopup;
    private PopupWindow mLeftPopup;
    private PopupWindow mRightPopup;
    private TextView mPopupTextView;
    private TextView mPopupLeftView;
    private TextView mPopupRightView;
    private int mYLocationOffset;
    private float leftText = 0;
    private float rightText = 0;
    private float progressText = 0;
    private float step;
    private OnSeekBarChangeListener mInternalListener;
    private OnSeekBarChangeListener mExternalListener;

    private OnSeekBarHintProgressChangeListener mProgressChangeListener;

    public interface OnSeekBarHintProgressChangeListener {
        public String onHintTextChanged(SeekBarHint seekBarHint, float progress);
    }

    public SeekBarHint (Context context) {
        super(context);
        init(context, null);
    }

    public SeekBarHint (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public SeekBarHint (Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){

        setOnSeekBarChangeListener(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeekBarHint);

        mPopupWidth = (int) a.getDimension(R.styleable.SeekBarHint_popupWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        mYLocationOffset = (int) a.getDimension(R.styleable.SeekBarHint_yOffset, 0);
        mPopupStyle = a.getInt(R.styleable.SeekBarHint_popupStyle, POPUP_FOLLOW);

        a.recycle();
        
        
    }

    public void setPopupStyle(int style){
        mPopupStyle = style;
    }

    public int getPopupStyle(){
        return mPopupStyle;
    }

    private void initHintPopup(){
        String popupText = null;

        if (mProgressChangeListener!=null){
            popupText = mProgressChangeListener.onHintTextChanged(this, cuclaProcess(leftText));
        }

        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View undoView = inflater.inflate(R.layout.popup, null);
        mPopupTextView = (TextView)undoView.findViewById(R.id.text);
        mPopupTextView.setText(popupText!=null? popupText : String.valueOf(cuclaProcess(leftText)));
       
       // mPopup.dismiss();
        if(mPopup == null)
            mPopup = new PopupWindow(undoView, mPopupWidth, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        else{
        	mPopup.dismiss();
            mPopup = new PopupWindow(undoView, mPopupWidth, ViewGroup.LayoutParams.WRAP_CONTENT, false);

        }
        //}
       // }
       // mPopup.setAnimationStyle(R.style.fade_animation);
        //showPopup();
        

    }
    
    private void initLeftText(){
    	
    	 LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         final View undoView = inflater.inflate(R.layout.leftpop, null);
         mPopupLeftView = (TextView)undoView.findViewById(R.id.lefttext);
         mPopupLeftView.setText(leftText+"");

         mLeftPopup = new PopupWindow(undoView, mPopupWidth, ViewGroup.LayoutParams.WRAP_CONTENT, false);

         mLeftPopup.setAnimationStyle(R.style.fade_animation);
    }
    
    private void initRightText(){
    	
   	 LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View undoView = inflater.inflate(R.layout.rightpop, null);
        mPopupRightView = (TextView)undoView.findViewById(R.id.righttext);
        mPopupRightView.setText(rightText+"");

        mRightPopup = new PopupWindow(undoView, mPopupWidth, ViewGroup.LayoutParams.WRAP_CONTENT, false);

        mRightPopup.setAnimationStyle(R.style.fade_animation);
   }
    public void setLeftText(float str){
    	this.leftText = str;
    }
    public void setRightText(float str){
	this.rightText = str;
    }
    public void setProgressText(float str){
    	this.progressText = str;
        }
    private void showPopup(){

        if(mPopupStyle==POPUP_FOLLOW){
            mPopup.showAtLocation(this, Gravity.LEFT | Gravity.BOTTOM, (int) (this.getX()+(int) getXPosition(this)), (int) (this.getY()+mYLocationOffset+this.getHeight()));
        }
        if (mPopupStyle==POPUP_FIXED){
            mPopup.showAtLocation(this, Gravity.CENTER | Gravity.BOTTOM, 0, (int) (this.getY()+mYLocationOffset+this.getHeight()));
        }
       
    }
    public void initShow(){
    	initHintPopup();
        initLeftText();
        initRightText();
       
        this.setMax((int)(rightText-leftText)*10);
        this.setProgress((int)((progressText-leftText)*10));
        mPopup.showAtLocation(this, Gravity.START | Gravity.BOTTOM, (int)(this.getX()+(int) getXPosition(this)), (int) (SeekBarHint.this.getY()+2*mYLocationOffset+SeekBarHint.this.getHeight()));
       // mPopupTextView.setText("100");
        mRightPopup.showAtLocation(this, Gravity.START | Gravity.BOTTOM, (int)this.getWidth()-this.getThumbOffset(), (int) (this.getY()+this.getHeight()));
        
        mPopupRightView.setText(rightText+"");
        mLeftPopup.showAtLocation(this, Gravity.START | Gravity.BOTTOM, 0, (int) (this.getY()+this.getHeight()));
        //mRightPopup.update( (int)this.getWidth(), (int) (this.getY()+mYLocationOffset+this.getHeight()),-1,-1);
       // mLeftPopup.update( 0, (int) (this.getY()+mYLocationOffset+this.getHeight()),-1,-1);
        
        mPopupLeftView.setText(leftText+"");
    }
    private void hidePopup(){
        if(mPopup.isShowing()) {
            mPopup.dismiss();
        }
    }

    public void setHintView(View view){
        //TODO
        //initHintPopup();
    }

    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        if (mInternalListener==null){
            mInternalListener = l;
            super.setOnSeekBarChangeListener(l);
        }else {
            mExternalListener = l;
        }
    }

    public void setOnProgressChangeListener(OnSeekBarHintProgressChangeListener l){
        mProgressChangeListener = l;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        String popupText = null;
        if (mProgressChangeListener!=null){
            popupText = mProgressChangeListener.onHintTextChanged(this, cuclaProcess(leftText));
        }

        if(mExternalListener !=null){
            mExternalListener.onProgressChanged(seekBar, progress, b);
        }

        step = cuclaProcess(leftText);
        mPopupTextView.setText(popupText!=null? popupText : String.valueOf(step));

        if(mPopupStyle==POPUP_FOLLOW){
            mPopup.update((int) (this.getX()+(int) getXPosition(seekBar)), (int) (this.getY()+2*mYLocationOffset+this.getHeight()), -1, -1);
        }
       /* mRightPopup.update( (int)this.getWidth(), (int) (this.getY()+mYLocationOffset+this.getHeight()),-1,-1);
        
        mPopupRightView.setText(rightText+"");*/
       /* mLeftPopup.update( 0, (int) (this.getY()+mYLocationOffset+this.getHeight()),-1,-1);
        mPopupLeftView.setText(leftText+"");*/

    }
    public float cuclaProcess(float left){
    	return (leftText*10+ getProgress())/10f;
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if(mExternalListener !=null){
            mExternalListener.onStartTrackingTouch(seekBar);
        }
        showPopup();
        
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(mExternalListener !=null){
            mExternalListener.onStopTrackingTouch(seekBar);
        }

        //hidePopup();
    }


    private float getXPosition(SeekBar seekBar){
        float val = (((float)seekBar.getProgress() * (float)(seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax());
        float offset = seekBar.getThumbOffset()*2;

        int textWidth = mPopupWidth;
        float textCenter = (textWidth/2.0f);

        float newX = val+offset - textCenter;

        return newX;
    }
}
