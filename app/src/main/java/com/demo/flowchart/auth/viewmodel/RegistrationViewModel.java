package com.demo.flowchart.auth.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.flowchart.auth.AuthRepository;
import com.demo.flowchart.auth.AuthValidation;
import com.demo.flowchart.auth.result.AuthResult;
import com.demo.flowchart.auth.result.EmailError;
import com.demo.flowchart.auth.result.PasswordError;
import com.demo.flowchart.auth.result.VerificationPasswordError;

public class RegistrationViewModel extends ViewModel {

    private AuthRepository authRepository = new AuthRepository();
    public MutableLiveData<AuthResult> resultLiveData = authRepository.getResultLiveData();
    public MutableLiveData<Boolean> loadingState = new MutableLiveData<>();

    public void signUp(String email, String password, String verificationPassword) {
        email = email.trim();
        password = password.trim();
        verificationPassword = verificationPassword.trim();

        loadingState.postValue(true);

        if (AuthValidation.isEmailEmpty(email)) {
            resultLiveData.postValue(new EmailError("Email не может быть пустым"));
        }
        else if (!AuthValidation.isValidEmail(email)) {
            resultLiveData.postValue(new EmailError("Неверный Email"));
        }
        else if (AuthValidation.isPasswordEmpty(password)) {
            resultLiveData.postValue(new PasswordError("Пароль не может быть пустым"));
        }
        else if (!AuthValidation.isValidPassword(password)) {
            resultLiveData.postValue(new PasswordError("Длина пароля должна быть меньше 6 символов"));
        }
        else if (!AuthValidation.arePasswordsEqual(password, verificationPassword)) {
            resultLiveData.postValue(new VerificationPasswordError("Пароли не совпадают"));
        }
        else {
            authRepository.register(email, password);
        }

        loadingState.postValue(false);
    }
}

