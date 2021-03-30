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

import com.qinggan.speech.customqa.CustomQAMgr;
import com.xl.testqgspeech.ui.MainFragmentDirections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

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

//        try {
//            Class<?> activityStackSupervisorCls = Class.forName("com.android.server.am.ActivityStackSupervisor");
//            Object activityStackSupervisor =activityStackSupervisorCls.newInstance();
//            Method method_getAllStackInfosLocked = activityStackSupervisorCls.getDeclaredMethod("getAllStackInfosLocked");
//            method_getAllStackInfosLocked.setAccessible(true);
//            Object stackInfoListCls = method_getAllStackInfosLocked.invoke(activityStackSupervisor,null);
//            Object stackInfo = ((ArrayList)stackInfoListCls).get(0);
//            Log.d("xxx","stackInfo:"+stackInfo.toString());
//            Field field  = stackInfo.getClass().getDeclaredField("topActivity");
//            field.setAccessible(true);
//            field.get(stackInfo);
//
//
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
    }

}