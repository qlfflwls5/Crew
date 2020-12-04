package com.example.crew.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crew.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchAdapter extends FirestoreRecyclerAdapter<SearchGroupsModel, SearchAdapter.GroupsViewHolder> {

    public SearchAdapter(@NonNull FirestoreRecyclerOptions<SearchGroupsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final GroupsViewHolder holder, int position, @NonNull final SearchGroupsModel model) {
        holder.group_name.setText(model.getName());
        holder.group_info.setText(model.getInfo());
    }

    @NonNull
    @Override
    public GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_group_model, parent, false);
        return new GroupsViewHolder(v);
    }


    //이하 리사이클러뷰 클릭을 위한 코드
    public interface OnItemClickListener {
        void onItemClick(View v, int position, String name) ;
    }

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    //검색 그룹 뷰홀더
    class GroupsViewHolder extends RecyclerView.ViewHolder {

        private TextView group_name;
        private TextView group_info;

        public GroupsViewHolder(@NonNull View itemView) {
            super(itemView);

            group_name = itemView.findViewById(R.id.group_name);
            group_info = itemView.findViewById(R.id.group_info);

            //리사이클러뷰 클릭을 위한 코드와 연동
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(v, pos, group_name.getText().toString()) ;
                        }
                    }
                }
            });

        }
    }
}
