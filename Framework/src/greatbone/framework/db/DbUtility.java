package greatbone.framework.db;

import com.zaxxer.hikari.HikariConfig;
import greatbone.framework.Config;
import greatbone.framework.Greatbone;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

/**
 * The managerial point of the database access facilities.
 */
public class DbUtility implements DbMBean, Config {

    static final String POSTGRESQL = "org.postgresql.ds.PGSimpleDataSource";

    static final DbUtility INST = new DbUtility();

    final Element config;

    // poolable db sources, defined in config.xml
    final ArrayList<DbSource> sources = new ArrayList<>(4);

    DbUtility() {

        config = Greatbone.getXmlTopTag("db");

        NodeList lst = config.getElementsByTagName("source");

        DbSource default_ = null;

        for (int i = 0; i < lst.getLength(); i++) {
            Element e = (Element) lst.item(i);

            String key = e.getAttribute("key");
            // get attributes from xml
            Properties props = new Properties();
            String clazz = e.getAttribute("class");
            if (clazz == null) {
                clazz = POSTGRESQL;
            }
            String user = e.getAttribute("user");
            String password = e.getAttribute("password");
            String database = e.getAttribute("database");
            props.setProperty("dataSourceClassName", clazz);
            props.setProperty("dataSource.user", user);
            props.setProperty("dataSource.password", password);
            props.setProperty("dataSource.databaseName", database);

            DbSource source = new DbSource(key, new HikariConfig(props), e);
            sources.add(source);
        }

    }

    @Override
    public void close() {
        for (int i = 0; i < sources.size(); i++) {
            sources.get(i).close();
        }
    }

    public static DbSource getSource() {
        return INST.source("");
    }

    public static DbSource getSource(String key) {
        return INST.source(key);
    }

    DbSource source(String key) {
        for (int i = 0; i < sources.size(); i++) {
            DbSource src = sources.get(i);
            if (key.equals(src.key)) {
                return src;
            }
        }
        return null;
    }

    @Override
    public Element config() {
        return config;
    }

    /**
     * Returns the connection to the pool.
     *
     * @param conn connection
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
            }
        }
    }

    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
            }
        }
    }

    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
        }
    }

}
