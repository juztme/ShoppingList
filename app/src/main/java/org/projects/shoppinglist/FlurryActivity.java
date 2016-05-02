package org.projects.shoppinglist;

import android.app.Application;
import com.flurry.android.FlurryAgent;

/**
 * Created by juztm_000 on 02-May-16.
 */
public class FlurryActivity extends Application {
  @Override
  public void onCreate(){
    super.onCreate();
    //configure Flurry
    FlurryAgent.setLogEnabled(false);
    //init Flurry
    FlurryAgent.init(this, "PFG46JBQTRQ96BJTNSVM");
  }
}
