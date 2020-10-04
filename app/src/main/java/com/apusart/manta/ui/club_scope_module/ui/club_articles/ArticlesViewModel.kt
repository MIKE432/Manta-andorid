package com.apusart.manta.ui.club_scope_module.ui.club_articles

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.manta.api.models.Article
import com.apusart.manta.api.models.MantaDatabase
import com.apusart.manta.api.serivces.MantaService
import kotlinx.coroutines.launch
import java.lang.Exception

class ArticlesViewModel: ViewModel() {
    private val mantaService =  MantaService()
    val articles = MutableLiveData<List<Article>>()

    fun getArticleById() {
        viewModelScope.launch {
            try {
                val databaseArticles = MantaDatabase.database.articlesListDao().getAllArticles()
                if(databaseArticles.isNotEmpty())
                    articles.value = databaseArticles

                val fetchedArticles = mantaService.getArticles().filter { it.title != "" && it.fulltext.contains("<p>") }
                articles.value = fetchedArticles
                fetchedArticles.forEach { MantaDatabase.database.articlesListDao().insertArticle(it) }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}