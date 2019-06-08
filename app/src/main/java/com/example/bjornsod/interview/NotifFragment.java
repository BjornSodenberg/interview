package com.example.bjornsod.interview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class NotifFragment extends Fragment {

    List<Notif_post> lstNotif;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notif, container, false);

        lstNotif = new ArrayList<>();
        lstNotif.add(new Notif_post("notification message", "Tom", R.raw.tom));
        lstNotif.add(new Notif_post("notification message", "Jessie", R.raw.tom));
        lstNotif.add(new Notif_post("notification message", "Adam", R.raw.tom));
        lstNotif.add(new Notif_post("notification message", "John", R.raw.tom));
        lstNotif.add(new Notif_post("notification message", "Tom", R.raw.tom));



        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_id);
        RecyclerViewAdapter_notiff recyclerViewAdapter = new RecyclerViewAdapter_notiff(getActivity(),lstNotif);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        recyclerView.setAdapter(recyclerViewAdapter);

        return view;
    }
}
