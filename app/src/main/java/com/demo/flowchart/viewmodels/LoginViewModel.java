package com.demo.flowchart.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.flowchart.auth.AuthRepository;
import com.demo.flowchart.auth.AuthValidation;
import com.demo.flowchart.auth.result.AuthResult;
import com.demo.flowchart.auth.result.EmailError;
import com.demo.flowchart.auth.result.PasswordError;

public class LoginViewModel extends ViewModel {

    private final AuthRepository authRepository = new AuthRepository();
    public MutableLiveData<AuthResult> resultLiveData = authRepository.getResultLiveData();

    public void signIn(String email, String password) {
        email = email.trim();
        password = password.trim();

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
        else {
            authRepository.login(email, password);
        }
    }
}
