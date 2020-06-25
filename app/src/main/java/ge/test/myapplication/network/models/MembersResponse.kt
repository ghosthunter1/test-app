package ge.test.myapplication.network.models

data class MembersResponse(
    val members: List<Member>,
    val hasMore: Boolean
)