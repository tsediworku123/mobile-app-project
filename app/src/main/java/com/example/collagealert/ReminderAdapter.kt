package com.example.collagealert

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.collagealert.databinding.ItemReminderBinding
import java.text.SimpleDateFormat
import java.util.*

class ReminderAdapter(
    private val onCheckedChange: (ReminderEntity, Boolean) -> Unit
) : ListAdapter<ReminderEntity, ReminderAdapter.ReminderViewHolder>(ReminderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val binding = ItemReminderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReminderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ReminderViewHolder(private val binding: ItemReminderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reminder: ReminderEntity) {
            binding.reminderTitle.text = reminder.title
            binding.reminderDescription.text = reminder.description
            
            val sdf = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
            binding.reminderTime.text = sdf.format(Date(reminder.dateTime))
            
            binding.reminderCheckbox.isChecked = reminder.isCompleted
            
            binding.reminderCheckbox.setOnCheckedChangeListener { _, isChecked ->
                onCheckedChange(reminder, isChecked)
            }
        }
    }

    class ReminderDiffCallback : DiffUtil.ItemCallback<ReminderEntity>() {
        override fun areItemsTheSame(oldItem: ReminderEntity, newItem: ReminderEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ReminderEntity, newItem: ReminderEntity): Boolean {
            return oldItem == newItem
        }
    }
}
