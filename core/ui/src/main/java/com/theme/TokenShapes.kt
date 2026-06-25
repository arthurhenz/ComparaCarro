package com.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

object TokenShapes {
    val Sm = RoundedCornerShape(2.dp)
    val Button = RoundedCornerShape(4.dp)
    val Md = RoundedCornerShape(6.dp)
    val Card = Md
    val Pill = RoundedCornerShape(percent = 50)
    val StraightEdge = RectangleShape
}
