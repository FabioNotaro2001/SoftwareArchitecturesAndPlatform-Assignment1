package it.unibo.sap;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * Unit test for simple App.
 */
public class AppTest {
    // TODO: add comments here and report section with further explanation about tests that don't pass.
    /**
     * Test architetturale per verificare le dipendenze tra i layer.
     */
    @Test
    public void architecturalDependenciesTest() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("sap.ass01");

        // Layered architecture definition.
        layeredArchitecture().consideringAllDependencies()
            .layer("presentation").definedBy("sap.ass01.presentation..")
            .layer("service").definedBy("sap.ass01.service..")
            .layer("businessLogic").definedBy("sap.ass01.businessLogic..")
            .layer("persistence").definedBy("sap.ass01.persistence")

            // Access rules between layers.
            .whereLayer("presentation").mayNotBeAccessedByAnyLayer() 
            .whereLayer("service").mayOnlyBeAccessedByLayers("presentation") 

            // Strange case: in our implementation businessLogic can be accssed also by presentation layer becuase of EBikeInfo, UserInfo...
            .whereLayer("businessLogic").mayOnlyBeAccessedByLayers("service", "presentation", "persistence")
            .whereLayer("persistence").mayOnlyBeAccessedByLayers("businessLogic") 
            .check(importedClasses);
    }
}
