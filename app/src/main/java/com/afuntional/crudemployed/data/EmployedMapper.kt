package com.afuntional.crudemployed.data

import com.afuntional.crudemployed.data.dtos.EmployedDTO
import com.afuntional.crudemployed.data.model.Employed

class EmployedMapper {

     fun fromEmployedDTOToEmployedDomain(employedDTO: EmployedDTO): Employed{
        return Employed(employedDTO.id,employedDTO.name,employedDTO.lastName,employedDTO.address,employedDTO.age,employedDTO.flag,employedDTO.rfc)
    }

    fun fromEmployedDTOListToEmployedDomainList(employedDtoList: List<EmployedDTO>) : List<Employed>{
        return employedDtoList.map { list->
            fromEmployedDTOToEmployedDomain(list) }
    }

}