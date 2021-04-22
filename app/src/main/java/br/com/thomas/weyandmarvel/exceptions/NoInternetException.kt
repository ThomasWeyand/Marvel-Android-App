package br.com.thomas.weyandmarvel.exceptions

import java.io.IOException

class NoInternetException : IOException() {

    override val message: String?
        get() = "Não há conexão de internet"
}