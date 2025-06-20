package one.avdeev.controller

import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.resteasy.reactive.RestForm
import org.jboss.resteasy.reactive.multipart.FileUpload
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption


@Path("upload")
class Uploader(@ConfigProperty(name = "storage.path") val storagePath: String) {
    @POST
    fun uploadFile(
        @RestForm("file") file: FileUpload?
    ) {
        Files.copy(file!!.filePath(), Paths.get(storagePath, file.fileName()), StandardCopyOption.REPLACE_EXISTING);
    }
}