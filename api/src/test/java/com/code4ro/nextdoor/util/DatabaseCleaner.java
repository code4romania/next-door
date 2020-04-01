package com.code4ro.nextdoor.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Utility class that cleans all database tables content.
 * Tables that must not be cleaned can be added with {@link #excludeTables(String...)}.
 */
class DatabaseCleaner {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private Supplier<Connection> connectionSupplier;

    private Set<String> tablesToExclude = new HashSet<>();
    private List<TableRef> tablesForClearing;

    DatabaseCleaner(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    public void reset() {
        if (isNotPrepared()) {
            prepare();
        }
        executeReset();
    }

    public void excludeTables(String... tableNames) {
        tablesToExclude.addAll(Arrays.stream(tableNames).map(String::toLowerCase).collect(Collectors.toList()));
    }

    private void prepare() {
        try (Connection connection = connectionSupplier.get()) {
            DatabaseMetaData metaData = connection.getMetaData();
            List<TableRef> tableRefs = new ArrayList<>();
            try (ResultSet rs =
                     metaData.getTables(connection.getCatalog(), null, null, new String[]{"TABLE"})) {

                while (rs.next()) {
                    String tableName = rs.getString("TABLE_NAME");
                    if (!tablesToExclude.contains(tableName)) {
                        tableRefs.add(new TableRef(tableName));
                    }
                }
            }

            tablesForClearing = tableRefs;

            LOG.info("Prepared clean db command: {}", tablesForClearing);

        } catch (SQLException e) {
            LOG.error("Prepare cleanup error", e);
            throw new RuntimeException(e);
        }
    }

    private void executeReset() {
        try (Connection connection = connectionSupplier.get(); Statement reset = buildClearStatement(connection)) {
            reset.executeBatch();
        } catch (SQLException e) {
            // Only for MySQL
            // String status = dbEngineStatus();
            // LOG.error("Failed to remove rows. Engine status: {}" , status, e);
            LOG.error("Failed to remove rows", e);
            throw new RuntimeException(e);
        }
    }

    private boolean isNotPrepared() {
        return tablesForClearing == null;
    }

    private Statement buildClearStatement(Connection connection) throws SQLException {
        Statement reset = connection.createStatement();

        /*
            Disable referential integrity checks:
            - MySQL: "SET FOREIGN_KEY_CHECKS = 0"
            - H2: "SET REFERENTIAL_INTEGRITY FALSE" - see http://h2database.com/html/commands.html#set_referential_integrity

            Enable referential integrity checks:
            - MySQL: "SET FOREIGN_KEY_CHECKS = 1"
            - H2: "SET REFERENTIAL_INTEGRITY TRUE"
         */

        reset.addBatch("SET REFERENTIAL_INTEGRITY FALSE");
        for (TableRef ref : tablesForClearing) {
            reset.addBatch("DELETE FROM " + ref.getName());
        }
        reset.addBatch("SET REFERENTIAL_INTEGRITY TRUE");
        return reset;
    }

    private String dbEngineStatus() {
        try (Connection connection = connectionSupplier.get(); Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SHOW ENGINE INNODB STATUS")) {

            StringBuilder status = new StringBuilder();
            while (rs.next()) {
                status.append(rs.getString("Status")).append("||");
            }
            return status.toString();

        } catch (SQLException e) {
            LOG.error("Failed to get DB engine status", e);
            return StringUtils.EMPTY;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class TableRef {

        private String name;
    }
}
