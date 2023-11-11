package com.training.abarsukov.helpdesk.controller;

import com.training.abarsukov.helpdesk.dto.AttachmentDto;
import com.training.abarsukov.helpdesk.model.Attachment;
import com.training.abarsukov.helpdesk.service.AttachmentService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class AttachmentController {

  private final AttachmentService attachmentService;

  @GetMapping("/{ticketId}/attachment")
  public ResponseEntity<AttachmentDto> getAttachment(
      @PathVariable Long ticketId) {
    final AttachmentDto attachmentDto = attachmentService.find(ticketId);
    return ResponseEntity.ok(attachmentDto);
  }

  @PostMapping(value = "/{ticketId}/attachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> createAttachment(
      @PathVariable Long ticketId,
      @Valid @ModelAttribute AttachmentDto attachmentDto) {
    attachmentService.save(ticketId, attachmentDto);
    final URI uri = URI.create("/tickets/" + ticketId + "/attachments/");
    return ResponseEntity.created(uri)
        .build();
  }

  @PutMapping("/{ticketId}/attachment")
  public ResponseEntity<Void> editAttachment(
      @PathVariable Long ticketId,
      @Valid @ModelAttribute AttachmentDto attachmentDto) {
    attachmentService.edit(ticketId, attachmentDto);
    return ResponseEntity.ok()
        .build();
  }

  @DeleteMapping("/{ticketId}/attachment")
  public ResponseEntity<Void> deleteAttachment(
      @PathVariable Long ticketId) {
    attachmentService.delete(ticketId);
    return ResponseEntity.ok()
        .build();
  }

  @GetMapping("/{ticketId}/attachment/download")
  public ResponseEntity<Resource> downloadAttachment(
      @PathVariable Long ticketId) {
    final Attachment attachment = attachmentService.findToDownload(ticketId);

    final String contentDisposition = "attachment; filename=\"" + attachment.getName() + "\"";
    return ResponseEntity.ok()
        .contentType(MediaType.MULTIPART_FORM_DATA)
        .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
        .body(new ByteArrayResource(attachment.getBlob()));
  }
}
