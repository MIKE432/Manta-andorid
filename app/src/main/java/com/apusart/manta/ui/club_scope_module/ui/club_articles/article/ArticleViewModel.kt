package com.apusart.manta.ui.club_scope_module.ui.club_articles.article

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apusart.manta.api.models.Article
import com.apusart.manta.api.serivces.MantaService
import kotlinx.coroutines.launch
import java.lang.Exception

class ArticleViewModel: ViewModel() {
    private val mantaService =  MantaService()
    val article = MutableLiveData<Article>()

    fun getArticleById(id: Int = 389) {
        viewModelScope.launch {
            try {
                mantaService.getArticles().let {
                    if(it.isNotEmpty()) {
                        article.value = it[3]
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}