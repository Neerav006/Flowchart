package com.demo.flowchart.screens;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.flowchart.auth.result.AuthError;
import com.demo.flowchart.auth.result.AuthSuccess;
import com.demo.flowchart.auth.result.EmailError;
import com.demo.flowchart.auth.result.PasswordError;
import com.demo.flowchart.viewmodels.LoginViewModel;
import com.demo.flowchart.navigation.Navigator;
import com.demo.flowchart.R;
import com.demo.flowchart.auth.result.AuthResult;


public class LoginFragment extends Fragment {

    private Navigator navigator;

    private LoginViewModel loginViewModel;

    private EditText email;
    private EditText password;
    private Button login;
    private Button register;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        navigator = (Navigator) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navigator.setUpNavBar(true);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        email = view.findViewById(R.id.et_login_email);
        password = view.findViewById(R.id.et_login_password);
        login = view.findViewById(R.id.btn_login);
        register = view.findViewById(R.id.btn_login_register);

        loginViewModel.resultLiveData.observe(this.getViewLifecycleOwner(), this::onLoginResult);

        login.setOnClickListener(v ->
                loginViewModel.signIn(email.getText().toString(), password.getText().toString())
        );

        register.setOnClickListener(v -> {
            Navigator navigator = (Navigator) LoginFragment.this.getActivity();
            if (navigator != null) {
                navigator.navigateTo(RegistrationFragment.newInstance());
            }
        });
    }

    private void onLoginResult(AuthResult authResult) {
        if (authResult instanceof EmailError) {
            email.setError(((EmailError) authResult).message);
        }
        else if (authResult instanceof PasswordError) {
            password.setError(((PasswordError) authResult).message);
        }
        else if (authResult instanceof AuthError) {
            Toast.makeText(this.getContext(), ((AuthError) authResult).message, Toast.LENGTH_LONG).show();
        }
        else if (authResult instanceof AuthSuccess) {
            navigator.navigateTo(ProfileFragment.newInstance());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        email = null;
        password = null;
        login = null;
        register = null;
    }
}