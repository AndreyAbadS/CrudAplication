package com.afuntional.crudemployed.data.response

import com.afuntional.crudemployed.data.dtos.EmployedDTO

class EmployedListApiResponse(
    val message: String,
    val data: List<EmployedDTO>
)