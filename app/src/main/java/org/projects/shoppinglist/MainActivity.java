package org.projects.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<Product> adapter;
    ListView listView;
    ArrayList<Product> bag = new ArrayList<>();

    public ArrayAdapter getMyAdapter()
    {
        return adapter;
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

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = productInput.getText().toString();
                int quantity = Integer.valueOf(productAmount.getText().toString());
                bag.add(new Product(name, quantity));
                //you have to use getText() for EditText types

                //The next line is needed in order to say to the ListView
                //that the data has changed - we have added stuff now!
                getMyAdapter().notifyDataSetChanged();
            }
        });

        //add some stuff to the list
        //bag.add("Bananas");
        //bag.add("Apples");

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
        bag.remove(listView.getCheckedItemPosition());
        //you use getMyAdapter so that the app doesn't crash after changes
        getMyAdapter().notifyDataSetChanged();
    }

    //clear/delete button for the entire list
    public void onClickClear(View view){
        bag.clear();
        getMyAdapter().notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
