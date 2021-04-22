package br.com.thomas.weyandmarvel.presentation.utils

import br.com.thomas.weyandmarvel.model.CharacterClickData

interface CharacterClickListener {

    fun clickedHeroe(selectedItem: CharacterClickData)
}