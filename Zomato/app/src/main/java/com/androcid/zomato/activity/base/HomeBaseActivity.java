package com.androcid.zomato.activity.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androcid.zomato.R;
import com.androcid.zomato.activity.AccountActivity;
import com.androcid.zomato.activity.collection.CollectionActivity;
import com.androcid.zomato.activity.FeedActivity;
import com.androcid.zomato.activity.collection.AddCollectionActivity;
import com.androcid.zomato.activity.photos.PickPhotosActivity;
import com.androcid.zomato.activity.review.AddReviewActivity;
import com.androcid.zomato.util.Constant;

import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by Androcid on 27-12-2016.
 */

public class HomeBaseActivity extends AppCompatActivity {

    //Init Bottom View
    public static final int SCREEN_NONE = 100;
    public static final int SCREEN_HOME = 101;
    public static final int SCREEN_COLLECTION = 102;
    public static final int SCREEN_FEED = 103;
    public static final int SCREEN_ACCOUNT = 104;
    private static final String TAG = HomeBaseActivity.class.getSimpleName();
    int screenType = 0;
    //Home
    LinearLayout bottom_menu_home;
    //Collection
    LinearLayout bottom_menu_collection;
    //Feed
    LinearLayout bottom_menu_feed;
    //Account
    LinearLayout bottom_menu_account;
    //Center Button
    boolean menuShown = false;
    LinearLayout centerButtonLay;
    ImageView centerButton;
    LinearLayout revealLay;
    private Context context = HomeBaseActivity.this;

    public void initialiseBottomView(final int screenType) {
        this.screenType = screenType;

        //FOR CLICK
        bottom_menu_home = (LinearLayout) findViewById(R.id.bottom_menu_home);
        bottom_menu_collection = (LinearLayout) findViewById(R.id.bottom_menu_collection);
        bottom_menu_feed = (LinearLayout) findViewById(R.id.bottom_menu_feed);
        bottom_menu_account = (LinearLayout) findViewById(R.id.bottom_menu_account);

        //For Selected

        setSelected(screenType);

        bottom_menu_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (menuShown) {
                    exitReveal(revealLay, SCREEN_HOME);
                } else {
                    openActivity(SCREEN_HOME);
                }

            }
        });

        bottom_menu_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (menuShown) {
                    exitReveal(revealLay, SCREEN_COLLECTION);
                } else {
                    openActivity(SCREEN_COLLECTION);
                }

            }
        });

        bottom_menu_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (menuShown) {
                    exitReveal(revealLay, SCREEN_FEED);
                } else {
                    openActivity(SCREEN_FEED);
                }

            }
        });

        bottom_menu_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuShown) {
                    exitReveal(revealLay, SCREEN_ACCOUNT);
                } else {
                    openActivity(SCREEN_ACCOUNT);
                }
            }
        });


        centerButtonLay = (LinearLayout) findViewById(R.id.centerButtonLay);
        centerButton = (ImageView) findViewById(R.id.centerButton);
        revealLay = (LinearLayout) findViewById(R.id.revealLay);
        revealLay.setVisibility(View.INVISIBLE);

        revealLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuShown) {
                    exitReveal(revealLay);
                }
            }
        });

        centerButtonLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!menuShown) {
                    enterReveal(revealLay);
                } else {
                    exitReveal(revealLay);
                }
            }
        });

    }

    private void openActivity(int screenOpen) {
        setSelected(screenOpen);

        boolean canFinish = false;

        switch (screenOpen) {
            case SCREEN_HOME:
                canFinish = true;
                break;
            case SCREEN_COLLECTION:
                if (screenType != SCREEN_COLLECTION) {
                    startActivity(CollectionActivity.getCallIntent(context));
                    canFinish = true;
                }
                break;
            case SCREEN_FEED:
                if (screenType != SCREEN_FEED) {
                    startActivity(FeedActivity.getCallIntent(context));
                    canFinish = true;
                }
                break;
            case SCREEN_ACCOUNT:
                if (screenType != SCREEN_ACCOUNT) {
                    startActivity(AccountActivity.getCallIntent(context));
                    canFinish = true;
                }
                break;
        }

        if (canFinish && screenType != SCREEN_HOME) {
            finish();
        }
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {

        if (menuShown) {
            exitReveal(revealLay);
        } else {
            if (screenType != SCREEN_HOME) {
                openActivity(SCREEN_HOME);
            } else {
                finish();
            }
        }

    }

    /**
     * Select the current Tab
     *
     * @param screenType
     */
    private void setSelected(int screenType) {

        removeSelected();

        switch (screenType) {
            case SCREEN_HOME:
                bottom_menu_home.setSelected(true);
                break;
            case SCREEN_COLLECTION:
                bottom_menu_collection.setSelected(true);
                break;
            case SCREEN_FEED:
                bottom_menu_feed.setSelected(true);
                break;
            case SCREEN_ACCOUNT:
                bottom_menu_account.setSelected(true);
                break;
        }

    }

    /**
     * Unselect all tabs
     */
    private void removeSelected() {
        bottom_menu_home.setSelected(false);
        bottom_menu_collection.setSelected(false);
        bottom_menu_feed.setSelected(false);
        bottom_menu_account.setSelected(false);
    }

    private void exitReveal(View myView) {
        exitReveal(myView, screenType);
    }

    private void exitReveal(final View myView, final int screenType) {
        menuShown = false;
        centerButton.animate().rotation(0).setInterpolator(new AccelerateInterpolator()).setDuration(50);

        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight();

        // get the initial radius for the clipping circle
        int initialRadius = Math.max(myView.getWidth(), myView.getHeight());

        // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                revealLay.setVisibility(View.INVISIBLE);
                openActivity(screenType);
            }
        });

        anim.setDuration(Constant.REVEAL_DURATION);
        // start the animation
        anim.start();

        setSelected(screenType);
    }

    private void enterReveal(View myView) {

        menuShown = true;
        centerButton.animate().rotation(45).setInterpolator(new AccelerateInterpolator()).setDuration(50);

        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight();

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        myView.setVisibility(View.VISIBLE);
        anim.setDuration(Constant.REVEAL_DURATION);
        anim.start();

        removeSelected();
    }


    public void addPhoto(View view) {
        startActivity(PickPhotosActivity.getCallIntent(context,PickPhotosActivity.TYPE_NORMAL));
        exitReveal(revealLay);
    }

    public void addReview(View view) {
        startActivity(AddReviewActivity.getCallIntent(context));
        exitReveal(revealLay);
    }

    public void createCollection(View view) {
        startActivity(AddCollectionActivity.getCallIntent(context));
        exitReveal(revealLay);
    }
}
