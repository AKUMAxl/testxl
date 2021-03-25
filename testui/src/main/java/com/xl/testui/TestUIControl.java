package com.xl.testui;

import com.qinggan.speech.UIControl;
import com.qinggan.speech.UIControlElementItem;

import java.util.ArrayList;

public class TestUIControl {

    public static final String IDENTIFY = "test";


    public void registerUIControl(){
        UIControlElementItem uiControlElementItem = new UIControlElementItem();
        uiControlElementItem.setIdentify(IDENTIFY);
        uiControlElementItem.setWord("今天天气");
        ArrayList<UIControlElementItem> list = new ArrayList<>();
        list.add(uiControlElementItem);
        UIControl.getInstance().setElementUCWords(list);
    }

    public void unRegisterUIControl(){
        UIControl.getInstance().clearElementUCWords();
        UIControl.getInstance().clearAllUIControls();
    }


}
