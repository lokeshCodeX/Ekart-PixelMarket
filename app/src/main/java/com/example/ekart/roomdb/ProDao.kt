package com.example.ekart.roomdb



import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.ekart.roomdb.ProModel

@Dao
interface ProDao {

    @Insert
     fun insertProduct(product:ProModel)

    @Delete

     fun deleteProduct(product:ProModel)

    @Query("SELECT * FROM products")
    fun getAllProducts(): LiveData<List<ProModel>>

    @Query("SELECT * FROM products WHERE productId = :id")
     fun isexit(id: String): ProModel

}



