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
import com.example.crew.customClass.Notice;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {

    private ArrayList<Notice> noticeList;
    private Context context;

    public NoticeAdapter(@NonNull ArrayList<Notice> noticeList, Context context) {
        this.noticeList = noticeList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        holder.tv_subject.setText(noticeList.get(position).getSubject());
        holder.tv_date.setText((CharSequence) noticeList.get(position).getDate());
        holder.tv_content.setText(noticeList.get(position).getContent());
        holder.tv_reply.setText(String.valueOf(noticeList.get(position).getReplyCount()));
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.frag_notice_model, parent, false);
        return new NoticeViewHolder(v);
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
        return noticeList.size();
        //return (memberList != null ? memberList.size() : 0);
    }

    //검색 그룹 뷰홀더
    class NoticeViewHolder extends RecyclerView.ViewHolder {

        TextView tv_subject;
        TextView tv_date;
        TextView tv_content;
        TextView tv_reply;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_subject = itemView.findViewById(R.id.tv_subject);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_reply = itemView.findViewById(R.id.tv_reply);


            //리사이클러뷰 클릭을 위한 코드와 연동
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(v, pos, tv_subject.getText().toString()) ;
                        }
                    }
                }
            });

        }
    }
}
