package com.training.abarsukov.helpdesk.controller;

import com.training.abarsukov.helpdesk.dto.CommentDto;
import com.training.abarsukov.helpdesk.model.Comment;
import com.training.abarsukov.helpdesk.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/{ticketId}/comments")
  public ResponseEntity<Void> createComment(
      @PathVariable Long ticketId, @Valid @RequestBody CommentDto commentDto) {
    final Comment savedComment = commentService.save(ticketId, commentDto);
    final String uri = "/tickets/" + ticketId + "/comments/" + savedComment.getId();
    return ResponseEntity.created(URI.create(uri)).build();
  }

  @GetMapping("/{ticketId}/comments")
  public ResponseEntity<List<CommentDto>> getComments(
      @PathVariable Long ticketId, @RequestParam(required = false) Boolean doGetAll) {
    final Boolean processedDoGetAll = Optional.ofNullable(doGetAll).orElse(false);

    final List<CommentDto> commentDtoList =
        commentService.findByTicketId(ticketId, processedDoGetAll);
    return ResponseEntity.ok(commentDtoList);
  }
}
