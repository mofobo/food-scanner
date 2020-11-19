package ch.mofobo.foodscanner.domain.exception

sealed class BaseException : Exception() {
    object ProductNotFoundException : BaseException()
    object ProductSearchException : BaseException()
    object NetworkException : BaseException()
    object UnknownException : BaseException()
}