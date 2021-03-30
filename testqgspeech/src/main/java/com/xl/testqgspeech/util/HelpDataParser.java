package com.xl.testqgspeech.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xl.testqgspeech.bean.voiceBean.HelpDataNewBean;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 解析帮助数据
 */
public class HelpDataParser {

    @Inject
    AssetManager mAssetManager;

    @Inject
    public HelpDataParser(){

    }

    public final String wakeupHelpFileName = "wakeup_help_data.json";

    public ArrayList<HelpDataNewBean> getWakeUpHelpData() {

        ArrayList<HelpDataNewBean> helpDataNewBeans;
        String jsonResult = getJsonFromAsset(wakeupHelpFileName);

        Type type = new TypeToken<List<HelpDataNewBean>>() {
        }.getType();
        helpDataNewBeans = new Gson().fromJson(jsonResult, type);
        return helpDataNewBeans;
    }


    /**
     * 解析filename中的json数据
     */
    private String getJsonFromAsset(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = null;
        BufferedReader bf = null;
        try {
            //通过管理器打开文件并读取
            inputStreamReader = new InputStreamReader(mAssetManager.open(fileName));
            bf = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (bf != null) {
                    bf.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

}
