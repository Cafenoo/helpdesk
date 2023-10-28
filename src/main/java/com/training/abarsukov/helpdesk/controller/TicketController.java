package com.training.abarsukov.helpdesk.controller;

import com.training.abarsukov.helpdesk.dto.TicketDto;
import com.training.abarsukov.helpdesk.model.Ticket;
import com.training.abarsukov.helpdesk.model.enums.Action;
import com.training.abarsukov.helpdesk.model.enums.SortingField;
import com.training.abarsukov.helpdesk.service.TicketService;
import com.training.abarsukov.helpdesk.validation.constraints.EnterField;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

import static com.training.abarsukov.helpdesk.model.enums.SortingField.DEFAULT;
import static java.util.Optional.ofNullable;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Validated
public class TicketController {

  private final TicketService ticketService;

  @GetMapping
  public ResponseEntity<List<TicketDto>> getAll(
      @Positive(message = "Page can't be zero or negative") @RequestParam(required = false)
          Integer page,
      @Positive(message = "Page size can't be zero or negative")
          @Max(value = 100, message = "Page size maximum is 100")
          @RequestParam(required = false)
          Integer pageSize,
      @RequestParam(required = false) SortingField field,
      @EnterField @RequestParam(required = false) String keyword,
      @RequestParam(required = false) Boolean isPersonal) {
    final Integer processedPage = ofNullable(page).orElse(1);
    final Integer processedPageSize = ofNullable(pageSize).orElse(10);
    final SortingField processedSortingField = ofNullable(field).orElse(DEFAULT);
    final Boolean processedIsPersonal = ofNullable(isPersonal).orElse(false);

    final List<TicketDto> tickets =
        ticketService.findAll(
            processedPage, processedPageSize, processedSortingField, keyword, processedIsPersonal);
    return ResponseEntity.ok(tickets);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TicketDto> getById(@PathVariable Long id) {
    final TicketDto ticketDto = ticketService.findById(id);
    return ResponseEntity.ok(ticketDto);
  }

  @PostMapping
  public ResponseEntity<Void> create(@Valid @ModelAttribute TicketDto ticketDto) {
    final Ticket ticket = ticketService.save(ticketDto);
    final String uri = "/tickets/" + ticket.getId();
    return ResponseEntity.created(URI.create(uri)).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> edit(
      @PathVariable Long id, @Valid @ModelAttribute TicketDto ticketDto) {
    ticketService.edit(id, ticketDto);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}/action")
  public ResponseEntity<Void> transitState(@PathVariable Long id, @RequestParam Action action) {
    ticketService.transitState(id, action);
    return ResponseEntity.ok().build();
  }
}
