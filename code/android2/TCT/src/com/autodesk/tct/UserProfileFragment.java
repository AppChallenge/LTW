package com.autodesk.tct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.autodesk.tct.authentication.User;
import com.autodesk.tct.authentication.UserUtility;

public class UserProfileFragment extends BaseFragment {

    private static final String TAG = "UserProfile";

    private User mUser;

    public UserProfileFragment(User user) {
        mUser = user;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_userprofile, container, false);

        TextView avatarView = (TextView) rootView.findViewById(R.id.avatar_view);
        avatarView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.avatar_5_large, 0, 0);
        avatarView.setText(mUser.getName());

        TextView followView = (TextView) rootView.findViewById(R.id.follow_view);
        followView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Not Implemented!", Toast.LENGTH_SHORT).show();
            }

        });
        if (UserUtility.isCurrentUser(mUser)) {
            followView.setVisibility(View.GONE);
        }

        TextView emailView = (TextView) rootView.findViewById(R.id.email_view);
        emailView.setText(mUser.getEmail());
        TextView titleView = (TextView) rootView.findViewById(R.id.title_view);
        titleView.setText("Software Developer");

        return rootView;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// this is really important in order to save the state across screen
		// configuration changes for example
        setRetainInstance(true);
    }

    @Override
    public void onFragmentResume() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.action_bar_title_userprofile);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (UserUtility.isCurrentUser(mUser)) {
            inflater.inflate(R.menu.userprofile, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.menu_signout:
                signout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signout() {
        UserUtility.signout();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }
}
