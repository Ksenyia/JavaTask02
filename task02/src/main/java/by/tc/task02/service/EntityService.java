package by.tc.task02.service;

import by.tc.task02.entity.Entity;
import by.tc.task02.service.exeption.ServiceExeption;

public interface EntityService {
	Entity parse() throws ServiceExeption;
}
