package co.id.bismalabs.mvvm_architecture_android_beginners.data.api

class ApiHelper(private val apiService: ApiService) {
    fun getUsers() = apiService.getUsers()
}