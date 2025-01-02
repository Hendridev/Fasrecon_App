package com.application.fasrecon.ui.profile

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.application.fasrecon.R
import com.application.fasrecon.data.local.entity.UserEntity
import com.application.fasrecon.databinding.FragmentProfileBinding
import com.application.fasrecon.ui.languagesettings.LanguageSettingsActivity
import com.application.fasrecon.ui.login.LoginActivity
import com.application.fasrecon.ui.profilesettings.ProfileSettingsActivity
import com.application.fasrecon.ui.viewmodelfactory.ViewModelFactoryUser
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding
    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactoryUser.getInstance(
            requireActivity()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionBar()
        profileViewModel.getUserData().observe(viewLifecycleOwner) {
            setUserData(it)
        }

        binding?.profileSettings?.setOnClickListener {
            val intent = Intent(requireContext(), ProfileSettingsActivity::class.java)
            startActivity(intent)
        }

        binding?.languageSettings?.setOnClickListener {
            val intent = Intent(requireContext(), LanguageSettingsActivity::class.java)
            startActivity(intent)
        }

        binding?.LogoutMenu?.setOnClickListener {
            showAlertDialog()
        }

        binding?.logoutIcon?.setOnClickListener {
            showAlertDialog()
        }
    }

    private fun setActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding?.topAppBar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title =
            getString(R.string.profile)
    }

    private fun setUserData(userData: UserEntity) {
        binding?.UserName?.text = userData.name
        binding?.userEmail?.text = userData.email
        if (userData.photoUrl != null) {
            binding?.userProfile?.let {
                Glide.with(requireActivity())
                    .load(userData.photoUrl)
                    .error(R.drawable.no_profile)
                    .into(it)
            }
        }
    }

    private fun showAlertDialog() {

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.logout_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        dialog.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.btn_logout).setOnClickListener {

            lifecycleScope.launch {
                profileViewModel.logout()
            }
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        dialog.show()
    }
}