package ru.sber

import ru.sber.io.Archivator


fun main() {
    val archivator = Archivator()
    archivator.zipLogfile()
    archivator.unzipLogfile()
}

