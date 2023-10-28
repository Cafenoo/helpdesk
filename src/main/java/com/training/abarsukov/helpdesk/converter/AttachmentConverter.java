package com.training.abarsukov.helpdesk.converter;

import com.training.abarsukov.helpdesk.dto.AttachmentDto;
import com.training.abarsukov.helpdesk.model.Attachment;

public interface AttachmentConverter {
  AttachmentDto convertToDto(Attachment attachment);

  Attachment convertToEntity(AttachmentDto attachmentDto);
}
