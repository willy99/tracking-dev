package com.tmw.tracking.utils;

import com.google.firebase.database.*;
import com.google.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Locale;

public class LangProvider implements Provider<HashMap<String, String>> {

    private Locale locale;
    private HashMap<String, String> bundle;
    private Boolean filled = false;

    private final static Logger logger = LoggerFactory.getLogger(LangProvider.class);


    LangProvider(Locale locale) {
        this.locale = locale;
    }

    @Override
    public HashMap<String, String> get() {

        //check if bundle is empty
        if (bundle == null) {
            //get from firebase
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://artinlog-clc.firebaseio.com");
            DatabaseReference ref = firebaseDatabase.getReference();
            DatabaseReference localeRef = ref.child("locale").child(locale.getLanguage());
            HashMap<String, String> langHashMap = new HashMap<>();
            localeRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot label: dataSnapshot.getChildren()) {
                        langHashMap.put(label.getKey(), (String)label.getValue());
                    }

                    bundle = langHashMap;
                    logger.debug(">>> reloaded bundle for " + locale.getLanguage() + " / " +bundle.keySet().size());

                    filled = true;
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw new RuntimeException("Error occured while filling the resources map");
                }
            });

            //wait until saves the map of labels.
            while (!filled) {
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return bundle;
    }
}


