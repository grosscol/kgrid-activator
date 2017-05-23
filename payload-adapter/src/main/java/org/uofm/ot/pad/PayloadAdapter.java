package org.uofm.ot.pad;

import org.uofm.ot.knowledge.KnowledgeObject.Payload;
import org.uofm.ot.pad.exceptions.ExecException;

import java.util.Map;

/** Payload Adapter Interface
 * A payload adapter receives a payload and takes appropriate action in order to return a result.
 *
 * Created by grosscol on 5/19/17.
 */
 public interface PayloadAdapter {

    /** This is the hand off between the Activator and whatever will resolve the payload.  From the
     * point of view of the Activator, there be dragons beyond this call.  It is up to the impl to
     * wrangle those dragons and return the specified type.
     *
     * @param params Map of parameters.  Keys are parameter names with the corresponding value.
     * @param payload Payload from a knowledge object.  The contents of which should be actionable by the adapter.
     * @return Java object of a type specified by the Type T.
     */
    public <T> T execute(Map<String, Object> params, Payload payload, Class<T> c) throws ExecException;
 }