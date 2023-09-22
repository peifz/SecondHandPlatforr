package com.example.secondhandplatforms;
import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MessageFragment extends Fragment {

    private TextView textPublished;
    private TextView textSaved;
    private TextView textNew;
    private TextView textSale;
    public MessageFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        textPublished = view.findViewById(R.id.textPublished);
        textSaved = view.findViewById(R.id.textSaved);
        textNew = view.findViewById(R.id.textNew);
        loadFragment(new MyMerchantFragment());
        textPublished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load the PublishedFragment when "已发布" is clicked
                loadFragment(new MyMerchantFragment());
            }
        });
        return view;
    }

    private void loadFragment(Fragment fragment) {
        // Replace the current fragment with the selected fragment
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment); // Use the ID of your FrameLayout as the container
        transaction.addToBackStack(null); // Add to back stack for navigation
        transaction.commit();
    }
}