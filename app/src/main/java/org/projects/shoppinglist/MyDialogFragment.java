package org.projects.shoppinglist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by juztm_000 on 14-Mar-16.
 */
public class MyDialogFragment extends DialogFragment{

  public MyDialogFragment() {

  }



  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState){
    //create a new dialogbuilder
    AlertDialog.Builder alert = new AlertDialog.Builder(
            getActivity());
    alert.setTitle("Confirmation");
    alert.setMessage("Are you sure?");
    alert.setPositiveButton("Yes", pListener);
    alert.setNegativeButton("No", nListener);
    return alert.create();
  }

  //listener for positive response (yes button)
  DialogInterface.OnClickListener pListener = new DialogInterface.OnClickListener(){
    @Override
    public void onClick(DialogInterface arg0, int arg1){
      positiveClick();
    }
  };

  //listener for negative response (no button)
  DialogInterface.OnClickListener nListener = new DialogInterface.OnClickListener(){
    @Override
    public void onClick(DialogInterface arg0, int arg1){
      negativeClick();
    }
  };

  //empty methods which will be overridden in the main activity file
  protected void positiveClick(){}
  protected void negativeClick(){}
}
