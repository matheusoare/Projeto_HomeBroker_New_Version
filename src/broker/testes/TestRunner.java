package broker.testes;

import org.junit.platform.launcher.*;
import org.junit.platform.launcher.core.*;
import org.junit.platform.launcher.listeners.*;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.DiscoverySelector;

public class TestRunner {
    public static void main(String[] args) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
            .selectors(DiscoverySelectors.selectPackage("broker.testes"))
            .build();

        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        Launcher launcher = LauncherFactory.create();
        launcher.discover(request);
        launcher.execute(request, listener);

        TestExecutionSummary summary = listener.getSummary();
        long total  = summary.getTestsStartedCount();
        long passed = summary.getTestsSucceededCount();
        long failed = summary.getTestsFailedCount();

        System.out.println("\n=== Resultado dos Testes ===");
        System.out.printf("Total: %d  |  Passou: %d  |  Falhou: %d%n", total, passed, failed);

        if (failed > 0) {
            System.out.println("\nFalhas:");
            summary.getFailures().forEach(f ->
                System.out.println("  - " + f.getTestIdentifier().getDisplayName()
                    + ": " + f.getException().getMessage()));
        }

        System.exit(failed > 0 ? 1 : 0);
    }
}
