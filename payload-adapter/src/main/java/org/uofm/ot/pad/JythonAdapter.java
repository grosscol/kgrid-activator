package org.uofm.ot.pad;

import org.python.core.PyDictionary;
import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.springframework.stereotype.Component;
import org.uofm.ot.knowledge.KnowledgeObject;
import org.uofm.ot.pad.exceptions.PayloadExecException;

import java.util.Map;

/**
 * Created by grosscol on 5/23/17.
 */
@Component(value="jython")
public class JythonAdapter implements PayloadAdapter {
    /**
     * This is the hand off between the Activator and whatever will resolve the payload.  From the
     * point of view of the Activator, there be dragons beyond this call.  It is up to the impl to
     * wrangle those dragons and return the specified type.
     *
     * @param params  Map of parameters.  Keys are parameter names with the corresponding value.
     * @param payload Payload from a knowledge object.  The contents of which should be actionable by the adapter.
     * @param clazz class of return type.
     * @return Java object of a type specified by the Type T.
     */
    @Override
    public <T> T execute(Map<String, Object> params, KnowledgeObject.Payload payload, Class<T> clazz) throws PayloadExecException {

        PythonInterpreter interpreter = new PythonInterpreter();

        T resObj;

        PyDictionary dictionary = new PyDictionary();
        dictionary.putAll(params);
        try {
            interpreter.exec(payload.content);

            PyObject someFunc = interpreter.get(payload.functionName);
            if(someFunc != null) {
                PyObject result = someFunc.__call__(dictionary);

                resObj = mapResult(clazz, result);
            } else {
                throw new PayloadExecException(payload.functionName + " function not found in object payload ");
            }
        } catch(PyException ex) {
            throw new PayloadExecException("Error while executing payload code " + ex);
        } finally {
            interpreter.close();
        }

        return resObj;
    }

    private <T> T mapResult(Class<T> clazz, PyObject result) {

        T casted;

        try {
            if( clazz == Float.class){
                float f = (float) result.__tojava__(float.class);
                casted = (T) new Float(f);
            }else if(clazz == Integer.class){
                int i = (int) result.__tojava__(int.class);
                casted = (T) new Integer(i);
            }else if(clazz == String.class){
                String s = (String) result.__tojava__(String.class);
                casted = (T) new String(s);
            } else if( clazz == Map.class) {
                Map mapped = (Map<String, Object>) result.__tojava__(Map.class);
                casted = (T) mapped;
            } else {
                casted = clazz.newInstance();
            }

        } catch (ClassCastException ex) {
            throw new PayloadExecException("Type mismatch while converting python result to java. Check input spec and code payload " + ex);
        } catch (IllegalAccessException | InstantiationException instEx){
            PayloadExecException otEx;
            String msg = "Could not instantiate return object for: " + clazz.toString();
            otEx = new PayloadExecException(msg, instEx);
            throw(otEx);
        }

        return casted;
    }
}
