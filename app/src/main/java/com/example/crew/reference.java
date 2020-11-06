package com.example.crew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class reference {
    /*
    //쿼리
    Query query = firebaseFirestore.collection("groups");
    //리사이클러 옵션
    FirestoreRecyclerOptions<SearchGroupsModel> options = new FirestoreRecyclerOptions.Builder<SearchGroupsModel>()
            .setQuery(query, SearchGroupsModel.class)
            .build();

    adapter = new FirestoreRecyclerAdapter<SearchGroupsModel, SearchActivity.GroupsViewHolder>(options) {
        @NonNull
        @Override
        public SearchActivity.GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_group_model, parent, false);
            return new SearchActivity.GroupsViewHolder(view);
        }

        @Override
        protected void onBindViewHolder(@NonNull GroupsViewHolder holder, int position, @NonNull SearchGroupsModel model) {
            holder.group_name.setText((model.getName()));
            holder.group_info.setText((model.getInfo()));
        }
    };

        rv_search.setHasFixedSize(true);
        rv_search.setLayoutManager(new LinearLayoutManager(this));
        rv_search.setAdapter(adapter);


}

//검색 그룹 뷰홀더
private class GroupsViewHolder extends RecyclerView.ViewHolder {

    View groupView;

    public GroupsViewHolder(@NonNull View itemView) {
        super(itemView);

        groupView = itemView;

    }

}

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

class rv_searchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }*/
}

