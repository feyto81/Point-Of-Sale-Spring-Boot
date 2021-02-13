package com.crocodic.koperasi.helpers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Controller
public class FileHelpers {


    private Helpers help = new Helpers();

    @GetMapping("/storage/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(
            HttpServletRequest request,
            @PathVariable String filename,
            @RequestParam Optional<String> download
    ) {
        Resource file = loadAsResource(request, filename);
        String contentType = null;
        String down = download.orElse("false");
        try {
            contentType = request.getServletContext().getMimeType(file.getFile().getAbsolutePath());
        } catch (IOException ex) {
//            System.out.println("Could not determine file type.");
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        if (!down.equals("false")) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
            //DOWNLOAD
        } else {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(file);
            //PREVIEW
        }
    }

    private Resource loadAsResource(
            HttpServletRequest request,
            String filename
    ) {
        try {
            Path uploadLocation = Paths.get(help.uploadDir(request));
            Path file = uploadLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                String img = help.url(request, "/assets/img/logo.png");
                Resource resourceImg = new UrlResource(img);
//                System.out.println("file "+filename +" are not found");
//                throw new RuntimeException("Could not read file: " + filename);
                return resourceImg;
            }
        } catch (MalformedURLException e) {
//            throw new RuntimeException("Could not read file: " + filename, e);
//            System.out.println("file "+filename +" are not found "+e.getMessage());
            return null;
        }
    }

}
