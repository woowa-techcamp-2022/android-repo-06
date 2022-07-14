package com.example.woowagithubrepositoryapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.woowagithubrepositoryapp.R
import com.example.woowagithubrepositoryapp.databinding.ItemSpinnerBinding

class SpinnerAdapter(context: Context, var resource: Int, var items: List<IssueOption>) :
    ArrayAdapter<SpinnerAdapter.IssueOption>(context, resource, items) {

    data class IssueOption(val text: String, var check: Boolean)

    private val inflater = LayoutInflater.from(context)

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(resource, parent, false)
        val binding = ItemSpinnerBinding.bind(view!!)
        binding.text.text = items[position].text
        binding.check.setImageResource(R.drawable.ic_arrow_drop_down)
        binding.root.viewTreeObserver.addOnWindowFocusChangeListener {
            if (it) binding.check.rotation = 0f
            else binding.check.rotation = 180f
        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = inflater.inflate(resource, parent, false)
        val binding = ItemSpinnerBinding.bind(view)
        binding.text.text = items[position].text
        binding.check.visibility = if (items[position].check) View.VISIBLE else View.GONE
        return binding.root
    }
}
