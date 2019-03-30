package com.example.mytvs.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytvs.R;
import com.example.mytvs.adapters.PersonAdapter;
import com.example.mytvs.model.Person;
import com.example.mytvs.model.User;
import com.example.mytvs.retrofit.ApiClient;
import com.example.mytvs.retrofit.ApiInterface;
import com.example.mytvs.retrofit.DataResponse;
import com.example.mytvs.retrofit.TableDataResponse;
import com.example.mytvs.utils.Utility;
import com.google.gson.Gson;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity implements PersonAdapter.PersonClicked {

    private PersonAdapter personAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        TextView toolText = toolbar.findViewById(R.id.toolbar_title);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/logo.ttf");
        toolText.setTypeface(custom_font);

        RecyclerView recyclerView = findViewById(R.id.data_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        personAdapter = new PersonAdapter(this);
        recyclerView.setAdapter(personAdapter);
        getJsonData();
    }

    /*
    Method used to get JSON data by starting retrofit
     */
    private void getJsonData(){
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        // Setting username and password entered by user
        User user = new User();
        user.setUserName(getIntent().getStringExtra(Utility.INTENT_USERNAME));
        user.setPassWord(getIntent().getStringExtra(Utility.INTENT_PASSWORD));

        Call<TableDataResponse> call = apiInterface.getData(user);
        call.enqueue(new Callback<TableDataResponse>() {
            @Override
            public void onResponse(Call<TableDataResponse> call, Response<TableDataResponse> response) {
                assert response.body() != null;
                DataResponse dataResponse = new Gson().fromJson(response.body().getTableData(), DataResponse.class);
                personAdapter.setPersonList(Utility.getPersonList(dataResponse));
            }

            @Override
            public void onFailure(Call<TableDataResponse> call, Throwable t) {
                Toast.makeText(ListActivity.this, "No Data Returned", Toast.LENGTH_LONG).show();
            }
        });
    }

    /*
    Method triggered on list item click
     */
    @Override
    public void onPersonClicked(Person person) {

        Intent intent = new Intent(ListActivity.this, DetailActivity.class);
        intent.putExtra(Utility.INTENT_PERSON, person);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //Associate Searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        //Listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //Filter recycler view when query submitted
                personAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //Filter recycler view when text change
                personAdapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_search)
            return true;

        return super.onOptionsItemSelected(item);
    }
}
