package com.github.jntakpe.devsskills.extension

import com.github.jntakpe.devsskills.dto.EmployeeDTO
import com.github.jntakpe.devsskills.dto.EmployeeDetailDTO
import com.github.jntakpe.devsskills.model.Employee

fun EmployeeDTO.toEntity() = Employee(login, email, firstName, lastName, emptySet(), id?.toId())

fun EmployeeDetailDTO.toEntity() = Employee(login, email, firstName, lastName, skills.map { it.toEntity() }.toSet(), id.toId())

fun Employee.toDTO() = EmployeeDTO(login, email, firstName, lastName, id.toString())

fun Employee.toDetailDTO() = EmployeeDetailDTO(id.toString(), login, email, firstName, lastName, skills.map { it.toDTO() }.toSet())