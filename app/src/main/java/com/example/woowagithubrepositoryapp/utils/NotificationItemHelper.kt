package com.example.woowagithubrepositoryapp.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.woowagithubrepositoryapp.R
import com.example.woowagithubrepositoryapp.model.Notification
import com.example.woowagithubrepositoryapp.ui.adapter.NotificationAdapter
import kotlin.math.roundToInt

class NotificationItemHelper(val context: Context,
                             val mark : (notification : Notification,position : Int) -> Unit,
) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0,ItemTouchHelper.LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val notificationViewHolder = viewHolder as NotificationAdapter.NotificationViewHolder
        notificationViewHolder.binding.notification?.let {
            mark(it,notificationViewHolder.adapterPosition)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val view = viewHolder.itemView
            if (dX != 0F) {
                val paint = Paint().apply {
                    this.color = ContextCompat.getColor(context,R.color.primary)
                }
                view.also {
                    c.drawRect(it.right.toFloat() + dX, it.top.toFloat(),
                        it.right.toFloat(), it.bottom.toFloat(), paint)
                }
                val margin = 48.dpToPx(context)
                val icon = ContextCompat.getDrawable(context, R.drawable.ic_check) ?: return
                icon.let {
                    val start = (view.right - margin - it.intrinsicWidth).toInt()
                    val top = view.top + ((view.bottom - view.top - it.intrinsicHeight) / 2)
                    val end = (view.right - margin).toInt()
                    val bottom = top + it.intrinsicHeight
                    it.bounds = Rect(start, top, end, bottom)
                    it.draw(c)
                }
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
fun Int.dpToPx(context : Context):Float {
    val density = context.resources.displayMetrics.density
    return (this * density).roundToInt().toFloat()
}