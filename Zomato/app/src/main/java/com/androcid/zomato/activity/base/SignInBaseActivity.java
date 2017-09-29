package com.androcid.zomato.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.androcid.zomato.BuildConfig;
import com.androcid.zomato.R;
import com.androcid.zomato.activity.HomeActivity;
import com.androcid.zomato.model.User;
import com.androcid.zomato.preference.SessionPreference;
import com.androcid.zomato.retro.RetroInterface;
import com.androcid.zomato.retro.UserResponse;
import com.androcid.zomato.util.CommonFunctions;
import com.androcid.zomato.util.FireBase;
import com.androcid.zomato.util.MyLg;
import com.androcid.zomato.util.Toas;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.androcid.zomato.util.Constant.USER_INITIATED_REQUEST;

/**
 * To handle activity Transitions
 */

public class SignInBaseActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = SignInBaseActivity.class.getSimpleName();
    private Context context = SignInBaseActivity.this;

    //For Google Login
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    //For Google Login End

    CallbackManager callbackManager;

    boolean hasRegistered;
    private int signInType;
    public static final int SOCIAL = 1;
    public static final int LOGIN = 2;
    public static final int REGISTER = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        // App code
                        MyLg.e(TAG, "Get Facebook User Details");
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        MyLg.e(TAG, "Get Facebook onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        MyLg.e(TAG, "Get Facebook onError ");
                        exception.printStackTrace();
                    }
                });


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    MyLg.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    MyLg.d(TAG, "onAuthStateChanged:signed_out");
                }

                onGotFirebaseUser(user);
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with FireBase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        MyLg.d(TAG, "firebaseAuthWithGoogle:" + acct.getId()+" "+acct.getPhotoUrl());
        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        MyLg.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            MyLg.w(TAG, "signInWithCredential");
                            Toas.show(context, "Authentication failed.");
                        }
                        hideProgressDialog();
                    }
                });
    }

    public void googleLoginClick() {
        signInType = SOCIAL;
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        MyLg.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    private void onGotFirebaseUser(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {

            String uuid = user.getUid();
            String email = CommonFunctions.checkNull(user.getEmail());
            String name = CommonFunctions.checkNull(user.getDisplayName());
            String image = CommonFunctions.checkNull(user.getPhotoUrl());

            if (email.equals("")) {
                //Ask user to Enter Email


            } else {

            }

            if (!name.equals("")) { //Name Yet Not Updated Wait

                MyLg.e(TAG, "Uid " + uuid);
                MyLg.e(TAG, "Email " + email);
                MyLg.e(TAG, "Name " + name);
                MyLg.e(TAG, "Image " + image);

                User appUser = new User(
                        0,
                        uuid,
                        name,
                        email,
                        image
                );

                if (signInType == SOCIAL) {
                    goSocialLogin(appUser);
                } else if (signInType == LOGIN) {
                    appUser.setPassword(password);
                    goNormalLogin(appUser);
                } else if (signInType == REGISTER) {
                    appUser.setPassword(password);
                    goNormalRegister(appUser);
                }else{
                    FireBase.getInstance().signOut();
                }


            }

        }
    }

    private void goSocialLogin(User user) {

        if (BuildConfig.NETWORK_TEST && Build.VERSION.SDK_INT >= 14) {
            try {
                TrafficStats.setThreadStatsTag(USER_INITIATED_REQUEST);
                // Once tag has been applied, network request has to be made request
            } finally {
                TrafficStats.clearThreadStatsTag();
            }
        }


        RetroInterface.getZomatoRestApi().loginSocial(
                CommonFunctions.getHashMap(user),
                new Callback<UserResponse>() {
                    @Override
                    public void success(UserResponse userResponse, Response response) {

                        if (userResponse != null && userResponse.isSuccess()) {

                            if (userResponse.getUser() != null) {
                                loginSuccess(userResponse.getUser());
                            }
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );

    }

    private void goNormalLogin(User user) {

        if (BuildConfig.NETWORK_TEST && Build.VERSION.SDK_INT >= 14) {
            try {
                TrafficStats.setThreadStatsTag(USER_INITIATED_REQUEST);
                // Once tag has been applied, network request has to be made request
            } finally {
                TrafficStats.clearThreadStatsTag();
            }
        }

        RetroInterface.getZomatoRestApi().loginNormal(
                CommonFunctions.getHashMap(user),
                new Callback<UserResponse>() {
                    @Override
                    public void success(UserResponse userResponse, Response response) {

                        if (userResponse != null && userResponse.isSuccess()) {

                            if (userResponse.getUser() != null) {
                                loginSuccess(userResponse.getUser());
                            }
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );
    }

    private void goNormalRegister(User user) {

        if (BuildConfig.NETWORK_TEST && Build.VERSION.SDK_INT >= 14) {
            try {
                TrafficStats.setThreadStatsTag(USER_INITIATED_REQUEST);
                // Once tag has been applied, network request has to be made request
            } finally {
                TrafficStats.clearThreadStatsTag();
            }
        }

        RetroInterface.getZomatoRestApi().registerNormal(
                CommonFunctions.getHashMap(user),
                new Callback<UserResponse>() {
                    @Override
                    public void success(UserResponse userResponse, Response response) {

                        if (userResponse != null && userResponse.isSuccess()) {

                            if (userResponse.getUser() != null) {
                                loginSuccess(userResponse.getUser());
                            }
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                }
        );

    }

    private void loginSuccess(User user) {
        SessionPreference.setUserParams(context, user);
        startActivity(HomeActivity.getCallIntent(context));
        finish();
    }

    public void facebookLoginClick() {
        signInType = SOCIAL;
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
    }

    private void handleFacebookAccessToken(final AccessToken token) {
        MyLg.d("KOI", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        MyLg.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                    }
                });
    }


    private String password;

    public void registerClick(final String name, final String email, final String password) {

        signInType = REGISTER;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        MyLg.d("koi", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {

                            MyLg.e(TAG, "Set Name " + name);

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        hasRegistered = true;
                                        FirebaseAuth.getInstance().signOut();
                                        loginClick(email, password);

                                    }
                                }
                            });


                        } else {
                            try {
                                task.getException().printStackTrace();
                                MyLg.e(TAG, task.getException().getMessage());

                                String message = task.getException().getMessage();
                                new AlertDialog.Builder(context).setTitle(message).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

    }


    public void loginClick(String email, String password) {

        this.password = password;
        if (!hasRegistered) {
            MyLg.e(TAG , "Type Login");
            signInType = LOGIN;
        }else{
            MyLg.e(TAG , "Type Register");
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        MyLg.d("koi", "signInWithEmailAndPassword:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            try {
                                task.getException().printStackTrace();
                                MyLg.e(TAG, task.getException().getMessage());

                                String message = task.getException().getMessage();
                                new AlertDialog.Builder(context).setTitle(message).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

    }

    private void hideProgressDialog() {

    }

    private void showProgressDialog() {

    }

}
