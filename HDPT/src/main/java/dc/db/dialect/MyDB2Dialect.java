package dc.db.dialect;

import java.sql.Types;

import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class MyDB2Dialect extends DB2Dialect {

	public MyDB2Dialect() {
		super();
		registerHibernateType(Types.CHAR, StandardBasicTypes.STRING.getName());
		registerFunction( "sum_day", new SQLFunctionTemplate(StandardBasicTypes.DATE, "?1 DAY") );
//		registerFunction( "str_date", new SQLFunctionTemplate(Hibernate.STRING, "char(?1)") );
	}
	
}
