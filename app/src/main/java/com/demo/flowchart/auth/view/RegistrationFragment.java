package com.demo.flowchart.auth.view;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.demo.flowchart.EditorFragment;
import com.demo.flowchart.Navigator;
import com.demo.flowchart.R;
import com.demo.flowchart.auth.util.AuthResult;
import com.demo.flowchart.auth.viewmodel.RegistrationViewModel;


public class RegistrationFragment extends Fragment {

    private EditText email;
    private EditText firstPassword;
    private EditText secondPassword;
    private Button btnRegister;
    private SharedPreferences sp;

    private RegistrationViewModel registrationViewModel;

    public RegistrationFragment() {
    }

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email = view.findViewById(R.id.editText_emailRegister);
        firstPassword = view.findViewById(R.id.editText_firstPasswordRegister);
        secondPassword = view.findViewById(R.id.editText_secondPasswordRegister);
        btnRegister = view.findViewById(R.id.btn_register);
        registrationViewModel = new RegistrationViewModel();

        btnRegister.setOnClickListener(v -> registrationViewModel.signUp(email.getText().toString(),
                firstPassword.getText().toString(), secondPassword.getText().toString()));
        registrationViewModel.result.observe(this.getViewLifecycleOwner(), this::onRegistrationResult);
    }

    private void onRegistrationResult(AuthResult authResult) {
        if (authResult instanceof AuthResult.EmailError) {
            email.setError("Неверный логин");
        } else if (authResult instanceof AuthResult.EmailNotValid) {
            email.setError("Неверный формат логина");
        }
        if (authResult instanceof AuthResult.PasswordError) {
            firstPassword.setError("Неверный пароль");
        } else if (authResult instanceof AuthResult.PasswordIsShorterThanSixCharacters) {
            firstPassword.setError("Пароль должен быть не менее шести символов");
        } else if (authResult instanceof AuthResult.PasswordsAreNotEqual) {
            secondPassword.setError("Пароли не совпадают");
        }else if(authResult instanceof AuthResult.AuthSuccess){
            Navigator navigator = (Navigator) this.getActivity();
            if (navigator != null) {
                navigator.navigateTo(EditorFragment.newInstance());
            }
        }

    }
}