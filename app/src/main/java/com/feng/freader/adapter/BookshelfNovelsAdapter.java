package com.feng.freader.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feng.freader.R;
import com.feng.freader.entity.data.BookshelfNovelDbData;
import com.feng.freader.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/10/28
 */
public class BookshelfNovelsAdapter extends RecyclerView.Adapter {
    private static final String TAG = "BookshelfNovelsAdapter";

    private Context mContext;
    private List<BookshelfNovelDbData> mDataList;

    private BookshelfNovelListener mListener;

    public interface BookshelfNovelListener {
        void clickItem(int position);
    }

    public void setBookshelfNovelListener(BookshelfNovelListener mListener) {
        this.mListener = mListener;
    }

    public BookshelfNovelsAdapter(Context mContext, List<BookshelfNovelDbData> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ContentViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.item_bookshelf_novel, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        ContentViewHolder contentViewHolder = (ContentViewHolder) viewHolder;

        contentViewHolder.name.setText(mDataList.get(i).getName());

        if (mDataList.get(i).getType() == 0) {  // 网络小说
            Glide.with(mContext)
                    .load(mDataList.get(i).getCover())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.cover_place_holder)
                            .error(R.drawable.cover_error))
                    .into(contentViewHolder.cover);
        } else if (mDataList.get(i).getType() == 1){    // 本地 txt 小说
            contentViewHolder.cover.setImageResource(R.drawable.cover_error);
        } else if (mDataList.get(i).getType() == 2) {   // 本地 epub 小说
            if (mDataList.get(i).getCover().equals("")) {
                contentViewHolder.cover.setImageResource(R.drawable.cover_error);
            } else {
                String coverPath = mDataList.get(i).getCover();
//                Log.d(TAG, "onBindViewHolder: coverPath = " + coverPath);
                Bitmap bitmap = FileUtil.loadLocalPicture(coverPath);
                if (bitmap != null) {
                    contentViewHolder.cover.setImageBitmap(bitmap);
                } else {
                    contentViewHolder.cover.setImageResource(R.drawable.cover_error);
                }
            }
        }

        contentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.clickItem(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {

        ImageView cover;
        TextView name;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.iv_item_bookshelf_novel_cover);
            name = itemView.findViewById(R.id.tv_item_bookshelf_novel_name);
        }
    }
}
