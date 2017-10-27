package by.tc.task02.service.impl;

import by.tc.task02.dao.DAOFactory;
import by.tc.task02.dao.EntityDAO;
import by.tc.task02.dao.exeption.DaoExeption;
import by.tc.task02.entity.Entity;
import by.tc.task02.service.EntityService;
import by.tc.task02.service.exeption.ServiceExeption;

public class EntityServiceImpl implements EntityService {

	public Entity parse() throws ServiceExeption {
		DAOFactory factory = DAOFactory.getInstance();
		EntityDAO entityDAO = factory.getEntityDAO();
		Entity entity = new Entity();
		try {
			entity = entityDAO.parse();
		} catch (DaoExeption e) {
			throw  new ServiceExeption();
		}
		return entity;
	}

}
