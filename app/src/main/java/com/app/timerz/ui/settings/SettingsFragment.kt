package com.app.timerz.ui.settings

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.timerz.BuildConfig
import com.app.timerz.R
import com.app.timerz.databinding.FragmentSettingsBinding
import com.app.timerz.databinding.ThemeSelectionDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingsFragment : Fragment() {

    lateinit var binding: FragmentSettingsBinding
    lateinit var themeType: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.themeContainer.setOnClickListener { showThemeSelectionDialog() }

        themeType = getDefaultSetThemeType()

        binding.themeTypeValue = themeType

        binding.version.text = getAppVersion()

        return binding.root
    }

    private fun getAppVersion(): String {
        return BuildConfig.VERSION_NAME
    }

    private fun showThemeSelectionDialog() {
        val dialogBinding: ThemeSelectionDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.theme_selection_dialog,
            null,
            false
        )

        val dialog: AlertDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        dialog.show()

        dialogBinding.okay.setOnClickListener { v -> dialog.dismiss() }

        dialogBinding.themeGroup.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId) {
                R.id.dark -> {
                    Toast.makeText(requireContext(), "dark theme set", Toast.LENGTH_SHORT).show()

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                    themeType = getString(R.string.dark)

                    dialog.dismiss()
                }

                R.id.light -> {
                    Toast.makeText(requireContext(), "light theme set", Toast.LENGTH_SHORT).show()

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                    themeType = getString(R.string.light)

                    dialog.dismiss()
                }
            }
        }
    }

    private fun getDefaultSetThemeType(): String {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            themeType = getString(R.string.light)
            return themeType
        }

        themeType = getString(R.string.dark)

        return themeType
    }

}