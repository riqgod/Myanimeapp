package com.riqsphere.myapplication.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.riqsphere.myapplication.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_hierarchy, rootKey)
    }
}