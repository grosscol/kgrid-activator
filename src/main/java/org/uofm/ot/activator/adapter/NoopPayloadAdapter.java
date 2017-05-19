package org.uofm.ot.activator.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.uofm.ot.activator.domain.KnowledgeObject;
import org.uofm.ot.activator.exception.OTExecutionStackException;
import org.uofm.ot.activator.services.ioSpec;

import java.util.Map;

/**
 * This dummy implementation of a PayloadAdapter does not execute the contents. Instead it simply
 * returns a new instance of whatever return type was specified.
 *
 * Created by grosscol on 5/19/17.
 */
public class NoopPayloadAdapter implements PayloadAdapter {
    /**
     * Dummy implementation that does nothing with the payload contents.
     *
     * @param params Map of parameters.
     * @param payload Payload from a knowledge object.  Contents will NOT be acted upon.
     * @param retClazz The class that should be returned from the payload execution.
     * @return a new object of type specified by retClazz parameter.
     */
    @Override
    public <T> T execute(Map<String, Object> params, KnowledgeObject.Payload payload) throws OTExecutionStackException {
        T retObj;
        retObj = new T();
        return retObj;
    }
}
