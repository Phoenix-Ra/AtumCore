package me.phoenixra.atum.craft.effects

import me.phoenixra.atum.core.effects.types.ImageEffect
import java.awt.image.BufferedImage
import java.io.File
import java.util.logging.Level
import javax.imageio.ImageIO

class ImageLoadTask(
    val effectsManager: AtumEffectsManager,
    val file: File,
    val callback: ImageEffect.ImageLoadCallback
): Runnable {
    override fun run() {
        var images: Array<BufferedImage?>
        if (!file.exists()) {
            effectsManager.plugin.logger.log(Level.WARNING,
                "Failed to find file for imageLoad task! file: ${file.name} doesn't exists")
            images = arrayOfNulls(0)
            callback.loaded(images)
            return
        }
        try {
            if (file.name.endsWith(".gif")) {
                val reader = ImageIO.getImageReadersBySuffix("GIF").next()
                val `in` = ImageIO.createImageInputStream(file)
                reader.input = `in`
                val numImages = reader.getNumImages(true)
                images = arrayOfNulls(numImages)
                for (i in 0 until numImages) {
                    images[i] = reader.read(i)
                }
            } else {
                images = arrayOfNulls(1)
                images[0] = ImageIO.read(file)
            }
        } catch (ex: Exception) {
            effectsManager.plugin.logger.log(Level.WARNING,
                "Failed to find file for imageLoad task! file: ${file.name}", ex)
            images = arrayOfNulls(0)
        }

        callback.loaded(images)
    }
}