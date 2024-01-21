package com.example.biletzone.activities
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.biletzone.databinding.ActivityCreateEventBinding
import com.example.biletzone.models.Event
import com.example.biletzone.services.EventService

class CreateEventActivity : AppCompatActivity() {
    private var binding: ActivityCreateEventBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnAddEvent?.setOnClickListener { onAddEventButtonClick(it) }
    }

    private fun onAddEventButtonClick(view: View) {
        // Retrieve event data from EditText fields
        val eventName = binding?.editTextName?.text.toString()
        val eventDescription = binding?.editTextDescription?.text.toString()
        val eventCity = binding?.editTextCity?.text.toString()
        val eventLocation = binding?.editTextLocation?.text.toString()
        val eventType = binding?.editTextType?.text.toString()
        val eventDate = binding?.editTextDate?.text.toString()
        val eventImage = binding?.editTextImage?.text.toString()

        // Create Event object with the entered data
        val newEvent = Event(
            id = System.currentTimeMillis(), // Use a unique identifier, for example, timestamp
            name = eventName,
            description = eventDescription,
            city = eventCity,
            location = eventLocation,
            type = eventType,
            date = eventDate,
            image = eventImage
        )

        EventService().addEvent(this, newEvent)

        // Send the new event data back to MainActivity
        val intent = Intent(this@CreateEventActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
