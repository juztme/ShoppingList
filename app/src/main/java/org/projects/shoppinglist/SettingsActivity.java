package org.projects.shoppinglist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    PreferenceManager manager = getPreferenceManager();
    //has to be the same as in MainActivity
    manager.setSharedPreferencesName("my_prefs");
    //adding the layout from the xml file
    addPreferencesFromResource(R.xml.prefs);
    //setContentView(R.layout.activity_settings);
  }

}
