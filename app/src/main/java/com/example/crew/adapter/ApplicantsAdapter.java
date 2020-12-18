package com.example.crew.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crew.R;
import com.example.crew.customClass.GroupApplicantsInfo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ApplicantsAdapter extends FirestoreRecyclerAdapter<GroupApplicantsInfo, ApplicantsAdapter.GroupsViewHolder> {

    public ApplicantsAdapter(@NonNull FirestoreRecyclerOptions<GroupApplicantsInfo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final GroupsViewHolder holder, int position, @NonNull final GroupApplicantsInfo model) {
        holder.applicants_name.setText(model.getName());
    }

    @NonNull
    @Override
    public GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_members_applicants, parent, false);
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

        private TextView applicants_name;

        public GroupsViewHolder(@NonNull View itemView) {
            super(itemView);

            applicants_name = itemView.findViewById(R.id.applicants_name);

            //리사이클러뷰 클릭을 위한 코드와 연동
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(v, pos, applicants_name.getText().toString()) ;
                        }
                    }
                }
            });

        }
    }
}
