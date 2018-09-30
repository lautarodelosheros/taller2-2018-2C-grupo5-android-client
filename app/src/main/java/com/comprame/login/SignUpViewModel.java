package com.comprame.login;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.comprame.R;
import com.comprame.domain.Session;
import com.comprame.domain.User;

public class SignUpViewModel extends AndroidViewModel {
    public final MutableLiveData<Session> session;
    public final MutableLiveData<String> name;
    public final MutableLiveData<String> firstname;
    public final MutableLiveData<String> lastname;
    public final MutableLiveData<String> email;
    public final MutableLiveData<String> password;
    public final MutableLiveData<Boolean> enabled;


    public SignUpViewModel(Application application) {
        super(application);
        password = new MutableLiveData<>();
        name = new MutableLiveData<>();
        session = new MutableLiveData<>();
        firstname = new MutableLiveData<>();
        lastname = new MutableLiveData<>();
        email = new MutableLiveData<>();

        enabled = new MutableLiveData<>();

        password.observeForever((s) -> enabled.setValue(this.isValid()));
        name.observeForever((s) -> enabled.setValue(this.isValid()));
        session.observeForever((s) -> enabled.setValue(this.isValid()));
        firstname.observeForever((s) -> enabled.setValue(this.isValid()));
        lastname.observeForever((s) -> enabled.setValue(this.isValid()));
        email.observeForever((s) -> enabled.setValue(this.isValid()));

    }

    private String getString(int id) {
        return getApplication().getString(id);
    }

    public String nameError() {
        String userName = name.getValue();
        if (userName == null || userName.isEmpty() || userName.length() < 2) {
            return getString(R.string.name_error);
        }
        return null;
    }

    public String firstnameError() {
        String nombre = firstname.getValue();
        if (nombre == null
                || nombre.isEmpty()
                || nombre.length() < 2) {
            return getString(R.string.name_error);
        }
        return null;
    }

    public String lastnameError() {
        String apellido = lastname.getValue();
        if (apellido == null
                || apellido.isEmpty()
                || apellido.length() < 2) {
            return getString(R.string.apellido_error);
        }
        return null;
    }

    public String emailError() {
        String email = this.email.getValue();
        if (email == null
                || email.isEmpty()
                || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).
                matches()) {
            return getString(R.string.email_error);
        }
        return null;
    }


    public String passwordError() {
        String password = this.password.getValue();
        if (password == null
                || password.isEmpty()
                || password.length() < 4
                || password.length() > 10) {
            return getString(R.string.password_error);
        }
        return null;
    }

    private boolean isValid() {
        return nameError() == null
                && passwordError() == null
                && emailError() == null
                && firstnameError() == null
                && lastnameError() == null;
    }

    public User asUser() {
        return new User(name.getValue()
                , firstname.getValue()
                , lastname.getValue()
                , password.getValue()
                , email.getValue());
    }
}