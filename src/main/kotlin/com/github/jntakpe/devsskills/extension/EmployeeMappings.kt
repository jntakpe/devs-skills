package com.github.jntakpe.devsskills.extension

import com.github.jntakpe.devsskills.dto.EmployeeDTO
import com.github.jntakpe.devsskills.model.Employee

fun EmployeeDTO.toEntity() = Employee(login, email, firstName, lastName, emptySet(), id.toId())

fun Employee.toDTO() = EmployeeDTO(id.toString(), login, email, firstName, lastName)