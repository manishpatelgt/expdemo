package com.androidtestingdemo.utils

object RegistrationUtil {

    /** Testing on Android by Philipp Lackner - From YouTube **/

    private val existingUsers = listOf("Peter", "Carl")

    /**
     * the input is not valid if.......
     * ...the username/password is empty
     * ...the username is already exist
     * ...the confirm password is not the same as the real password
     * ...the password contains less than 2 digits
     */
    fun validateRegistrationInput(
        userName: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (userName.isEmpty() || password.isEmpty()) {
            return false
        }

        if (userName in existingUsers) {
            return false
        }

        if (password != confirmPassword) {
            return false
        }

        if (password.count { it.isDefined() } < 2) {
            return false
        }

        return true
    }
}