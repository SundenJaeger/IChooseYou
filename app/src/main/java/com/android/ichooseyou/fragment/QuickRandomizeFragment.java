package com.android.ichooseyou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.ichooseyou.activity.CoinTossActivity;
import com.android.ichooseyou.activity.DiceRollActivity;
import com.android.ichooseyou.activity.ListPickerActivity;
import com.android.ichooseyou.activity.NumberGeneratorActivity;
import com.android.ichooseyou.activity.TeamPickerActivity;
import com.android.ichooseyou.activity.WheelOfNamesActivity;
import com.android.ichooseyou.model.User;
import com.android.ichooseyou2.R;
import com.google.android.material.button.MaterialButton;

public class QuickRandomizeFragment extends Fragment {

    private User user;

    public QuickRandomizeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable("USER_DATA");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment - use the same layout as FeatureListActivity
        return inflater.inflate(R.layout.activity_features_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize all buttons and set click listeners - same as in FeatureListActivity
        MaterialButton coinTossButton = view.findViewById(R.id.coin_toss_button);
        coinTossButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), CoinTossActivity.class));
        });

        MaterialButton diceRollButton = view.findViewById(R.id.dice_roll_button);
        diceRollButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), DiceRollActivity.class));
        });

        MaterialButton numberGeneratorButton = view.findViewById(R.id.number_generator_button);
        numberGeneratorButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), NumberGeneratorActivity.class));
        });

        MaterialButton listPickerButton = view.findViewById(R.id.list_picker_button);
        listPickerButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ListPickerActivity.class));
        });

        MaterialButton teamPickerButton = view.findViewById(R.id.team_picker_button);
        teamPickerButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), TeamPickerActivity.class));
        });

        MaterialButton wheelOfNamesButton = view.findViewById(R.id.wheel_of_names_button);
        wheelOfNamesButton.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), WheelOfNamesActivity.class));
        });
    }
}