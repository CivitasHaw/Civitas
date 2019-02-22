package com.example.tim.romaniitedomum;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

/**
 * Created by TimStaats 21.02.2019
 */

public class RegisterFragment extends Fragment {

    private static final String TAG = "RegisterFragment";

    private MainActivity mainActivity;

    private View mProgressView;
    private View mRegisterFormView;
    private TextView tvLoad;

    private EditText editRegisterName;
    private EditText editRegisterEmail;
    private EditText editRegisterPassword;
    private EditText editRegisterPasswordValidation;
    private Button btnSubmit;

    private String name, email, password, passwordValidation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        Log.d(TAG, "onCreateView: called");
        initRegister(view);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: submit clicked");
                name = editRegisterName.getText().toString().trim();
                email = editRegisterEmail.getText().toString().trim();
                password = editRegisterPassword.getText().toString().trim();
                passwordValidation = editRegisterPasswordValidation.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordValidation.isEmpty()){
                    Toast.makeText(getContext(), getResources().getText(R.string.toast_empty_fields), Toast.LENGTH_SHORT).show();
                } else {
                    if (password.equals(passwordValidation)){
                        Log.d(TAG, "onClick: registration successful");
                        BackendlessUser user = new BackendlessUser();
                        user.setEmail(email);
                        user.setPassword(password);
                        // for adding columns to backendless, use setProperty like below
                        user.setProperty("name", name);

                        showProgress(true);
                        tvLoad.setText(R.string.register_backendless_user);

                        // registering user to backendless
                        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                            @Override
                            public void handleResponse(BackendlessUser response) {

                                showProgress(false);
                                mainActivity.fragmentSwitch(new LoginFragment(), false, "");

                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(getContext(), getResources().getText(R.string.toast_backendless_error) + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                showProgress(false);
                            }
                        });

                    } else {
                        Toast.makeText(getContext(), getResources().getText(R.string.toast_invalid_password), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    private void initRegister(View view) {
        mRegisterFormView = view.findViewById(R.id.register_form);
        mProgressView = view.findViewById(R.id.register_progress);
        tvLoad = view.findViewById(R.id.tvLoad);

        editRegisterName = view.findViewById(R.id.edit_register_name);
        editRegisterEmail = view.findViewById(R.id.edit_register_email);
        editRegisterPassword = view.findViewById(R.id.edit_register_password);
        editRegisterPasswordValidation = view.findViewById(R.id.edit_register_password_again);
        btnSubmit = view.findViewById(R.id.button_register_submit);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity)getActivity();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
