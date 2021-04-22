package br.com.thomas.weyandmarvel.exceptions

import java.lang.Exception

class UnknownException : Exception(){

    override val message: String?
        get() = "Erro desconhecido"

}