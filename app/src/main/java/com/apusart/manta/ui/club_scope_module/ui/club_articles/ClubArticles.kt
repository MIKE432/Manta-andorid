package com.apusart.manta.ui.club_scope_module.ui.club_articles

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Range
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.apusart.manta.R
import com.apusart.manta.api.models.Article
import com.apusart.manta.api.models.MantaDatabase
import com.apusart.manta.ui.club_scope_module.ui.club_articles.article.ArticleActivity
import kotlinx.android.synthetic.main.article_item.view.*
import kotlinx.android.synthetic.main.club_articles_fragment.*

class ClubArticles: Fragment(R.layout.club_articles_fragment) {
    private lateinit var articlesAdapter: ArticlesAdapter
    private val articlesViewModel: ArticlesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MantaDatabase.initialize(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        articlesAdapter = ArticlesAdapter(this)


        club_articles_articles_list.apply {
            adapter = articlesAdapter
        }
        articlesViewModel.articles.observe(viewLifecycleOwner, Observer {
            articlesAdapter.submitList(it)
            club_articles_articles_list.apply {
                adapter = articlesAdapter
            }
        })
        articlesViewModel.getArticleById()
    }
}

class ArticlesAdapter(private val activity: ClubArticles): ListAdapter<Article, ArticleViewHolder>(diffUtil) {
    object diffUtil: DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.article_id == newItem.article_id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title == newItem.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val container = LayoutInflater.from(parent.context)
            .inflate(R.layout.article_item, parent, false)

        return ArticleViewHolder(container)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, ArticleActivity::class.java)
            intent
                .putExtra("title", article.title)
                .putExtra("fullText", article.fulltext)

            activity.startActivity(intent)
        }
    }
}

class ArticleViewHolder(container: View): RecyclerView.ViewHolder(container) {
    fun bind(item: Article) {
        itemView.apply {

            article_item_title.text = item.title
            article_item_subtitle.text = item.subtitle
            if(item.created?.length ?: 0 > 10) {
                article_item_date.text = item.created?.removeRange(10, item.created.length)
            } else {
                article_item_date.text = "Brak daty"
            }

        }
    }
}