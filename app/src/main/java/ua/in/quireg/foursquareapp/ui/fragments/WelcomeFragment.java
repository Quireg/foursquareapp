package ua.in.quireg.foursquareapp.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.common.Utils;
import ua.in.quireg.foursquareapp.mvp.presenters.WelcomeScreenPresenter;
import ua.in.quireg.foursquareapp.mvp.views.WelcomeView;
import ua.in.quireg.foursquareapp.ui.activities.MainActivity;

/**
 * Created by Arcturus Mengsk on 2/4/2018, 5:45 PM.
 * foursquareapp
 */

public class WelcomeFragment extends MvpFragment implements WelcomeView {

    private static final int WELCOME_ANIM_DURATION = 1200; //ms
    private static final int EXIT_ANIM_DURATION = 1200; //ms

    private boolean mAnimationsEnabled = true;
    private int OFFSCREEN_OFFSET_X;

    @BindView(R.id.welcome_text)
    TextView mWelcomeText;
    @BindView(R.id.request_permissions)
    Button mRequestPermissionsButton;
    @BindView(R.id.request_places)
    Button mLookAroundButton;
    @BindView(R.id.cloud)
    ImageView mCloudImageView;

    @InjectPresenter
    WelcomeScreenPresenter mWelcomeScreenPresenter;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_screen, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        OFFSCREEN_OFFSET_X = Utils.getDisplaySize(getActivity().getApplicationContext()).x * -1;
        return view;
    }

    @OnClick(R.id.request_permissions)
    void onRequestPermissions() {
        mWelcomeScreenPresenter.requestPermissions();
    }

    @OnClick(R.id.request_places)
    void onLookAround() {
        mWelcomeScreenPresenter.lookAround();
    }

    @Override
    public void setAnimationsEnabled(boolean enabled) {
        mAnimationsEnabled = enabled;
    }

    @Override
    public void setReadyState() {
        if (mAnimationsEnabled) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(animateText(R.string.permission_ok_text, WELCOME_ANIM_DURATION))
                    .with(animateCloudIn(WELCOME_ANIM_DURATION))
                    .before(animateToggleViewVisibility(mLookAroundButton, true));
            animatorSet.start();
        } else {
            mCloudImageView.setX(0);
            mCloudImageView.setVisibility(View.VISIBLE);
            mWelcomeText.setText(R.string.permission_ok_text);
            mLookAroundButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setPermissionsRequiredState() {
        if (mAnimationsEnabled) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(animateText(R.string.permission_required_text, WELCOME_ANIM_DURATION))
                    .with(animateCloudIn(WELCOME_ANIM_DURATION))
                    .before(animateToggleViewVisibility(mRequestPermissionsButton, true));
            animatorSet.start();
        } else {
            mCloudImageView.setX(0);
            mCloudImageView.setVisibility(View.VISIBLE);
            mWelcomeText.setText(R.string.permission_required_text);
            mRequestPermissionsButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void clearState() {
        if (mAnimationsEnabled) {
            AnimatorSet outAnimatorSet = new AnimatorSet();
            outAnimatorSet.setDuration(EXIT_ANIM_DURATION);

            AnimatorSet.Builder builder =
                    outAnimatorSet.play(animateToggleViewVisibility(mWelcomeText, false));
            builder.with(animateToggleViewVisibility(mCloudImageView, false));

            if (mRequestPermissionsButton.getVisibility() == View.VISIBLE) {
                builder.with(animateToggleViewVisibility(mRequestPermissionsButton, false));
            }
            if (mLookAroundButton.getVisibility() == View.VISIBLE) {
                builder.with(animateToggleViewVisibility(mLookAroundButton, false));
            }
            outAnimatorSet.start();
        } else {
            mRequestPermissionsButton.setVisibility(View.INVISIBLE);
            mLookAroundButton.setVisibility(View.INVISIBLE);
            mCloudImageView.setX(OFFSCREEN_OFFSET_X);
            mCloudImageView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.not_a_title);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @SuppressLint("SetTextI18n")
    public Animator animateText(@StringRes int textToAnimate, int duration) {
        final char[] lettersArray = getResources().getString(textToAnimate).toCharArray();

        ValueAnimator textAnimator = ValueAnimator.ofInt(0);
        textAnimator.setDuration(duration);
        textAnimator.addListener(new Animator.AnimatorListener() {
            Disposable d = null;

            @Override
            public void onAnimationStart(Animator animation) {
                mWelcomeText.setText("");
                long pauseBetweenLetters = textAnimator.getDuration()/ lettersArray.length;
                d = Observable.interval(pauseBetweenLetters, TimeUnit.MILLISECONDS)
                        .map(i -> lettersArray[i.intValue()])
                        .take(lettersArray.length)
                        .concatMap(Observable::just)
                        .map(character -> Character.toString(character))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> mWelcomeText.setText(mWelcomeText.getText() + s));
                mCompositeDisposable.add(d);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (d != null) d.dispose();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (d != null) d.dispose();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        return textAnimator;
    }

    private Animator animateToggleViewVisibility(View v, boolean visible) {
        return ObjectAnimator.ofArgb(v, "visibility", visible ? 1 : 0);
    }

    private Animator animateCloudIn(int duration) {
        AnimatorSet animatorSet = new AnimatorSet();

        final float WAVE_AMPLITUDE = 192f;

        //Set initial offset behind the screen
        mCloudImageView.setX(OFFSCREEN_OFFSET_X);

        ObjectAnimator cloudAnimVisibility =
                ObjectAnimator.ofInt(mCloudImageView, "visibility", 0, 1);

        //Animate cloud movement
        ValueAnimator cloudMoveAnim = ValueAnimator.ofInt(0, 1).setDuration(duration);

        cloudMoveAnim.addUpdateListener(animation -> {
            int translX = (int) (OFFSCREEN_OFFSET_X * (1 - animation.getAnimatedFraction()));
            int translY = (int) (WAVE_AMPLITUDE * Math.sin(animation.getAnimatedFraction() * Math.PI * 2));

            if (mCloudImageView != null) {
                mCloudImageView.setTranslationX(translX);
                mCloudImageView.setTranslationY(translY);
            }
        });
        animatorSet.play(cloudMoveAnim).after(cloudAnimVisibility);
        return animatorSet;
    }

    private Animator animateCloudOut(int duration) {
        AnimatorSet animatorSet = new AnimatorSet();
        final int OFFSCREEN_OFFSET_X = -1 * Utils.getDisplaySize(
                getActivity().getApplicationContext()).x;
        ObjectAnimator cloudAnimMoveOut = ObjectAnimator.ofFloat(
                mCloudImageView, "x", 0, OFFSCREEN_OFFSET_X)
                .setDuration(duration);
        ObjectAnimator cloudAnimVisibility = ObjectAnimator.ofArgb(
                mCloudImageView, "visibility", 1, 0);
        animatorSet.play(cloudAnimVisibility).after(cloudAnimMoveOut);
        return animatorSet;
    }
}
