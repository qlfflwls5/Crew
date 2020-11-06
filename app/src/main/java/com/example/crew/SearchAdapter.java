package com.example.crew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchAdapter extends FirestoreRecyclerAdapter<SearchGroupsModel, SearchAdapter.GroupsViewHolder> {

    public SearchAdapter(@NonNull FirestoreRecyclerOptions<SearchGroupsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GroupsViewHolder holder, int position, @NonNull SearchGroupsModel model) {
        holder.group_name.setText(model.getName());
        holder.group_info.setText(model.getInfo());
    }

    @NonNull
    @Override
    public GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_group_model, parent, false);
        return new GroupsViewHolder(v);
    }

    //검색 그룹 뷰홀더
    class GroupsViewHolder extends RecyclerView.ViewHolder {

        private TextView group_name;
        private TextView group_info;

        public GroupsViewHolder(@NonNull View itemView) {
            super(itemView);

            group_name = itemView.findViewById(R.id.group_name);
            group_info = itemView.findViewById(R.id.group_info);

        }
    }

}
