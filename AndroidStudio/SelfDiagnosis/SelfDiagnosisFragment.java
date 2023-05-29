package com.example.myapplication.ui.selfdiagnosis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.databinding.FragmentSelfdiagnosisBinding;

public class SelfDiagnosisFragment extends Fragment {


private FragmentSelfdiagnosisBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        SelfDiagnosisViewModel selfDiagnosisViewModel =
                new ViewModelProvider(this).get(SelfDiagnosisViewModel.class);

    binding = FragmentSelfdiagnosisBinding.inflate(inflater, container, false);
    View root = binding.getRoot();


        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
