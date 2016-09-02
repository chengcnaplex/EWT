package com.ateam.ewt;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WelcomeUi extends AppCompatActivity {

    // 采用注解的方式直接给对象赋值
    @InjectView(R.id.icon_w)
    public ImageView mIgIconW;
    @InjectView(R.id.icon_e)
    public ImageView mIgIconE;
    @InjectView(R.id.icon_t)
    public ImageView mIgIconT;

    // 对应三张图片的x轴坐标
    private int iconW_x;
    private int iconE_x;
    private int iconT_x;

    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_welcome);

        // 在这里会执行前面的注解
        ButterKnife.inject(this);

        // 初始化view
        initView();
    }

    private void initView() {
        // 隐藏view，刚开始view位置是布局好的
        hiddenView();

        // 这里就是随便找了一个view来添加布局完成监听，以获取屏幕宽度，只会运行一次
        mIgIconE.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 初始化x轴坐标，屏幕宽度
                initViewAxes();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mIgIconE.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }else{
                    mIgIconE.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                // 开始动画
                startAnim();
            }
        });
    }

    private void hiddenView() {
        mIgIconE.setVisibility(View.INVISIBLE);
        mIgIconW.setVisibility(View.INVISIBLE);
        mIgIconT.setVisibility(View.INVISIBLE);
    }

    private void initViewAxes() {
        iconW_x = mIgIconW.getLeft();
        iconE_x = mIgIconE.getLeft();
        iconT_x = mIgIconT.getLeft();

        WindowManager wm = (WindowManager) WelcomeUi.this
                .getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();

    }

    private void startAnim() {

        AnimatorSet animSet = new AnimatorSet();

        // 设置进入动画
        ObjectAnimator animW = ObjectAnimator.ofFloat(mIgIconW, "translationX",
                screenWidth, 0);
        ObjectAnimator animE = ObjectAnimator.ofFloat(mIgIconE,"translationX",
                screenWidth,0);
        ObjectAnimator animT = ObjectAnimator.ofFloat(mIgIconT,"translationX",
                screenWidth,0);
        animW.addListener(new StartAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mIgIconW.setVisibility(View.VISIBLE);
            }
        });
        animE.addListener(new StartAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mIgIconE.setVisibility(View.VISIBLE);
            }
        });
        animT.addListener(new StartAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mIgIconT.setVisibility(View.VISIBLE);
            }
        });
        animW.setDuration(500);
        animE.setDuration(500);
        animT.setDuration(500);
        animSet.play(animW);
        animSet.play(animE).after(animW);
        animSet.play(animT).after(animE);

        // 设置移除动画
        ObjectAnimator animWafter = ObjectAnimator.ofFloat(mIgIconW, "translationX",
                0, -screenWidth);
        ObjectAnimator animEafter = ObjectAnimator.ofFloat(mIgIconE,"translationX",
                0, -screenWidth);
        ObjectAnimator animTafter = ObjectAnimator.ofFloat(mIgIconT,"translationX",
                0, -screenWidth);
        animWafter.setDuration(500);
        animSet.play(animTafter).after(animT);
        animSet.play(animEafter).after(animT);
        animSet.play(animWafter).after(animT);

        animSet.start();

    }

    private abstract class StartAnimatorListener implements Animator.AnimatorListener{
        @Override
        public void onAnimationEnd(Animator animator) { }

        @Override
        public void onAnimationCancel(Animator animator) { }

        @Override
        public void onAnimationRepeat(Animator animator) { }
    }
}
