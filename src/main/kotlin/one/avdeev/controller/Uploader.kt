package one.avdeev.controller

import jakarta.inject.Inject
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestForm
import org.jboss.resteasy.reactive.multipart.FileUpload
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption


@Path("upload")
class Uploader(@ConfigProperty(name = "storage.path") val storagePath: String, @Inject val log:Logger) {

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    fun uploadFile(@RestForm("file") file: FileUpload?):String {
        log.info("Inside uploadFile")
        Files.copy(file!!.filePath(), Paths.get(storagePath, file.fileName()), StandardCopyOption.REPLACE_EXISTING)
        return "Ok"
    }
}