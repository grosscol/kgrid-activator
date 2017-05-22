package org.uofm.ot.activator.adapter;


import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.uofm.ot.activator.domain.KnowledgeObject;
import org.uofm.ot.activator.exception.OTExecutionStackException;
import org.uofm.ot.activator.domain.KnowledgeObjectBuilder;

/**
 *
 * Created by grosscol on 5/19/17.
 */
public class NoopPayloadAdapterTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void executeEmptyPayload() throws Exception {
        PayloadAdapter adapter = new NoopPayloadAdapter();
        Map<String, Object> map = new HashMap<>();
        KnowledgeObject ko = new KnowledgeObjectBuilder().payloadContent("").build();

        assertThat(adapter.execute(map, ko.payload, Object.class), instanceOf(Object.class));
    }

    @Test
    public void executeNonEmptyPayload() throws Exception {
        PayloadAdapter adapter = new NoopPayloadAdapter();
        Map<String, Object> map = new HashMap<>();
        KnowledgeObject ko = new KnowledgeObjectBuilder()
                .payloadContent("DEADBEEF")
                .payloadFunctionName("execute")
                .build();

        assertThat(adapter.execute(map, ko.payload, Object.class), instanceOf(Object.class));
    }

    @Test
    public void executeVariousReturnTypes() throws Exception {
        PayloadAdapter adapter = new NoopPayloadAdapter();
        Map<String, Object> map = new HashMap<>();
        KnowledgeObject ko = new KnowledgeObjectBuilder().payloadContent("").build();

        Class[] types = {Integer.class, Long.class, String.class,
                         Float.class, Double.class, Map.class};

        for(Class type : types){
            assertThat(adapter.execute(map, ko.payload, type), instanceOf(type));
        }

    }
}
