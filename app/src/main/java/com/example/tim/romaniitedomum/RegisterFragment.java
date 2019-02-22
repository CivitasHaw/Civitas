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

public class RegisterFragment extends Fragment {

    private static final String TAG = "RegisterFragment";
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
