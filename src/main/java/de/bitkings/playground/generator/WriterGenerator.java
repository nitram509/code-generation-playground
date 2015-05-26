package de.bitkings.playground.generator;

import com.sun.codemodel.*;
import com.sun.codemodel.writer.FileCodeWriter;
import de.bitkings.playground.model.TeilAntrag;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.File;
import java.lang.reflect.Field;

import static com.sun.codemodel.JMod.PUBLIC;

public class WriterGenerator {

  private static final String SRC_PATH = "src/generated/java";
  private static final String CLASS_SUFFIX = "Writer";

  private String className;

  public static void main(String[] args) {
    WriterGenerator wg = new WriterGenerator();
    try {
      wg.generateWriter();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void generateWriter() throws Exception {
    Class clazz = TeilAntrag.class;
    setClassName(clazz.getSimpleName());
    Field[] fields = clazz.getFields();

    JCodeModel codeModel = new JCodeModel();
    JDefinedClass jDefinedClass = codeModel._class(getClassName());
    jDefinedClass.javadoc().add("=== AUTO GENERATED ===\n");
    jDefinedClass.javadoc().add("Writer generated for class " + getClassName());

    JMethod writeMethod = jDefinedClass.method(PUBLIC, JsonObject.class, "write");
    writeMethod.param(TeilAntrag.class, "teilAntrag");
    writeFields(codeModel, writeMethod, fields);

    FileCodeWriter fileCodeWriter = new FileCodeWriter(new File(SRC_PATH));
    codeModel.build(fileCodeWriter);
  }

  private void writeFields(JCodeModel codeModel, JMethod method, Field[] fields) throws ClassNotFoundException, JClassAlreadyExistsException {
    JBlock body = method.body();
    JClass classJson = codeModel.ref(Json.class);
    JVar varObjectToSerialize = method.params().get(0);

    JVar varObjectBuilder = body.decl(codeModel._ref(JsonObjectBuilder.class), "objectBuilder", classJson.staticInvoke("createObjectBuilder"));
    for (Field field : fields) {
      JInvocation addInvocation = body.invoke(varObjectBuilder, "add");
      addInvocation.arg(field.getName());
      addInvocation.arg(varObjectToSerialize.ref(field.getName()));
    }
    body._return(varObjectBuilder.invoke("build"));
  }

  public String getClassName() {
    return className + CLASS_SUFFIX;
  }

  public void setClassName(String className) {
    this.className = className;
  }
}
