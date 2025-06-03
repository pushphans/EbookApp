package com.amazing.ebookapp.repository

import com.amazing.ebookapp.model.Books
import com.amazing.ebookapp.utils.ResultState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DataRepository {
    private val db = FirebaseFirestore.getInstance()

    private val bookCollection = db.collection("books")

    suspend fun addBook(book : Books) : ResultState<String>{
        return try {
            val document = bookCollection.document()
            val bookWithId = book.copy(id = document.id)

            document.set(bookWithId).await()
            ResultState.Success(data = "Book added successfully")
        }catch (e : Exception){
            ResultState.Error(error = e.message ?: "Data adding failed")
        }
    }

    suspend fun getBooks() : ResultState<List<Books>>{
        return try{
            val snapshot = bookCollection.get().await()
            val books = snapshot.toObjects(Books::class.java)
            ResultState.Success(books)
        }catch (e : Exception){
            ResultState.Error(error = e.message ?: "Fetching failed")
        }
    }

    suspend fun updateBook(book : Books) : ResultState<String>{
        return try{
            if(book.id.isBlank()){
                ResultState.Error("Invalid book ID")
            }

            bookCollection.document(book.id).set(book).await()
            ResultState.Success(data = "Book updated successfully")

        }catch (e : Exception){
            ResultState.Error(e.message ?:"Updating failed")
        }
    }

    suspend fun deleteBook(book : Books) : ResultState<String>{
        return try{
            if(book.id.isBlank()){
                ResultState.Error("Invalid book ID")
            }

            bookCollection.document(book.id).delete().await()
            ResultState.Success(data = "Book deleted successfully")

        }catch(e : Exception){
            ResultState.Error(e.message ?: "Delete failed")
        }
    }



}