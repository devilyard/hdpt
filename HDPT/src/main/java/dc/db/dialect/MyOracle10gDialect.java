package dc.db.dialect;

import java.sql.Types;

import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class MyOracle10gDialect extends Oracle10gDialect {

	public MyOracle10gDialect() {
		super();
		registerHibernateType(Types.CHAR, StandardBasicTypes.STRING.getName());
		registerHibernateType(100, "string");//binary_float  
	    registerHibernateType(101, "string");//binary_double  
		registerFunction( "date", new SQLFunctionTemplate(StandardBasicTypes.DATE, "to_date(?1,'yyyy-MM-dd')") );
		registerFunction( "sum_day", new SQLFunctionTemplate(StandardBasicTypes.DATE, "?1") );
//		registerFunction( "str_date", new SQLFunctionTemplate(Hibernate.STRING, "to_char(?1,'yyyy-MM-dd')") );
	}
	
}

