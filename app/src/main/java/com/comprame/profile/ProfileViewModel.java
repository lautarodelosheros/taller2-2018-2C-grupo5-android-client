package com.comprame.profile;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.comprame.R;
import com.comprame.login.Session;
import com.comprame.login.User;

public class ProfileViewModel extends AndroidViewModel {
    public final MutableLiveData<Session> session;
    public final MutableLiveData<String> name;
    public final MutableLiveData<String> firstName;
    public final MutableLiveData<String> lastName;
    public final MutableLiveData<String> email;
    public final MutableLiveData<String> password;
    public final MutableLiveData<Boolean> enabled;

    public ProfileViewModel(Application application) {
        super(application);
        session = new MutableLiveData<>();
        name = new MutableLiveData<>();
        firstName = new MutableLiveData<>();
        lastName = new MutableLiveData<>();
        email = new MutableLiveData<>();
        password = new MutableLiveData<>();
        enabled = new MutableLiveData<>();
        firstName.observeForever((s) -> enabled.setValue(this.isEnabled()));
        lastName.observeForever((s) -> enabled.setValue(this.isEnabled()));
        email.observeForever((s) -> enabled.setValue(this.isEnabled()));
        password.observeForever((s) -> enabled.setValue(this.isEnabled()));
    }

    public String firstNameError() {
        String nombre = firstName.getValue();
        if (nombre == null
                || nombre.isEmpty()
                || nombre.length() < 2) {
            return getApplication().getString(R.string.name_error);
        }
        return null;
    }

    public String lastNameError() {
        String apellido = lastName.getValue();
        if (apellido == null
                || apellido.isEmpty()
                || apellido.length() < 2) {
            return getApplication().getString(R.string.lastname_error);
        }
        return null;
    }

    public String emailError() {
        String email = this.email.getValue();
        if (email == null
                || email.isEmpty()
                || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).
                matches()) {
            return getApplication().getString(R.string.email_error);
        }
        return null;
    }


    public String passwordError() {
        String password = this.password.getValue();
        if (password == null
                || password.isEmpty()
                || password.length() < 4
                || password.length() > 10) {
            return getApplication().getString(R.string.password_error);
        }
        return null;
    }

    private boolean isEnabled() {
        return passwordError() == null
                && emailError() == null
                && firstNameError() == null
                && lastNameError() == null;
    }

    public void bindUser(User user) {
        name.setValue(user.getName());
        firstName.setValue(user.getFirstname());
        lastName.setValue(user.getLastname());
        email.setValue(user.getEmail());
        password.setValue(user.getPassword());
    }

    public User asUser() {
        return new User(name.getValue()
                , firstName.getValue()
                , lastName.getValue()
                , password.getValue()
                , email.getValue());
    }
}