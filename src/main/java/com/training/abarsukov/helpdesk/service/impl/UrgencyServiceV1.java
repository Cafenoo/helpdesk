package com.training.abarsukov.helpdesk.service.impl;

import com.training.abarsukov.helpdesk.model.enums.Urgency;
import com.training.abarsukov.helpdesk.service.UrgencyService;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UrgencyServiceV1 implements UrgencyService {

  @Override
  public List<Urgency> findAll() {
    return Arrays.asList(Urgency.values());
  }
}
