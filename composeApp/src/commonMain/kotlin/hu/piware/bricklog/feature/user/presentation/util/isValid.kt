package hu.piware.bricklog.feature.user.presentation.util

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
    return email.matches(emailRegex)
}

fun isValidPassword(password: String): Boolean {
    val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\w\\W]{8,}\$".toRegex()
    return password.matches(passwordRegex)
}

fun isValidName(name: String): Boolean {
    return name.length <= 20
}
