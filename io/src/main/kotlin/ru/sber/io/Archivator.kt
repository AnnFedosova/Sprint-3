package ru.sber.io

import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализации использовать ZipInputStream и ZipOutputStream.
 */
class Archivator {

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        ZipOutputStream(FileOutputStream("io/logfile.zip")).use { outputStream ->
            FileInputStream("io/logfile.log").use { inputStream ->
                outputStream.putNextEntry(ZipEntry("logfile.log"))
                inputStream.copyTo(outputStream, 1024)
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохранить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        FileOutputStream("io/unzippedLogfile.log").use { outputStream ->
            ZipInputStream(FileInputStream("io/logfile.zip")).use { inputStream ->
                inputStream.nextEntry
                inputStream.copyTo(outputStream, 1024)
            }
        }
    }
}