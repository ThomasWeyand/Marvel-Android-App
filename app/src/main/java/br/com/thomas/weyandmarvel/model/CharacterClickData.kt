package br.com.thomas.weyandmarvel.model

import androidx.appcompat.widget.AppCompatImageView
import br.com.thomas.weyandmarvel.presentation.utils.ClickCharacterType

data class CharacterClickData(
    val character: ResultItem.Character,
    val clickType: ClickCharacterType,
    val transitionImage: AppCompatImageView?
)