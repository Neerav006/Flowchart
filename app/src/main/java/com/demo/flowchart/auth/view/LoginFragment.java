package com.demo.flowchart.auth.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.flowchart.EditorFragment;
import com.demo.flowchart.Navigator;
import com.demo.flowchart.R;
import com.demo.flowchart.auth.util.AuthResult;
import com.demo.flowchart.auth.viewmodel.LoginViewModel;


public class LoginFragment extends Fragment {

    private EditText email;
    private EditText password;
    private Button btnLogin;
    private Button btnRegister;

    private LoginViewModel loginViewModel;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email = view.findViewById(R.id.editText_email);
        password = view.findViewById(R.id.editText_password);
        btnLogin = view.findViewById(R.id.btn_login);
        btnRegister = view.findViewById(R.id.btn_showRegisterFragment);
        loginViewModel = new LoginViewModel();

        btnLogin.setOnClickListener(v -> loginViewModel.signIn(email.getText().toString(), password.getText().toString()));
        loginViewModel.result.observe(this.getViewLifecycleOwner(), this::onLoginResult);
        btnRegister.setOnClickListener(v -> {
            Navigator navigator = (Navigator) LoginFragment.this.getActivity();
            if (navigator != null) {
                navigator.navigateTo(RegistrationFragment.newInstance());
            }
        });
    }

    private void onLoginResult(AuthResult authResult) {
        if (authResult instanceof AuthResult.EmailError) {
            email.setError("Неверный логин");
        } else if (authResult instanceof AuthResult.PasswordError) {
            password.setError("Неверный пароль");
        } else if (authResult instanceof AuthResult.AuthError) {
            Toast.makeText(this.getContext(), "Неверный логин или пароль", Toast.LENGTH_LONG).show();
        } else if (authResult instanceof AuthResult.AuthSuccess) {
            Navigator navigator = (Navigator) this.getActivity();
            if (navigator != null) {
                navigator.navigateTo(EditorFragment.newInstance());
            }
        }
    }
}