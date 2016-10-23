package jp.tsur.twitwear;


import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.tsur.twitwear.databinding.FragmentTimelineBinding;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;
import twitter4j.ResponseList;
import twitter4j.Status;


public class HomeFragment extends Fragment {

    private FragmentTimelineBinding binding;
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

        getTimeLine();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }

    private void getTimeLine() {
        subscription = Twitter.getHomeTimeline(getActivity())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseList<Status>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        binding.progress.setVisibility(View.GONE);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResponseList<Status> statuses) {
                        binding.progress.setVisibility(View.GONE);
                        binding.list.setVisibility(View.VISIBLE);
                        StatusAdapter adapter = new StatusAdapter(statuses);
                        binding.list.setAdapter(adapter);
                    }
                });
    }
}
