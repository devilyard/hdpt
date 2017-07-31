package dc.db.dialect;

import java.sql.Types;

import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class MySqlDialect extends MySQLDialect {

	public MySqlDialect() {
		super();
		registerHibernateType(Types.CHAR, StandardBasicTypes.STRING.getName());
		registerHibernateType(100, "string");//binary_float  
	    registerHibernateType(101, "string");//binary_double  
		registerFunction( "date", new SQLFunctionTemplate(StandardBasicTypes.DATE, "?1") );
		registerFunction( "sum_day", new SQLFunctionTemplate(StandardBasicTypes.DATE, "?1") );
//		registerFunction( "str_date", new SQLFunctionTemplate(Hibernate.STRING, "to_char(?1,'yyyy-MM-dd')") );
	}
	
}

