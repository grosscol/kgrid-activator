package org.uofm.ot.activator.adapter;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.python.core.PyDictionary;
import org.uofm.ot.activator.domain.KnowledgeObject;
import org.uofm.ot.activator.exception.OTExecutionStackException;
import org.uofm.ot.activator.domain.KnowledgeObjectBuilder;

/**
 * Created by nggittle on 3/29/17.
 */
public class PythonAdapterTest {

  @Rule
  public ExpectedException expectedEx = ExpectedException.none();
  PythonAdapter pythonAdapter;
  Map<String, Object> argMap;

  @Before
  public void setUp() {
    pythonAdapter = new PythonAdapter();
    argMap = new HashMap<>();
  }

  @Test
  public void executeEmptyPayload() throws Exception {
    KnowledgeObject ko = new KnowledgeObjectBuilder().payloadContent("").payloadFunctionName("").build();
    expectedEx.expect(OTExecutionStackException.class);
    expectedEx.expectMessage(" function not found in object payload ");
    pythonAdapter.execute(argMap, ko.payload.content, ko.payload.functionName, Integer.class);
  }

  @Test
  public void executePayloadWithBadSyntax() throws Exception {
    KnowledgeObject ko = new KnowledgeObjectBuilder()
        .payloadContent("def execute():")
        .payloadFunctionName("execute")
        .build();

    expectedEx.expect(OTExecutionStackException.class);
    expectedEx.expectMessage("Error occurred while executing python code SyntaxError:");
    pythonAdapter.execute(argMap, ko.payload.content, ko.payload.functionName, Integer.class);
  }

  @Test
  public void executePayloadWithGoodSyntax() throws Exception {
    KnowledgeObject ko = new KnowledgeObjectBuilder()
        .payloadContent("def execute(a):\n     return 1")
        .payloadFunctionName("execute")
        .build();

    Object result = pythonAdapter.execute(argMap, ko.payload.content, ko.payload.functionName, Integer.class);

    assertEquals(1, result);
  }

  @Test
  public void executePayloadWithReturnTypeMismatch() throws Exception {
      KnowledgeObject ko = new KnowledgeObjectBuilder()
          .payloadContent("def execute(a):\n    return \"True\"")
          .payloadFunctionName("execute").build();

    expectedEx.expect(OTExecutionStackException.class);
    expectedEx.expectMessage("Type mismatch while converting python result to java type");
    Object result = pythonAdapter.execute(argMap, ko.payload.content, ko.payload.functionName, Boolean.class);
  }

  @Test
  public void executePayloadWithReturnTypeMatch() throws Exception {
    KnowledgeObject ko = new KnowledgeObjectBuilder()
        .payloadContent("def execute(a):\n    return True")
        .payloadFunctionName("execute").build();

    Object result = pythonAdapter.execute(argMap, ko.payload.content, ko.payload.functionName, Boolean.class);
  }

  @Test
  public void executePayloadWithTwoObjectsInDictionary() throws Exception {
    KnowledgeObject ko = new KnowledgeObjectBuilder()
        .payloadContent("def execute(a):\n    return str(a[\"b\"]) + (str(a[\"a\"]))")
        .payloadFunctionName("execute").build();
    argMap.put("b", "this is a ");
    argMap.put("a", "test");

    Object result = pythonAdapter.execute(argMap, ko.payload.content, ko.payload.functionName, String.class);
    assertEquals("this is a test", result);
  }

  @Test
  public void executePayloadWithMassiveDictionary() throws Exception {
    KnowledgeObject ko = new KnowledgeObjectBuilder()
        .payloadContent("def execute(a):\n    return execute(str(a))")
        .payloadFunctionName("execute").build();

    expectedEx.expect(OTExecutionStackException.class);
    Object result = pythonAdapter.execute(argMap, ko.payload.content, ko.payload.functionName, String.class);
  }

  @Test
  public void executePayloadWithNullMap() throws Exception {
    KnowledgeObject ko = new KnowledgeObjectBuilder()
        .payloadContent("def execute(a):\n    return a[\"b\"]")
        .payloadFunctionName("execute").build();
    argMap.put("b", null);

    Object result = pythonAdapter.execute(argMap, ko.payload.content, ko.payload.functionName, String.class);
    assertEquals(null, result);
  }

  @Test
  public void executePayloadWithFloatIO() throws Exception {
    KnowledgeObject ko = new KnowledgeObjectBuilder()
        .payloadContent("def execute(a):\n    return a[\"b\"]")
        .payloadFunctionName("execute").build();
    argMap.put("b", new Float("1.532423444455223553399999331"));

    Object result = pythonAdapter.execute(argMap, ko.payload.content, ko.payload.functionName, Float.class);
    assertEquals(new Float("1.532423444455223553399999331"), result);
  }

  @Test
  public void executePayloadWithDoubleIO() throws Exception {
    KnowledgeObject ko = new KnowledgeObjectBuilder()
        .payloadContent("def execute(a):\n    return a[\"b\"]")
        .payloadFunctionName("execute").build();
    argMap.put("b", new Double("1.5324234444552235533999993319999999"));

    Object result = pythonAdapter.execute(argMap, ko.payload.content, ko.payload.functionName, Double.class);
    assertEquals(new Double("1.5324234444552235533999993319999999"), result);
  }

  @Test
  public void executePayloadWithMapIO() throws Exception {
    KnowledgeObject ko = new KnowledgeObjectBuilder()
        .payloadContent("def execute(a):\n    return a")
        .payloadFunctionName("execute").build();
    argMap.put("b", new Double("1.5324234444552235533999993319999999"));

    Object result = pythonAdapter.execute(argMap, ko.payload.content, ko.payload.functionName, Map.class);
    Map<String, Object> exMap = new PyDictionary();
    exMap.put("b", new Double("1.5324234444552235533999993319999999"));
    assertEquals(exMap, result);
  }

  @Test
  public void executePayloadWithNestedMapIO() throws Exception {
    KnowledgeObject ko = new KnowledgeObjectBuilder()
        .payloadContent("def execute(a):\n    return a")
        .payloadFunctionName("execute").build();
    Map<String, Object> inMap = new HashMap<>();
    inMap.put("double", new Double("1.5324234444552235533999993319999999"));
    argMap.put("b", inMap);

    Object result = pythonAdapter.execute(argMap, ko.payload.content, ko.payload.functionName, Map.class);
    Map<String, Object> exMap = new HashMap<>();
    exMap.put("double", new Double("1.5324234444552235533999993319999999"));
    assertEquals(exMap, ((Map)result).get("b"));
    exMap.clear();
    exMap.put("b", inMap);
    assertEquals(exMap, result);

  }


}