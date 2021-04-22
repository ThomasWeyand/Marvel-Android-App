package br.com.thomas.weyandmarvel.db

import androidx.paging.PagingSource
import androidx.room.*
import br.com.thomas.weyandmarvel.model.ResultItem

@Dao
interface CharacterDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ResultItem.Character>)

    @Query(
        "SELECT * FROM character " + "ORDER BY name ASC"
    )
    fun getCharactersList(): PagingSource<Int, ResultItem.Character>

    @Query(
        "SELECT * FROM character WHERE " + "id = :itemId"
    )
    suspend fun getItemById(itemId: Long): ResultItem.Character?

    @Query(
        "UPDATE character SET isFavorite = :isItemFavorite " + "WHERE id = :itemId"
    )
    suspend fun updateFavoriteValue(isItemFavorite: Boolean, itemId: Long)

    @Query(
        "SELECT * FROM character WHERE " + "isFavorite = :isFavoriteSelected " + "ORDER BY name ASC"
    )
    fun getFavoriteCharacterList(isFavoriteSelected: Boolean = true): PagingSource<Int, ResultItem.Character>


    @Query(
        "SELECT * FROM character WHERE " +
                "name LIKE :queryString " +
                "ORDER BY name ASC"
    )
    fun charactersByName(queryString: String): PagingSource<Int, ResultItem.Character>

    @Query("DELETE FROM character")
    suspend fun clearCharacters()

}