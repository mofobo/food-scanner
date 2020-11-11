package ch.mofobo.foodscanner.features.common.search.gallery

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import ch.mofobo.foodscanner.utils.recyclerview.ItemSnapHelper


/**
 * Created by nbtk on 5/4/18.
 */
class ImageGalleryLayoutManager(
        context: Context?
) : LinearLayoutManager(context, HORIZONTAL, true) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var snapHelper: ItemSnapHelper

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        recyclerView = view!!

        // Smart snapping
        snapHelper = ItemSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
        val smoothScroller = StartSnappedSmoothScroller(recyclerView?.context)
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    private class StartSnappedSmoothScroller(context: Context?) : LinearSmoothScroller(context) {

        override fun getHorizontalSnapPreference(): Int {
            return SNAP_TO_START
        }
    }
}

