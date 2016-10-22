package jp.tsur.twitwear;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.WearableRecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.tsur.twitwear.databinding.ListItemStatusBinding;
import twitter4j.Status;
import twitter4j.User;


public class StatusAdapter extends WearableRecyclerView.Adapter<StatusAdapter.ViewHolder> {

    private List<Status> items = new ArrayList<>();

    public StatusAdapter(List<Status> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_status, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListItemStatusBinding binding = holder.getBinding();
        Status status = items.get(position);
        User user = status.getUser();
        binding.name.setText(user.getName());
        binding.screenName.setText("@" + user.getScreenName());
        binding.text.setText(status.getText());

        Picasso.with(binding.icon.getContext()).load(user.getProfileImageURL()).into(binding.icon);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ListItemStatusBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }

        ListItemStatusBinding getBinding() {
            return binding;
        }
    }
}
