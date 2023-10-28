package com.training.abarsukov.helpdesk.service.impl;

import com.training.abarsukov.helpdesk.exception.EmailException;
import com.training.abarsukov.helpdesk.model.Ticket;
import com.training.abarsukov.helpdesk.model.User;
import com.training.abarsukov.helpdesk.model.enums.Role;
import com.training.abarsukov.helpdesk.security.UserService;
import com.training.abarsukov.helpdesk.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailServiceV1 implements EmailService {

  private static final String SPACE = " ";

  private static final String TICKET_ID_VARIABLE = "ticketId";

  private static final String URL_VARIABLE = "url";

  private static final String NAME_VARIABLE = "name";

  private final UserService userService;

  private final JavaMailSender mailSender;

  private final TemplateEngine templateEngine;

  @Value("${spring.mail.encoding}")
  private String encoding;

  @Value("${spring.mail.url}")
  private String url;

  private List<String> extractEmailsFromUsers(List<User> users) {
    return users.stream().map(User::getEmail).collect(Collectors.toList());
  }

  private void sendMail(
      String[] recipientsEmails,
      Map<String, Object> contextVariables,
      String htmlTemplateName,
      String emailSubject) {

    final Context context = new Context();
    context.setVariables(contextVariables);

    final String htmlContent = this.templateEngine.process(htmlTemplateName, context);

    final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
    final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, encoding);

    try {
      message.setSubject(emailSubject);
      message.setTo(recipientsEmails);
      message.setText(htmlContent, true);
    } catch (MessagingException exception) {
      throw new EmailException(exception);
    }

    this.mailSender.send(mimeMessage);
  }

  private void sendMail(
      String recipientsEmail,
      Map<String, Object> contextVariables,
      String htmlTemplateName,
      String emailSubject) {
    final String[] recipientEmails = new String[] {recipientsEmail};
    sendMail(recipientEmails, contextVariables, htmlTemplateName, emailSubject);
  }

  @Async
  @Override
  public void sendMailThatTicketHasToBeApproved(Ticket ticket) {
    final List<User> recipients = userService.getUsersByRole(Role.MANAGER);
    final String[] recipientsEmails = extractEmailsFromUsers(recipients).toArray(String[]::new);

    final Map<String, Object> contextVariables =
        Map.of(URL_VARIABLE, url, TICKET_ID_VARIABLE, ticket.getId());

    final String htmlTemplateName = "new-ticket-for-approval";

    final String emailSubject = "New ticket for approval";

    this.sendMail(recipientsEmails, contextVariables, htmlTemplateName, emailSubject);
  }

  @Async
  @Override
  public void sendMailThatTicketHasBeenApproved(Ticket ticket) {
    final User owner = ticket.getOwner();
    final List<User> engineers = userService.getUsersByRole(Role.ENGINEER);

    final List<User> recipients = new ArrayList<>(engineers);
    recipients.add(owner);

    final String[] recipientsEmails = extractEmailsFromUsers(recipients).toArray(String[]::new);

    final Map<String, Object> contextVariables =
        Map.of(URL_VARIABLE, url, TICKET_ID_VARIABLE, ticket.getId());

    final String htmlTemplateName = "ticket-was-approved";

    final String emailSubject = "Ticket was approved";

    this.sendMail(recipientsEmails, contextVariables, htmlTemplateName, emailSubject);
  }

  @Async
  @Override
  public void sendMailThatTicketHasBeenDeclined(Ticket ticket) {
    final User owner = ticket.getOwner();
    final String recipientEmail = owner.getEmail();
    final String name = owner.getFirstName() + SPACE + owner.getLastName();

    final Map<String, Object> contextVariables =
        Map.of(
            URL_VARIABLE, url,
            TICKET_ID_VARIABLE, ticket.getId(),
            NAME_VARIABLE, name);

    final String htmlTemplateName = "ticket-was-declined";

    final String emailSubject = "Ticket was declined";

    this.sendMail(recipientEmail, contextVariables, htmlTemplateName, emailSubject);
  }

  @Async
  @Override
  public void sendMailThatTicketHasBeenCancelled(Ticket ticket) {
    final User owner = ticket.getOwner();
    final String recipientEmail = owner.getEmail();
    final String name = owner.getFirstName() + SPACE + owner.getLastName();

    final Map<String, Object> contextVariables =
        Map.of(
            URL_VARIABLE, url,
            TICKET_ID_VARIABLE, ticket.getId(),
            NAME_VARIABLE, name);

    final String htmlTemplateName = "ticket-was-cancelled";

    final String emailSubject = "Ticket was cancelled";

    this.sendMail(recipientEmail, contextVariables, htmlTemplateName, emailSubject);
  }

  @Async
  @Override
  public void sendMailThatTicketHasBeenCancelledByEngineer(Ticket ticket) {
    final User owner = ticket.getOwner();
    final User approver = ticket.getApprover();

    final List<User> recipients = List.of(owner, approver);

    final String[] recipientsEmails = extractEmailsFromUsers(recipients).toArray(String[]::new);

    final Map<String, Object> contextVariables =
        Map.of(URL_VARIABLE, url, TICKET_ID_VARIABLE, ticket.getId());

    final String htmlTemplateName = "ticket-was-cancelled-by-engineer";

    final String emailSubject = "Ticket was cancelled";

    this.sendMail(recipientsEmails, contextVariables, htmlTemplateName, emailSubject);
  }

  @Async
  @Override
  public void sendMailThatTicketHasBeenDone(Ticket ticket) {
    final User owner = ticket.getOwner();
    final String recipientEmail = owner.getEmail();
    final String name = owner.getFirstName() + SPACE + owner.getLastName();

    final Map<String, Object> contextVariables =
        Map.of(
            URL_VARIABLE, url,
            TICKET_ID_VARIABLE, ticket.getId(),
            NAME_VARIABLE, name);

    final String htmlTemplateName = "ticket-was-done";

    final String emailSubject = "Ticket was done";

    this.sendMail(recipientEmail, contextVariables, htmlTemplateName, emailSubject);
  }

  @Async
  @Override
  public void sendMailThatTicketHasGotFeedback(Ticket ticket) {
    final User assignee = ticket.getAssignee();
    final String recipientEmail = assignee.getEmail();
    final String name = assignee.getFirstName() + SPACE + assignee.getLastName();

    final Map<String, Object> contextVariables =
        Map.of(
            URL_VARIABLE, url,
            TICKET_ID_VARIABLE, ticket.getId(),
            NAME_VARIABLE, name);

    final String htmlTemplateName = "ticket-feedback-was-provided";

    final String emailSubject = "Feedback was provided";

    this.sendMail(recipientEmail, contextVariables, htmlTemplateName, emailSubject);
  }
}
