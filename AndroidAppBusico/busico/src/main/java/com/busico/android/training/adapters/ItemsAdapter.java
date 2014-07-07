package com.busico.android.training.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.busico.android.training.R;
import com.busico.android.training.entities.Item;
import com.busico.android.training.utils.ContentDownloaderService;

import java.text.NumberFormat;
import java.util.List;


public class ItemsAdapter extends BaseAdapter {

    private final LayoutInflater layoutInflater;
    private final List<Item> items;

    public ItemsAdapter(LayoutInflater layoutInflater, List<Item> items) {
        this.layoutInflater = layoutInflater;
        this.items = items;
    }

    @Override
    public int getCount() {
        if (items == null) {
            return 0;
        } else {
            return items.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ItemListViewHolder itemListViewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listitem_product, viewGroup, false);
            itemListViewHolder = new ItemListViewHolder();
            itemListViewHolder.txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
            itemListViewHolder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
            itemListViewHolder.txtQuantity = (TextView) convertView.findViewById(R.id.txtQuantity);
            itemListViewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(itemListViewHolder);
        } else {
            itemListViewHolder = (ItemListViewHolder) convertView.getTag();
        }

        Item item = items.get(position);

        itemListViewHolder.txtDescription.setText(item.getDescription());

        if (itemListViewHolder.txtSubtitle != null) {
            itemListViewHolder.txtSubtitle.setText(item.getSubtitle());
        }

        if (item.getPrice() == null) {
            itemListViewHolder.txtPrice.setText(null);
        } else {
            itemListViewHolder.txtPrice.setText("$ " + NumberFormat.getInstance().format(item.getPrice()));
        }

        if (itemListViewHolder.txtQuantity != null) {
            if (item.getAvailableQuantity() == null) {
                itemListViewHolder.txtQuantity.setText(null);
            } else {
                String quantityStr = convertView.getContext().getString(R.string.quantity);
                itemListViewHolder.txtQuantity.setText(quantityStr + ": " + NumberFormat.getInstance().format(item.getAvailableQuantity()));
            }
        }

        if (item.getImage() == null) {
            itemListViewHolder.imageView.setImageDrawable(viewGroup.getResources().getDrawable(R.drawable.product_placeholder));

            if (!item.isDownloadingImage()) {
                item.setDownloadingImage(true);

                //Schedule the download of the item image.
                Intent contentDownloaderIntent = new Intent(convertView.getContext(), ContentDownloaderService.class);
                contentDownloaderIntent.putExtra(ContentDownloaderService.URL, item.getImageUrl());
                contentDownloaderIntent.putExtra(ContentDownloaderService.CONTENT_ID, item.getId());
                contentDownloaderIntent.putExtra(ContentDownloaderService.INDEX, position);
                convertView.getContext().startService(contentDownloaderIntent);
            }
        } else {
            //Set the image already downloaded.
            itemListViewHolder.imageView.setImageBitmap(item.getImage());
        }

        return convertView;
    }

}
