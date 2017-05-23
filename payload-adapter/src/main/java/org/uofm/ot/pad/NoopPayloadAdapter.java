package org.uofm.ot.pad;

import org.springframework.stereotype.Component;
import org.uofm.ot.knowledge.KnowledgeObject;
import org.uofm.ot.pad.exceptions.ExecException;

import java.util.HashMap;
import java.util.Map;

/**
 * This dummy implementation of a PayloadAdapter does not execute the contents. Instead it simply
 * returns a dummy instance of whatever return type was specified.
 *
 * Created by grosscol on 5/19/17.
 */
@Component(value="noop")
public class NoopPayloadAdapter implements PayloadAdapter {
    /**
     * Dummy implementation that does nothing with the payload contents.
     *
     * @param params Map of parameters. Ignored.
     * @param payload Payload from a knowledge object.  Ignored.
     * @return a new object of type specified by parameter clazz.
     */
    @Override
    public <T> T execute(Map<String, Object> params, KnowledgeObject.Payload payload, Class<T> clazz)
            throws ExecException {

        Object retVal;

        // Handle classes for which newInstance() will fail.
        if(clazz==Integer.class){
            retVal = new Integer(0);
        }else if(clazz == Long.class){
            retVal = new Long(0);
        }else if(clazz == Float.class){
            retVal = new Float(0.0);
        }else if(clazz == Double.class){
            retVal = new Double(0.0);
        }else if(clazz == Map.class){
            retVal = new HashMap<>();
        }else{
            retVal = defaultInitializer(clazz);
        }
        return clazz.cast(retVal);
    }

    /**
     * Attempt to create an instances of Class<T>
     * @param clazz Class to instantiate
     * @param <T> Type of
     * @return
     */
    public <T> T defaultInitializer(Class<T> clazz){
        try{
            T retObject;
            retObject = clazz.newInstance();
            return retObject;
        }catch (IllegalAccessException illEx){
            ExecException otEx;
            String msg = "Could not instantiate return object for: " + clazz.toString();
            otEx = new ExecException(msg, illEx);
            throw(otEx);
        } catch (InstantiationException instEx) {
            ExecException otEx;
            String msg = "Could not instantiate return object for: " + clazz.toString();
            otEx = new ExecException(msg, instEx);
            throw(otEx);
        }
    }
}
