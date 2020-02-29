package thanh.ha.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_home.*
import thanh.ha.R
import thanh.ha.base.BaseFragment
import thanh.ha.ui.adapters.KeywordAdapter
import thanh.ha.ui.adapters.SmallWordDefAdapter


class HomeFragment : BaseFragment(),
        SmallWordDefAdapter.ClickListener,
        KeywordAdapter.ClickListener {

    private lateinit var mHomeViewModel: HomeViewModel
    private lateinit var mDisposable: Disposable
    private lateinit var savedWordAdapter: SmallWordDefAdapter
    private lateinit var recentSearchAdapter: KeywordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home,
                container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getLocalSaved()
        getLocalRecentKeyword()
    }

    private fun initView() {

        //set up for save definitions adapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_saved_def.layoutManager = layoutManager

        savedWordAdapter = SmallWordDefAdapter(context, this)
        rv_saved_def.layoutManager = layoutManager
        rv_saved_def.adapter = savedWordAdapter


        //set up for recent search keyword adapter
        val layoutManager2 = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        recentSearchAdapter = KeywordAdapter(context, this)
        rv_recent.layoutManager = layoutManager2
        rv_recent.adapter = recentSearchAdapter

        tv_clear_saved.setOnClickListener {
            saved_def_desc.visibility = View.VISIBLE
            mHomeViewModel.deleteAllLocalWord()
        }

        tv_clear_search.setOnClickListener {
            tv_recent_search_desc.visibility = View.VISIBLE
            mHomeViewModel.deleteAllKeyword()
        }
    }

    private fun getLocalRecentKeyword() {
        mHomeViewModel.getLocalKeyword()?.observe(
                this,
                Observer {
                    if (it.isNullOrEmpty()) {
                        tv_recent_search_desc.visibility = View.VISIBLE
                    } else {
                        tv_recent_search_desc.visibility = View.GONE
                    }
                    recentSearchAdapter.clear()
                    recentSearchAdapter.updateInfo(it.toMutableList())
                }
        )
    }

    override fun onClickKeyWord(string: String) {
        //TODO
    }

    private fun getLocalSaved() {
        mHomeViewModel.getLocalDefinitions()?.observe(
                this,
                Observer {
                    if (it.isNullOrEmpty()) {
                        saved_def_desc.visibility = View.VISIBLE
                    } else {
                        saved_def_desc.visibility = View.GONE
                    }
                    recentSearchAdapter.clear()
                    savedWordAdapter.updateInfo(it.toMutableList())
                }
        )
    }

    private fun initViewModel() {
        mHomeViewModel = ViewModelProviders
                .of(this)
                .get(HomeViewModel::class.java)
        mHomeViewModel
                .let {
                    lifecycle.addObserver(it)
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!mDisposable.isDisposed) mDisposable.dispose()
    }
}
