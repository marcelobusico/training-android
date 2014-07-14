package com.busico.android.training;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.busico.android.training.R;
import com.busico.android.training.entities.Item;

import java.text.NumberFormat;

public class ItemDetailsActivity extends ActionBarActivity {

    private ImageView itemImage;
    private TextView itemTitle;
    private TextView itemPrice;
    private TextView itemQuantity;
    private TextView itemCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        itemImage = (ImageView) findViewById(R.id.itemImage);
        itemTitle = (TextView) findViewById(R.id.itemTitle);
        itemPrice = (TextView) findViewById(R.id.itemPrice);
        itemQuantity = (TextView) findViewById(R.id.itemQuantity);
        itemCondition = (TextView) findViewById(R.id.itemCondition);

        Item item;
        if (savedInstanceState == null) {
            item = (Item) getIntent().getSerializableExtra("item");
        } else {
            item = (Item) savedInstanceState.getSerializable("item");
        }

        itemTitle.setText(item.getDescription());

        if (item.getPrice() == null) {
            itemPrice.setText(null);
        } else {
            itemPrice.setText("$ " + NumberFormat.getInstance().format(item.getPrice()));
        }

        if (item.getAvailableQuantity() == null) {
            itemQuantity.setText(null);
        } else {
            String quantityStr = getString(R.string.quantity);
            itemQuantity.setText(quantityStr + ": " + NumberFormat.getInstance().format(item.getAvailableQuantity()));
        }
    }
}
