package com.busico.android.training.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.busico.android.training.R;
import com.busico.android.training.entities.Product;

import java.text.NumberFormat;
import java.util.List;


public class ProductsAdapter extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private final List<Product> products;

    public ProductsAdapter(LayoutInflater layoutInflater, List<Product> products) {
        this.layoutInflater = layoutInflater;
        this.products = products;
    }

    @Override
    public int getCount() {
        if (products == null) {
            return 0;
        } else {
            return products.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ProductListViewHolder viewHolder;

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listitem_product, viewGroup, false);
            viewHolder = new ProductListViewHolder();
            viewHolder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
            viewHolder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ProductListViewHolder) convertView.getTag();
        }

        Product product = products.get(i);

        viewHolder.txtDescription.setText(product.getDescription());
        viewHolder.txtPrice.setText("$ " + NumberFormat.getInstance().format(product.getPrice()));

        return convertView;
    }
}
