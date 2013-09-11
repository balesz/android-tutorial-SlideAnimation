package net.solutinno.tutorial.slide;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private TextView mTextView;
    private ListView mListView;

    private GestureDetectorCompat mGesture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGesture = new GestureDetectorCompat(this, mOnGestureListener);

        mTextView = (TextView) findViewById(R.id.textview);
        mListView = (ListView) findViewById(R.id.listview);

        initListView();
    }

    void initListView() {
        mTextView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        View header = new View(this);
        header.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mTextView.getMeasuredHeight()));

        mListView.addHeaderView(header, null, false);
        mListView.setHeaderDividersEnabled(true);
        mListView.setOnTouchListener(mOnTouchListener);

        ArrayList<String> data = new ArrayList<String>();
        for (int i = 1; i <= 100; ++i) data.add(String.format("Test Data %d", i));
        mListView.setAdapter(new ColorArrayAdapter(this, android.R.layout.simple_list_item_1, data));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reset) {
            resetListViewAndBar();
        }
        return true;
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
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            topBarAnimation(distanceY);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    };

    float mTranslation;

    void topBarAnimation(float distanceY) {
        mTranslation += (-distanceY);
        if (distanceY > 0) mTranslation = mTranslation < -mTextView.getHeight() ? -mTextView.getHeight() : mTranslation;
        else if (distanceY < 0) mTranslation = mTranslation > 0 ? 0 : mTranslation;

        AnimatorSet set = new AnimatorSet();
        set.setDuration(0);
        set.playTogether(
            ObjectAnimator.ofInt(mTextView, "top", (int)mTranslation).setDuration(0),
            ObjectAnimator.ofInt(mTextView, "bottom", mTextView.getHeight()+(int)mTranslation).setDuration(0)
        );
        set.start();
    }

    void resetListViewAndBar() {
        AnimatorSet set = new AnimatorSet();
        set.setDuration(0);
        set.addListener(new AnimationListenerImp() {
            @Override
            public void onAnimationEnd(Animator animator) {
                mTranslation = 0;
                mListView.setSelection(0);
            }
        });
        set.playTogether(
            ObjectAnimator.ofInt(mTextView, "top", 0).setDuration(0),
            ObjectAnimator.ofInt(mTextView, "bottom", mTextView.getHeight()).setDuration(0)
        );
        set.start();
    }

    public abstract class AnimationListenerImp implements Animator.AnimatorListener {
        public void onAnimationStart(Animator animator) {}
        public void onAnimationEnd(Animator animator) {}
        public void onAnimationCancel(Animator animator) {}
        public void onAnimationRepeat(Animator animator) {}
    }

    public static class ColorArrayAdapter extends ArrayAdapter<String> {
        public ColorArrayAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            assert view != null;
            if (position % 2 == 0) view.setBackgroundResource(R.color.blue);
            else if (position % 3 == 0) view.setBackgroundResource(R.color.green);
            else view.setBackgroundResource(R.color.red);
            return view;
        }
    }

}
