package com.qiren.geoapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.qiren.geoapp.databinding.FragmentHomeBinding;
import com.qiren.geoapp.tensorflow.MainActivity;
import com.qiren.geoapp.tensorflow.activities.LoginActivity;
import com.qiren.geoapp.tensorflow.services.UserService;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tensorflowButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            if (!UserService.INSTANCE.checkLogin()) {
                intent.setClass(getContext(), LoginActivity.class);
                getActivity().startActivity(intent);
                return;
            }
            intent.setClass(getContext(), MainActivity.class);
            getActivity().startActivity(intent);
        });
        binding.pytorchButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            if (!UserService.INSTANCE.checkLogin()) {
                intent.setClass(getContext(), LoginActivity.class);
                getActivity().startActivity(intent);
                return;
            }
            intent.setClass(getContext(), com.qiren.geoapp.pytorch.MainActivity.class);
            getActivity().startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}