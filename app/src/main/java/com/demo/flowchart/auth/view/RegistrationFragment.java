package com.demo.flowchart.auth.view;

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
import com.demo.flowchart.auth.result.VerificationPasswordError;
import com.demo.flowchart.navigation.Navigator;
import com.demo.flowchart.R;
import com.demo.flowchart.auth.result.AuthResult;
import com.demo.flowchart.auth.viewmodel.RegistrationViewModel;
import com.demo.flowchart.profile.ProfileFragment;


public class RegistrationFragment extends Fragment {

    private Navigator navigator;

    private RegistrationViewModel registrationViewModel;

    private EditText email;
    private EditText firstPassword;
    private EditText secondPassword;
    private Button register;

    public RegistrationFragment() {
    }

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        navigator = (Navigator) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navigator.setUpNavBar(true);

        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);

        email = view.findViewById(R.id.editText_emailRegister);
        firstPassword = view.findViewById(R.id.editText_firstPasswordRegister);
        secondPassword = view.findViewById(R.id.editText_secondPasswordRegister);
        register = view.findViewById(R.id.btn_register);

        registrationViewModel.resultLiveData.observe(
                this.getViewLifecycleOwner(), this::onRegistrationResult
        );

        register.setOnClickListener(v -> registrationViewModel.signUp(
                email.getText().toString(),
                firstPassword.getText().toString(),
                secondPassword.getText().toString()
        ));
    }

    private void onRegistrationResult(AuthResult authResult) {
        if (authResult instanceof EmailError) {
            email.setError(((EmailError) authResult).message);
        } else if (authResult instanceof PasswordError) {
            firstPassword.setError(((PasswordError) authResult).message);
        } else if (authResult instanceof VerificationPasswordError) {
            secondPassword.setError(((VerificationPasswordError) authResult).message);
        } else if (authResult instanceof AuthError) {
            Toast.makeText(this.getContext(), ((AuthError) authResult).message, Toast.LENGTH_LONG).show();
        } else if (authResult instanceof AuthSuccess) {
            navigator.navigateTo(ProfileFragment.newInstance());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        email = null;
        firstPassword = null;
        secondPassword = null;
        register = null;
    }
}