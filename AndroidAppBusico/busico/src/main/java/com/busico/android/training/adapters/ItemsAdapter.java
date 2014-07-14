package com.busico.android.training.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.busico.android.training.R;
import com.busico.android.training.entities.Item;
import com.busico.android.training.tasks.SearchItemsTask;
import com.busico.android.training.utils.Closure;
import com.busico.android.training.utils.ContentDownloaderService;
import com.busico.android.training.utils.ContentListener;
import com.busico.android.training.utils.ImageRepository;
import com.busico.android.training.utils.MainThreadHandler;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;


public class ItemsAdapter extends BaseAdapter implements ContentListener {

    private static final String TAG = "ItemsAdapter";
    private final LayoutInflater layoutInflater;
    private final List<Item> items;
    private String queryString;
    private boolean loadingItems = false;

    public ItemsAdapter(LayoutInflater layoutInflater, List<Item> items) {
        this.layoutInflater = layoutInflater;
        this.items = items;
        MainThreadHandler.getInstance().registerContentListener(this);
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
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
        if (i >= getCount()) {
            return null;
        }
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

                ImageRepository.getInstance().getImage(getContentListenerId(), position, item.getImageUrl());

                //Schedule the download of the item image.
//                Intent contentDownloaderIntent = new Intent(convertView.getContext(), ContentDownloaderService.class);
//                contentDownloaderIntent.putExtra(ContentDownloaderService.URL, item.getImageUrl());
//                contentDownloaderIntent.putExtra(ContentDownloaderService.CONTENT_ID, item.getId());
//                contentDownloaderIntent.putExtra(ContentDownloaderService.INDEX, position);
//                convertView.getContext().startService(contentDownloaderIntent);
            }
        } else {
            //Set the image already downloaded.
            itemListViewHolder.imageView.setImageBitmap(item.getImage());
        }

        if (position > getCount() - 15) {
            loadMoreData();
        }

        return convertView;
    }

    @Override
    public int getContentListenerId() {
        return 90000;
    }

    @Override
    public void notifyContentAvailable(int position, Object content) {
        Item item = (Item) getItem(position);
        if (item != null) {
            item.setImage((Bitmap) content);
            notifyDataSetChanged();
        }
    }

    private void loadMoreData() {
        if (loadingItems) {
            return;
        }
        loadingItems = true;
        Log.d(TAG, "Load more items!!");

        SearchItemsTask.searchItems(queryString, 15, getCount(), new Closure<LinkedList<Item>>() {
            @Override
            public void executeOnSuccess(LinkedList<Item> result) {
                items.addAll(result);
                loadingItems = false;
                notifyDataSetChanged();
            }
        });
    }
}
