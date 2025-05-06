package com.android.ichooseyou.activity

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.ichooseyou.activity.WheelOfNamesActivity.NamesAdapter.NameViewHolder
import com.android.ichooseyou.model.WheelView
import com.android.ichooseyou2.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Random

class WheelOfNamesActivity : AppCompatActivity() {
    private lateinit var nameInput: TextInputEditText
    private lateinit var nameInputLayout: TextInputLayout
    private lateinit var addNameButton: MaterialButton
    private lateinit var spinButton: MaterialButton
    private lateinit var clearNamesButton: MaterialButton
    private lateinit var wheelView: WheelView
    private lateinit var namesCountText: TextView
    private lateinit var namesRecyclerView: RecyclerView
    private lateinit var namesAdapter: NamesAdapter
    private val names = ArrayList<String>()
    private val random = Random()
    private var isSpinning = false
    private lateinit var wheelAnimator: ValueAnimator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wheel_of_names)

        // Initialize views
        nameInput = findViewById(R.id.name_input)
        nameInputLayout = findViewById(R.id.name_input_layout)
        addNameButton = findViewById(R.id.add_name_button)
        spinButton = findViewById(R.id.spin_button)
        clearNamesButton = findViewById(R.id.clear_names_button)
        wheelView = findViewById(R.id.wheel_view)
        namesCountText = findViewById(R.id.names_count_text)
        namesRecyclerView = findViewById(R.id.names_recycler_view)

        // Set up RecyclerView
        namesAdapter = NamesAdapter()
        namesRecyclerView.layoutManager = LinearLayoutManager(this)
        namesRecyclerView.adapter = namesAdapter
        namesRecyclerView.setHasFixedSize(true)

        // Set up toolbar navigation
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        // Check if items were passed from SectionDetailActivity
        val passedItems = intent.getStringArrayListExtra("ITEMS")
        if (!passedItems.isNullOrEmpty()) {
            names.addAll(passedItems)
            updateWheelAndCount()
        }

        // Set up input field behavior
        nameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                addNameButton.setEnabled(s.isNotEmpty())
            }

            override fun afterTextChanged(s: Editable) {}
        })
        nameInput.setOnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addName()
                return@setOnEditorActionListener true
            }
            false
        }

        // Set up button click listeners
        addNameButton.setOnClickListener { addName() }
        spinButton.setOnClickListener { spinWheel() }
        clearNamesButton.setOnClickListener {
            if (names.isEmpty()) {
                Toast.makeText(this, "The list is already empty", Toast.LENGTH_SHORT).show()
            } else {
                names.clear()
                updateWheelAndCount()
                Toast.makeText(this, "All names removed", Toast.LENGTH_SHORT).show()
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        // Update initial state
        updateWheelAndCount()
        addNameButton.setEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addName() {
        val input = nameInput.getText().toString().trim()
        if (input.isNotEmpty()) {
            // Split by new lines and filter out empty lines
            val lines =
                input.split("\\r?\\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var addedAny = false
            for (line in lines) {
                val name = line.trim()
                if (name.isNotEmpty()) {
                    names.add(name)
                    addedAny = true
                }
            }
            if (addedAny) {
                nameInput.setText("")
                updateWheelAndCount()
                nameInputLayout.requestFocus()
                namesAdapter.notifyDataSetChanged()
                namesRecyclerView.smoothScrollToPosition(names.size - 1)
            } else {
                nameInputLayout.error = "Please enter valid names"
            }
        } else {
            nameInputLayout.error = "Please enter names"
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateWheelAndCount() {
        wheelView.setItems(names)
        namesCountText.text = resources.getQuantityString(
            R.plurals.names_count, names.size, names.size
        )
        spinButton.setEnabled(names.size > 1)
        namesAdapter.notifyDataSetChanged()
    }

    private fun spinWheel() {
        if (names.size < 2) {
            Toast.makeText(this, "Please add at least 2 names", Toast.LENGTH_SHORT).show()
            return
        }
        if (isSpinning) return
        isSpinning = true
        val selectedIndex = random.nextInt(names.size)
        val anglePerSegment = 360f / names.size
        val segmentCenterAngle = selectedIndex * anglePerSegment + anglePerSegment / 2
        val arrowPositionAngle = 270f
        val rotations = 7f
        val finalAngle = rotations * 360f + (arrowPositionAngle - segmentCenterAngle)
        wheelAnimator = ValueAnimator.ofFloat(0f, finalAngle)
        wheelAnimator.setDuration(6000)
        wheelAnimator.setInterpolator { input: Float ->
            if (input < 0.8f) return@setInterpolator input
            val t = (input - 0.8f) / 0.2f
            0.8f + (1 - (1 - t) * (1 - t)) * 0.2f
        }
        wheelAnimator.addUpdateListener { animation: ValueAnimator ->
            wheelView.setRotationAngle(
                animation.getAnimatedValue() as Float
            )
        }
        wheelAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                spinButton.setEnabled(false)
            }

            override fun onAnimationEnd(animation: Animator) {
                isSpinning = false
                spinButton.setEnabled(names.size > 1)
                wheelView.setSelectedIndex(selectedIndex)
                showWinnerDialog(names[selectedIndex])
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        wheelAnimator.start()
    }

    private fun showWinnerDialog(winnerName: String) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_winner, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val winnerText = dialogView.findViewById<TextView>(R.id.winner_name)
        winnerText.text = winnerName
        val closeButton = dialogView.findViewById<MaterialButton>(R.id.close_button)
        closeButton.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private inner class NamesAdapter : RecyclerView.Adapter<NameViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_name, parent, false)
            return NameViewHolder(view)
        }

        override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
            holder.nameText.text = names[position]
            holder.deleteButton.setOnClickListener {
                val adapterPosition = holder.adapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    names.removeAt(adapterPosition)
                    updateWheelAndCount()
                    notifyItemRemoved(adapterPosition)
                }
            }
        }

        override fun getItemCount(): Int {
            return names.size
        }

        inner class NameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var nameText: TextView
            var deleteButton: MaterialButton

            init {
                nameText = itemView.findViewById(R.id.name_text)
                deleteButton = itemView.findViewById(R.id.delete_button)
            }
        }
    }

    override fun onDestroy() {
        wheelAnimator.cancel()
        super.onDestroy()
    }
}