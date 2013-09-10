package net.solutinno.tutorial.slide;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewPropertyAnimator;

public class MainActivity extends ActionBarActivity {

    private TextView mTextView;
    private ScrollView mScrollView;

    private GestureDetectorCompat mGesture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.textview);
        mScrollView = (ScrollView) findViewById(R.id.scrollview);
        mGesture = new GestureDetectorCompat(this, mOnGestureListener);
        mScrollView.setOnTouchListener(mOnTouchListener);
    }

    void log(String format, Object... args) {
        Log.d("SOLUTINNO", String.format(format, args));
    }

    View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mGesture.onTouchEvent(motionEvent);
            return false;
        }
    };

    GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            mTextView.setTranslationY(0);
            textViewAnimator(0);
            return true;
        }
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            textViewPropertyAnimation(distanceY);
            //textViewAnimator(distanceY);
            return true;
        }
    };

    private void textViewAnimator(float distanceY) {
        if (mTextView.getVisibility() == View.GONE) {
            TranslateAnimation anim = new TranslateAnimation(0,0,-mTextView.getHeight(),0);
            anim.setDuration(200);
            anim.setFillAfter(true);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) { }
                @Override
                public void onAnimationEnd(Animation animation) { mTextView.setVisibility(View.VISIBLE); }
                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
            mTextView.startAnimation(anim);
        }
        else {
            TranslateAnimation anim = new TranslateAnimation(0,0,0,-mTextView.getHeight());
            anim.setDuration(200);
            anim.setFillAfter(true);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) { mTextView.setVisibility(View.GONE); }
                @Override
                public void onAnimationEnd(Animation animation) { }
                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
            mTextView.startAnimation(anim);
        }
    }

    float textViewTranslation;
    void textViewPropertyAnimation(float distanceY) {
        textViewTranslation += (-distanceY);
        if (distanceY > 0) textViewTranslation = textViewTranslation < -mTextView.getHeight() ? -mTextView.getHeight() : textViewTranslation;
        else if (distanceY < 0) textViewTranslation = textViewTranslation > 0 ? 0 : textViewTranslation;
        ViewPropertyAnimator.animate(mTextView).setDuration(0).translationY(textViewTranslation);
    }
}
