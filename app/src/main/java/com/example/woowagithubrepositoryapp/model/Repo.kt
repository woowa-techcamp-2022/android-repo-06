package com.example.woowagithubrepositoryapp.model

import com.google.gson.annotations.SerializedName

data class Repo(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("owner") val owner : RepoOwner,
    @SerializedName("description") val description : String,
    @SerializedName("stargazers_count") val stargazersCount : Int,
    @SerializedName("language") val language : String,
    @SerializedName("full_name") val fullName : String
){
    val starCnt : String
    get() {
        return if (stargazersCount < 1000){
            stargazersCount.toString()
        }else{
            val t = stargazersCount/1000
            val h = (stargazersCount % 1000) / 100
            "${t}.${h}k"
        }
    }

    val languageColor : Int
    get() {
        return if (colorMap.containsKey(language)){
            colorMap[language]!!
        }else{
            var color : Int
            do {
                color = (0x33333333..0xffffffff).random().toInt()
            } while(colorSet.contains(color))
            colorSet.add(color)
            colorMap[language] = color
            color
        }
    }

    companion object{
        private val colorMap = hashMapOf<String,Int>()
        private val colorSet = hashSetOf<Int>()
    }
}
