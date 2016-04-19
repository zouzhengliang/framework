package greatbone.framework.grid;

import greatbone.framework.Configurable;
import greatbone.framework.Greatbone;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A dataset or fileset.
 */
public abstract class GridSet implements Configurable {

    // the container grid instance
    final GridUtility grid;

    final String key;

    // configurative xml element, can be null when the set is replicated to every node
    final Element xmlcfg;

    final List<String> local;

    GridSet(GridUtility grid) {
        this.grid = grid;

        // derive the key
        Class c = getClass();
        this.key = c.getSimpleName().toLowerCase(); // from class name

        // derive the config tag
        Class p = c;
        String tag = null;
        while ((c = c.getSuperclass()) != GridSet.class) {
            String n = c.getSimpleName().toLowerCase();
            if (n.startsWith("grid") && n.endsWith("set") && n.length() > 8) {
                tag = n.substring(4);
            }
        }
        this.xmlcfg = Greatbone.childOf(grid.config, tag, key);

        // parse the local attribute
        String attlocal = xmlcfg.getAttribute("local");
        List<String> lst = null;
        StringTokenizer st = new StringTokenizer(attlocal, ",");
        while (st.hasMoreTokens()) {
            String tok = st.nextToken().trim();
            if (!tok.isEmpty()) {
                if (lst == null) lst = new ArrayList<>(16);
                lst.add(tok);
            }
        }
        this.local = lst;
    }

    abstract void flush();

    @Override
    public Element xmlcfg() {
        return xmlcfg;
    }

}
