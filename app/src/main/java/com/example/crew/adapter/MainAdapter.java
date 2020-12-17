package com.example.crew.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.crew.R;
import com.example.crew.customClass.GroupInfo;
import com.example.crew.customClass.GroupMembersInfo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class MainAdapter extends FirestoreRecyclerAdapter<SearchGroupsModel, MainAdapter.GroupsViewHolder> {

    public MainAdapter(@NonNull FirestoreRecyclerOptions<SearchGroupsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GroupsViewHolder holder, int position, @NonNull SearchGroupsModel model) {

        holder.group_name.setText(model.getName());
        holder.group_info.setText(model.getInfo());
        Glide.with(holder.iv_group_profile.getContext())
            .load(model.getProfileUrl())
            //.apply(new RequestOptions().circleCrop())
            .into(holder.iv_group_profile);
    }

    @NonNull
    @Override
    public GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_group_model, parent, false);
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

        TextView group_name;
        TextView group_info;
        ImageView iv_group_profile;

        public GroupsViewHolder(@NonNull View itemView) {
            super(itemView);

            group_name = itemView.findViewById(R.id.group_name);
            group_info = itemView.findViewById(R.id.group_info);
            iv_group_profile = itemView.findViewById(R.id.iv_group_profile);

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
