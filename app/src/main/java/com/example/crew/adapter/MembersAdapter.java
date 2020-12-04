package com.example.crew.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crew.R;
import com.example.crew.customClass.GroupMembersInfo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MembersViewHolder> {

    private ArrayList<GroupMembersInfo> memberList;
    private Context context;

    public MembersAdapter(@NonNull ArrayList<GroupMembersInfo> memberList, Context context) {
        this.memberList = memberList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull MembersViewHolder holder, int position) {
        holder.members_name.setText(memberList.get(position).getName());
        holder.members_position.setText(memberList.get(position).getPosition());
    }

    @NonNull
    @Override
    public MembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_members_model, parent, false);
        return new MembersViewHolder(v);
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

    @Override
    public int getItemCount() {
        // 삼항 연산자
        return memberList.size();
        //return (memberList != null ? memberList.size() : 0);
    }

    //검색 그룹 뷰홀더
    class MembersViewHolder extends RecyclerView.ViewHolder {

        TextView members_name;
        TextView members_position;

        public MembersViewHolder(@NonNull View itemView) {
            super(itemView);

            members_name = itemView.findViewById(R.id.tv_memberName);
            members_position = itemView.findViewById(R.id.tv_memberPosition);


            //리사이클러뷰 클릭을 위한 코드와 연동
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(v, pos, members_name.getText().toString()) ;
                        }
                    }
                }
            });

        }
    }
}
