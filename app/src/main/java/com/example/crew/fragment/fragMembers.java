package com.example.crew.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crew.R;
import com.example.crew.adapter.MembersAdapter;
import com.example.crew.customClass.GroupMembersInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class fragMembers extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<GroupMembersInfo> memberList;

    private RecyclerView rv_members;
    private String group_name;
    private MembersAdapter adapter;

    private View view;
    //ctrl + O 누른 후, onCreateView 검색. View는 onCreateView로

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_members, container, false);

        TextView tv_memberFrag = view.findViewById(R.id.tv_memberFrag);
        tv_memberFrag.setText(group_name);

        rv_members = view.findViewById(R.id.rv_members);
        rv_members.setHasFixedSize(true);
        rv_members.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MembersAdapter(memberList, getContext());
        rv_members.setAdapter(adapter);

        ImageButton ib_goMain_member = view.findViewById(R.id.ib_goMain_member);
        ib_goMain_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            group_name = this.getArguments().getString("group_name");
        }
        CollectionReference collectionReference = db.collection("groups").document(group_name)
                .collection("members");

        memberList = new ArrayList<>();



        collectionReference.orderBy("positionIndex").orderBy("name").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        GroupMembersInfo groupMembersInfo = new GroupMembersInfo(document.getString("name"),
                                document.getString("position"), document.getLong("positionIndex").intValue());
                        memberList.add(groupMembersInfo);

                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
