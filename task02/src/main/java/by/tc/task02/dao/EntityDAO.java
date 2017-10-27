package by.tc.task02.dao;

import by.tc.task02.dao.exeption.DaoExeption;
import by.tc.task02.entity.Entity;

public interface EntityDAO {
	Entity parse() throws DaoExeption;

}
