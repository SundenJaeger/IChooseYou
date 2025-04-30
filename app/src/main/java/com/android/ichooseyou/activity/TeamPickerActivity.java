package com.android.ichooseyou.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.ichooseyou2.R;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.widget.ImageView;

public class TeamPickerActivity extends AppCompatActivity {

    private EditText teamCountInput, participantsInput;
    private Button generateTeamsButton;
    private LinearLayout teamsContainer;
    private TextView resultsTitle;
    private ArrayList<String> itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_picker);

        teamCountInput = findViewById(R.id.team_count_input);
        participantsInput = findViewById(R.id.participants_input);
        generateTeamsButton = findViewById(R.id.generate_teams_button);
        teamsContainer = findViewById(R.id.teams_container);
        resultsTitle = findViewById(R.id.results_title);

        // Get the items list from the intent
        itemsList = getIntent().getStringArrayListExtra("ITEMS");

        // If items were passed, populate the participants field
        if (itemsList != null && !itemsList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String item : itemsList) {
                sb.append(item).append("\n");
            }
            participantsInput.setText(sb.toString().trim());
        }

        generateTeamsButton.setOnClickListener(v -> generateBalancedTeams());
    }

    private void generateBalancedTeams() {
        // Validate inputs
        if (TextUtils.isEmpty(teamCountInput.getText())) {
            teamCountInput.setError("Enter number of teams");
            return;
        }

        if (TextUtils.isEmpty(participantsInput.getText())) {
            participantsInput.setError("Enter participants");
            return;
        }

        int teamCount = Integer.parseInt(teamCountInput.getText().toString());
        if (teamCount < 2 || teamCount > 6) {
            teamCountInput.setError("Enter between 2-6 teams");
            return;
        }

        String[] participantsArray = participantsInput.getText().toString().split("\n");
        if (participantsArray.length < teamCount) {
            participantsInput.setError("Need at least " + teamCount + " participants");
            return;
        }

        // Convert to list and shuffle
        List<String> participants = new ArrayList<>();
        for (String participant : participantsArray) {
            if (!participant.trim().isEmpty()) {
                participants.add(participant.trim());
            }
        }
        Collections.shuffle(participants);

        // Clear previous teams
        teamsContainer.removeAllViews();
        resultsTitle.setVisibility(View.VISIBLE);

        // Create balanced teams
        for (int i = 0; i < teamCount; i++) {
            MaterialCardView teamCard = createTeamCard(i);
            LinearLayout participantsLayout = teamCard.findViewById(R.id.participants_layout);

            // Distribute participants using round-robin
            for (int j = i; j < participants.size(); j += teamCount) {
                addParticipantToTeam(participantsLayout, participants.get(j));
            }

            teamsContainer.addView(teamCard);
        }
    }

    private MaterialCardView createTeamCard(int teamIndex) {
        MaterialCardView card = (MaterialCardView) getLayoutInflater()
                .inflate(R.layout.team_card, teamsContainer, false);

        TextView teamName = card.findViewById(R.id.team_name);
        teamName.setText("Team " + (teamIndex + 1));

        ImageView teamIcon = card.findViewById(R.id.team_icon);
        teamIcon.setImageResource(getTeamIcon(teamIndex));

        return card;
    }

    private void addParticipantToTeam(LinearLayout layout, String participant) {
        TextView participantView = new TextView(this);
        participantView.setText(participant);
        participantView.setTextSize(16);
        participantView.setTextColor(getResources().getColor(R.color.text_dark));
        participantView.setTypeface(getResources().getFont(R.font.poppins_regular));
        participantView.setPadding(0, dpToPx(4), 0, dpToPx(4));
        layout.addView(participantView);
    }

    private int getTeamIcon(int index) {
        int[] teamIcons = {
                R.drawable.ic_team_red,
                R.drawable.ic_team_blue,
                R.drawable.ic_team_green,
                R.drawable.ic_team_yellow,
                R.drawable.ic_team_purple,
                R.drawable.ic_team_orange
        };
        return teamIcons[index % teamIcons.length];
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}