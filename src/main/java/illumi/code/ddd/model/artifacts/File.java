package illumi.code.ddd.model.artifacts;

import illumi.code.ddd.model.DDDType;
import illumi.code.ddd.service.JavaArtifactService;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;

import java.util.ArrayList;
import java.util.List;

public abstract class File extends Artifact {

    private ArrayList<Field> fields;
    private ArrayList<Method> methods;
    private ArrayList<Annotation> annotations;
    private ArrayList<Interface> implInterfaces;


    File(Record record, DDDType type) {
        super(record, type);
        init();
    }

    File(String name, String path, DDDType type) {
        super(name, path, type);
        init();
    }

    private void init() {
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.annotations = new ArrayList<>();
        this.implInterfaces = new ArrayList<>();
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(Driver driver) {
        this.fields = (ArrayList<Field>) new JavaArtifactService(driver, getPath()).getFields();
    }

    public void addField(Field field) {
        this.fields.add(field);
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(Driver driver) {
        this.methods = (ArrayList<Method>) new JavaArtifactService(driver, getPath()).getMethods();
    }

    public void addMethod(Method method) {
        this.methods.add(method);
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Driver driver, List<Annotation> annotations) {
        this.annotations = (ArrayList<Annotation>) new JavaArtifactService(driver, getPath()).getAnnotations(annotations);
    }

    public List<Interface> getImplInterfaces() {
        return implInterfaces;
    }

    public void setImplInterfaces(Driver driver, List<Interface> interfaces) {
        this.implInterfaces =  (ArrayList<Interface>) new JavaArtifactService(driver, getPath()).getImplInterfaces(interfaces);
    }

    public void addImplInterface(Interface implInterface) {
        this.implInterfaces.add(implInterface);
    }

}
