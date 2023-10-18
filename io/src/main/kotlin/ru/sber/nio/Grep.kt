package ru.sber.nio

import java.io.BufferedWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.useLines

/**
 * Реализовать простой аналог утилиты grep с использованием калссов из пакета java.nio.
 */
class Grep {

    companion object {
        val sourcePaths: Path = Paths.get("io/logs")
        val resultPaths: Path = Paths.get("io/result.txt")
    }

    /**
     * Метод должен выполнить поиск подстроки subString во всех файлах каталога logs.
     * Каталог logs размещен в данном проекте (io/logs) и внутри содержит другие каталоги.
     * Результатом работы метода должен быть файл в каталоге io(на том же уровне, что и каталог logs), с названием result.txt.
     * Формат содержимого файла result.txt следующий:
     * имя файла, в котором найдена подстрока : номер строки в файле : содержимое найденной строки
     * Результирующий файл должен содержать данные о найденной подстроке во всех файлах.
     * Пример для подстроки "22/Jan/2001:14:27:46":
     * 22-01-2001-1.log : 3 : 192.168.1.1 - - [22/Jan/2001:14:27:46 +0000] "POST /files HTTP/1.1" 200 - "-"
     */
    fun find(subString: String) {
        val fileList: List<Path> = Files.walk(sourcePaths)
            .filter { path -> Files.isRegularFile(path) }.toList()

        Files.newBufferedWriter(resultPaths).use { writer ->
            fileList.forEach { file ->
                file.useLines {
                    it.withIndex()
                        .filter { line -> line.value.contains(subString) }
                        .map { line -> writeResult(writer, file.fileName, line) }
                        .toList()
                }
            }
        }
    }

    private fun writeResult(writer: BufferedWriter, fileName: Path, line: IndexedValue<String>) {
        writer.appendLine("$fileName : ${line.index} : ${line.value}")
    }

}