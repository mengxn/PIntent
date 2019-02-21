package me.codego.delegate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Window;

/**
 *
 * @author mengxn 2017/6/20
 */

public class PIntent {

    public static IRequest from(Context context) {
        if (context instanceof FragmentActivity) {
            return new IntentRequest((FragmentActivity) context);
        }
        throw new IllegalArgumentException("context should extend FragmentActivity");
    }

    /**
     * 执行当前界面场景动画
     * @param activity activity
     */
    public static void applyScene(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = activity.getIntent();
            if (intent != null && intent.hasExtra(IntentRequest.ANIMATION_SCENE)) {
                Window window = activity.getWindow();
                window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
                int resId = intent.getIntExtra(IntentRequest.ANIMATION_SCENE, 0);
                if (resId > 0) {
                    Transition transition = TransitionInflater.from(activity).inflateTransition(resId);
                    window.setEnterTransition(transition);
                }
            }
        }
    }

    public static class Config {

        private int enterResId, exitResId;

        /**
         * 动画参数
         */
        Bundle options;

        private Config() {
        }

        public static Config getInstance() {
            return new Config();
        }

        public void reset() {
            this.enterResId = 0;
            this.exitResId = 0;
        }

        public Config transition(int enterResId, int exitResId) {
            this.enterResId = enterResId;
            this.exitResId = exitResId;
            return this;
        }

        public void apply(Context context) {
            IntentRequest.defaultConfig = this;
            options = ActivityOptionsCompat.makeCustomAnimation(context, enterResId, exitResId).toBundle();
        }
    }

}