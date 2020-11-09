package ch.mofobo.foodscanner.utils.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewDividerMarginItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            left = spaceHeight

            if (parent.getChildAdapterPosition(view) == 0) {
                right = spaceHeight
            }
        }
    }
}