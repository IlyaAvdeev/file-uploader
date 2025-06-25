package one.avdeev.resource

import jakarta.annotation.security.RolesAllowed
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.SecurityContext
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestForm
import org.jboss.resteasy.reactive.multipart.FileUpload
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

class FileManagerResource(@ConfigProperty(name = "storage.path") val storagePath: String, @Inject val log:Logger) {

    @POST
    @Path("upload")
    @Produces(MediaType.TEXT_PLAIN)
    @RolesAllowed("user")
    fun uploadFile(@RestForm("file") file: FileUpload?, @Context securityContext: SecurityContext):Response {
        val resultPath = Files.copy(file!!.filePath(), Paths.get(storagePath  + '/' +
                securityContext.userPrincipal.name, file.fileName()), StandardCopyOption.REPLACE_EXISTING)
        log.infof("File '%s' uploading finished with '%s' result", file.fileName(), resultPath != null)
        return Response.status(201).build()
    }

    @GET
    @Path("ls")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    fun listFiles(@Context securityContext: SecurityContext):Response {
        val files = Files.list(Paths.get(storagePath + '/' + securityContext.userPrincipal.name))
            .map{it.fileName}
            .toList()
        return Response.ok(files).build()
    }
}