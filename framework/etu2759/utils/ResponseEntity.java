package framework.etu2759.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

public class ResponseEntity {
    String filename;
    String contentType;
    byte[] bytes;

    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public byte[] getBytes() {
        return bytes;
    }
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public void sendResponse(HttpServletResponse response) throws IOException {
        response.setContentType(this.getContentType());
        response.setContentLength(this.getBytes().length);
        response.setHeader("Content-Disposition", "attachment; filename=\""+ this.filename +"\"");

        OutputStream out = response.getOutputStream();
        out.write(this.getBytes());
        out.flush();
    }
}
