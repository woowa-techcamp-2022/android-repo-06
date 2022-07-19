package com.example.woowagithubrepositoryapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.woowagithubrepositoryapp.R
import com.example.woowagithubrepositoryapp.databinding.ItemSpinnerBinding
import com.example.woowagithubrepositoryapp.databinding.ItemSpinnerDefaultBinding

class SpinnerAdapter(context: Context, var resource: Int, var items: List<IssueOption>) :
    BaseAdapter() {

    data class IssueOption(val text: String, var check: Boolean)

    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int = items.size

    override fun getItem(position: Int) = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = inflater.inflate(R.layout.item_spinner_default, parent, false)
        val binding = ItemSpinnerDefaultBinding.bind(view)
        binding.text.text = items[position].text
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = inflater.inflate(resource, parent, false)
        val binding = ItemSpinnerBinding.bind(view)
        binding.option = items[position]
        return binding.root
    }
}
