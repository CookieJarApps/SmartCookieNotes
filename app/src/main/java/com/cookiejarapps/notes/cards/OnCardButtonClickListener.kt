package com.cookiejarapps.notes.cards

interface OnCardButtonClickListener {
    fun onPositionClicked(position: Int)
    fun onLongClicked(position: Int)
}