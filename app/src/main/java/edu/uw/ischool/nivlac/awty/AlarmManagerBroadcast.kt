package edu.uw.ischool.nivlac.awty

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmManagerBroadcast : BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra("message")
        var phone = intent.getStringExtra("phone")

        if (phone != null) {
            phone = "(${phone.substring(0, 3)}) ${phone.substring(3, 6)}-${phone.substring(6, 10)}"
        }

        Toast.makeText(context, "$phone: $message", Toast.LENGTH_LONG).show()
    }
}