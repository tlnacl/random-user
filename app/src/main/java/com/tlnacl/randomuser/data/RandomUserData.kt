package com.tlnacl.randomuser.data

data class RandomUserData(val results: List<UserData>)
data class UserData(val name: UserName, val gender: String, val dob: DOB, val picture: Picture, val login: Login)
data class UserName(val first: String, val last: String)
data class DOB(val date: String, val age: Int)
data class Picture(val thumbnail: String, val large: String)
data class Login(val userName: String, val uuid: String)