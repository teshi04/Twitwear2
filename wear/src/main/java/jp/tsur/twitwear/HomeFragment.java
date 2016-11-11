package jp.tsur.twitwear;


import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.tsur.twitwear.databinding.FragmentTimelineBinding;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import twitter4j.ResponseList;
import twitter4j.Status;


public class HomeFragment extends Fragment {

    private FragmentTimelineBinding binding;
    private StatusAdapter adapter;
    private Subscription subscription = Subscriptions.empty();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = DataBindingUtil.bind(getView());

        assert binding != null;
        binding.list.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.list.hasFixedSize();
        binding.list.addItemDecoration(new DividerDecoration(getActivity()));
        binding.progress.setColors(new int[]{ContextCompat.getColor(getContext(), R.color.progress)});

        adapter = new StatusAdapter() {
            @Override
            protected void onItemClick(@NonNull View view, @NonNull Status status) {
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(), view, "icon");
                startActivity(StatusActivity.createIntent(getActivity(), status), options.toBundle());
            }
        };
        binding.list.setAdapter(adapter);

        getTimeLine();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    public void refresh() {
        binding.progress.showWithAnimation();
        adapter.clear();
        getTimeLine();
    }

    private void getTimeLine() {
        subscription = Twitter.getHomeTimeline(getActivity())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ResponseList<Status>>() {
                    @Override
                    public void onSuccess(ResponseList<Status> statuses) {
                        binding.progress.hide();
                        adapter.addAll(statuses);
                    }

                    @Override
                    public void onError(Throwable e) {
                        binding.progress.hide();
                        e.printStackTrace();
                    }
                });
    }
}
