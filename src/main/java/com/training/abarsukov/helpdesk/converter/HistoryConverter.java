package com.training.abarsukov.helpdesk.converter;

import com.training.abarsukov.helpdesk.dto.HistoryDto;
import com.training.abarsukov.helpdesk.model.History;

public interface HistoryConverter {

  HistoryDto convertToDto(History history);
}
