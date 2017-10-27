package by.tc.task02.dao.exeption;

import java.io.IOException;

@SuppressWarnings("serial")
public class DaoExeption extends IOException {
	public DaoExeption () {
		System.out.println("Erro" );
    }
}
