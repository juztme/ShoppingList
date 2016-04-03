package org.projects.shoppinglist;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<Product> adapter;
    ListView listView;
    ArrayList<Product> bag = new ArrayList<>();
    public ArrayAdapter<Product> getMyAdapter()
    {
        return adapter;
    }

    //declare elements for saving a copy of the product selected by the user
    Product lastDeletedProduct;
    int lastDeletedPosition;
    //method for saving a copy of the product selected
    public void saveCopy(){
        //define the elements to be saved
        lastDeletedPosition = listView.getCheckedItemPosition();
        lastDeletedProduct = bag.get(lastDeletedPosition);
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
            bag = savedInstanceState.getParcelableArrayList("bag");
            position = savedInstanceState.getInt("position");
        }

        //getting our listiew - you can check the ID in the xml to see that it
        //is indeed specified as "list"
        listView = (ListView) findViewById(R.id.list);
        //here we create a new adapter linking the bag and the
        //listview
        adapter =  new ArrayAdapter<Product>(this,
                android.R.layout.simple_list_item_checked, bag );

        //setting the adapter on the listview
        listView.setAdapter(adapter);
        //here we set the choice mode - meaning in this case we can
        //only select one item at a time.
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        if(position != -1){
            listView.setSelection(position);
        }

        Button addButton = (Button) findViewById(R.id.addButton);
        final EditText productInput = (EditText) findViewById(R.id.productInput);
        final EditText productAmount = (EditText) findViewById(R.id.productAmount);
        final Spinner dropdownAmount = (Spinner) findViewById(R.id.dropDownAmount);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get input from user and transform it to string for the product name and int for
                // the quantity
                String name = productInput.getText().toString();
                //int quantity = Integer.valueOf(productAmount.getText().toString());
                int quantity = Integer.valueOf(productAmount.getText().toString());
                String listQuantity = (String) dropdownAmount.getSelectedItem();

                //add the product to the list
                bag.add(new Product(name, quantity, listQuantity));
                //you have to use getText() for EditText types

                //The next line is needed in order to say to the ListView
                //that the data has changed - we have added stuff now!
                getMyAdapter().notifyDataSetChanged();
            }
        });

    }

    //save the info before it gets destroyed when the screen gets rotated
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        //say what properties you want saved
        savedInstanceState.putParcelableArrayList("bag", bag);
        //the name between quotes doesn't have to match the original name of the
        // variable/whatever that is
        savedInstanceState.putInt("position", listView.getCheckedItemPosition());
    }

    //delete button for a checked item
    public void onClickDelete(View view){
        saveCopy(); //save a copy of the item selected before you delete it
        bag.remove(listView.getCheckedItemPosition());
        Snackbar snackbar = Snackbar
                .make(listView, "Item Deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener(){
                   @Override
                   public void onClick(View view){
                       bag.add(lastDeletedPosition, lastDeletedProduct);
                       getMyAdapter().notifyDataSetChanged();
                       Snackbar snackbar = Snackbar.make(listView, "Item restored!", Snackbar
                               .LENGTH_SHORT);
                       snackbar.show();
                   }
                });
        snackbar.show();
        //you use getMyAdapter so that the app doesn't crash after changes
        getMyAdapter().notifyDataSetChanged();
    }

    //clear/delete button for the entire list
    public void onClickClear(View view){
        //showing dialog before anything gets deleted
        MyDialogFragment dialog = new MyDialogFragment(){
            @Override
            protected void positiveClick(){
                bag.clear();
                getMyAdapter().notifyDataSetChanged();
            }

            @Override
            protected void negativeClick(){
                Toast toast = Toast.makeText(getApplicationContext(), "List not cleared", Toast
                        .LENGTH_LONG);
                toast.show();
            }
        };
        dialog.show(getFragmentManager(), "MyFragment");
    }

    //switch to using manual amount
    public void onClickUseManualAmount(View view){
        Spinner dropDown = (Spinner) findViewById(R.id.dropDownAmount);
        EditText manualAmount = (EditText) findViewById(R.id.productAmount);
        dropDown.setVisibility(View.GONE);
        manualAmount.setVisibility(View.VISIBLE);
    }

    //switch to using the dropdown list for the amount
    public void onClickUseDropdownAmount(View view){
        Spinner dropDown = (Spinner) findViewById(R.id.dropDownAmount);
        EditText manualAmount = (EditText) findViewById(R.id.productAmount);
        manualAmount.setVisibility(View.GONE);
        dropDown.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //return true;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        switch(item.getItemId()){
            case R.id.clear_all:
                //showing dialog before anything gets deleted
                MyDialogFragment dialog = new MyDialogFragment(){
                    @Override
                    protected void positiveClick(){
                        bag.clear();
                        getMyAdapter().notifyDataSetChanged();
                    }

                    @Override
                    protected void negativeClick(){
                        Toast toast = Toast.makeText(getApplicationContext(), "List not cleared", Toast
                                .LENGTH_LONG);
                        toast.show();
                    }
                };
                dialog.show(getFragmentManager(), "MyFragment");
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
