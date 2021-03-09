package com.xl.testqgspeech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Inject
    Test test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        test.get();
        setContentView(R.layout.activity_main);

//        Navigation.findNavController(this,R.id.help_fragment).navigate(R.id.action_main_fragment_to_help_fragment);
    }

}