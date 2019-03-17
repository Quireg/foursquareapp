package ua.in.quireg.foursquareapp.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import io.reactivex.disposables.Disposable;
import ua.in.quireg.foursquareapp.R;
import ua.in.quireg.foursquareapp.common.Utils;
import ua.in.quireg.foursquareapp.mvp.presenters.WelcomeScreenPresenter;
import ua.in.quireg.foursquareapp.mvp.views.WelcomeView;

/**
 * Created by Arcturus Mengsk on 2/4/2018, 5:45 PM.
 * foursquareapp
 */

public class WelcomeFragment extends MvpFragment implements WelcomeView {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
            getActivity().getActionBar().setTitle(R.string.app_name);
        }

        View view = inflater.inflate(R.layout.fragment_welcome_screen, container, false);

        unbinder = ButterKnife.bind(this, view);

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
    public void animateEnterFragment(int duration) {
        animateCloudIn(duration).start();
    }

    @Override
    public void animateExitFragment(int duration) {
        animateCloudOut(duration).start();
        mWelcomeText.setText("");
        animateToggleViewVisibility(mRequestPermissionsButton, false);
        animateToggleViewVisibility(mLookAroundButton, false);
    }

    @Override
    public void setReadyState() {
        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.play(animateText(R.string.permission_ok_text, false))
                .before(animateToggleViewVisibility(mLookAroundButton, true));

        animatorSet.start();
    }

    @Override
    public void setPermissionsRequiredState() {

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.play(animateText(R.string.permission_required_text, false))
                .before(animateToggleViewVisibility(mRequestPermissionsButton, true));

        animatorSet.start();
    }

    public Animator animateText(int resId, boolean append) {

        if (!append) {
            mWelcomeText.setText("");
        }

        int pauseBetweenLetters = 50;

        final String[] lettersArray = getResources().getString(resId).split("");

        Observable<String> textEmitObservable = Observable
                .interval(pauseBetweenLetters, TimeUnit.MILLISECONDS)
                .map(i -> lettersArray[i.intValue()])
                .take(lettersArray.length)
                .concatMap(Observable::just)
                .observeOn(AndroidSchedulers.mainThread());

        ValueAnimator textAnimator = ValueAnimator.ofInt(0);

        textAnimator.setDuration(pauseBetweenLetters * lettersArray.length);

        Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {

            Disposable d = null;

            @Override
            public void onAnimationStart(Animator animation) {

                d = textEmitObservable.subscribe(
                        s -> mWelcomeText.setText(String.format("%s%s", mWelcomeText.getText(), s)),
                        t -> animation.end(),
                        animation::end
                );
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
        };

        textAnimator.addListener(animatorListener);

        return textAnimator;

    }

    private Animator animateToggleViewVisibility(View v, boolean visible) {
        return ObjectAnimator.ofArgb(v, "visibility", visible ? 1 : 0);
    }

    private Animator animateCloudIn(int duration) {

        AnimatorSet animatorSet = new AnimatorSet();

        final float WAVE_AMPLITUDE = 192f;

        final int OFFSCREEN_OFFSET_X = Utils.getDisplaySize(getActivity().getApplicationContext()).x * -1;

        //Set initial offset behind the screen
        mCloudImageView.setX(OFFSCREEN_OFFSET_X);

        ObjectAnimator cloudAnimVisibility = ObjectAnimator.ofArgb(mCloudImageView, "visibility", 0, 1);

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

        final int OFFSCREEN_OFFSET_X = -1 * Utils.getDisplaySize(getActivity().getApplicationContext()).x;

        ObjectAnimator cloudAnimMoveOut = ObjectAnimator.ofFloat(mCloudImageView, "x", 0, OFFSCREEN_OFFSET_X).setDuration(duration);

        ObjectAnimator cloudAnimVisibility = ObjectAnimator.ofArgb(mCloudImageView, "visibility", 1, 0);

        animatorSet.play(cloudAnimVisibility).after(cloudAnimMoveOut);

        return animatorSet;
    }

}
