package com.androidtestingdemo.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest {

    @Test
    fun `empty username return false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "",
            "123",
            "123"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `valid username and correctly repeated password return true`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "Philip",
            "123",
            "123"
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `username already exists return false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "Carl",
            "123",
            "123"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `empty password return false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "Carl",
            "",
            ""
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `incorrectly confirm password return false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "Carl",
            "1234",
            "abcd"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `less than 2 digit password return false`() {
        val result = RegistrationUtil.validateRegistrationInput(
            "Carl",
            "abcd1g",
            "abcd1g"
        )
        assertThat(result).isFalse()
    }
}