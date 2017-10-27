package by.tc.task02.service.exeption;

import by.tc.task02.dao.exeption.DaoExeption;

@SuppressWarnings("serial")
public class ServiceExeption extends DaoExeption {
	
	public ServiceExeption () {
		System.out.println("Erro" );
    }

}
