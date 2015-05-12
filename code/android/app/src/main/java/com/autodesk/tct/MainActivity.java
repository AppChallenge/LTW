package com.autodesk.tct;

import android.app.Activity;
import android.os.Bundle;

import com.autodesk.tct.com.autodesk.tct.authentication.AuthenticationUtil;
import com.autodesk.tct.com.autodesk.tct.authentication.LoginFragment;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!AuthenticationUtil.isSignedIn()) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new LoginFragment())
                    .commit();
        }
    }
}
