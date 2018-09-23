package taller2_2018_2c_grupo5.comprame.actividades.comunes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import taller2_2018_2c_grupo5.comprame.R;

public class FragmentLoader {

    public static void load(AppCompatActivity activity, Fragment fragment, String name) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        int stackCount = fragmentManager.getBackStackEntryCount();
        String previous = getFragmentName(fragmentManager, stackCount - 1);

        if (previous.equals(name)) { return;}

        fragmentManager.beginTransaction()
                .replace(R.id.contenedor, fragment)
                .addToBackStack(name)
                .commit();
    }

    private static String getFragmentName(FragmentManager fragmentManager, int pos){


        if (pos >= 0) {
            return fragmentManager.getBackStackEntryAt(pos).getName();
        }

        return "";

    }
}
