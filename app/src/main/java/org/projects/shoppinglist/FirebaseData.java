package org.projects.shoppinglist;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by juztm_000 on 20-Apr-16.
 */
public class FirebaseData extends Application {
  @Override
  public void onCreate(){
    super.onCreate();
    Firebase.setAndroidContext(this);
    //setPersistenceEnabled is for when the user is offline and you still want to save the data
    // in cache so that it would be updated when the user goes online again; so basically,
    // offline storage
    Firebase.getDefaultConfig().setPersistenceEnabled(true);
  }
}
