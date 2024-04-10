package com.HaveBinProject.HaveBin.AWS;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.UUID;

@Service
public class ImageService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final S3Client s3;

    public ImageService(S3Client s3) {
        this.s3 = s3;
    }

    // 원본 이미지 이름에 붙일 랜덤 UUID를 생성하는 메소드 (이름 중복 방지)
    private String changedImageName(String originName) {
        String random = UUID.randomUUID().toString();
        return random + originName;
    }

    //이미지를 S3에 업로드하고 이미지의 url을 반환 (Trashcan)
    public String uploadImageToS3(MultipartFile image, String type) {
        String originName = image.getOriginalFilename(); //원본 이미지 이름
        String ext = originName.substring(originName.lastIndexOf(".")); //확장자

        String changedName = ""; // 새로 생성된 이미지 이름

        // ============= 이미지 경로 지정 =============
        if (type.equals("Trashcan")) { changedName = "Trashcan/" + changedImageName(originName); }

        else if (type.equals("UnknownTrashcan")) { changedName = "Unknown_Trashcan/" + changedImageName(originName); }

        else { changedName = "User/" + changedImageName(originName); }
        // ============= ============ =============

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(changedName)
                    .acl("public-read")
                    .contentType("image/png")
                    .build();

            PutObjectResponse putObjectResponse = s3.putObject(putObjectRequest, RequestBody.fromInputStream(image.getInputStream(), image.getSize()));

        } catch (IOException e) {
            throw new ImageUploadException(); //커스텀 예외 던짐.
        }

        return s3.utilities().getUrl(builder -> builder.bucket(bucketName).key(changedName)).toExternalForm(); //데이터베이스에 저장할 이미지가 저장된 주소
    }
}
