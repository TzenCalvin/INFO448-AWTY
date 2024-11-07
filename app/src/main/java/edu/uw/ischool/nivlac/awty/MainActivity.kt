package edu.uw.ischool.nivlac.awty

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val message = findViewById<EditText>(R.id.message)
        val phoneNumber = findViewById<EditText>(R.id.phoneNumber)
        val minutes = findViewById<EditText>(R.id.minutes)
        val startStopButton = findViewById<Button>(R.id.startStopButton)
        var alarmOn = false

        var alarmManager: AlarmManager
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        fun checkValid(): Boolean {
            return (message.text.toString().trim{ it <= ' ' }.isNotEmpty() &&
                    phoneNumber.text.toString().trim{ it <= ' ' }.isNotEmpty() &&
                    phoneNumber.text.toString().length == 10 &&
                    minutes.text.toString().trim{ it <= ' ' }.isNotEmpty() &&
                    minutes.text.toString().toInt() > 0)
        }

        startStopButton.setOnClickListener {
            if(checkValid()) {
                val time = minutes.text.toString().toInt()
                val intent = Intent(this, AlarmManagerBroadcast::class.java)
                intent.putExtra("message", message.text.toString())
                intent.putExtra("phone", phoneNumber.text.toString())
                val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

                if (!alarmOn) {
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + (time * 60000),
                        ((time * 60000).toLong()),
                        pendingIntent)
                    Toast.makeText(this, "Message set to repeat every $time minute(s).", Toast.LENGTH_SHORT).show()

                    startStopButton.text = "Stop"
                    alarmOn = true
                } else {
                    alarmManager.cancel(pendingIntent)
                    Toast.makeText(this, "Message stopped.", Toast.LENGTH_SHORT).show()

                    startStopButton.text = "Start"
                    alarmOn = false
                }
            } else if (message.text.toString().trim{ it <= ' ' }.isEmpty()) {
                Toast.makeText(this, "Cannot start due to no message!", Toast.LENGTH_SHORT).show()
            } else if (phoneNumber.text.toString().trim{ it <= ' ' }.isEmpty()) {
                Toast.makeText(this, "Cannot start due to no phone number!", Toast.LENGTH_SHORT).show()
            } else if (phoneNumber.text.toString().length != 10) {
                Toast.makeText(this, "Cannot start due to an invalid phone number (must be 10 digits)!", Toast.LENGTH_SHORT).show()
            } else if (minutes.text.toString().trim{ it <= ' ' }.isEmpty()){
                Toast.makeText(this, "Cannot start due to no time set!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Cannot start due to invalid time (must be greater than 0)!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}