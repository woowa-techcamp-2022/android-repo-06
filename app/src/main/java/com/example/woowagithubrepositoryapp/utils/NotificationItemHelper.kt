package com.example.woowagithubrepositoryapp.utils

import android.content.Context
import android.graphics.Canvas
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.woowagithubrepositoryapp.model.Notification
import com.example.woowagithubrepositoryapp.ui.adapter.NotificationAdapter

class NotificationItemHelper(
    val context: Context,
    val mark : (notification : Notification?,position:Int) -> Unit)
        : ItemTouchHelper.Callback() {

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
        Log.d("onSwiped",notificationViewHolder.binding.notification?.subject?.title
                + "was swiped")
        mark(notificationViewHolder.binding.notification,notificationViewHolder.adapterPosition)
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
        val targetView = (viewHolder as NotificationAdapter.NotificationViewHolder).binding.coverLayout
        getDefaultUIUtil().onDraw(c, recyclerView, targetView,dX, dY, actionState, isCurrentlyActive)
    }
}