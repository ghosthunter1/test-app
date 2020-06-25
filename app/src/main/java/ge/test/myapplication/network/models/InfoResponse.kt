package ge.test.myapplication.network.models

data class InfoResponse(
    val name: String,
    val imageUrl: String,
    val info: List<Info>,
    val me: Member
) {
}