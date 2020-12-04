package com.example.crew.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crew.R;
import com.example.crew.adapter.MembersAdapter;
import com.example.crew.adapter.NoticeAdapter;
import com.example.crew.customClass.GroupMembersInfo;
import com.example.crew.customClass.Notice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class fragNotice extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Notice> noticelist;

    private RecyclerView rv_notice;
    private String group_name;
    private NoticeAdapter adapter;

    private View view;
    //ctrl + O 누른 후, onCreateView 검색. View는 onCreateView로

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_notice, container, false);

        rv_notice = view.findViewById(R.id.rv_notice);
        rv_notice.setHasFixedSize(true);
        rv_notice.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new NoticeAdapter(noticelist, getContext());
        rv_notice.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            group_name = this.getArguments().getString("group_name");
        }
        CollectionReference collectionReference = db.collection("groups").document(group_name)
                .collection("notice");

        noticelist = new ArrayList<>();

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Notice notice = new Notice(document.getString("subject"), document.getString("content"),
                                document.getString("date"), document.getLong("replyCount").intValue());
                        noticelist.add(notice);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
