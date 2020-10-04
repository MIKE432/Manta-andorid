package com.apusart.manta.ui.club_scope_module.ui.club_articles.article

import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.apusart.manta.R
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.article_activity.*
import kotlinx.android.synthetic.main.user_activity.view.*
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.apusart.manta.ui.tools.Prefs
import java.lang.StringBuilder
import java.nio.charset.Charset

class ArticleActivity: AppCompatActivity() {
    private val articleViewModel: ArticleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(if(Prefs.getCurrentTheme() == 1) R.style.Manta_Theme_Dark else R.style.Manta_Theme_Light)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.article_activity)

        val title = intent.getStringExtra("title")?.removePrefix(" ")
        val fullText = intent.getStringExtra("fullText")


        var firstParagraphIndex = -1
        fullText?.toCharArray()?.forEachIndexed { index, c ->
            if(firstParagraphIndex == -1)
                if(index + 1 < fullText.length) {
                    if(c == '<' && fullText[index + 1] == 'p') {
                        firstParagraphIndex = index
                    }
                }
        }

        if(firstParagraphIndex != -1) {
            val parsedArticle = fullText?.removeRange(0, firstParagraphIndex)
            article_activity_text.text = Html.fromHtml(parsedArticle).toString()
        }

        article_activity_appbar_content.title = title
    }
}