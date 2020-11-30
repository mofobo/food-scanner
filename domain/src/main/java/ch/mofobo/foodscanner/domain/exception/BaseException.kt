package ch.mofobo.foodscanner.domain.exception

sealed class BaseException(open val barcode: String) : Exception() {
    data class ProductNotFoundException(override val barcode: String) : BaseException(barcode)
    data class ProductSearchException(override val barcode: String) : BaseException(barcode)
    data class NetworkException(override val barcode: String) : BaseException(barcode)
    data class UnknownException(override val barcode: String) : BaseException(barcode)
}