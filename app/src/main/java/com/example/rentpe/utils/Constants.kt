package com.example.rentpe.utils

object Constants {
    const val BASE_URL = "https://rentpe-api-ygwi.onrender.com"

    //auth
    const val LOGIN_URL = "login/"
    const val REGISTER_URL = "register/"
    const val NEW_TOKEN = "new-token/"

    //house
    const val GET_HOME_URL = "read-home/"
    const val POST_HOME_URL = "create-home/"
    const val PUT_HOME_URL = "update-home/{home_id}/"

    //transactions
    const val GET_TRANSACTIONS_URL = "read-transactions/"
    const val POST_TRANSACTIONS_URL = "create-transactions/"


    //JWT
    const val TOKEN_FILE = "TOKEN_FILE"
    const val ACCESS_TOKEN = "ACCESS_TOKEN"
    const val REFRESH_TOKEN = "ACCESS_TOKEN"
}