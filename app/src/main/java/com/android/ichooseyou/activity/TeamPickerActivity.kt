package com.android.ichooseyou.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.android.ichooseyou2.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import java.util.Collections

class TeamPickerActivity : AppCompatActivity() {
    private lateinit var teamCountInput: TextInputEditText
    private lateinit var participantsInput: TextInputEditText
    private lateinit var generateTeamsButton: MaterialButton
    private lateinit var saveTeamsButton: MaterialButton
    private lateinit var shuffleAgainButton: MaterialButton
    private lateinit var teamsContainer: LinearLayout
    private lateinit var resultsTitle: TextView
    private var itemsList: ArrayList<String> = ArrayList()
    private val generatedTeams: MutableList<MutableList<String>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_picker)

        // Initialize views
        teamCountInput = findViewById(R.id.team_count_input)
        participantsInput = findViewById(R.id.participants_input)
        generateTeamsButton = findViewById(R.id.generate_teams_button)
        saveTeamsButton = findViewById(R.id.save_teams_button)
        shuffleAgainButton = findViewById(R.id.shuffle_again_button)
        teamsContainer = findViewById(R.id.teams_container)
        resultsTitle = findViewById(R.id.results_title)

        // Get the items list from the intent - Fixed type mismatch
        val intentItems = intent.getStringArrayListExtra("ITEMS")
        if (intentItems != null) {
            itemsList = ArrayList(intentItems)
        }

        // If items were passed, populate the participants field
        if (itemsList.isNotEmpty()) {
            participantsInput.setText(TextUtils.join("\n", itemsList))
        }

        generateTeamsButton.setOnClickListener { generateBalancedTeams() }
        saveTeamsButton.setOnClickListener { saveTeams() }
        shuffleAgainButton.setOnClickListener { shuffleTeams() }
    }

    private fun generateBalancedTeams() {
        // Validate inputs
        if (TextUtils.isEmpty(teamCountInput.text)) {
            teamCountInput.error = "Enter number of teams"
            return
        }
        if (TextUtils.isEmpty(participantsInput.text)) {
            participantsInput.error = "Enter participants"
            return
        }

        val teamCount: Int
        try {
            teamCount = teamCountInput.text.toString().toInt()
            if (teamCount < 2 || teamCount > 6) {
                teamCountInput.error = "Enter between 2-6 teams"
                return
            }
        } catch (e: NumberFormatException) {
            teamCountInput.error = "Invalid number"
            return
        }

        val participantsArray = participantsInput.text.toString().split("\n".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()

        if (participantsArray.size < teamCount) {
            participantsInput.error = "Need at least $teamCount participants"
            return
        }

        // Convert to list and shuffle
        val participants: MutableList<String> = ArrayList()
        for (participant in participantsArray) {
            if (participant.trim().isNotEmpty()) {
                participants.add(participant.trim())
            }
        }
        participants.shuffle()

        // Clear previous teams
        teamsContainer.removeAllViews()
        generatedTeams.clear()
        resultsTitle.visibility = View.VISIBLE

        // Create balanced teams
        for (i in 0 until teamCount) {
            val team: MutableList<String> = ArrayList()
            generatedTeams.add(team)
        }

        // Distribute participants using round-robin
        for (i in participants.indices) {
            generatedTeams[i % teamCount].add(participants[i])
        }

        // Display teams
        for (i in generatedTeams.indices) {
            val teamCard = createTeamCard(i, generatedTeams[i])
            teamsContainer.addView(teamCard)
        }

        // Show action buttons
        saveTeamsButton.visibility = View.VISIBLE
        shuffleAgainButton.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun createTeamCard(teamIndex: Int, teamMembers: List<String>): MaterialCardView {
        val card = layoutInflater
            .inflate(R.layout.team_card, teamsContainer, false) as MaterialCardView
        val teamName = card.findViewById<TextView>(R.id.team_name)
        teamName.text = "Team " + (teamIndex + 1)
        val teamIcon = card.findViewById<ImageView>(R.id.team_icon)
        teamIcon.setImageResource(getTeamIcon(teamIndex))
        val participantsLayout = card.findViewById<LinearLayout>(R.id.participants_layout)
        for (member in teamMembers) {
            addParticipantToTeam(participantsLayout, member)
        }

        // Set team color
        card.setCardBackgroundColor(ContextCompat.getColor(this, getTeamColor(teamIndex)))
        return card
    }

    private fun addParticipantToTeam(layout: LinearLayout, participant: String) {
        val participantView = TextView(this)
        participantView.text = participant
        participantView.textSize = 16f
        participantView.setTextColor(ContextCompat.getColor(this, R.color.white))

        // Fixed API level issue by using ResourcesCompat instead of direct getFont
        ResourcesCompat.getFont(this, R.font.poppins_regular)?.let {
            participantView.typeface = it
        }

        participantView.setPadding(dpToPx(16), dpToPx(8), dpToPx(16), dpToPx(8))
        layout.addView(participantView)
    }

    private fun shuffleTeams() {
        if (generatedTeams.isEmpty()) {
            Toast.makeText(this, "Generate teams first", Toast.LENGTH_SHORT).show()
            return
        }

        // Combine all participants
        val allParticipants: MutableList<String> = ArrayList()
        for (team in generatedTeams) {
            allParticipants.addAll(team)
        }
        allParticipants.shuffle()

        // Redistribute
        for (team in generatedTeams) {
            team.clear()
        }
        for (i in allParticipants.indices) {
            generatedTeams[i % generatedTeams.size].add(allParticipants[i])
        }

        // Refresh display
        teamsContainer.removeAllViews()
        for (i in generatedTeams.indices) {
            val teamCard = createTeamCard(i, generatedTeams[i])
            teamsContainer.addView(teamCard)
        }
    }

    private fun saveTeams() {
        if (generatedTeams.isEmpty()) {
            Toast.makeText(this, "No teams to save", Toast.LENGTH_SHORT).show()
            return
        }

        // In a real app, save to database or shared preferences
        val sb = StringBuilder()
        for (i in generatedTeams.indices) {
            sb.append("Team ").append(i + 1).append(":\n")
            for (member in generatedTeams[i]) {
                sb.append("- ").append(member).append("\n")
            }
            sb.append("\n")
        }
        Toast.makeText(this, "Teams saved to history", Toast.LENGTH_LONG).show()
    }

    private fun getTeamIcon(index: Int): Int {
        val teamIcons = intArrayOf(
            R.drawable.ic_team_red,
            R.drawable.ic_team_blue,
            R.drawable.ic_team_green,
            R.drawable.ic_team_yellow,
            R.drawable.ic_team_purple,
            R.drawable.ic_team_orange
        )
        return teamIcons[index % teamIcons.size]
    }

    private fun getTeamColor(index: Int): Int {
        val teamColors = intArrayOf(
            R.color.team_red,
            R.color.team_blue,
            R.color.team_green,
            R.color.team_yellow,
            R.color.team_purple,
            R.color.team_orange
        )
        return teamColors[index % teamColors.size]
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}