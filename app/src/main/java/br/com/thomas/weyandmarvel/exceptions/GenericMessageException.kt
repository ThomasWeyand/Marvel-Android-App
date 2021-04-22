package br.com.thomas.weyandmarvel.exceptions

import java.lang.Exception

class GenericMessageException : Exception() {

    override val message: String?
        get() = "Erro ao carregar os dados"
}