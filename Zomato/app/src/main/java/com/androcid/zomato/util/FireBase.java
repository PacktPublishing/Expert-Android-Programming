package com.androcid.zomato.util;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Androcid on 16-01-2017.
 */

public class FireBase {


    private static final String TAG = FireBase.class.getSimpleName();
    private static FireBase sFirebase = null;
    public final FirebaseAuth auth;
    private final FirebaseApp app;
    // public final DatabaseReference database;

    private FireBase() {
        app = FirebaseApp.getInstance();
        auth = FirebaseAuth.getInstance(app);

        /*
        FirebaseDatabase fdb = FirebaseDatabase.getInstance(app);
        fdb.setPersistenceEnabled(true);
        database = fdb.getReference();
        */

    }

    public static FireBase getInstance() {
        if (sFirebase == null)
            sFirebase = new FireBase();
        return sFirebase;
    }

    public FirebaseApp getApp() {
        return app;
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public boolean isSignedIn() {
        return auth.getCurrentUser() != null;
    }

    public void signOut() {
        auth.signOut();
    }

    public String getUserId() {
        return getCurrentUser().getUid();
    }

}