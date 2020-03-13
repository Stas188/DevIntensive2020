package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting
import java.util.logging.LogManager

object UserHolder {

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder(){
        map.clear()
    }

    private  val map = mutableMapOf<String,User>()

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): User {
        val user = User.makeUser(fullName, email = email, password = password)
            return if (!map.keys.contains(user.login))
                        user.also { user -> map[user.login] = user}
            else throw IllegalArgumentException("A user with this email already exists")
    }

    fun loginUser(login: String, password: String): String? {
        val login = if (login.trimPhoneNumber().isValidNumber()) login.trimPhoneNumber()
        else login.trim().toLowerCase()

        return map[login]?.run {
            if (checkPassword(password)) userInfo
            else null
        }
    }

    fun registerUserByPhone(name: String, rawPhone: String): User {
        var number = rawPhone.trimPhoneNumber()
        if (!number.isValidNumber()) throw IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")
        if (map.keys.contains(number)) throw IllegalArgumentException("A user with this email already exists")
        return User.makeUser(name,phone = number).also { map[it.login] = it}
    }

    fun requestAccessCode(login: String):Unit {
        val login = if (login.trimPhoneNumber().isValidNumber()) login.trimPhoneNumber()
        else throw IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits") //login.trim().toLowerCase()

        if (!map.keys.contains(login)) throw IllegalArgumentException("A user with this email not exists")
        map[login]?.changePassword(login)
    }
}


