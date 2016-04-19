package greatbone.framework.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import greatbone.framework.Configurable;
import org.w3c.dom.Element;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * A poolable data source based on HikariCP, from which contexts can be created.
 */
public class DbSource extends HikariDataSource implements DbSourceMBean, Configurable {

    final String key;

    // the configuration xml element
    final Element config;

    public DbSource(String key, HikariConfig hikari, Element config) {
        // construct a so-called fast-path pool
        super(hikari);

        this.key = key;
        this.config = config;
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName objname = new ObjectName("db:type=DbSource");
            mbs.registerMBean(this, objname);
        } catch (Exception e) {
        }
    }

    public DbContext newContext() {
        return new DbContext(this);
    }

    @Override
    public Element config() {
        return config;
    }

}
