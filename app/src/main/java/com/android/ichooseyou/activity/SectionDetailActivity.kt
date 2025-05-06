package com.android.ichooseyou.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.ichooseyou2.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SectionDetailActivity : AppCompatActivity() {
    private lateinit var sectionTitleTextView: TextView
    private lateinit var itemsListView: ListView
    private lateinit var editButton: Button
    private lateinit var removeButton: Button
    private lateinit var randomizeButton: Button
    private lateinit var fabAddItem: FloatingActionButton
    private var sectionName: String? = null
    private val items = ArrayList<String?>()
    private lateinit var adapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section_detail)

        // Get section name from intent
        sectionName = intent.getStringExtra("SECTION_NAME")
        if (TextUtils.isEmpty(sectionName)) {
            sectionName = "Unnamed Section"
        }

        // Initialize views
        sectionTitleTextView = findViewById(R.id.section_title)
        itemsListView = findViewById(R.id.items_list_view)
        editButton = findViewById(R.id.edit_button)
        removeButton = findViewById(R.id.remove_button)
        randomizeButton = findViewById(R.id.randomize_button)
        fabAddItem = findViewById(R.id.fab_add_item)

        // Set up toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Replace deprecated onBackPressed with OnBackPressedCallback
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Set section title
        sectionTitleTextView.text = sectionName

        // Set up adapter for list view with custom item layout
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        itemsListView.setAdapter(adapter)

        // Set click listeners for buttons
        editButton.setOnClickListener { showEditDialog() }
        removeButton.setOnClickListener { showRemoveConfirmationDialog() }
        randomizeButton.setOnClickListener { showRandomizeOptions() }
        fabAddItem.setOnClickListener { showAddItemDialog() }

        // Set item click listener for list view
        itemsListView.setOnItemLongClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
            showItemOptionsDialog(position)
            true
        }
    }

    private fun showAddItemDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add New Item")

        // Set up the input field
        val input = EditText(this)
        input.setHint("Enter item name")

        // Add padding to the input field
        val container = LinearLayout(this)
        container.orientation = LinearLayout.VERTICAL
        val padding = (16 * resources.displayMetrics.density).toInt()
        container.setPadding(padding, padding, padding, padding)
        container.addView(input)
        builder.setView(container)

        // Set up the buttons
        builder.setPositiveButton("Add") { _: DialogInterface?, _: Int ->
            val itemText = input.getText().toString().trim { it <= ' ' }
            if (!TextUtils.isEmpty(itemText)) {
                items.add(itemText)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int -> dialog.cancel() }
        builder.show()
    }

    private fun showEditDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Items")

        // Set up the input field
        val input = EditText(this)
        input.setHint("Enter items (one per line)")

        // Pre-populate with existing items
        if (items.isNotEmpty()) {
            input.setText(TextUtils.join("\n", items))
        }

        // Add padding to the input field
        val container = LinearLayout(this)
        container.orientation = LinearLayout.VERTICAL
        val padding = (16 * resources.displayMetrics.density).toInt()
        container.setPadding(padding, padding, padding, padding)
        container.addView(input)
        builder.setView(container)

        // Set up the buttons
        builder.setPositiveButton("Save") { _: DialogInterface?, _: Int ->
            val itemsText = input.getText().toString().trim()
            if (!TextUtils.isEmpty(itemsText)) {
                // Clear existing items
                items.clear()

                // Add new items
                val itemsArray =
                    itemsText.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (item in itemsArray) {
                    if (item.trim().isNotEmpty()) {
                        items.add(item.trim())
                    }
                }

                // Update the adapter
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Items updated", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int -> dialog.cancel() }
        builder.show()
    }

    private fun showRemoveConfirmationDialog() {
        if (items.isEmpty()) {
            Toast.makeText(this, "No items to remove", Toast.LENGTH_SHORT).show()
            return
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Remove Items")
        builder.setMessage("Are you sure you want to remove all items?")
        builder.setPositiveButton("Remove All") { _: DialogInterface?, _: Int ->
            items.clear()
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "All items removed", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int -> dialog.cancel() }
        builder.show()
    }

    private fun showRandomizeOptions() {
        if (items.isEmpty()) {
            Toast.makeText(this, "Please add some items first", Toast.LENGTH_SHORT).show()
            return
        }
        val options = arrayOf("List Picker", "Wheel of Names", "Team Picker")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose Randomization Method")
        builder.setItems(options) { _: DialogInterface?, which: Int ->
            when (which) {
                0 -> openListPicker()
                1 -> openWheelOfNames()
                2 -> openTeamPicker()
            }
        }
        builder.show()
    }

    private fun openListPicker() {
        val intent = Intent(this, ListPickerActivity::class.java)
        intent.putStringArrayListExtra("ITEMS", items)
        startActivity(intent)
    }

    private fun openWheelOfNames() {
        val intent = Intent(this, WheelOfNamesActivity::class.java)
        intent.putStringArrayListExtra("ITEMS", items)
        startActivity(intent)
    }

    private fun openTeamPicker() {
        val intent = Intent(this, TeamPickerActivity::class.java)
        intent.putStringArrayListExtra("ITEMS", items)
        startActivity(intent)
    }

    private fun showItemOptionsDialog(position: Int) {
        val options = arrayOf("Edit", "Remove")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Item Options")
        builder.setItems(options) { _: DialogInterface?, which: Int ->
            when (which) {
                0 -> showEditItemDialog(position)
                1 -> removeItem(position)
            }
        }
        builder.show()
    }

    private fun showEditItemDialog(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Item")

        // Set up the input field
        val input = EditText(this)
        input.setText(items[position])

        // Add padding to the input field
        val container = LinearLayout(this)
        container.orientation = LinearLayout.VERTICAL
        val padding = (16 * resources.displayMetrics.density).toInt()
        container.setPadding(padding, padding, padding, padding)
        container.addView(input)
        builder.setView(container)

        // Set up the buttons
        builder.setPositiveButton("Save") { _: DialogInterface?, _: Int ->
            val itemText = input.getText().toString().trim()
            if (!TextUtils.isEmpty(itemText)) {
                items[position] = itemText
                adapter.notifyDataSetChanged()
            }
        }
        builder.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int -> dialog.cancel() }
        builder.show()
    }

    private fun removeItem(position: Int) {
        items.removeAt(position)
        adapter.notifyDataSetChanged()
        Toast.makeText(this, "Item removed", Toast.LENGTH_SHORT).show()
    }
}