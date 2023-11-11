package com.training.abarsukov.helpdesk.service;

import com.training.abarsukov.helpdesk.model.enums.Urgency;
import java.util.List;

public interface UrgencyService {

  List<Urgency> findAll();
}
