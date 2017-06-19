package com.fyales.android.demo.rx;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.fyales.android.demo.R;
import com.fyales.android.demo.cache.CacheManager;
import com.fyales.android.demo.cache.NetworkCache;
import com.fyales.android.demo.model.Girl;
import com.fyales.android.demo.model.GirlModel;
import com.fyales.android.demo.model.Image;
import com.fyales.android.demo.net.ApiClient;
import com.fyales.android.demo.net.GankService;
import com.fyales.android.demo.rx.adapter.ImageAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxCacheActivity extends AppCompatActivity {

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private int mPageNum;
    private ImageAdapter mAdapter;
    private GankService mGankService;
    private Subscription mSubscription;


    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView,
                                         int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == recyclerView.getAdapter().getItemCount()) {
                //加载数据
                loadData(mPageNum);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            int[] lastPositions = layoutManager.findLastVisibleItemPositions(null);
            lastVisibleItem = getMaxPosition(lastPositions);
        }

        /**
         * 获得最大的位置
         * @param positions
         * @return
         * */
        private int getMaxPosition(int[] positions) {
            int size = positions.length;
            int maxPosition = Integer.MIN_VALUE;
            for (int i = 0; i < size; i++) {
                maxPosition = Math.max(maxPosition, positions[i]);
            }
            return maxPosition;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache);
        ButterKnife.bind(this);

        Retrofit retrofit = ApiClient.getInstance().getRetrofit("http://gank.io/api/");
        mGankService = retrofit.create(GankService.class);

        initViews();
    }


    private void initViews() {
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNum = 0;
                loadData(mPageNum);
            }
        });

        mAdapter = new ImageAdapter(this);
        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerview.setAdapter(mAdapter);
        recyclerview.addOnScrollListener(mScrollListener);
        mPageNum = 0;
        loadData(mPageNum);

    }

    /**
     * 加载数据
     *
     * @param pageNum 页面
     */
    private void loadData(final int pageNum) {

        final String url = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/15/" + pageNum;

        NetworkCache<GirlModel> networkCache = new NetworkCache<GirlModel>() {
            @Override
            public Observable<GirlModel> get(String key, Class<GirlModel> cls) {
                return mGankService
                        .getFuli(15, pageNum)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };

        Observable<GirlModel> observable = CacheManager.getInstance(this).load(url, GirlModel.class, networkCache);

        mSubscription = observable.map(new Func1<GirlModel, List<Image>>() {

            @Override
            public List<Image> call(GirlModel fuliBean) {

                List<Girl> girls = fuliBean.results;
                List<Image> imageList = new ArrayList<>(girls.size());
                for (Girl girl : girls) {
                    Image image = new Image();
                    image.desc = girl.desc;
                    image.url = girl.url;
                    imageList.add(image);
                }
                return imageList;

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Image>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("rx", e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(List<Image> imageList) {
                        swiperefresh.setRefreshing(false);
                        if (pageNum == 0){
                            mAdapter.setData(imageList);
                        } else{
                            mAdapter.addData(imageList);
                        }
                        mPageNum++;
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null && mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
