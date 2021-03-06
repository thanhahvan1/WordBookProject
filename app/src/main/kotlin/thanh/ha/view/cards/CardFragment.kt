package thanh.ha.view.cards

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.view_word_definition.*
import thanh.ha.R
import thanh.ha.domain.DefinitionInfo
import thanh.ha.helpers.SpanHelper
import thanh.ha.ui.customSpanable.SpannableClickAction

@Deprecated ("Not use anymore, have bug")
class CardFragment(private val definitionInfo: DefinitionInfo, private val keyWordListener: KeyWordListener) : Fragment(), SpannableClickAction {

    var cardView: CardView? = null
        private set

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.view_word_definition, container, false)
        cardView = view.findViewById<View>(R.id.cv_def) as CardView
        cardView!!.maxCardElevation = (cardView!!.cardElevation
                * CardAdapter.MAX_ELEVATION_FACTOR)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // to override the onclick of text view
        tv_definition.movementMethod = LinkMovementMethod.getInstance()
        tv_example.movementMethod = LinkMovementMethod.getInstance()

        // map data
        tv_definition.text =
                SpanHelper.appSpirit(tv_definition.context, definitionInfo.definition, this)

        tv_example.text =
                SpanHelper.appSpirit(tv_definition.context, definitionInfo.example, this)
        tv_word.text = definitionInfo.word
        tv_author.text = definitionInfo.example
        tv_thumb_down_value.text = definitionInfo.thumbsDown.toString()
        tv_thumb_up_value.text = definitionInfo.thumbsUp.toString()
        thumpDownButton.setOnClickListener {
            keyWordListener.onThumbUpDown()
        }
        thumpUpButton.setOnClickListener {
            keyWordListener.onThumbUp()
        }
        likeIcon.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                keyWordListener.onSaveClicked(definitionInfo)
            } else {
                keyWordListener.onUnSaveClicked(definitionInfo)
            }
        }
    }

    override fun onClick(string: String) {
        keyWordListener.onKeyWordClicked(string)
    }

    interface KeyWordListener {
        fun onThumbUp()
        fun onThumbUpDown()
        fun onSaveClicked(def: DefinitionInfo)
        fun onUnSaveClicked(def: DefinitionInfo)
        fun onKeyWordClicked(string: String)
    }
}