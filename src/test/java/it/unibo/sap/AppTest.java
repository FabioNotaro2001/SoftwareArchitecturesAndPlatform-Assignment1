package it.unibo.sap;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * Unit test for verifying architectural constraints of the application.
 */
public class AppTest {
    /**
     * Architectural test to verify dependencies between layers.
     */
    @Test
    public void testOnLayeredArchitectureDependencies() {
        // Import classes from specified packages to analyze their dependencies.
        JavaClasses importedClasses = new ClassFileImporter().importPackages(
            "sap.ass01.layered.presentation", 
            "sap.ass01.layered.service", 
            "sap.ass01.layered.businessLogic", 
            "sap.ass01.layered.persistence"
        );

        // Define the layered architecture.
        layeredArchitecture()
            .consideringAllDependencies() // Consider all dependencies, including indirect ones.
            .layer("presentation").definedBy("sap.ass01.layered.presentation") // Presentation layer
            .layer("service").definedBy("sap.ass01.layered.service") // Service layer
            .layer("businessLogic").definedBy("sap.ass01.layered.businessLogic") // Business logic layer
            .layer("persistence").definedBy("sap.ass01.layered.persistence") // Persistence layer

            // Access rules between layers.
            .whereLayer("presentation").mayNotBeAccessedByAnyLayer() // Presentation layer should not be accessed by any other layer.
            .whereLayer("service").mayOnlyBeAccessedByLayers("presentation") // Service layer can only be accessed by the presentation layer.
            .whereLayer("businessLogic").mayOnlyBeAccessedByLayers("service", "persistence", "presentation") // Business logic can be accessed by service, persistence, or presentation.
            .whereLayer("persistence").mayOnlyBeAccessedByLayers("businessLogic") // Persistence layer can only be accessed by business logic layer.
            
            // Check the defined rules against the imported classes.
            .check(importedClasses);
    }
}
