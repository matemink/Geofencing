package android.ihorkostenko.slatetestassignment.utils

enum class Status(val status: Int) {
    INSIDE(1),
    OUTSIDE(2),
    UNKNOWN(0);

    companion object {
        fun from(id: Int): Status? = values().find { it.status == id }
    }
}