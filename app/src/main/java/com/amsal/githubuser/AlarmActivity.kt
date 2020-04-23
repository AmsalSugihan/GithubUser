package com.amsal.githubuser

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amsal.githubuser.manager.AlarmReceiver
import kotlinx.android.synthetic.main.activity_alarm.*


class AlarmActivity : AppCompatActivity() {

    private lateinit var alarmReceiver: AlarmReceiver

    companion object {
        private const val MESSAGE = "Hey udah jam 9 pagi nih waktunya buka apps ini !"
        private const val TIME = "09:00"
        private const val PREFS_NAME = "alarm_preferences"
        private const val ISCHECKED = "isChecked"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        val preferences = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        if(preferences.getBoolean(ISCHECKED, false) == true){
            btn_switch.setChecked(true)
        }

        alarmReceiver = AlarmReceiver()

        btn_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val editor = preferences.edit()
                editor.putBoolean(ISCHECKED, true)
                editor.apply()

                alarmReceiver.setRepeatingAlarm(
                    this, AlarmReceiver.TYPE_REPEATING,
                    TIME, MESSAGE
                )

            } else {

                val editor = preferences.edit()
                editor.putBoolean(ISCHECKED, false)
                editor.apply()

                alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING)
            }
        }
    }
}
