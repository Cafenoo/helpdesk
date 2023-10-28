package com.training.abarsukov.helpdesk.service.impl;

import com.training.abarsukov.helpdesk.converter.AttachmentConverter;
import com.training.abarsukov.helpdesk.dto.AttachmentDto;
import com.training.abarsukov.helpdesk.model.Attachment;
import com.training.abarsukov.helpdesk.model.Ticket;
import com.training.abarsukov.helpdesk.repository.AttachmentRepository;
import com.training.abarsukov.helpdesk.security.UserService;
import com.training.abarsukov.helpdesk.service.AttachmentService;
import com.training.abarsukov.helpdesk.service.HistoryService;
import com.training.abarsukov.helpdesk.service.TicketAccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@RequiredArgsConstructor
public class AttachmentServiceV1 implements AttachmentService {

  private final AttachmentRepository attachmentRepository;

  private final AttachmentConverter attachmentConverter;

  private final HistoryService historyService;

  private final UserService userService;

  private final TicketAccessHandler ticketAccessHandler;

  @Override
  @Transactional
  public void delete(Long ticketId) {
    ticketAccessHandler.findByIdToEdit(ticketId);

    final Attachment attachment =
        attachmentRepository
            .findByTicketId(ticketId, userService.getUser())
            .orElseThrow(EntityNotFoundException::new);

    attachmentRepository.deleteByTicketId(ticketId);

    final String action = "File is removed";
    final String description = "File is removed: " + attachment.getName();
    historyService.save(ticketId, action, description);
  }

  @Override
  @Transactional
  public void save(Long ticketId, AttachmentDto attachmentDto) {
    ticketAccessHandler.findByIdToEdit(ticketId);

    final Attachment attachment = attachmentConverter.convertToEntity(attachmentDto);
    attachment.setTicket(Ticket.builder().id(ticketId).build());
    attachmentRepository.save(attachment);

    final String action = "File is attached";
    final String description = "File is attached: " + attachment.getName();
    historyService.save(ticketId, action, description);
  }

  @Override
  @Transactional
  public void edit(Long ticketId, AttachmentDto attachmentDto) {
    ticketAccessHandler.findByIdToEdit(ticketId);

    attachmentRepository
        .findByTicketId(ticketId, userService.getUser())
        .ifPresentOrElse(
            attachment -> editAttachment(attachmentDto, ticketId, attachment),
            () -> save(ticketId, attachmentDto));
  }

  @Override
  @Transactional(propagation = SUPPORTS, readOnly = true)
  public AttachmentDto find(Long ticketId) {
    final Attachment attachment =
        attachmentRepository
            .findByTicketId(ticketId, userService.getUser())
            .orElseThrow(EntityNotFoundException::new);
    return attachmentConverter.convertToDto(attachment);
  }

  @Override
  @Transactional(propagation = SUPPORTS, readOnly = true)
  public Attachment findToDownload(Long attachmentId) {
    return attachmentRepository
        .findByTicketId(attachmentId, userService.getUser())
        .orElseThrow(EntityNotFoundException::new);
  }

  private void editAttachment(AttachmentDto attachmentDto, Long ticketId, Attachment attachment) {
    final Attachment newAttachment = attachmentConverter.convertToEntity(attachmentDto);
    newAttachment.setId(attachment.getId());
    newAttachment.setTicket(Ticket.builder().id(ticketId).build());

    attachmentRepository.update(newAttachment);
  }
}
