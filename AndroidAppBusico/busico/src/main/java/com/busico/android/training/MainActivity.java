package com.busico.android.training;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.busico.android.training.storage.AppSettings;


public class MainActivity extends ActionBarActivity {

    private Button btnSearch;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = (SearchView) findViewById(R.id.searchView);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchProduct();
            }
        });

        String savedValue = new AppSettings(this).getSavedValue("item_query");
        searchView.setQuery(savedValue, false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String queryString = searchView.getQuery().toString().trim();
        new AppSettings(this).saveValue("item_query", queryString);
    }

    private void searchProduct() {
        String queryString = searchView.getQuery().toString().trim();
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("queryString", queryString);
        startActivity(intent);
        //showSearchResult(getString(R.string.searchSuccessful, queryString));
    }

//    private void showSearchResult(String content) {
//        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//        alertDialog.setTitle(getString(R.string.dialogTitle));
//        alertDialog.setMessage(content);
//        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.btnOk),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//        alertDialog.setIcon(R.drawable.ic_launcher);
//        alertDialog.show();
//    }
}
