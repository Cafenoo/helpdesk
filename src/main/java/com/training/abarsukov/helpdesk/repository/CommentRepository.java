package com.training.abarsukov.helpdesk.repository;

import com.training.abarsukov.helpdesk.model.Comment;
import com.training.abarsukov.helpdesk.model.User;
import com.training.abarsukov.helpdesk.repository.generic.Repository;

import java.util.List;

public interface CommentRepository extends Repository<Comment, Long> {
  List<Comment> findByTicketId(Long ticketId, boolean doGetAll, User user);
}
