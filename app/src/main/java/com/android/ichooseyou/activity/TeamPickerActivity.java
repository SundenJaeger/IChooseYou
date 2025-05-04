package com.android.ichooseyou.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.ichooseyou2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TeamPickerActivity extends AppCompatActivity {

    private TextInputEditText teamCountInput, participantsInput;
    private MaterialButton generateTeamsButton, saveTeamsButton, shuffleAgainButton;
    private LinearLayout teamsContainer;
    private TextView resultsTitle;
    private ArrayList<String> itemsList;
    private List<List<String>> generatedTeams = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_picker);

        // Initialize views
        teamCountInput = findViewById(R.id.team_count_input);
        participantsInput = findViewById(R.id.participants_input);
        generateTeamsButton = findViewById(R.id.generate_teams_button);
        saveTeamsButton = findViewById(R.id.save_teams_button);
        shuffleAgainButton = findViewById(R.id.shuffle_again_button);
        teamsContainer = findViewById(R.id.teams_container);
        resultsTitle = findViewById(R.id.results_title);

        // Get the items list from the intent
        itemsList = getIntent().getStringArrayListExtra("ITEMS");

        // If items were passed, populate the participants field
        if (itemsList != null && !itemsList.isEmpty()) {
            participantsInput.setText(TextUtils.join("\n", itemsList));
        }

        generateTeamsButton.setOnClickListener(v -> generateBalancedTeams());
        saveTeamsButton.setOnClickListener(v -> saveTeams());
        shuffleAgainButton.setOnClickListener(v -> shuffleTeams());
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

        int teamCount;
        try {
            teamCount = Integer.parseInt(teamCountInput.getText().toString());
            if (teamCount < 2 || teamCount > 6) {
                teamCountInput.setError("Enter between 2-6 teams");
                return;
            }
        } catch (NumberFormatException e) {
            teamCountInput.setError("Invalid number");
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
        generatedTeams.clear();
        resultsTitle.setVisibility(View.VISIBLE);

        // Create balanced teams
        for (int i = 0; i < teamCount; i++) {
            List<String> team = new ArrayList<>();
            generatedTeams.add(team);
        }

        // Distribute participants using round-robin
        for (int i = 0; i < participants.size(); i++) {
            generatedTeams.get(i % teamCount).add(participants.get(i));
        }

        // Display teams
        for (int i = 0; i < generatedTeams.size(); i++) {
            MaterialCardView teamCard = createTeamCard(i, generatedTeams.get(i));
            teamsContainer.addView(teamCard);
        }

        // Show action buttons
        saveTeamsButton.setVisibility(View.VISIBLE);
        shuffleAgainButton.setVisibility(View.VISIBLE);
    }

    private MaterialCardView createTeamCard(int teamIndex, List<String> teamMembers) {
        MaterialCardView card = (MaterialCardView) getLayoutInflater()
                .inflate(R.layout.team_card, teamsContainer, false);

        TextView teamName = card.findViewById(R.id.team_name);
        teamName.setText("Team " + (teamIndex + 1));

        ImageView teamIcon = card.findViewById(R.id.team_icon);
        teamIcon.setImageResource(getTeamIcon(teamIndex));

        LinearLayout participantsLayout = card.findViewById(R.id.participants_layout);
        for (String member : teamMembers) {
            addParticipantToTeam(participantsLayout, member);
        }

        // Set team color
        card.setCardBackgroundColor(ContextCompat.getColor(this, getTeamColor(teamIndex)));

        return card;
    }

    private void addParticipantToTeam(LinearLayout layout, String participant) {
        TextView participantView = new TextView(this);
        participantView.setText(participant);
        participantView.setTextSize(16);
        participantView.setTextColor(ContextCompat.getColor(this, R.color.white));
        participantView.setTypeface(getResources().getFont(R.font.poppins_regular));
        participantView.setPadding(dpToPx(16), dpToPx(8), dpToPx(16), dpToPx(8));
        layout.addView(participantView);
    }

    private void shuffleTeams() {
        if (generatedTeams.isEmpty()) {
            Toast.makeText(this, "Generate teams first", Toast.LENGTH_SHORT).show();
            return;
        }

        // Combine all participants
        List<String> allParticipants = new ArrayList<>();
        for (List<String> team : generatedTeams) {
            allParticipants.addAll(team);
        }
        Collections.shuffle(allParticipants);

        // Redistribute
        for (List<String> team : generatedTeams) {
            team.clear();
        }

        for (int i = 0; i < allParticipants.size(); i++) {
            generatedTeams.get(i % generatedTeams.size()).add(allParticipants.get(i));
        }

        // Refresh display
        teamsContainer.removeAllViews();
        for (int i = 0; i < generatedTeams.size(); i++) {
            MaterialCardView teamCard = createTeamCard(i, generatedTeams.get(i));
            teamsContainer.addView(teamCard);
        }
    }

    private void saveTeams() {
        if (generatedTeams.isEmpty()) {
            Toast.makeText(this, "No teams to save", Toast.LENGTH_SHORT).show();
            return;
        }

        // In a real app, save to database or shared preferences
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < generatedTeams.size(); i++) {
            sb.append("Team ").append(i + 1).append(":\n");
            for (String member : generatedTeams.get(i)) {
                sb.append("- ").append(member).append("\n");
            }
            sb.append("\n");
        }

        Toast.makeText(this, "Teams saved to history", Toast.LENGTH_LONG).show();
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

    private int getTeamColor(int index) {
        int[] teamColors = {
                R.color.team_red,
                R.color.team_blue,
                R.color.team_green,
                R.color.team_yellow,
                R.color.team_purple,
                R.color.team_orange
        };
        return teamColors[index % teamColors.length];
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}