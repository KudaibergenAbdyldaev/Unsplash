package com.example.unsplash.presentation.adapter

class OnClickListener(val clickListener: (meme: String) -> Unit) {
    fun onClick(meme: String) = clickListener(meme)
}