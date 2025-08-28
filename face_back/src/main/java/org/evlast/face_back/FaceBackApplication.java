package org.evlast.face_back;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.evlast.face_back.mapper")
public class FaceBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(FaceBackApplication.class, args);
    }

}
