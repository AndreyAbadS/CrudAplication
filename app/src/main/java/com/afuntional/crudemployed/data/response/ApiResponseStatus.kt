package com.afuntional.crudemployed.data.response

import com.afuntional.crudemployed.data.model.Employed

sealed class ApiResponseStatusList {
    class Success(val employedList: List<Employed>): ApiResponseStatusList()
    class Loading(): ApiResponseStatusList()
    class Error(val message: String): ApiResponseStatusList()
}

sealed class ApiResponseStatus {
    class Success(val employed: Employed): ApiResponseStatus()
    class Loading(): ApiResponseStatus()
    class Error(val message: String): ApiResponseStatus()
}

sealed class ApiResponseStatusDelete {
    class Success(val messageSucess: String): ApiResponseStatusDelete()
    class Loading(): ApiResponseStatusDelete()
    class Error(val message: String): ApiResponseStatusDelete()
}

sealed class ApiResponseStatusPatch {
    class Success(val messageSucess: String): ApiResponseStatusPatch()
    class Loading(): ApiResponseStatusPatch()
    class Error(val message: String): ApiResponseStatusPatch()
}