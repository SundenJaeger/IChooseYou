package com.android.ichooseyou.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.ichooseyou2.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import java.util.Random

class ListPickerActivity : AppCompatActivity() {
    private lateinit var itemInput: TextInputEditText
    private lateinit var addButton: MaterialButton
    private lateinit var pickButton: MaterialButton
    private lateinit var backButton: MaterialButton
    private lateinit var clearButton: MaterialButton
    private lateinit var shuffleButton: MaterialButton
    private lateinit var itemsListView: ListView
    private lateinit var resultText: MaterialTextView
    private lateinit var resultCard: MaterialCardView
    private val items = ArrayList<String?>()
    private lateinit var adapter: ArrayAdapter<String>
    private val random = Random()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_picker)

        // Initialize views
        itemInput = findViewById(R.id.item_input)
        addButton = findViewById(R.id.add_button)
        pickButton = findViewById(R.id.pick_button)
        clearButton = findViewById(R.id.clear_button)
        shuffleButton = findViewById(R.id.shuffle_button)
        itemsListView = findViewById(R.id.items_list)
        resultText = findViewById(R.id.result_text)
        resultCard = findViewById(R.id.result_card)
        backButton = findViewById(R.id.list_picker_back_button)

        // Check for passed items
        val passedItems = intent.getStringArrayListExtra("ITEMS")
        if (passedItems != null) {
            items.addAll(passedItems)
        }

        // Set up list adapter with custom layout
        adapter = ArrayAdapter(this, R.layout.list_item, R.id.item_text, items)
        itemsListView.setAdapter(adapter)

        // Set click listeners
        addButton.setOnClickListener { addItem() }
        pickButton.setOnClickListener { pickRandomItem() }
        clearButton.setOnClickListener { clearList() }
        shuffleButton.setOnClickListener { shuffleList() }
        backButton.setOnClickListener { finish() }

        // Set long click listener for item removal
        itemsListView.setOnItemLongClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            removeItem(position)
            true
        }

        // Hide result card initially
        resultCard.visibility = View.GONE
    }

    private fun addItem() {
        val item = itemInput.getText().toString().trim()
        if (item.isNotEmpty()) {
            items.add(item)
            adapter.notifyDataSetChanged()
            itemInput.setText("")
            resultCard.visibility = View.GONE
        } else {
            showToast("Please enter an item")
        }
    }

    private fun removeItem(position: Int) {
        items.removeAt(position)
        adapter.notifyDataSetChanged()
        showToast("Item removed")
    }

    private fun clearList() {
        if (items.isNotEmpty()) {
            items.clear()
            adapter.notifyDataSetChanged()
            resultCard.visibility = View.GONE
            showToast("List cleared")
        }
    }

    private fun shuffleList() {
        if (items.size > 1) {
            items.shuffle()
            adapter.notifyDataSetChanged()
            showToast("List shuffled")
        }
    }

    private fun pickRandomItem() {
        if (items.isEmpty()) {
            showToast("Please add some items first")
            return
        }
        resultCard.visibility = View.VISIBLE
        val randomIndex = random.nextInt(items.size)
        val selectedItem = items[randomIndex]
        resultText.text = selectedItem

        // Highlight the selected item in the list
        itemsListView.setItemChecked(randomIndex, true)
        itemsListView.smoothScrollToPosition(randomIndex)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}