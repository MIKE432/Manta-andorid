package com.apusart.manta.ui.club_scope_module.ui.club_articles

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apusart.manta.api.models.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticlesListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(item: Article)

    @Query("SELECT * FROM Article ORDER BY article_id DESC")
    suspend fun getAllArticles(): List<Article>

}