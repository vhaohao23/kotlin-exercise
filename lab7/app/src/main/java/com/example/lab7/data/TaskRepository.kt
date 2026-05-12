package com.example.lab7.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TaskRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("tasks")

    fun getTasks(): Flow<List<Task>> = callbackFlow {
        val listener = collection.addSnapshotListener { snapshot, error ->
            if (error != null) { close(error); return@addSnapshotListener }
            val tasks = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject<Task>()?.copy(id = doc.id)
            } ?: emptyList()
            trySend(tasks)
        }
        awaitClose { listener.remove() }
    }

    suspend fun addTask(task: Task): String {
        val doc = collection.add(
            mapOf("title" to task.title, "description" to task.description, "completed" to task.completed)
        ).await()
        return doc.id
    }

    suspend fun updateTask(task: Task) {
        collection.document(task.id).set(
            mapOf("title" to task.title, "description" to task.description, "completed" to task.completed)
        ).await()
    }

    suspend fun deleteTask(taskId: String) {
        collection.document(taskId).delete().await()
    }

    suspend fun toggleComplete(task: Task) {
        collection.document(task.id).update("completed", !task.completed).await()
    }
}
