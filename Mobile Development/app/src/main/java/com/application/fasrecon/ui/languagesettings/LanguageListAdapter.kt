package com.application.fasrecon.ui.languagesettings

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.application.fasrecon.R
import com.application.fasrecon.data.preferences.LanguageModel
import com.application.fasrecon.databinding.ItemLanguageSettingsBinding

class LanguageListAdapter :
    ListAdapter<LanguageModel, LanguageListAdapter.LanguageViewHolder>(DIFF_CALLBACK) {

        private var languageChecked = -1

    inner class LanguageViewHolder(private val binding: ItemLanguageSettingsBinding) :
        ViewHolder(binding.root) {
        fun bind(language: LanguageModel, pos: Int) {
            binding.countryImage.setImageDrawable(language.countryImage)
            binding.countryName.text = language.countryName

            if (pos == languageChecked) {
                binding.languageChecked.visibility = View.VISIBLE
                binding.languageContainer.setBackgroundResource(R.drawable.custom_language_settings_checked)
            } else {
                binding.languageChecked.visibility = View.GONE
                binding.languageContainer.setBackgroundResource(R.drawable.custom_language_settings_unchecked)
            }

            itemView.setOnClickListener {
                languageCheckedInfo()
            }
        }

        private fun languageCheckedInfo() {
            val previousLanguageChecked = languageChecked
            languageChecked = adapterPosition
            notifyItemChanged(previousLanguageChecked)
            notifyItemChanged(languageChecked)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LanguageViewHolder {
        val binding =
            ItemLanguageSettingsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LanguageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(getItem(position), position)
        Log.d("Mantap", getItem(position).toString())
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<LanguageModel> =
            object : DiffUtil.ItemCallback<LanguageModel>() {
                override fun areItemsTheSame(
                    oldItem: LanguageModel,
                    newItem: LanguageModel
                ): Boolean {
                    return oldItem.countryName == newItem.countryName
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: LanguageModel,
                    newItem: LanguageModel
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}