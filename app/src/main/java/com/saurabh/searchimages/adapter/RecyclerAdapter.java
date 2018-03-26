package com.saurabh.searchimages.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.saurabh.searchimages.R;
import com.saurabh.searchimages.listener.OnLoadMoreListener;
import com.saurabh.searchimages.model.ResultsResponse;
import com.saurabh.searchimages.model.UrlResponse;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by kiris on 3/24/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater inflater;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener mOnLoadMoreListener;
    private ArrayList<ResultsResponse> resultsResponses;
    private Context context;

    public RecyclerAdapter(Context context,ArrayList<ResultsResponse> resultList,RecyclerView recyclerView){
        this.context=context;
        inflater = LayoutInflater.from(context);
        resultsResponses=resultList;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            final LinearLayoutManager linearLayoutManager= (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount=linearLayoutManager.getItemCount();
                    lastVisibleItem=linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading&&totalItemCount<=(lastVisibleItem+visibleThreshold)){
                        // End has been reached
                        // Do something
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }

    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return resultsResponses.get(position)==null?VIEW_TYPE_LOADING:VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==VIEW_TYPE_ITEM){
            View view = inflater.inflate(R.layout.list_item, parent, false);
            return new MyViewHolder(view);
        }else if(viewType==VIEW_TYPE_LOADING){
            View view=inflater.inflate(R.layout.loading_item,parent,false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder){
            UrlResponse urlResponse=resultsResponses.get(position).getUrls();
            MyViewHolder myViewHolder= (MyViewHolder) holder;
            Picasso.with(context).load(urlResponse.getRegular()).memoryPolicy(MemoryPolicy.NO_CACHE).into(myViewHolder.imageView);
        }else if (holder instanceof LoadingViewHolder){
            LoadingViewHolder loadingViewHolder= (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return resultsResponses==null?0:resultsResponses.size();
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar1);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
        }
    }
}
