package top.writerpass.micromessage.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun logger(name: String): Logger {
    return LoggerFactory.getLogger(name)!!
}