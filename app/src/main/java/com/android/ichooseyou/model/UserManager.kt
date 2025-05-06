package com.android.ichooseyou.model

enum class UserManager {
    INSTANCE;

    private val userList: MutableList<User> = mutableListOf()

    fun updateUser(oldUser: User, newUser: User) {
        val index = userList.indexOf(oldUser)
        if (index != -1) {
            userList[index] = newUser
        }
    }

    fun add(user: User) {
        userList.add(user)
    }

    fun isUserDuplicate(username: String): Boolean {
        return userList.any { user -> user.name == username }
    }

    fun isUserExists(username: String): Boolean {
        return userList.any { user -> user.name == username }
    }

    fun isValidLogin(username: String, password: String): Boolean {
        return userList.any { user -> user.name == username && user.password == password }
    }

    fun getUserList(): MutableList<User> {
        return userList
    }
}