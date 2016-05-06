package org.projects.shoppinglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;
import com.flurry.android.FlurryAgent;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText productInput;
    EditText productAmount;
    Spinner dropdownAmount;

    MyDialogFragment dialog = new MyDialogFragment(){
        @Override
        protected void positiveClick(){
            //event to log: clearing the list; no parameters should be logged
            FlurryAgent.logEvent("Clear_List");
                userItemsRef.setValue(null);
            //bag.clear();
            getMyAdapter().notifyDataSetChanged();
        }

        @Override
        protected void negativeClick(){
            Toast toast = Toast.makeText(getApplicationContext(), "List not cleared", Toast
                    .LENGTH_LONG);
            toast.show();
        }
    };

    Firebase userItemsRef;
    FirebaseListAdapter<Product> fireAdapter;
    Query queryRef;
    ListView listView;
    public FirebaseListAdapter<Product> getMyAdapter() { return fireAdapter; }
    public Product getItem(int index){
        return getMyAdapter().getItem(index);
    }
    //declare elements for saving a copy of the product selected by the user
    Product lastDeletedProduct;
    int lastDeletedPosition;
    //method for saving a copy of the product selected
    public void saveCopy(){
        //define the elements to be saved

        lastDeletedPosition = listView.getCheckedItemPosition();
        lastDeletedProduct = getItem(lastDeletedPosition);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int position = -1;

        //get previous state of the app before it gets destroyed
        if(savedInstanceState != null){
            position = savedInstanceState.getInt("position");
        }

        //getting our listView - you can check the ID in the xml to see that it
        //is indeed specified as "list"
        listView = (ListView) findViewById(R.id.list);

        userItemsRef = new Firebase("https://shoppinglistbaaa.firebaseio.com/items");
        //connection between UI and the db connection
        fireAdapter = new FirebaseListAdapter<Product>(this, Product
                .class, android.R.layout.simple_list_item_checked, userItemsRef){
            @Override
            protected void populateView(View v, Product product, int i){
                TextView text = (TextView) v.findViewById(android.R.id.text1);
                text.setText(product.toString());
            }
        };

        //setting the adapter on the listview
        listView.setAdapter(fireAdapter);

        //here we set the choice mode - meaning in this case we can
        //only select one item at a time.
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        if(position != -1){
            listView.setSelection(position);
        }

        getPreferences(); //get the preferences when the user starts the app

        //get the preferences for the switch
        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        boolean manualInputSwitch = prefs.getBoolean("productAmountSwitch", false);
        Spinner dropDown = (Spinner) findViewById(R.id.dropDownAmount);
        EditText manualAmount = (EditText) findViewById(R.id.productAmount);
        if(manualInputSwitch){
            dropDown.setVisibility(View.GONE);
            manualAmount.setVisibility(View.VISIBLE);
        } else {
            manualAmount.setVisibility(View.GONE);
            dropDown.setVisibility(View.VISIBLE);
        }

        Button addButton = (Button) findViewById(R.id.addButton);
        //final SearchView searchButton = (SearchView) findViewById(R.id.searchButton);
        productInput = (EditText) findViewById(R.id.productInput);
        productAmount = (EditText) findViewById(R.id.productAmount);
        dropdownAmount = (Spinner) findViewById(R.id.dropDownAmount);
        final Map<String, String> productParams = new HashMap<String, String>();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get input from user and transform it to string for the product name and int for
                // the quantity; you have to use getText() for EditText types
                String name = productInput.getText().toString();
                int quantity = Integer.valueOf(productAmount.getText().toString());
                String listQuantity = (String) dropdownAmount.getSelectedItem();
                Product p = new Product(name, quantity, listQuantity);

                //add the product to the list
                userItemsRef.push().setValue(p);

                //add params to flurry when products are added to the basket
                productParams.put(p.getName(), String.valueOf(p.getQuantity()));
                FlurryAgent.logEvent("Add_Product", productParams);

                //tell the listView that the data has changed
                getMyAdapter().notifyDataSetChanged();
            }
        });
    }

    //save the info before it gets destroyed when the screen gets rotated
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        //say what properties you want saved
        //the name between quotes doesn't have to match the original name of the
        // variable/whatever that is
        savedInstanceState.putInt("position", listView.getCheckedItemPosition());
    }

    //delete button for a checked item
    public void onClickDelete(View view){
        saveCopy(); //save a copy of the item selected before you delete it
        int index = listView.getCheckedItemPosition(); //get the index of the selected item
            getMyAdapter().getRef(index).setValue(null); //set the selected item to null in order
        // to delete it
            //give the user the option to restore the product
            Snackbar snackbar = Snackbar
                    .make(listView, "Item Deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            userItemsRef.push().setValue(lastDeletedProduct);
                            getMyAdapter().notifyDataSetChanged();
                            Snackbar snackbar = Snackbar.make(listView, "Item restored!", Snackbar
                                    .LENGTH_SHORT);
                            snackbar.show();
                        }
                    });
            snackbar.show();
    }

    public void setPreferences(){
        //create a new activity and instruct the Android system to start it
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, 1);
    }

    //after you exit the preferences screen, you have to update the data so it works with the
    // other activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1){
          SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
          boolean manualInputSwitch = prefs.getBoolean("productAmountSwitch", false);
          Spinner dropDown = (Spinner) findViewById(R.id.dropDownAmount);
          EditText manualAmount = (EditText) findViewById(R.id.productAmount);
          if(manualInputSwitch){
              dropDown.setVisibility(View.GONE);
              manualAmount.setVisibility(View.VISIBLE);
          } else {
              manualAmount.setVisibility(View.GONE);
              dropDown.setVisibility(View.VISIBLE);
          }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getPreferences(){
        //read the shared preferences
        SharedPreferences prefs = getSharedPreferences("my_prefs", MODE_PRIVATE);
        String username = prefs.getString("username", "");
        //welcome the user when they enter the app
        Toast.makeText(
                this,
                "Welcome back, " + username + "!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //return true;
        return super.onCreateOptionsMenu(menu);
    }

    //take the product list and transform it into a string that can be shared with others later on
    public String convertListToString(){
      String result = "";
        for (int i = 0; i < fireAdapter.getCount(); i++){
            Product p = fireAdapter.getItem(i);
            result += p.getQuantity() + " " + p.getName() + "\n";
        }
      return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case R.id.clear_all:
                //showing dialog before anything gets deleted
                dialog.show(getFragmentManager(), "MyFragment");
                return true;
            case R.id.share_list:
                String finalList = convertListToString();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, finalList);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            case R.id.action_settings:
                setPreferences(); //go to the preferences Activity
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
