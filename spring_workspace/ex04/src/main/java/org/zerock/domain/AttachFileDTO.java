package org.zerock.domain;

import lombok.Data;

@Data // Getter, Setter, toString 등을 자동 생성
public class AttachFileDTO {

  private String fileName;   // 원본 파일 이름
  private String uploadPath; // 년/월/일 형태의 폴더 저장 경로
  private String uuid;       // 파일명 중복 방지용 고유 식별자
  private boolean image;     // 이미지 파일 여부 (섬네일 존재 여부)

}