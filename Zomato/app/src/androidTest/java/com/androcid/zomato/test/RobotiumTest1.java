package com.androcid.zomato.test;

import android.test.ActivityInstrumentationTestCase2;

import com.androcid.zomato.activity.SplashActivity;
import com.robotium.solo.Solo;
import com.robotium.solo.Timeout;


public class RobotiumTest1 extends ActivityInstrumentationTestCase2<SplashActivity> {
  	private Solo solo;
  	
  	public RobotiumTest1() {
		super(SplashActivity.class);
  	}

  	public void setUp() throws Exception {
        super.setUp();
		solo = new Solo(getInstrumentation());
		getActivity();
  	}
  
   	@Override
   	public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
  	}
  
	public void testRun() {
        //Wait for activity: 'com.androcid.zomato.activity.SplashActivity'
		solo.waitForActivity(com.androcid.zomato.activity.SplashActivity.class, 2000);
        //Wait for activity: 'com.androcid.zomato.activity.login.StartActivity'
		assertTrue("com.androcid.zomato.activity.login.StartActivity is not found!", solo.waitForActivity(com.androcid.zomato.activity.login.StartActivity.class));
        //Click on SKIP
		solo.clickOnText(java.util.regex.Pattern.quote("SKIP"));
        //Wait for activity: 'com.androcid.zomato.activity.HomeActivity'
		assertTrue("com.androcid.zomato.activity.HomeActivity is not found!", solo.waitForActivity(com.androcid.zomato.activity.HomeActivity.class));
        //Click on NEARBY St Inez, Panaji, Goa
		solo.clickOnText(java.util.regex.Pattern.quote("NEARBY"));
        //Wait for activity: 'com.androcid.zomato.activity.SelectLocationActivity'
		assertTrue("com.androcid.zomato.activity.SelectLocationActivity is not found!", solo.waitForActivity(com.androcid.zomato.activity.SelectLocationActivity.class));
        //Click on Empty Text View
		solo.clickOnView(solo.getView(com.androcid.zomato.R.id.searchText));
        //Enter the text: 'pa'
		solo.clearEditText((android.widget.EditText) solo.getView(com.androcid.zomato.R.id.searchText));
		solo.enterText((android.widget.EditText) solo.getView(com.androcid.zomato.R.id.searchText), "pa");
		//Click on Collection
		solo.clickOnView(solo.getView(com.androcid.zomato.view.custom.TintableImageView.class, 1));
		Timeout.setSmallTimeout(5000);
        //Click on Collection
		solo.clickOnView(solo.getView(com.androcid.zomato.R.id.bottom_menu_collection));
        //Wait for activity: 'com.androcid.zomato.activity.collection.CollectionActivity'
		assertTrue("com.androcid.zomato.activity.collection.CollectionActivity is not found!", solo.waitForActivity(com.androcid.zomato.activity.collection.CollectionActivity.class));
        //Set default small timeout to 14750 milliseconds
		Timeout.setSmallTimeout(14750);
        //Click on Feed
		solo.clickOnView(solo.getView(com.androcid.zomato.R.id.bottom_menu_feed, 1));
        //Wait for activity: 'com.androcid.zomato.activity.FeedActivity'
		assertTrue("com.androcid.zomato.activity.FeedActivity is not found!", solo.waitForActivity(com.androcid.zomato.activity.FeedActivity.class));
        //Click on Account
		solo.clickOnView(solo.getView(com.androcid.zomato.R.id.bottom_menu_account, 1));
        //Wait for activity: 'com.androcid.zomato.activity.AccountActivity'
		assertTrue("com.androcid.zomato.activity.AccountActivity is not found!", solo.waitForActivity(com.androcid.zomato.activity.AccountActivity.class));
        //Click on Home
		solo.clickOnView(solo.getView(com.androcid.zomato.R.id.bottom_menu_home, 1));
        //Click on RelativeLayout Barbeque Nation Panaji, Goa Best dine out experience ever!
		solo.clickInRecyclerView(0, 0);
        //Wait for activity: 'com.androcid.zomato.activity.PlaceDetailActivity'
		assertTrue("com.androcid.zomato.activity.PlaceDetailActivity is not found!", solo.waitForActivity(com.androcid.zomato.activity.PlaceDetailActivity.class));
	}
}
