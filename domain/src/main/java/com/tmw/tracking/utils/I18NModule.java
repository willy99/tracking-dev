package com.tmw.tracking.utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.name.Names;
import com.tmw.tracking.entity.enums.Langs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class I18NModule extends AbstractModule {

    static final String LANG_BUNDLE = "langBundle";

    protected MapBinder<Locale, LangProvider> binder;


    @Override
    protected void configure() {

        InputStream serviceAccount = this.getClass().getResourceAsStream("/firebase_artinlog.json");
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://artinlog-clc.firebaseio.com/")
                .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }


        binder = MapBinder.newMapBinder(
                binder(),
                Locale.class,
                LangProvider.class, Names.named(LANG_BUNDLE ));

        //save and create bindings
        for (Langs langs: Langs.values()) {
            LangProvider langProvider = new LangProvider(langs.getLocale());
            binder.addBinding(langs.getLocale()).toInstance(langProvider);
        }

    }

	/*private void bindEnums() {
		ClassInfoList enumClasses =
				new ClassGraph()
						.enableAllInfo()
						.whitelistPackages("com.tmw.tracking.domain")
						.scan()
						.getClassesImplementing("com.tmw.tracking.domain.I18NEnum").getEnums();
		List<String> enumValues = new ArrayList<>();

		for (int i=0; i<enumClasses.getNames().size() ; i++) {
			for (String value : enumClasses.get(i).getFieldInfo().getNames()) {
				String name = enumClasses.get(i).getName();
				if (value.contains("$")) continue;
				String key = ("dict_" + name.substring(name.lastIndexOf(".")+1) + "_" + value).toLowerCase();

				enumValues.add(key);
				Key<String> guiceKey = Key.get(String.class, getValue(key));
				bind(guiceKey).toInstance(value + i);

				//TODO what to do dalshe - her ego znaet

			}
		}

		System.out.println(enumClasses);
	}*/



}

