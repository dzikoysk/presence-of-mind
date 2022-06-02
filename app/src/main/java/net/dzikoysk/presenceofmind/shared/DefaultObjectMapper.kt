package net.dzikoysk.presenceofmind.shared

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object DefaultObjectMapper {

    val DEFAULT_OBJECT_MAPPER: ObjectMapper = JsonMapper.builder()
        .addModule(JavaTimeModule())
        .build()
        .registerKotlinModule()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)

}