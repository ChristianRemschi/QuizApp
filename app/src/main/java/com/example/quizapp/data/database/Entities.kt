package com.example.quizapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity
data class Quiz (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    var name: String,

    @ColumnInfo
    var description: String,

    @ColumnInfo
    val imageUri: String?,

    @ColumnInfo
    var isComplete: Boolean
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = Quiz::class,
        parentColumns = ["id"],
        childColumns = ["quizId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Question(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    var questionText: String,

    @ColumnInfo(index = true)
    val quizId: Int
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = Question::class,
        parentColumns = ["id"],
        childColumns = ["questionId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Answer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    var answerText: String,

    @ColumnInfo(index = true)
    val questionId: Int,

    @ColumnInfo
    var isCorrect: Boolean = false
)

@Entity
data class Person(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    var name: String,

    @ColumnInfo
    var password: String,

    @ColumnInfo
    var photo: String?,

    @ColumnInfo
    var biography: String?
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Person::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Quiz::class,
            parentColumns = ["id"],
            childColumns = ["quizId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class Score(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(index = true)
    val personId: Int,

    @ColumnInfo(index = true)
    val quizId: Int,

    @ColumnInfo
    val score: Int
)

@Entity
data class Badge(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val iconUri: String? = null
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Person::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Badge::class,
            parentColumns = ["id"],
            childColumns = ["badgeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PersonBadge(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val personId: Int,
    val badgeId: Int,
    val dateEarned: Long = System.currentTimeMillis()
)
