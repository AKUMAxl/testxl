package com.xl.testqgspeech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.security.ConfirmationPrompt;
import android.util.Log;
import android.view.View;

import com.xl.testqgspeech.ui.MainFragmentDirections;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int index = getIntent().getIntExtra(Contants.EXTRA_KEY.INDEX,-1);
        Log.d("xLLL","main activity "+index);
        if (index!=-1){
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);
            NavController navController = navHostFragment.getNavController();
            MainNavDirections.ActionGlobalMainFragment actionGlobalMainFragment = MainNavDirections.actionGlobalMainFragment();
            actionGlobalMainFragment.setIndex(index);
            navController.navigate(actionGlobalMainFragment);
        }

    }

}