package thanh.ha.view.search

import androidx.cardview.widget.CardView

interface CardAdapter {

    val baseElevation: Float

    fun getCardViewAt(position: Int): CardView?

    val mCount: Int

    companion object {
        const val MAX_ELEVATION_FACTOR = 6
    }
}