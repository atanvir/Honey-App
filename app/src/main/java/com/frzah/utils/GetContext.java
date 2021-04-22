package com.frzah.utils;

import android.content.Context;

public class GetContext  {
    private static GetContext getContext=null;
    private static Context context;
    private GetContext(){
    }



    public static GetContext getInstance(Context cont){
        if(getContext==null) {
            getContext= new GetContext();
            context=cont.getApplicationContext();
        }
        return getContext;
    }

    public static Context getContext(){
        return context;
    }

}
