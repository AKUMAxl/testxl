package com.xl.testqgspeech.di.module;

import android.content.Context;
import android.content.res.AssetManager;


import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class AndroidManagerModule {

    @Provides
    AssetManager provideAssetManager(@ApplicationContext Context context){
        return context.getAssets();
    }



}
