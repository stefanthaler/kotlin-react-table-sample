package example.hook

import example.QueryKey
import example.data.User
import kotlinx.browser.window
import kotlinx.js.jso
import react.query.useMutation
import react.query.useQueryClient
import kotlin.js.Promise

typealias DeleteUser = (User) -> Unit

fun useDeleteUser(): DeleteUser {
    val queryClient = useQueryClient()
    val mutation = useMutation<Unit, Error, User, Nothing>(
        mutationFn = { user -> deleteUser(user) },
        options = jso { onSuccess = { _, _, _ -> queryClient.invalidateQueries<Nothing>(QueryKey.USERS.name) } }
    )
    return { user ->
        mutation.mutate(user, jso())
    }
}

private fun deleteUser(user: User): Promise<Unit> =
    window.fetch(
        input = "https://jsonplaceholder.typicode.com/users/${user.id}",
        init = jso { method = "DELETE" }
    ).then {}
