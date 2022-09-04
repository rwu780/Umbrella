package com.example.umbrella.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.preference.*
import com.example.umbrella.R
import com.example.umbrella.viewmodel.WeatherViewModel


class SettingFragment : PreferenceFragmentCompat() {

    private val shareViewModel : WeatherViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        addPreferencesFromResource(R.xml.preferences)

        findPreference<EditTextPreference>("zip_code")?.text = shareViewModel.zip.value

        findPreference<EditTextPreference>("zip_code")?.summaryProvider =
            Preference.SummaryProvider<EditTextPreference> { preference ->
                val text = preference.text

                if(text == null || TextUtils.isEmpty(text)){
                    "Not Set"
                } else{
                    shareViewModel.setZip(text)
                    text
                }
            }

        findPreference<EditTextPreference>("zip_code")?.setOnBindEditTextListener {
            editText -> editText.inputType = InputType.TYPE_CLASS_NUMBER
        }


        findPreference<ListPreference>("units_select")?.value = shareViewModel.unit.value.toString()

        findPreference<ListPreference>("units_select")?.summaryProvider =
            Preference.SummaryProvider<ListPreference> { preference ->
                val text = preference.value

                shareViewModel.setUnit(text)
                shareViewModel.unit.value.toString()
            }
    }
}