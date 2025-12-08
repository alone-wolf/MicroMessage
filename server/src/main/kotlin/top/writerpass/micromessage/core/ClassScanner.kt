package top.writerpass.micromessage.core

import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult
import org.jetbrains.exposed.sql.Table
import org.slf4j.LoggerFactory
import top.writerpass.micromessage.common.utils.WithLogger
import top.writerpass.micromessage.common.utils.logWrapper
import top.writerpass.micromessage.core.data.base.BaseRouting

class ClassScanner: WithLogger {
    override val logger = LoggerFactory.getLogger("ClassScanner")

    private val SERVICE_PACKAGE = "top.writerpass.micromessage.core.data.service"
    // top/writerpass/micromessage/core/data/service
    val tables: Array<Table>
    val routings: Array<BaseRouting>

    init {
        logWrapper("scan classes under [$SERVICE_PACKAGE]") {
            val scanResult = ClassGraph()
                .enableClassInfo()
                .acceptPackages(SERVICE_PACKAGE)
                .scan()
            val tableInstances = scanResult.tables().also { its ->
                its.forEach { it ->
                    "Table: [${it::class.qualifiedName?.removePrefix(SERVICE_PACKAGE)}]".logi()
                }
            }

            val routingInstances = scanResult.routings().also { its ->
                its.forEach { it ->
                    "Routing: [${it::class.qualifiedName?.removePrefix(SERVICE_PACKAGE)}]".logi()
                }
            }
            tableInstances to routingInstances
        }.also {
            tables = it.first
            routings = it.second
        }
    }

    private fun ScanResult.tables(): Array<Table> {
        return getSubclasses(Table::class.qualifiedName)
            .loadClasses(Table::class.java)
            .filterNot { it.isInterface }
            .mapNotNull { clazz -> clazz.kotlin.objectInstance }
            .toTypedArray()
    }

    private fun ScanResult.routings(): Array<BaseRouting> {
        return getClassesImplementing(BaseRouting::class.qualifiedName)
            .loadClasses(BaseRouting::class.java)
            .filterNot { it.isInterface }
            .mapNotNull { clazz -> clazz.kotlin.objectInstance }
            .toTypedArray()
    }
}