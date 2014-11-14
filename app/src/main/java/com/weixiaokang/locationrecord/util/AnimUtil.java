package com.weixiaokang.locationrecord.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

public class AnimUtil {

    public static AnimatorSet loadAnimation(View view, int duration, final int translationX, float... alpha) {
        AnimatorSet animatorSet = new AnimatorSet();
        view.setTranslationX(-translationX);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, Constants.TRANSLATIONX, 2 * translationX).setDuration(duration);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(view, Constants.ALPHA, alpha).setDuration(duration);
        objectAnimator.setRepeatCount(Animation.INFINITE);
        objectAnimator.setRepeatMode(Animation.RESTART);
        objectAnimator1.setRepeatCount(Animation.INFINITE);
        objectAnimator1.setRepeatMode(Animation.RESTART);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                LogUtil.i(Constants.WEATHER, "-->start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                LogUtil.i(Constants.WEATHER, "-->end");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                LogUtil.i(Constants.WEATHER, "-->cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                LogUtil.i(Constants.WEATHER, "-->repeat");
            }
        });

        objectAnimator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                LogUtil.i(Constants.WEATHER, "1-->start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                LogUtil.i(Constants.WEATHER, "1-->end");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                LogUtil.i(Constants.WEATHER, "1-->cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                LogUtil.i(Constants.WEATHER, "1-->repeat");
            }
        });
        animatorSet.playTogether(objectAnimator, objectAnimator1);
        animatorSet.start();
        return animatorSet;
    }

    public static void rotate(View view, float... a) {
        ObjectAnimator.ofFloat(view, "rotation", a).setDuration(3000).start();
    }
}