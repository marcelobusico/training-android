package com.busico.android.training.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.busico.android.training.R;
import com.busico.android.training.entities.Product;
import com.busico.android.training.utils.ImageDownloader;

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
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ProductListViewHolder) convertView.getTag();
        }

        Product product = products.get(i);

        viewHolder.txtDescription.setText(product.getDescription());
        viewHolder.txtPrice.setText("$ " + NumberFormat.getInstance().format(product.getPrice()));

        //Schedule the download of the product image.
        Intent imageDownloaderIntent = new Intent(convertView.getContext(), ImageDownloader.class);
        imageDownloaderIntent.putExtra(ImageDownloader.IN_URL, product.getImageUrl());
        convertView.getContext().startService(imageDownloaderIntent);

        //Register the receiver.
        IntentFilter filter = new IntentFilter(ImageDownloader.OUT_ACTION);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        ImageDownloadedReceiver receiver = new ImageDownloadedReceiver(convertView, this);
        convertView.getContext().registerReceiver(receiver, filter);

        return convertView;
    }

    public static class ImageDownloadedReceiver extends BroadcastReceiver {

        private View view;
        private ProductsAdapter productsAdapter;

        public ImageDownloadedReceiver(View view, ProductsAdapter productsAdapter) {
            this.view = view;
            this.productsAdapter = productsAdapter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Bitmap image = intent.getParcelableExtra(ImageDownloader.OUT_IMAGE);
            ((ProductListViewHolder) view.getTag()).imageView.setImageBitmap(image);
            productsAdapter.notifyDataSetChanged();
        }
    }
}
