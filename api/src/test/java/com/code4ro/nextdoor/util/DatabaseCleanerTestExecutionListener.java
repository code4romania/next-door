package com.code4ro.nextdoor.util;

import com.code4ro.nextdoor.common.controller.AbstractControllerIntegrationTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

import javax.sql.DataSource;

/**
 * Spring test execution listener that performs database cleanup before each test.
 *
 * Only applicable to integration tests that extend AbstractControllerIntegrationTest, due to the need for a valid datasource.
 */
@Service
public class DatabaseCleanerTestExecutionListener extends AbstractTestExecutionListener {

    private DatabaseCleaner databaseCleaner;

    /**
     * Returns {@code 4999} to be executed before the SqlScriptsTestExecutionListener.
     */
    @Override
    public final int getOrder() {
        return 4999;
    }

    @Override
    public void beforeTestMethod(TestContext testContext) {
        if (!AbstractControllerIntegrationTest.class.isAssignableFrom(testContext.getTestClass())) {
            return;
        }

        if (databaseCleaner == null) {
            // TODO: Consider inspecting dataSource to check if we are connecting to test database and not a prod one!
            databaseCleaner = new DatabaseCleaner(getConnectionSupplier(testContext));
        }
        databaseCleaner.reset();
    }

    private Supplier<Connection> getConnectionSupplier(TestContext testContext) {
        DataSource dataSource = testContext.getApplicationContext().getBean(DataSource.class);
        return () -> {
            try {
                return dataSource.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
