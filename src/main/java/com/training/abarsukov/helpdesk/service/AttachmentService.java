package com.training.abarsukov.helpdesk.service;

import com.training.abarsukov.helpdesk.dto.AttachmentDto;
import com.training.abarsukov.helpdesk.model.Attachment;

public interface AttachmentService {

  void delete(Long ticketId);

  void save(Long ticketId, AttachmentDto attachmentDto);

  void edit(Long ticketId, AttachmentDto attachmentDto);

  AttachmentDto find(Long ticketId);

  Attachment findToDownload(Long ticketId);
}
