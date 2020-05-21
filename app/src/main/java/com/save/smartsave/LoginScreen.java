package com.save.smartsave;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class LoginScreen extends AppCompatActivity {
    public static String userName;
    public static String userEmail;
    public static String userPhoto;
    public static FirebaseUser currentUser;

    static final int GOOGLE_SIGN_IN = 123;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    Button googleSignIn;
    CallbackManager mCallBackManager;
    Button facebookLoginButton;
    public static Boolean fillUserDetail;

    public static String currentUserUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);


        Transition explode =new Explode();
        explode.setDuration(1000);
        explode.setInterpolator(new FastOutSlowInInterpolator());
        getWindow().setEnterTransition(explode);

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        fillUserDetail =true;

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        if (mAuth.getCurrentUser() != null) {
            fillUserDetail =false;
            currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
        }



        FacebookSdk.sdkInitialize(getApplicationContext());

        facebookLoginButton=findViewById(R.id.fb_login_button);
        mCallBackManager = CallbackManager.Factory.create();

        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginScreen.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallBackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
            }
        });

        googleSignIn=findViewById(R.id.google_sign_in_button);

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInGoogle();

            }
        });


    }



    private void handleFacebookAccessToken (AccessToken token){

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in currentUser's information
                            Log.d("hey", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the currentUser.
                            Log.w("hey", "signInWithCredential:failure", task.getException());

                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    public void SignInGoogle () {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }
    private void firebaseAuthWithGoogle (GoogleSignInAccount acct){

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in currentUser's information
                            Log.d("hello", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the currentUser.
                            Log.w("hello", "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                    }
                });
    }
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        mCallBackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }
    private void updateUI (FirebaseUser user){
        if (user != null) {
            userName = user.getDisplayName();
            userEmail = user.getEmail();
            userPhoto = String.valueOf(user.getPhotoUrl());
            currentUser=user;
            currentUserUid=currentUser.getUid();

            if (fillUserDetail == true) {
                startActivity(new Intent(LoginScreen.this, UserDetails.class));
                finish();
            } else {
                Intent intent = new Intent(LoginScreen.this, ML_Process.class);
                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(LoginScreen.this).toBundle());
                finish();
            }

        }
    }

    }



