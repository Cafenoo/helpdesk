package com.training.abarsukov.helpdesk.service;

import com.training.abarsukov.helpdesk.model.Ticket;

public interface EmailService {
  void sendMailThatTicketHasToBeApproved(Ticket ticket);

  void sendMailThatTicketHasBeenApproved(Ticket ticket);

  void sendMailThatTicketHasBeenDeclined(Ticket ticket);

  void sendMailThatTicketHasBeenCancelled(Ticket ticket);

  void sendMailThatTicketHasBeenCancelledByEngineer(Ticket ticket);

  void sendMailThatTicketHasBeenDone(Ticket ticket);

  void sendMailThatTicketHasGotFeedback(Ticket ticket);
}
