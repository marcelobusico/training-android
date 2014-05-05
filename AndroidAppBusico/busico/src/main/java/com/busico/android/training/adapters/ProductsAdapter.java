package com.busico.android.training.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.busico.android.training.R;
import com.busico.android.training.entities.Product;
import com.busico.android.training.utils.ContentDownloaderService;

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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ProductListViewHolder viewHolder;

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listitem_product, viewGroup, false);
            viewHolder = new ProductListViewHolder();
            viewHolder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
            viewHolder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ProductListViewHolder) convertView.getTag();
        }

        Product product = products.get(position);

        viewHolder.txtDescription.setText(product.getDescription());
        viewHolder.txtPrice.setText("$ " + NumberFormat.getInstance().format(product.getPrice()));

        if(product.getImage() == null) {
            viewHolder.imageView.setImageDrawable(viewGroup.getResources().getDrawable(R.drawable.product_placeholder));

            if(!product.isDownloadingImage()) {
                product.setDownloadingImage(true);

                //Schedule the download of the product image.
                Intent contentDownloaderIntent = new Intent(convertView.getContext(), ContentDownloaderService.class);
                contentDownloaderIntent.putExtra(ContentDownloaderService.URL, product.getImageUrl());
                contentDownloaderIntent.putExtra(ContentDownloaderService.CONTENT_ID, product.getId());
                contentDownloaderIntent.putExtra(ContentDownloaderService.INDEX, position);
                convertView.getContext().startService(contentDownloaderIntent);
            }
        } else {
            //Set the image already downloaded.
            viewHolder.imageView.setImageBitmap(product.getImage());
        }

        return convertView;
    }

}
