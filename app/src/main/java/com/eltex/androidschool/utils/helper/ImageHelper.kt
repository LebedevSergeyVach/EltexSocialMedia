package com.eltex.androidschool.utils.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.net.Uri
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface

import com.eltex.androidschool.BuildConfig

import okio.IOException

import java.io.File
import java.io.FileOutputStream

/**
 * Вспомогательный класс для работы с изображениями.
 * Предоставляет методы для создания URI изображений, сжатия изображений, удаления метаданных и управления временными файлами.
 *
 * @property context Контекст приложения, необходимый для доступа к ресурсам и файловой системе.
 */
class ImageHelper(private val context: Context) {

    /**
     * Создает URI для сохранения изображения во временном каталоге приложения.
     * Если каталог не существует, он будет создан.
     *
     * @return URI созданного файла изображения.
     * @see FileProvider.getUriForFile
     * @sample createPhotoUri()
     */
    fun createPhotoUri(): Uri {
        val directory: File = context.cacheDir.resolve("file_picker").apply {
            mkdir()
        }

        val file: File = directory.resolve("image.jpg")

        if (BuildConfig.DEBUG) {
            LoggerHelper.d("Путь к файлу: ${file.absolutePath}")
            LoggerHelper.d("Файл существует: ${file.exists()}")
            LoggerHelper.d("Файл доступен для чтения: ${file.canRead()}")
        }

        return FileProvider.getUriForFile(
            context,
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            file
        )
    }

    /**
     * Сжимает изображение, представленное URI, и сохраняет его во временный файл.
     * Удаляет метаданные из сжатого изображения.
     *
     * @param uri URI исходного изображения.
     * @return Сжатый файл изображения или `null`, если сжатие не удалось.
     * @see compressBitmapToFile
     * @see removeMetadata
     * @sample compressImage(uri)
     * @throws IOException Если произошла ошибка при чтении или записи файла.
     */
    fun compressImage(uri: Uri): File? {
        val inputStream = context.contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)

        val compressedFile = originalBitmap?.let { bitmap: Bitmap ->
            val tempFile = createTempFile()

            compressBitmapToFile(bitmap, tempFile, 80)
            removeMetadata(tempFile)

            tempFile
        }

        inputStream?.close()

        return compressedFile
    }

    /**
     * Сжимает Bitmap и сохраняет его в указанный файл с заданным качеством.
     *
     * @param bitmap Bitmap, который нужно сжать.
     * @param file Файл, в который будет сохранено сжатое изображение.
     * @param quality Качество сжатия (от 0 до 100).
     * @return Файл, в который было сохранено изображение, или `null`, если произошла ошибка.
     * @see Bitmap.compress
     * @sample compressBitmapToFile(bitmap, file, 80)
     * @throws IOException Если произошла ошибка при записи файла.
     */
    private fun compressBitmapToFile(bitmap: Bitmap, file: File, quality: Int): File? {
        return try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
                out.flush()
            }
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Удаляет метаданные из файла изображения, такие как информация о производителе, модели, программном обеспечении, дате и GPS-координатах.
     *
     * @param file Файл изображения, из которого нужно удалить метаданные.
     * @see ExifInterface
     * @sample removeMetadata(file)
     * @throws IOException Если произошла ошибка при работе с ExifInterface.
     */
    private fun removeMetadata(file: File) {
        try {
            val exif = ExifInterface(file.path)
            exif.setAttribute(ExifInterface.TAG_MAKE, null)
            exif.setAttribute(ExifInterface.TAG_MODEL, null)
            exif.setAttribute(ExifInterface.TAG_SOFTWARE, null)
            exif.setAttribute(ExifInterface.TAG_DATETIME, null)
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, null)
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, null)
            exif.saveAttributes()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Создает временный файл в кэш-директории приложения.
     *
     * @return Созданный временный файл.
     * @sample createTempFile()
     */
    private fun createTempFile(): File {
        return File(context.cacheDir, "compressed_image_${System.currentTimeMillis()}.jpg").apply {
            createNewFile()
        }
    }

    /**
     * Удаляет временный файл, если он существует.
     *
     * @param file Файл, который нужно удалить.
     * @sample deleteTempFile(file)
     */
    fun deleteTempFile(file: File) {
        if (file.exists()) {
            file.delete()
        }
    }
}
