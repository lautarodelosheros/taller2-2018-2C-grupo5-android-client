package com.comprame.sell;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.comprame.R;
import com.comprame.overview.Question;

public class CategoryPopupViewModel extends AndroidViewModel {

    public final MutableLiveData<String> category;
    public final MutableLiveData<Boolean> enabled;

    public CategoryPopupViewModel(Application application) {
        super(application);
        category = new MutableLiveData<>();
        enabled = new MutableLiveData<>();
        category.observeForever((s) -> enabled.setValue(this.isEnabled()));
    }

    public String categoryError() {
        if (category.getValue() == null
                || category.getValue().length() < 2) {
            return getApplication().getString(R.string.question_error);
        }
        return null;
    }

    private boolean isEnabled() {
        return categoryError() == null;
    }

    public String getCategory() {
        return category.getValue();
    }

    public void setCategory(String category) {
        this.category.setValue(category);
    }
}
