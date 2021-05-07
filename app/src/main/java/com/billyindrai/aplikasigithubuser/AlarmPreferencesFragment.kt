package com.billyindrai.aplikasigithubuser

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference

class AlarmPreferencesFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var reminder: String
    private lateinit var language: String
    private lateinit var reminderPreference: SwitchPreference
    private lateinit var languagePreference: Preference

    companion object {
        private const val DEFAULT_VALUE = "none"
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.alarmpreferences)
        reminder = resources.getString(R.string.reminder)
        reminderPreference = findPreference<SwitchPreference>(reminder) as SwitchPreference
        language = resources.getString(R.string.language)
        languagePreference = findPreference<Preference>(language) as Preference

        val alarmReceiver = AlarmReceiver()

        val shared = preferenceManager.sharedPreferences
        reminderPreference.isChecked = shared.getBoolean(reminder, true)

        reminderPreference.setOnPreferenceChangeListener { preference, newValue ->
            val switched = (preference as SwitchPreference).isChecked
            if (!switched) {
                alarmReceiver.setAlarm(requireContext(), AlarmReceiver.TYPE_ALARM,
                        "09:00", "Open Github User App")
            } else {
                alarmReceiver.cancelAlarm(requireContext(), AlarmReceiver.TYPE_ALARM)
            }
            true
        }

        languagePreference.setOnPreferenceClickListener {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
            true
        }

    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == reminder) {
            reminderPreference.isChecked = sharedPreferences.getBoolean(reminder, true)
        }
    }
}