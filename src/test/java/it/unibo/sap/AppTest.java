package it.unibo.sap;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

/**
 * Unit test for verifying architectural constraints of the application.
 */
public class AppTest {
    /**
     * Architectural test to verify dependencies between layers in layered architecture.
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

    @Test
    public void testNoCyclicDependenciesOnLayeredArchitecture() {
        slices().matching("sap.ass01.layered..").should().beFreeOfCycles();
    }

    /**
     * Architectural test to verify dependencies between layers in clean architecture.
     */
    @Test
    public void testOnCleanArchitectureDependencies() {
        // Import classes from specified packages to analyze their dependencies.
        JavaClasses importedClasses = new ClassFileImporter().importPackages(
            "sap.ass01.clean.domain", 
            "sap.ass01.clean.infrastructure"
        );

        // Define the clean architecture as a layered architecture.
        layeredArchitecture()
            .consideringAllDependencies() // Consider all dependencies, including indirect ones.
            .layer("domain").definedBy("sap.ass01.clean.domain") 
            .layer("infrastructure").definedBy("sap.ass01.clean.infrastructure..") 

            // Access rules between layers.
            .whereLayer("domain").mayOnlyBeAccessedByLayers("infrastructure") 
            .whereLayer("infrastructure").mayNotBeAccessedByAnyLayer() 
            
            .check(importedClasses);
    }

    @Test
    public void testNoCyclicDependenciesOnCleanArchitecture() {
        slices().matching("sap.ass01.clean..").should().beFreeOfCycles();
    }
}
