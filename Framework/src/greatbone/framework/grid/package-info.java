/**
 * In-Memory Data Grid
 *
 * there is no lookup overhead for data sharding
 *
 * support fast node-local JOIN operations.
 *
 * <h3>Replicated, Ranged, Hashed, and Prefixed datasets</h3>
 *
 * Stream API
 *
 * <h3>enable large memory pages on linux</h3>
 * <li>-XX:+UseLargePages</li>
 * <li>-XX:MaxDirectMemorySize=size, when applying for fileset memory storage</li>
 * <li>increase TCP buffer size</li>
 * cat /proc/meminfo | grep Huge
 */
package greatbone.framework.grid;