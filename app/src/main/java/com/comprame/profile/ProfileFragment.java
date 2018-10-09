package com.comprame.profile;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.comprame.App;
import com.comprame.MainActivity;
import com.comprame.R;
import com.comprame.databinding.ProfileFragmentBinding;
import com.comprame.login.User;
import com.comprame.library.view.ProgressPopup;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        ProfileFragmentBinding binding = DataBindingUtil.inflate(inflater
                , R.layout.profile_fragment
                , container
                , false);
        binding.setProfileModel(ViewModelProviders.of(this)
                .get(ProfileViewModel.class));
        binding.setFragment(this);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        profileViewModel = ViewModelProviders.of(this)
                .get(ProfileViewModel.class);

        loadUserProfile();
    }

    private void loadUserProfile() {
        ProgressPopup progressPopup = new ProgressPopup("Cargando perfil...", getContext());
        progressPopup.show();

        String path = "/user/" + ((MainActivity) Objects.requireNonNull(getActivity())).session.getSession();

        App.appServer.get(path, User.class)
                .onDone((i, ex) -> progressPopup.dismiss())
                .run(
                        (User user) -> {
                            if (user.getName().isEmpty()) {
                                Toast.makeText(getContext()
                                        , "No se encuentra el usuario"
                                        , Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                profileViewModel.bindUser(user);
                            }
                        }
                        , (Exception ex) -> {
                            Log.d("ProfileListener", "Error al recuperar el perfil", ex);
                            Toast.makeText(getContext()
                                    , "Error al cargar el perfil"
                                    , Toast.LENGTH_LONG)
                                    .show();
                        }
                );
    }

    public void updateProfile(View view) {
        ProgressPopup progressPopup = new ProgressPopup("Actualizando perfil...", getContext());
        progressPopup.show();

        String path = "/user/" + ((MainActivity) Objects.requireNonNull(getActivity())).session.getSession();

        App.appServer.put(path, profileViewModel.asUser(), User.class)
                .onDone((i, ex) -> progressPopup.dismiss())
                .run(
                        (User user) -> {
                            if (user.getName().isEmpty()) {
                                Log.d("ProfileListener", "No se encuentra el usuario");
                                Toast.makeText(getContext()
                                        , "No se encuentra el usuario"
                                        , Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                profileViewModel.bindUser(user);
                                Toast.makeText(getContext()
                                        , "Perfil actualizado!"
                                        , Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                        , (Exception ex) -> {
                            Log.d("ProfileListener", "Error al actualizar el perfil", ex);
                            Toast.makeText(getContext()
                                    , "Error al actualizar el perfil"
                                    , Toast.LENGTH_LONG)
                                    .show();
                        }
                );
    }

}