package peakspace.config.amazonS3;

import org.springframework.web.multipart.MultipartFile;
import peakspace.dto.response.S3Response;
import peakspace.dto.response.SimpleResponse;

import java.util.List;

public interface AwsS3Service {

    S3Response uploadFile(MultipartFile file);

    S3Response uploadFiles(List<MultipartFile> files);

    SimpleResponse deleteFile(String key);

}
