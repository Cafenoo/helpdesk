package com.training.abarsukov.helpdesk.converter.impl;

import com.training.abarsukov.helpdesk.converter.AttachmentConverter;
import com.training.abarsukov.helpdesk.dto.AttachmentDto;
import com.training.abarsukov.helpdesk.exception.FileException;
import com.training.abarsukov.helpdesk.model.Attachment;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class AttachmentConverterV1 implements AttachmentConverter {

  @Override
  public AttachmentDto convertToDto(Attachment attachment) {
    return AttachmentDto.builder()
        .id(attachment.getId())
        .name(attachment.getName())
        .build();
  }

  @Override
  public Attachment convertToEntity(AttachmentDto attachmentDto) {
    try {
      return Attachment.builder()
          .blob(attachmentDto.getFile()
              .getBytes())
          .name(attachmentDto.getFile()
              .getOriginalFilename())
          .build();
    } catch (IOException e) {
      throw new FileException(e.getMessage(), e);
    }
  }
}
