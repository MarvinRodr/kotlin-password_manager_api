package com.marvinrodr.shared

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("com.marvinrodr")
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}