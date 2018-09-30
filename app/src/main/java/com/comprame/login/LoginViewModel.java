package com.comprame.login;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.Nullable;

import com.comprame.R;
import com.comprame.domain.Session;

public class LoginViewModel extends AndroidViewModel {
    public final MutableLiveData<Session> session;
    public final MutableLiveData<String> name;
    public final MutableLiveData<String> password;
    public final MutableLiveData<Boolean> enabled;


    public LoginViewModel(Application application) {
        super(application);
        password = new MutableLiveData<>();
        name = new MutableLiveData<>();
        session = new MutableLiveData<>();
        enabled = new MutableLiveData<>();
        name.observeForever((s) -> enabled.setValue(this.isEnabled()));
        password.observeForever((s) -> enabled.setValue(this.isEnabled()));
    }

    public String nameError() {
        String nameValue = name.getValue();
        return nameValidator(nameValue);
    }

    @Nullable
    public String nameValidator(CharSequence nameValue) {
        if (nameValue == null
                || nameValue.length() < 2) {
            return getApplication().getString(R.string.name_error);
        }
        return null;
    }

    public String passwordError() {
        String passwordValue = password.getValue();
        if (passwordValue == null
                || passwordValue.isEmpty()
                || passwordValue.length() < 4
                || passwordValue.length() > 10) {
            return getApplication().getString(R.string.password_error);
        }
        return null;
    }

    private boolean isEnabled() {
        return (nameError() == null && passwordError() == null);
    }
}