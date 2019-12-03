package illumi.code.ddd.service.refactor.impl;

import illumi.code.ddd.model.DDDRefactorData;
import illumi.code.ddd.model.DDDStructure;
import illumi.code.ddd.model.DDDType;
import illumi.code.ddd.model.artifacts.*;
import illumi.code.ddd.model.artifacts.Class;
import illumi.code.ddd.model.artifacts.Package;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DomainEventRefactorServiceTest {
    private DDDStructure structure;
    private DomainEventRefactorService service;
    private DDDRefactorData refactorData;

    private Package module;

    @BeforeEach
    void init() {
        structure = new DDDStructure();
        structure.setPath("de.test");

        module = new Package("test", "de.test");
        module.setType(DDDType.MODULE);

        Class root = new Class("Root", "de.test.Root");
        root.setType(DDDType.AGGREGATE_ROOT);
        root.setDomain("domain0");
        module.addContains(root);
        structure.addClass(root);

        ArrayList<Artifact> data = new ArrayList<>();
        data.add(module);
        structure.setStructure(data);

        refactorData = new DDDRefactorData(structure);

        new InitializeService(refactorData).initModules();

        Interface repo = new Interface("Repo", "de.test.Repo");
        repo.setType(DDDType.REPOSITORY);
        refactorData.getModelModule().addContains(repo);

        Class repoImpl = new Class("RepoImpl", "de.test.RepoImpl");
        repoImpl.setType(DDDType.REPOSITORY);
        refactorData.getModelModule().addContains(repoImpl);

        refactorData.getDomainModule().addContains(repo);


        Class value = new Class("EntityId", "de.test.EntityId");
        value.setType(DDDType.VALUE_OBJECT);
        value.addField(new Field("private", "id", "java.lang.Long"));
        value.addMethod(new Method("public", "equals", "java.lang.Boolean equals(Object)"));
        value.addMethod(new Method("public", "hashCode", "java.lang.Integer hashCode()"));
        value.addMethod(new Method("public", "id", "java.lang.Long id()"));
        value.addMethod(new Method("public", "setId", "void setId(java.lang.Long)"));
        refactorData.getNewStructure().addClass(value);
        refactorData.getModelModule().addContains(value);

        service = new DomainEventRefactorService(refactorData);
    }

    @Test
    void testRefactor() {
        Class value = new Class("Event", "de.test.Event");
        value.setType(DDDType.DOMAIN_EVENT);
        value.addField(new Field("private", "entityId", "de.test.EntityId"));
        value.addField(new Field("private", "date", "java.lang.String"));
        value.addMethod(new Method("public", "getEntityId", "de.test.EntityId getEntityId()"));
        value.addMethod(new Method("private", "setEntityId", "void setEntityId(de.test.EntityId)"));
        value.addMethod(new Method("public", "getDate", "java.time.Date getDate()"));
        value.addMethod(new Method("private", "setDate", "void setDate(java.time.Date)"));
        refactorData.getNewStructure().addClass(value);
        refactorData.getModelModule().addContains(value);

        service.refactor();

        assertAll(  () -> assertEquals(2, value.getFields().size(), "#Field"),
                    () -> assertEquals(4, value.getMethods().size(), "#Method"));
    }

    @Test
    void testRefactorInvalidEvent() {
        Class value = new Class("Event", "de.test.Event");
        value.setType(DDDType.DOMAIN_EVENT);
        value.addField(new Field("private", "entityId", "java.lang.Long"));
        value.addField(new Field("private", "date", "java.lang.String"));
        value.addMethod(new Method("private", "setEntityId", "void setEntityId(java.lang.Long)"));
        value.addMethod(new Method("public", "getDate", "java.time.Date getDate()"));
        refactorData.getNewStructure().addClass(value);
        refactorData.getModelModule().addContains(value);

        service.refactor();

        assertAll(  () -> assertEquals(2, value.getFields().size(), "#Field"),
                    () -> assertEquals(4, value.getMethods().size(), "#Method"));
    }
}