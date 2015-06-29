package com.autodesk.tct;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.autodesk.tct.server.ServerUtil;
import com.autodesk.tct.server.ServerUtil.SignHandler;

public class LoginActivity extends AppCompatActivity implements SignHandler{

	private boolean mSigninPage = true;
	
	private EditText mEmailEditText;
	private EditText mPasswordEditText;
	private ImageView mPasswordIndicator;
	private View mConformPwdGroup;
	private EditText mConformPwdEditText;
	private ImageView mConformPwdIndicator;
	private Button mSignButton;
	private TextView mSigninSignupSwitch;
	private ProgressBar mProgressBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_login);
    	
    	mEmailEditText = (EditText)findViewById(R.id.edit_email);
    	mEmailEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					verifyEmailAddress();
				}
			}
        	
        });
    	
        mPasswordEditText = (EditText)findViewById(R.id.edit_password);
        mPasswordEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					conformPassword();
				}
			}
        	
        });
        mPasswordIndicator = (ImageView)findViewById(R.id.edit_password_indicator);
        
        mConformPwdGroup = findViewById(R.id.confirm_password_group);
        mConformPwdEditText = (EditText)findViewById(R.id.confirm_password);
        mConformPwdEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					conformPassword();
				}
			}
        	
        });
        mConformPwdIndicator = (ImageView)findViewById(R.id.confirm_password_indicator);
        
        mSignButton = (Button)findViewById(R.id.btn_login);
        mSignButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!verifyForm()) {
					Toast.makeText(LoginActivity.this, R.string.login_error_input, Toast.LENGTH_SHORT).show();
					return;
				}
				
				mProgressBar.setVisibility(View.VISIBLE);
				
				String email = mEmailEditText.getText().toString();
				String password = mPasswordEditText.getText().toString();
				if (mSigninPage) {
					// sign in
					ServerUtil.signIn(email, password);
				} else {
					// sign up
					ServerUtil.signUp(email, password);
				}
			}
        	
        });
        mSigninSignupSwitch = (TextView)findViewById(R.id.signup_signin_switch);
        mSigninSignupSwitch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mSigninPage = !mSigninPage;
				updateSignInSignUpPage(mSigninPage);
			}
        	
        });
        
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
             
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); 
        setSupportActionBar(toolbar); 
        
		updateSignInSignUpPage(mSigninPage);
		
		ServerUtil.setSignHandler(this);
    }
    
    private boolean verifyForm() {
    	if(mSigninPage) {
    		return verifyEmailAddress();
    	} else {
    		return verifyEmailAddress() && conformPassword();
    	}
    }
    
    private boolean verifyEmailAddress() {
		String emailAddress = mEmailEditText.getText().toString();
		if(emailAddress == null || !emailAddress.endsWith("@autodesk.com")) {
			String errorMsg = getResources().getString(R.string.invalid_email_address);
			mEmailEditText.setError(errorMsg);;
			mConformPwdEditText.setBackgroundResource(R.drawable.register_edit_text_bg_error);
			return false;
		} else {
			mEmailEditText.setError(null);
			mConformPwdEditText.setBackgroundResource(R.drawable.register_edit_text_bg_normal);
			return true;
		}
    }
    
    private boolean conformPassword() {
    	mPasswordIndicator.setVisibility(View.VISIBLE);
		mConformPwdIndicator.setVisibility(View.VISIBLE);
		String pwdStr = mPasswordEditText.getText().toString();
		String conformPWDStr = mConformPwdEditText.getText().toString();
		if(pwdStr != null && pwdStr.equals(conformPWDStr)) {
			mConformPwdIndicator.setImageResource(R.drawable.pswd_right);
			mConformPwdEditText.setBackgroundResource(R.drawable.register_edit_text_bg_normal);
			return true;
		} else {
			mConformPwdIndicator.setImageResource(R.drawable.pswd_wrong);
			mConformPwdEditText.setBackgroundResource(R.drawable.register_edit_text_bg_error);		
			return false;
		}
    }
    
    private void updateSignInSignUpPage(boolean signInPage) {
    	if(signInPage) {
			// switch to sign in page
			mConformPwdGroup.setVisibility(View.GONE);
			mSignButton.setText(R.string.button_sign_in);
			mSigninSignupSwitch.setText(R.string.switch_to_sign_up);
    		getSupportActionBar().setTitle(R.string.action_bar_title_signin);
    	} else {
			// switch to sign up page
			mConformPwdGroup.setVisibility(View.VISIBLE);
			mSignButton.setText(R.string.button_sign_up);
			mSigninSignupSwitch.setText(R.string.switch_to_sign_in);
    		getSupportActionBar().setTitle(R.string.actionbar_title_signup);
    	}
    }

	@Override
	public void onSignSucceed(boolean userTrigger) {
		if(userTrigger) {
			mProgressBar.setVisibility(View.INVISIBLE);
		}
		finish();
	}

	@Override
	public void onSignFailed() {
		mProgressBar.setVisibility(View.INVISIBLE);
		Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
	}
}
